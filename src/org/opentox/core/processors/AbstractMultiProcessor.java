/*
 *
 * YAQP - Yet Another QSAR Project:
 * Machine Learning algorithms designed for the prediction of toxicological
 * features of chemical compounds become available on the Web. Yaqp is developed
 * under OpenTox (http://opentox.org) which is an FP7-funded EU research project.
 * This project was developed at the Automatic Control Lab in the Chemical Engineering
 * School of National Technical University of Athens. Please read README for more
 * information.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact:
 * Pantelis Sopasakis
 * chvng@mail.ntua.gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 */


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
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
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

    private boolean isSynchronized = false;

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

    public boolean isSynchronized() {
        return isSynchronized;
    }

    public void setSynchronized(boolean synch) {
        this.isSynchronized = synch;
    }



}
