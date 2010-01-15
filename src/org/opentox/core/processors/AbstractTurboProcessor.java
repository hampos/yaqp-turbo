package org.opentox.core.processors;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import org.opentox.core.interfaces.JTurboProcessor;

/**
 *
 * @author chung
 */
public abstract class AbstractTurboProcessor<Input, Output>
        extends Processor<Input, Output>
        implements JTurboProcessor<Input, Output>{

   private PropertyChangeSupport propertyChangeSupport;

   public AbstractTurboProcessor(){
       super();
       propertyChangeSupport =  new PropertyChangeSupport(this);
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

    

}
