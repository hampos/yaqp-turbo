/*
 *
 * YAQP - Yet Another QSAR Project:
 * Machine Learning algorithms designed for the prediction of toxicological
 * features of chemical compounds become available on the Web. Yaqp is developed
 * under OpenTox (http://opentox.org) which is an FP7-funded EU research project.
 * This project was developed at the Automatic Control Lab in the Chemical Engineering
 * School of the National Technical University of Athens. Please read README for more
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

package org.opentox.ontology.interfaces;

import org.opentox.ontology.exceptions.YaqpOntException;

/**
 * Interface for a generic ontological processor. The processor <code>converts</code>
 * its input to a specified Output.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 *
 * @param <Input> Input Type.
 * @param <Output> Conversion Target Datatype.
 */
public interface JOntProcessor<Input , Output> {

    /**
     *
     * Converts the input data to the requested output.
     * @param data Input data to be converted.
     * @return The input data converted to some other data type.
     * @throws YaqpOntException in case the conversion is not feasible or some
     * error occurs.
     */
    Output convert(Input data) throws YaqpOntException;
}
