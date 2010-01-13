package org.opentox.core.processors;

import java.util.ArrayList;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.interfaces.JProcessor;

/**
 * A set of jobs to be executed through a pipeline of processors
 * is a sequential layout if some processor is enabled.
 * @author chung
 */
public class Pipeline<Input, Output, P extends JProcessor>
        extends ArrayList<P>
        implements JProcessor<Input, Output> {

    /**
     * Constructs a new Pipeline which is an ordered sequence of Processors
     * that are used to process input data sequentially (if enabled)
     */
    public Pipeline() {
        super();
    }
    /**
     * A flag to denote if the pipeline is fail safe, i.e. if some
     * of its processes fails, should the whole pipeline be aborted.
     * If set true, the pipeline is rendered fail-sensitive.
     */
    private boolean failSensitive = true;

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
     * This method processes the input data to produce some output,
     * using the sequence of processors. The result from the first processor,
     * is input to the second and so on. Any non-enabled processors are overriden.
     * @param data
     * @return
     * @throws YaqpException
     */
    public Output process(Input data) throws YaqpException {
        Object o = data;
       // Process:
        for (int i = 0; i < size(); i++) {
            try {
                if (get(i).isEnabled()) {
                    o = get(i).process(o);
                }
            } catch (Exception exc) {
                if (failSensitive) {
                    throw new YaqpException();
                }
                // TODO log it!
            }
        }
        // Try to cast the result as
        try {
            return (Output) o;
        } catch (Exception exc) {
            throw new YaqpException();
            // TODO log it!
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


}
