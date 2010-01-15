package org.opentox.core.processors;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import org.opentox.core.interfaces.JMultiProcessor;
import org.opentox.core.interfaces.JMultiProcessorStatus;
import org.opentox.core.interfaces.JProcessor;
import org.opentox.core.util.MultiProcessorStatus;

/**
 *
 * @author chung
 */
public abstract class AbstractMultiProcessor<Input, Output, P extends JProcessor<Input, Output>>
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
    private boolean failSensitive = false;

    public AbstractMultiProcessor() {
        super();
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

    public MultiProcessorStatus getStatus() {
        return (MultiProcessorStatus) status;
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
    // public abstract Output process(Input data) throws YaqpException;

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
}
