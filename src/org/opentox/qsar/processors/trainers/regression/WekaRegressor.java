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


package org.opentox.qsar.processors.trainers.regression;

import java.util.Map;
import org.opentox.core.exceptions.Cause;
import org.opentox.ontology.components.QSARModel;
import org.opentox.ontology.util.AlgorithmParameter;
import org.opentox.qsar.exceptions.QSARException;
import org.opentox.qsar.processors.filters.AttributeCleanup;
import org.opentox.qsar.processors.filters.AttributeCleanup.ATTRIBUTE_TYPE;
import org.opentox.qsar.processors.filters.SimpleMVHFilter;
import org.opentox.qsar.processors.trainers.AbstractTrainer;
import org.opentox.qsar.processors.trainers.WekaTrainer;
import org.opentox.www.rest.components.YaqpForm;
import weka.core.Instances;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class WekaRegressor extends WekaTrainer {

    public WekaRegressor(final YaqpForm form) throws QSARException {
        super(form);
    }

    public WekaRegressor(final Map<String, AlgorithmParameter> parameters) throws QSARException {
        super(parameters);
    }

    public WekaRegressor() {
        super();
    }



    @Override
    public Instances preprocessData(Instances data) throws QSARException {
       /* Check if data == null*/
        if (data == null) throw new NullPointerException("Trainers do not accept null datasets.");

        /* The incoming dataset always has the first attribute set to
        'compound_uri' which is of type "String". This is removed at the
        begining of the training procedure */
        AttributeCleanup filter = new AttributeCleanup(ATTRIBUTE_TYPE.string);
        // NOTE: Removal of string attributes should be always performed prior to any kind of training!
        data = filter.filter(data);
        SimpleMVHFilter fil = new SimpleMVHFilter();
        data = fil.filter(data);
        if (data.attribute(predictionFeature) == null) {
            throw new QSARException(Cause.XQReg202,
                    "The prediction feature you provided is not a valid numeric attribute of the dataset :{"
                    + predictionFeature + "}");
        }
        return data;

    }


}