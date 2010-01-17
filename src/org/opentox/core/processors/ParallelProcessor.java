package org.opentox.core.processors;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.interfaces.JMultiProcessorStatus.STATUS;
import org.opentox.core.interfaces.JProcessor;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.*;
import org.opentox.core.exceptions.YaqpException.CAUSE;

/**
 * A Parallel Processor is a collection of processors that run in parallel. 
 * Use this processor in cases where no synchronization is needed. Note also that a
 * parallel processor is by default enabled and not fail-sensitive. If some process
 * fails, the corresponding output of the ParallelProcessor will be null but the whole
 * processor will not throw an Exception.
 * @param <Input> Input type
 * @param <Output> Output type
 * @param <P> Processor type
 * @author Sopasakis Pantelis
 * @author Charalampos Chomenides
 */
public class ParallelProcessor<Input, Output, P extends JProcessor<ArrayList<Input>, ArrayList<Output>>>
          extends AbstractMultiProcessor<ArrayList<Input>, ArrayList<Output>, P> {

    private ThreadPoolExecutor parallel_executor;
    private String PROPERTY_PARALLEL_STATUS = getStatus().getClass().getName();
    private int corePoolSize = 4, maxPoolSize = 4;
    private int timeout = 1;
    private TimeUnit timeUnit = TimeUnit.HOURS;

    public ParallelProcessor() {
        super();
    }

    /**
     * Construct a ParallelProcessor with fixed pool size.
     * @param poolSize fixed pool size for the processor.
     */
    public ParallelProcessor(final int poolSize) {
        this.corePoolSize = poolSize;
        this.maxPoolSize = poolSize;
    }

    /**
     * Once the parallel processor is initialized, a number of threads is generated
     * which is equal to the parameter corePoolSize. On runtime, the number of threads
     * will not exceed maxPoolSize.
     * @param corePoolSize initial pool size for the Parallel Processor.
     * @param maxPoolSize maximum pool size.
     */
    public ParallelProcessor(final int corePoolSize, final int maxPoolSize) {
        this();
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
    }

    /**
     * Construct a parallel processor
     * @param corePoolSize initial pool size for the Parallel Processor.
     * @param maxPoolSize maximum pool size.
     * @param timeout when to interrupt the procedure
     * @param timeunit units for the timeout
     */
    public ParallelProcessor(final int corePoolSize, final int maxPoolSize,
            final int timeout, final TimeUnit timeunit) {
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.timeout = timeout;
        this.timeUnit = timeunit;
    }

    public ArrayList<Output> process(final ArrayList<Input> data) throws YaqpException {

        if (data == null) {
            throw new YaqpException(CAUSE.null_input_to_parallel_processor);
        }

        if (data.size() != size()) {
            throw new YaqpException("sizes of input array list and processors unequal!");
        }

        if (size() == 0) {
            throw new YaqpException(CAUSE.no_processors_found);
        }

        /**
         * A null processor (a processor always returning (Object)null ; created on the fly.
         */
        JProcessor nullProcessor = new Processor() {

            public Object process(Object data) throws YaqpException {
                return null;
            }
        };
        final ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(size() + 2);
        parallel_executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 1, TimeUnit.MINUTES, queue);
        getStatus().setMessage("processing in progress");
        long start_time = 0;

        Future[] future_array = new Future[size()];
        ArrayList result = new ArrayList(size());
        start_time = System.currentTimeMillis();

        /**
         * Do the nasty job....
         * Every processor takes the corresponding job
         */
        for (int i = 0; i < size(); i++) {
            getStatus().increment(STATUS.INITIALIZED);
            if (get(i).isEnabled()) {
                /**
                 * If the processor is enabled, submit the job to be done...
                 */
                future_array[i] = parallel_executor.submit(getCallable(get(i), data.get(i)));
            } else {
                /**
                 * If the i-th processor is not enabled, it is not parallelized with
                 * other processors and a nullProcessor is used instead which returns
                 * just a null object.
                 */
                future_array[i] = parallel_executor.submit(getCallable(nullProcessor, null));
            }
        }
        /**
         * Graceful shutdown of the parallel executor.
         */
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
            firePropertyChange(PROPERTY_PARALLEL_STATUS, null, getStatus());
            throw new YaqpException(CAUSE.processor_interruption);
        }

        for (int i = 0; i < size(); i++) {
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

                firePropertyChange(PROPERTY_PARALLEL_STATUS, null, getStatus());
            } catch (InterruptedException ex) {
                YaqpLogger.INSTANCE.log(new ScrewedUp(ParallelProcessor.class,
                        "Noway!!!! We didn't expect this to happen! (?)"));
                getStatus().setMessage("completed unexpectedly");
                getStatus().completed();
                firePropertyChange(PROPERTY_PARALLEL_STATUS, null, getStatus());
                throw new YaqpException(CAUSE.unknown_cause);
            } catch (Exception ex) {

                /**
                 * If a processor error happens and the Parallel Processor is fail
                 * sensitive it should throw an exception and terminate.
                 */
                if (isfailSensitive()) {
                    YaqpLogger.INSTANCE.log(new ScrewedUp(ParallelProcessor.class,
                            "It seems a computation within this ParallelProcessor died."));
                    parallel_executor.shutdownNow(); // force shut down.
                    getStatus().setMessage("completed unsuccessfully");
                    getStatus().completed();
                    firePropertyChange(PROPERTY_PARALLEL_STATUS, null, getStatus());
                    throw new YaqpException();
                }

                /**
                 * In case a Yaqp exception is thrown, it seems there is an error
                 * with the processor - input could not be properly processed.
                 */
                if (ex instanceof YaqpException) {
                    YaqpLogger.INSTANCE.log(new ScrewedUp(ParallelProcessor.class,
                            "It seems a computation within this ParallelProcessor died."));
                }
                result.add(null); // If the processor fails, the result should be null.
                getStatus().increment(STATUS.ERROR);
                getStatus().incrementElapsedTime(STATUS.ERROR, System.currentTimeMillis() - error_start_time);
                firePropertyChange(PROPERTY_PARALLEL_STATUS, null, getStatus());

            }
        }
        getStatus().incrementElapsedTime(STATUS.PROCESSED, System.currentTimeMillis() - start_time);
        getStatus().setMessage("completed successfully");
        getStatus().completed();
        firePropertyChange(PROPERTY_PARALLEL_STATUS, null, getStatus());

        try {
            return (ArrayList<Output>) result;
        } catch (Exception ex) {
            final String explanation = "Output of parallel processor cannot be cast as the specified type!";
            YaqpLogger.INSTANCE.log(new Debug(ParallelProcessor.class, explanation));
            throw new YaqpException(explanation);
        }

    }

    /**
     * Set the timeout for the batch processor. 
     * @param timeout when to interrupt the procedure
     * @param timeout_unit units for the timeout
     */
    public void setTimeOut(final int timeout, final TimeUnit timeout_unit) {
        this.timeout = timeout;
        this.timeUnit = timeout_unit;
    }

    /**
     * The corresponding callable for a given processor with a given input. The
     * result occcurs from the invokation of the method call in the callable object
     * which is equivalent to the method process in JProcessor. Thus a processor
     * is equivalent to a Callable when input is determined.
     * @param processor A processor
     * @param input Some input for the processor
     * @return
     */
    protected Callable getCallable(final JProcessor processor, final Object input) {
        Callable callable = new Callable() {

            public Object call() throws Exception {
                Object o;
                o = processor.process(input);
                return (Object) o;
            }
        };
        return callable;
    }


    /**
     * Handles a possible time out of a thread in the processor. If there are still
     * some open threads, i.e. the process has not completed, and the parallel
     * processor is fail-sensitive, then throw an exception. Otherwise, just log
     * the event.
     * @throws YaqpException
     */
    protected void handleTimeOut() throws YaqpException{
            if (!parallel_executor.isTerminated()) {
                if (isfailSensitive()) {
                    parallel_executor.shutdownNow();
                    YaqpLogger.INSTANCE.log(new ScrewedUp(ParallelProcessor.class,
                            CAUSE.time_out_exception.toString()));
                    System.out.println("Waiting for " + timeout + timeUnit);
                    getStatus().setMessage("completed unsuccessfully - timeout");
                    getStatus().completed();
                    firePropertyChange(PROPERTY_PARALLEL_STATUS, null, getStatus());
                    throw new YaqpException(CAUSE.time_out_exception);
                } else {
                    YaqpLogger.INSTANCE.log(new Debug(ParallelProcessor.class,
                            "Some processes in a parallel processor took very long "
                            + "to complete but the parallel is not fail-sensitive so "
                            + "no exception is thrown"));
                }
            }
    }


}
