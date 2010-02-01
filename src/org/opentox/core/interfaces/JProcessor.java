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

import org.opentox.core.exceptions.YaqpException;

/**
 * A Processor is any class having the ability to process some input
 * and produce some output provided that it is enabled.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface JProcessor<InputData, Result>
        {

    /**
     * Processes some input data to produce some output.
     * @param data Input data.
     * @return The output of the processor
     * @throws YaqpException Exception is thrown if the processor is unable
     * to produce a result for the given data.
     */
    Result process(InputData data) throws YaqpException;
    

    /**
     *
     * @return true if the processor is enabled, false otherwise.
     */
    boolean isEnabled();

    /**
     * A Processor will process the input only if it is enabled. Using
     * this method you can enable/disable the processor
     * @param enabled
     */
    void setEnabled(boolean enabled);

    
    boolean isSynchronized();

    void setSynchronized(boolean synch);

}
