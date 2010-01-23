package org.opentox.core.processors;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.opentox.core.exceptions.ExceptionDetails;
import org.opentox.core.exceptions.ProcessorException;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.interfaces.JProcessor;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.*;
import static org.opentox.core.util.MultiProcessorStatus.STATUS;

/**
 * <p>
 * Phase: Initial Testing...
 * </p>
 * <p>
 * The scope of this processor is to use a single processor in a multi-threaded
 * approach. Specifically, if you need to use a specific processor to perform a set
 * or processes, that is you want your processor to do a (considerable) number of mutually
 * independent jobs simultaneously, you can use a BatchProcessor. A BatchProcessor is a
 * processor it self, exploiting some other processor which is the one that does the
 * real job. A Batch Processor guarrantees that a set of jobs will run in parallel.
 * Provide the set of inputs to the BatchProcecssor and manipulate it as a single
 * {@link JProcessor Processor}.
 * </p>
 * <p>
 * A BatchProcessor is an extension of the default Yaqp {@link Processor }, hence
 * enabled by default. There are three ways to build your custom BatchProcessor: You
 * can implement the interface {@link BatchProcessor }, extends the abstract class
 * {@link AbstractBatchProcessor } or do both. The corresponding prototypes are:
 * <ul>
 * <li>class MyBatchProcessor&lt;I,O,P extends JProcessor&lt;I,O&gt;&gt;
 * implements JBatchProcessor&lt;I,O,P&lt;I,O&gt;&gt;</li>
 * <li>class MyBatchProcessor&lt;I,O,P extends JProcessor&lt;I,O&gt;&gt;
 * extends AbstractProcessor&lt;I,O,P&lt;I,O&gt;&gt;</li>
 * </ul>
 * </p>
 *
 * @author Sopasakis Pantelis
 * @author Charalampos Chomenides
 * @param <Input> Input to the processor
 * @param <Output> Output from the processor
 * @param <P> Type of internal processor
 */
