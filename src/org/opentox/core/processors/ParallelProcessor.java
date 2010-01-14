package org.opentox.core.processors;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.interfaces.JMultiProcessor;
import org.opentox.core.interfaces.JMultiProcessorStatus;
import org.opentox.core.interfaces.JMultiProcessorStatus.STATUS;
import org.opentox.core.interfaces.JProcessor;
import org.opentox.core.util.MultiProcessorStatus;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.*;
import org.opentox.core.exceptions.YaqpException.CAUSE;

/**
 * A Parallel Processor is a collection of processors that run in parallel. 
 * Use this processor in cases where no synchronization is needed. Note also that a
 * parallel processor is by default enabled and not fail-sensitive. If some process
 * fails, the corresponding output of the ParallelProcessor will be null but the whole
 * processor will not throw an Exception.
 * @author Sopasakis Pantelis
 * @author Charalampos Chomenides
 */
public class ParallelProcessor<P extends JProcessor>
        extends ArrayList<P>
        implements JMultiProcessor<ArrayList, ArrayList, P> {

    private ThreadPoolExecutor parallel_executor;
    private boolean failSensitive = false;
    private JMultiProcessorStatus status = new MultiProcessorStatus();
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private String PROPERTY = status.getClass().getName();
    private int corePoolSize = 4, maxPoolSize = 4;
    private int timeout = 1;
    private TimeUnit timeUnit = TimeUnit.HOURS;

    public ParallelProcessor() {
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
     * @param corePoolSize
     * @param maxPoolSize
     * @param timeout
     * @param timeunit
     */
    public ParallelProcessor(final int corePoolSize, final int maxPoolSize,
            final int timeout, final TimeUnit timeunit) {
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.timeout = timeout;
        this.timeUnit = timeunit;
    }

    /**
     * Returns true if the parallel processor is fail-sensitive. If yes, any
     * Exception thrown by any process in the parallel processor, aborts the
     * whole execution and proliferates the exception to the parallel processor itself, which
     * means that the execution of the parallel processor will stop if an exception is caught
     * in any of its processors. Otherwise, if the parallel processor is not fai-sensitive,
     * the result from a process overrides any failed error-state proceeses and
     * moves on to the next processor.
     * @return true if the parallel processor is failsensitive.
     */
    public boolean isfailSensitive() {
        return failSensitive;
    }

    /**
     * Sets the file-sensitive flag of the parallel processor to true of false accordingly.
     * If set to false, the parallel processor becomes fail-safe which means that it continues to
     * process the input data overriding any error-state procceses.
     * @param failSensitive
     */
    public void setfailSensitive(boolean failSensitive) {
        this.failSensitive = failSensitive;
    }

    public ArrayList process(final ArrayList data) throws YaqpException {



        if (data == null) {
            throw new YaqpException(CAUSE.null_input_to_parallel_processor);
        }

        if (data.size() != size()) {
            throw new YaqpException("sizes of input array list and processors unequal!");
        }

        if (size()==0){
            throw new YaqpException(CAUSE.no_processors_found);
        }


        final ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(size() + 2);

        /**
         * A null processor (a processor always returning (Object)null ; created on the fly.
         */
        JProcessor nullProcessor = new Processor() {

            public Object process(Object data) throws YaqpException {
                return null;
            }
        };

        parallel_executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 1, TimeUnit.MINUTES, queue);


        status.setMessage("processing in progress");

        long start_time = 0;

        Future[] future_array = new Future[size()];
        ArrayList result = new ArrayList();
        start_time = System.currentTimeMillis();


        /**
         * Do the nasty job....
         * Every processor takes the corresponding job
         */
        for (int i = 0; i < size(); i++) {
            status.increment(STATUS.INITIALIZED);
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


            /**
             * If there are still some open threads, i.e. the process has not
             * completed, and the parallel processor is fail-sensitive, then throw
             * an exception
             */
            if (!parallel_executor.isTerminated()) {
                if (failSensitive) {
                    parallel_executor.shutdownNow();
                    YaqpLogger.INSTANCE.log(new ScrewedUp(ParallelProcessor.class,
                            CAUSE.time_out_exception.toString()));
                    System.out.println("Waiting for " + timeout + timeUnit);
                    status.setMessage("completed unsuccessfully - timeout");
                    status.completed();
                    throw new YaqpException(CAUSE.time_out_exception);
                } else {
                    YaqpLogger.INSTANCE.log(new Debug(ParallelProcessor.class,
                            "Some processes in a parallel processor took very long "
                            + "to complete but the parallel is not fail-sensitive so "
                            + "no exception is thrown"));
                }
            }

        } catch (InterruptedException ex) {
            status.setMessage("interrupted - not running");
            status.completed();
            throw new YaqpException(CAUSE.processor_interruption);
        }



        for (int i = 0; i < size(); i++) {
            long error_start_time = 0;
            try {
                // Successful processing...
                error_start_time = System.currentTimeMillis();
                result.add(future_array[i].get());
                status.increment(STATUS.PROCESSED); // increment number of successful processors
            } catch (InterruptedException ex) {
                YaqpLogger.INSTANCE.log(new ScrewedUp(ParallelProcessor.class,
                        "Noway!!!! We didn't expect this to happen! (?)"));
                status.setMessage("completed unexpectedly");
                status.completed();
                throw new YaqpException(CAUSE.unknown_cause);
            } catch (Exception ex) {


                /**
                 * If a processor error happens and the Parallel Processor is fail
                 * sensitive it should throw an exception and terminate.
                 */
                if (failSensitive) {
                    YaqpLogger.INSTANCE.log(new ScrewedUp(ParallelProcessor.class,
                            "It seems a computation within this ParallelProcessor died."));
                    parallel_executor.shutdownNow(); // force shut down.
                    status.setMessage("completed unsuccessfully");
                    status.completed();
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
                status.increment(STATUS.ERROR);
                status.incrementElapsedTime(STATUS.ERROR, System.currentTimeMillis() - error_start_time);

            }
        }
        status.incrementElapsedTime(STATUS.PROCESSED, System.currentTimeMillis() - start_time);
        propertyChangeSupport.firePropertyChange(PROPERTY, null, status);
        status.setMessage("completed successfully");
        status.completed();


        return result;
    }

    public boolean isEnabled() {
        for (int i = 0; i < size(); i++) {
            if (get(i).isEnabled()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Enables/Disables every sub-processor, that is if enabled=true,
     * all processeors are enabled, while enabled=false disables all
     * processors in the parallel structure.
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
        for (int i = 0; i < size(); i++) {
            get(i).setEnabled(enabled);
        }
    }

    public void addPropertyChangeListener(final String propertyName,
            final PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(final String propertyName,
            final PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }

    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public JMultiProcessorStatus getStatus() {
        return status;
    }

    private Callable getCallable(final JProcessor processor, final Object input) {
        Callable callable = new Callable() {

            public Object call() throws Exception {
                Object o;
                o = processor.process(input);
                return (Object ) o;
            }
        };
        return callable;
    }
}
