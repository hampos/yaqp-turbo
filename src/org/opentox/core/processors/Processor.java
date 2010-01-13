package org.opentox.core.processors;


import org.opentox.core.interfaces.JProcessor;


/**
 *
 * This is an abstract implementation of JProcessor corresponding to a Processor
 * that is by default enabled; of course one can modify this.
 * @author chung
 */
public abstract class Processor<InputData, Result> implements JProcessor<InputData, Result> {

    private boolean enabled = true;

    public Processor() {
        super();
    }

    public boolean isEnabled(){
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
   
  
}