public class BatchProcessor<Input, Output, P extends JProcessor<Input, Output>>
          extends AbstractBatchProcessor<Input, Output, P> {

    private String PROPERTY = getStatus().getClass().getName();
    private int corePoolSize = 4, maxPoolSize = 4;
    private int timeout = 1;
    private TimeUnit timeUnit = TimeUnit.HOURS;
    private ThreadPoolExecutor parallel_executor;

    private BatchProcessor() {
        super();
    }

    public BatchProcessor(final P processor) {
        this();
        setProcessor(processor);
    }

    public BatchProcessor(final P processor, final int corePoolSize, final int maxPoolSize) {
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        setProcessor(processor);
    }

    public void setTimeOut(final int timeout, final TimeUnit timeout_unit) {
        this.timeout = timeout;
        this.timeUnit = timeout_unit;
    }

    
    /**
     * @param data A list of data to be processed by the BatchProcessor.
     * @return Batch Processor Output
     * @throws YaqpException In case something goes wrong while processing
     * input data.
     */
    public ArrayList<Output> process(ArrayList<Input> data) throws YaqpException {

        if (data == null) {
            throw new ProcessorException(ExceptionDetails.null_input_to_parallel_processor);
        }

        if (data.size() != data.size()) {
            throw new ProcessorException("sizes of input array list and processors unequal!");
        }

        if (data.size() == 0) {
            throw new ProcessorException("No batch!");
        }

        /**
         * If the internal processor is synchronized, run all processes in
         * a single query.
         */
        if (getProcessor().isSynchronized()){
            corePoolSize = 1;
            maxPoolSize = 1;
        }


        P nullProcessor = (P) new Processor() {

            public Object process(Object data) throws YaqpException {
                return null;
            }
        };
        final ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(data.size() + 2);
        parallel_executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 1, TimeUnit.MINUTES, queue);
        getStatus().setMessage("processing in progress");
        long start_time = 0;

        Future[] future_array = new Future[data.size()];
        ArrayList result = new ArrayList();
        start_time = System.currentTimeMillis();

        for (int i = 0; i < data.size(); i++) {
            getStatus().increment(STATUS.INITIALIZED);
            if (getProcessor().isEnabled()) {
                future_array[i] = parallel_executor.submit(getCallable(getProcessor(), data.get(i)));
            } else {
                future_array[i] = parallel_executor.submit(getCallable(nullProcessor, null));
            }
        }
        parallel_executor.shutdown();

        /**
         * Await for the processors to terminate, but no more than a fixed timeout.
         */
        try {
            parallel_executor.awaitTermination(timeout, timeUnit);
            handleTimeOut();
        } catch (InterruptedException ex) {
            getStatus().setMessage("interrupted - not running");
            getStatus().completed();
            firePropertyChange(PROPERTY, null, getStatus());
            throw new ProcessorException(ExceptionDetails.processor_interruption);
        }

        for (int i = 0; i < data.size(); i++) {
            long error_start_time = 0;

            try {
                // Successful processing...
                error_start_time = System.currentTimeMillis();
                if (future_array[i].isDone()) {
                    result.add(future_array[i].get());
                    getStatus().increment(STATUS.PROCESSED);
                } else {
                    result.add(null);
                    getStatus().increment(STATUS.ERROR);
                }

                firePropertyChange(PROPERTY, null, getStatus());
            } catch (InterruptedException ex) {
                YaqpLogger.LOG.log(new ScrewedUp(ParallelProcessor.class,
                        "Noway!!!! We didn't expect this to happen! (?)"));
                getStatus().setMessage("completed unexpectedly");
                getStatus().completed();
                firePropertyChange(PROPERTY, null, getStatus());
                throw new ProcessorException(ExceptionDetails.unknown_cause);
            } catch (Exception ex) {

                /**
                 * If a processor error happens and the Parallel Processor is fail
                 * sensitive it should throw an exception and terminate.
                 */
                if (isfailSensitive()) {
                    YaqpLogger.LOG.log(new ScrewedUp(ParallelProcessor.class,
                            "It seems a computation within this ParallelProcessor died."));
                    parallel_executor.shutdownNow(); // force shut down.
                    getStatus().setMessage("completed unsuccessfully");
                    getStatus().completed();
                    firePropertyChange(PROPERTY, null, getStatus());
                    throw new ProcessorException("The batch Processor is fail-sensitive");
                }

                /**
                 * In case a Yaqp exception is thrown, it seems there is an error
                 * with the processor - input could not be properly processed.
                 */
                if (ex instanceof YaqpException) {
                    YaqpLogger.LOG.log(new ScrewedUp(ParallelProcessor.class,
                            "It seems a computation within this ParallelProcessor died."));
                }
                result.add(null); // If the processor fails, the result should be null.
                getStatus().increment(STATUS.ERROR);
                getStatus().incrementElapsedTime(STATUS.ERROR, System.currentTimeMillis() - error_start_time);
                firePropertyChange(PROPERTY, null, getStatus());

            }
        }
        getStatus().incrementElapsedTime(STATUS.PROCESSED, System.currentTimeMillis() - start_time);
        getStatus().setMessage("completed successfully");
        getStatus().completed();
        firePropertyChange(PROPERTY, null, getStatus());

        return result;

    }

    protected Callable getCallable(final P processor, final Input input) {
        Callable callable = new Callable() {

            public Output call() throws Exception {
                Object o;
                o = processor.process(input);
                return (Output) o;
            }
        };
        return callable;
    }




    /**
     * Specifies how to cope with timeouts.
     * @throws YaqpException
     */
    protected void handleTimeOut() throws YaqpException {
        if (!parallel_executor.isTerminated()) {
            if (isfailSensitive()) {
                parallel_executor.shutdownNow();
                YaqpLogger.LOG.log(new ScrewedUp(ParallelProcessor.class,
                        ExceptionDetails.time_out_exception.toString()));
                System.out.println("Waiting for " + timeout + timeUnit);
                getStatus().setMessage("completed unsuccessfully - timeout");
                getStatus().completed();
                firePropertyChange(PROPERTY, null, getStatus());
                throw new ProcessorException(ExceptionDetails.time_out_exception);
            } else {
                YaqpLogger.LOG.log(new Debug(ParallelProcessor.class,
                        "Some processes in a parallel processor took very long "
                        + "to complete but the parallel is not fail-sensitive so "
                        + "no exception is thrown"));
            }
        }

    }
}
