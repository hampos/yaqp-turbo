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


package org.opentox.qsar.processors.filters;

import org.opentox.core.exceptions.Cause;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.processors.Processor;
import org.opentox.qsar.exceptions.QSARException;

/**
 *
 * A Filter is a processor which in general accepts a certain input, processes
 * it, and produces an output of the same datatype. In YAQP, filters are used to
 * manipulate datasets. Filtering is applied to datasets in order to remove certain
 * type of attributes (e.g. removal of strings), perform supervised or unsupervised
 * feature selection, or fix issues with missing values.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 * @param <F> Datatype for the filtered entity.
 */
public abstract class AbstractFilter<F> extends Processor<F, F>{

    public AbstractFilter() {
        super();
    }
    
    public F process(F data) throws YaqpException {
        if (data!=null){
            return filter(data);
        }
        throw new YaqpException(Cause.XQF1, "Cannot filter null data");
    }

    /**
     *
     * @param data a dataset to be filtered
     * @return the filtered data
     */
    public abstract F filter(F data) throws QSARException;


}