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
package org.opentox.core.processors;

import java.util.ArrayList;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.interfaces.JBatchProcessor;
import org.opentox.core.interfaces.JProcessor;
import org.opentox.core.util.MultiProcessorStatus;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class AbstractBatchProcessor<Input, Output, P extends JProcessor<Input, Output>>
        extends AbstractTurboProcessor<ArrayList<Input>, ArrayList<Output>>
        implements JBatchProcessor<Input, Output, P> {

    private P processor;
    private boolean failSensitive = false;
    private MultiProcessorStatus status = new MultiProcessorStatus();

    public AbstractBatchProcessor() {
    }

    public AbstractBatchProcessor(final P processor) {
        this();
        this.processor = processor;
    }

    public abstract ArrayList<Output> process(ArrayList<Input> data) throws YaqpException;

    
    public MultiProcessorStatus getStatus() {
        return status;
    }

    public boolean isfailSensitive() {
        return failSensitive;
    }

    public void setfailSensitive(boolean failSensitive) {
        this.failSensitive = failSensitive;
    }

    public void setProcessor(final P processor) {
        this.processor = processor;
    }

    public P getProcessor() {
        return processor;
    }

   


}
