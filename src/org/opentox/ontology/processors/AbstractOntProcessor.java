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

package org.opentox.ontology.processors;

import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.processors.Processor;
import org.opentox.ontology.components.YaqpOntComponent;
import org.opentox.ontology.interfaces.JOntProcessor;

/**
 * Abstract Ontological Processor. Processes an Ontological Component (e.g. a Dataset,
 * a Feature, a Task etc) to produce some output.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class AbstractOntProcessor<Input extends YaqpOntComponent, Output>
        extends Processor<Input, Output>
        implements JOntProcessor<Input, Output> {

    /**
     * The processor invokes the method <code>convert</code> defined at
     * {@link JOntProcessor#convert(java.lang.Object) JOntProcessor}
     * @param data
     * @return
     * @throws YaqpException
     */
    public Output process(Input data) throws YaqpException {
        Output o = convert(data);
        return (Output ) o;
    }



}
