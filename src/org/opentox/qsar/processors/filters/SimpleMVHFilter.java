/*
 *
 *   ._.  ._.      ,__,   ._____.
 *    \ \ | |/\   /    \ /  ___ |
 *     \ \| /  \ |  ()  |  _____|
 *      \  / /\ \|  {}  | |
 *       || .--. |  [] \\ |
 *       ||_|  |_|\____/\\|      --.-.--.-...-..--..-.
 *
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
import org.opentox.qsar.exceptions.QSARException;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;

/**
 *
 * This filter is used to handle missing values in a weka dataset. The missing values
 * are substituted by some characteristic value which is calculated by all other
 * values in the dataset for the related attribute. The mean and the median are
 * mostly used.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class SimpleMVHFilter extends InstancesFilter {
    

    @Override
    public Instances filter(Instances data) throws QSARException {
        ReplaceMissingValues replacer = new ReplaceMissingValues();
        try {
            replacer.setInputFormat(data);
            return ReplaceMissingValues.useFilter(data, replacer);
        } catch (Exception ex) {
            String message = "Cannot apply missing values filtering";
            throw new QSARException(Cause.XQF412, message, ex);
        }

    }
}
