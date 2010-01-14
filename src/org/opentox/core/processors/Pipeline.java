package org.opentox.core.processors;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.interfaces.JMultiProcessor;
import org.opentox.core.interfaces.JMultiProcessorStatus;
import org.opentox.core.interfaces.JMultiProcessorStatus.STATUS;
import org.opentox.core.interfaces.JProcessor;
import org.opentox.core.util.MultiProcessorStatus;
import org.opentox.util.logging.levels.Debug;
import org.opentox.util.logging.levels.Trace;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.ScrewedUp;

/**
 * A set of jobs to be executed through a pipeline of processors
 * is a sequential layout if some processor is enabled. Note that if P1, P2, ..., PN
 * is a sequence of processors building a pipeline, then the output type from a processor
 * P_k should be a valid input to P_k+1 otherwise the processor P_k+1 will throw an
 * exception.
 * @param  <Input> Type for the input to the pipeline
 * @param  <Output> Type for the output of the pipeline
 * @param  <P> Type of processors contained in the pipeline
 * @author Sopasakis Pantelis
 * @author Charalampos Chomenides
 */
public class Pipeline<Input, Output, P extends JProcessor>
        extends ArrayList<P>
        implements JMultiProcessor<Input, Output, P> {

    
    /**
     * The status of the pipeline including statistics about the
     * processes running within the pipeline.
     */
    private JMultiProcessorStatus status = new MultiProcessorStatus();
    /**
     * Property Change Support for this class.
     */
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    /**
     * A flag to denote if the pipeline is fail safe, i.e. if some
     * of its processes fails, should the whole pipeline be aborted.
     * If set true, the pipeline is rendered fail-sensitive.
     */
    private boolean failSensitive = true;
    private String PIPELINE_STATUS_PROPERTY = status.getClass().getName();

    /**
     * Constructs a new Pipeline which is an ordered sequence of Processors
     * that are used to process input data sequentially (if enabled)
     */
    public Pipeline() {
        super();
        status = new MultiProcessorStatus();
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    /**
     * Returns true if the pipeline is fail-sensitive. If yes, any
     * Exception thrown by any process in the pipeline, aborts the
     * whole execution and proliferates the exception to the pipeline itself, which
     * means that the execution of the pipeline will stop if an exception is caught
     * in any of its processors. Otherwise, if the pipeline is not fai-sensitive,
     * the result from a process overrides any failed error-state proceeses and
     * moves on to the next processor.
     * @return true if the pipeline is failsensitive.
     */
    public boolean isfailSensitive() {
        return failSensitive;
    }

    /**
     * Sets the file-sensitive flag of the pipeline to true of false accordingly.
     * If set to false, the pipeline becomes fail-safe which means that it continues to
     * process the input data overriding any error-state procceses.
     * @param failSafe
     */
    public void setfailSensitive(boolean failSafe) {
        this.failSensitive = failSafe;
    }

    /**
     * Returns the status of the pipeline as an instance of MultiProcessorStatus, an object
     * that encapsulates statistics and valuable information about the pipeline such
     * as the number of processes in error state.
     * @return The pipeline's status as a MultiProcessorStatus object.
     * @see org.opentox.core.interfaces.JMultiProcessorStatus Interface for the pipeline status
     * @see org.opentox.core.interfaces.JPipeline Interface for the pipeline
     */
    public JMultiProcessorStatus getStatus() {
        return (MultiProcessorStatus) status;
    }

    /**
     *
     * TODO: Important! Implementation not finished!
     *
     * This method processes the input data to produce some output,
     * using the sequence of processors. The result from the first processor,
     * is input to the second and so on. Any non-enabled processors are overriden.
     * @param data
     * @return
     * @throws YaqpException
     */
    public Output process(Input data) throws YaqpException {

        long start_time = 0;
        status.setMessage("pipeline in process");
        Object o = data;
        // Process:
        for (int i = 0; i < size(); i++) {
            try {
                if (get(i).isEnabled()) {
                    start_time = System.currentTimeMillis();
                    status.increment(STATUS.INITIALIZED);
                    o = get(i).process(o); // --> this might throw a YaqpException
                    status.increment(STATUS.PROCESSED);
                    status.incrementElapsedTime(STATUS.PROCESSED, System.currentTimeMillis() - start_time);
                    propertyChangeSupport.firePropertyChange(PIPELINE_STATUS_PROPERTY, null, status);
                }
            } catch (Exception exc) {
                if (failSensitive) {
                    YaqpLogger.INSTANCE.log(new Debug(Pipeline.class,
                            "Processor " + i + " is in error state!"));
                    throw new YaqpException();
                }
                status.increment(STATUS.ERROR);
                status.incrementElapsedTime(STATUS.ERROR, System.currentTimeMillis() - start_time);
                YaqpLogger.INSTANCE.log(new Trace(Pipeline.class,
                        "Processor " + i + " is in error state!"));
            }
        }
        status.setMessage("Pipeline completed the job.");
        status.completed();
        propertyChangeSupport.firePropertyChange(PIPELINE_STATUS_PROPERTY, null, status);
        // Try to cast the result as 'Output'
        try {
            return (Output) o;
        } catch (Exception exc) {
            YaqpLogger.INSTANCE.log(new ScrewedUp( Pipeline.class,
                    YaqpException.CAUSE.pipeline_output_typecasting.toString()));
            throw new YaqpException(YaqpException.CAUSE.pipeline_output_typecasting);
        }
    }

    /**
     *
     * @return true if the pipeline if enabled, false otherwise
     */
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
     * processors in the pipeline.
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

   
}
