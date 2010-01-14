package org.opentox.core.processors;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
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

/**
 * A Parallel Processor is a collection of processors that run in parallel. 
 * Use this processor in cases where no synchronization is needed.
 * @param <I> Common Input Type - might be Object
 * @param <O> Common Output Type - might be Object
 * @author chung
 */
public class ParallelProcessor
        extends ArrayList<JProcessor>
        implements JMultiProcessor<ArrayList, ArrayList> {

    private ThreadPoolExecutor parallel_executor;
    private boolean failSensitive = true;
    private JMultiProcessorStatus status = new MultiProcessorStatus();
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private String PROPERTY = status.getClass().getName();

    private int corePoolSize = 4 , maxPoolSize = 4 ;


    public ParallelProcessor() {

    }

    public ParallelProcessor(int corePoolSize, int maxPoolSize){
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
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

        final ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(size()+2);
        System.err.println(size()+2);

        parallel_executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 1, TimeUnit.MINUTES, queue);


        status.setMessage("parallel processor initialized");
        long start_time = 0;

        Future[] future_array = new Future[size()];
        ArrayList result = new ArrayList();

        /**
         * Do the nasty job....
         */
        for (int i = 0; i < size(); i++) {
            start_time = System.currentTimeMillis();
            status.increment(STATUS.INITIALIZED);
            future_array[i] = parallel_executor.submit(getCallable(get(i), data.get(i)));
            status.increment(STATUS.PROCESSED);
            status.incrementElapsedTime(STATUS.PROCESSED, System.currentTimeMillis() - start_time);
            propertyChangeSupport.firePropertyChange(PROPERTY, null, status);
        }

        parallel_executor.shutdown();
        try {
            parallel_executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException ex) {
            start_time = System.currentTimeMillis();
            YaqpLogger.INSTANCE.log(new ScrewedUp(ParallelProcessor.class,
                    "It seems the parallel processor timed out"));
            status.increment(STATUS.ERROR);
            status.incrementElapsedTime(STATUS.ERROR, System.currentTimeMillis() - start_time);
            throw new YaqpException(YaqpException.CAUSE.time_out_exception);
        }

        for (int i = 0; i < size(); i++) {
            try {
                result.add(future_array[i].get());
            } catch (InterruptedException ex) {

                YaqpLogger.INSTANCE.log(new ScrewedUp(ParallelProcessor.class,
                        "Noway!!!! We didn't expect this to happen! (?)"));
            } catch (ExecutionException ex) {
                start_time = System.currentTimeMillis();
                if (failSensitive) {
                    YaqpLogger.INSTANCE.log(new ScrewedUp(ParallelProcessor.class,
                            "It seems a computation within this ParallelProcessor died."));
                    status.increment(STATUS.ERROR);
                    status.incrementElapsedTime(STATUS.ERROR, System.currentTimeMillis() - start_time);
                    throw new YaqpException();
                }
                // Computational Error in some processor & Not fail sensitive =>
                // do nothing
            }
        }
        status.setMessage("completed");
        status.completed();
        return result;
    }

    public boolean isEnabled() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setEnabled(boolean enabled) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private Callable getCallable(final JProcessor processor, final Object input) {
        Callable callable = new Callable() {

            public Object call() throws Exception {
                Object o;
                o = processor.process(input);
                return o;
            }
        };
        return callable;
    }

    public JMultiProcessorStatus getStatus() {
        return status;
    }
}
