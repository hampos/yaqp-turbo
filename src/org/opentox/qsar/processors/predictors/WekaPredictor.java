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
package org.opentox.qsar.processors.predictors;

import java.util.ArrayList;
import org.opentox.core.exceptions.Cause;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.processors.Processor;
import org.opentox.db.handlers.ReaderHandler;
import org.opentox.db.util.Page;
import org.opentox.ontology.components.ComponentList;
import org.opentox.ontology.components.Feature;
import org.opentox.ontology.components.QSARModel;
import org.opentox.ontology.components.YaqpComponent;
import org.opentox.qsar.exceptions.QSARException;
import weka.core.Instances;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
abstract public class WekaPredictor extends Processor<Instances, Instances> {

    protected WekaPredictor(){
        
    }

    /**
     *
     * The QSAR model to be used by the predictor.
     */
    protected QSARModel model;

    /**
     * Construct a new QSAR predictor given a QSARModel. The main information needed
     * in the QSARModel are the code (i.e. a pointer to the filepath where the model is) and
     * the algorithm used to train the model.
     * @param model
     *      Prototype for a model in the database.
     * @throws QSARException
     *      In case the provided prototype does not correspond to a database entry for
     *      a QSARModel
     */
    public WekaPredictor(QSARModel model) throws QSARException {
        if (model == null) {
            throw new NullPointerException("Cannot predict from a null model - Provide a model first");
        }
        this.model = model;
        // CHECK IF THE PROVIDED MODEL CONTAINS THE CODE OF THE QSAR MODEL.
        if (model.getCode() == null) {
            throw new QSARException(Cause.XQPred630,
                    "Unknown Model - Probably the model was deleted or does not exist");
        }
        // CHECK IF THE PROVIDED MODEL PROTOTYPE CORRESPONDS TO A
        if (!modelExists()) {
            throw new QSARException(Cause.XQPred631, "This model does not exist");
        }
    }

    /**
     *
     * Checks whether the requested model exists in the database. If not, a status
     * code 404 (not found) should be returned to the client.
     * @return
     *      <code>true</code> if the model is found, <code>false</code> otherwise.
     */
    private boolean modelExists() {
        QSARModel prototype = new QSARModel();
        prototype.setId(model.getId());
        try {
            ComponentList<YaqpComponent> list = ReaderHandler.search(model, new Page(), false);

            if (list.size() > 0) {
                this.model = (QSARModel) list.getFirst();
                return true;
            }
        } catch (YaqpException ex) {
            // 
        }
        return false;
    }

    private ArrayList<String> missingFeatures(Instances data) {
        ArrayList<String> missing = new ArrayList<String>();
        for (Feature f : model.getIndependentFeatures()) {
            if (data.attribute(f.getURI()) == null) {
                missing.add(f.getURI());
            }
        }
        return missing;
    }

    /**
     * Perform the prediction using the QSARModel provided upon construction of this object
     * @param data
     *      Data submitted to the predictor to produce predictions.
     * @return
     *      Instances containing the predicted values for every compound in the initial
     *      given set of data.
     * @throws YaqpException
     *      In case the prediction is infeasible, for example if the independent
     *      features of the QSAR model are not included in the given dataset.
     */
    public Instances process(Instances data) throws YaqpException {

        
        if (model.getAlgorithm() == null || model.getAlgorithm().getMeta() == null || model.getAlgorithm().getMeta().getName() == null) {
            throw new QSARException(Cause.XQPred631, "Unknown Model - Probably the model was deleted or does not exist");
        }
        ArrayList<String> missinfFeatures = missingFeatures(data);
        if (missinfFeatures.size() > 0) {
            final String NEWLINE = "\n";
            String message = "The dataset you provided is incompatible with this model because it does not "
                    + "contain the following features : " + NEWLINE;
            for (String missing : missinfFeatures) {
                message += missing + NEWLINE;
            }
            throw new QSARException(Cause.XQPred632, message);
        }
        return predict(data);
    }

    /**
     * Performs the prediction.
     * @param data
     *      Dataset for which the predictions are requested.
     * @return
     *      Dataset with the predictions.
     */
    public abstract Instances predict(Instances data) throws QSARException;
}
