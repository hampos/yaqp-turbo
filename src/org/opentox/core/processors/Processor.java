package org.opentox.core.processors;


import org.opentox.core.interfaces.JProcessor;

/**
 *
 * This is an abstract implementation of JProcessor corresponding to a Processor
 * that is by default enabled; of course one can modify this.
 * @author chung
 */
public abstract class Processor<InputData, Result> implements JProcessor<InputData, Result> {

    /**
     * A flag that is used to switch on and off the Processor.
     */
    private boolean enabled = true;

    

    /**
     * Initializes a new Processor which is by default enabled.
     */
    public Processor() {
        super();
        
    }

    /**
     *
     * @return true if the processor is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Enable or disable the processor
     * @param enabled true if you want to enable the processor, false otherwise.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }



}
