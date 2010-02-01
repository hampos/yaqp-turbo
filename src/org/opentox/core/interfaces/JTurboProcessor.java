/*
 * YAQP - Yet Another QSAR Project: Machine Learning algorithms designed for
 * the prediction of toxicological features of chemical compounds become
 * available on the Web. Yaqp is developed under OpenTox (http://opentox.org)
 * which is an FP7-funded EU research project.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
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
 */
package org.opentox.core.interfaces;

import java.beans.PropertyChangeListener;

/**
 * An interface for processor with change support. Contains methods to add and
 * remove property listeners for specific properties.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface JTurboProcessor<Input, Output> extends JProcessor<Input, Output> {

    void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener);

    void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener);

    void addPropertyChangeListener(final PropertyChangeListener listener);

    void removePropertyChangeListener(final PropertyChangeListener listener);
    
    void firePropertyChange(String propertyName, Object oldValue, Object newValue);


}
