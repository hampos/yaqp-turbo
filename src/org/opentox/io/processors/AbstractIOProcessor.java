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
package org.opentox.io.processors;

import org.opentox.core.exceptions.YaqpException;
import org.opentox.io.exceptions.YaqpIOException;
import org.opentox.core.processors.Processor;
import org.opentox.io.interfaces.JIOProcessor;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.ScrewedUp;
import static org.opentox.core.exceptions.Cause.XONT3;
/**
 *
 * @author Charalampos Chomenides
 */
public abstract class AbstractIOProcessor<Input, Output>
        extends Processor<Input, Output>
        implements JIOProcessor<Input, Output> {

    public AbstractIOProcessor() {
        super();
    }

    public Output process(Input data) throws YaqpIOException, YaqpException {
        if (data==null){
            throw new NullPointerException("NULL input to IO Processor");
        }
        try {
            return handle(data);
        } catch (YaqpException x) {
            YaqpLogger.LOG.log(new ScrewedUp(getClass(), x.toString()));
            throw x;
        }
    }

}
