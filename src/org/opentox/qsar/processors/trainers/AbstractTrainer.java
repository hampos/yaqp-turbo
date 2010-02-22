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
package org.opentox.qsar.processors.trainers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.opentox.core.exceptions.Cause;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.processors.Processor;
import org.opentox.ontology.components.QSARModel;
import org.opentox.ontology.util.AlgorithmParameter;
import org.opentox.ontology.util.vocabulary.ConstantParameters;
import org.opentox.qsar.exceptions.QSARException;
import org.opentox.qsar.interfaces.JTrainer;
import org.opentox.www.rest.components.YaqpForm;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class AbstractTrainer<Input> extends Processor<Input, QSARModel> implements JTrainer<Input, QSARModel> {

    private Map<String, AlgorithmParameter> parameters = new HashMap<String, AlgorithmParameter>();

     /**
     * The URI of the prediction feature (class attribute or target attribute) accoeding
     * to which the training is carried out.
     */
    protected String predictionFeature = null;
    /**
     * URI of the training dataset
     */
    protected String datasetUri = null;
    /**
     * Uniformly Unique Identifier used to identify the file path of the produced
     * model. This ID is stored in the database as well.
     */
    protected UUID uuid;

    public AbstractTrainer() {
        super();
        uuid = UUID.randomUUID();
    }

    public AbstractTrainer(final Map<String, AlgorithmParameter> parameters) throws QSARException {
        if (parameters==null) throw new NullPointerException("You provided 'null' as set " +
                "of parameters in a trainer");
        this.parameters = parameters;
        /* Retrieve the parameter prediction-feature */
        try {
            predictionFeature = getParameters().get(ConstantParameters.prediction_feature).paramValue.toString();
            new URI(predictionFeature);
        } catch (URISyntaxException ex) {
            throw new QSARException(Cause.XQReg200, "The prediction feature you provided is not a valid URI : {" + predictionFeature + "}", ex);
        } catch (NullPointerException ex) {
            String message = "MLR model cannot be trained because you "
                    + "did not provide the parameter " + ConstantParameters.prediction_feature;
            throw new NullPointerException(message);
        }
        /* Retrieve the parameter dataset_uri */
        try {
            datasetUri = getParameters().get(ConstantParameters.dataset_uri).paramValue.toString();
            new URI(datasetUri);
        } catch (URISyntaxException ex) {
            throw new QSARException(Cause.XQReg201, "The dataset_uri parameter you provided is not a valid URI {" + datasetUri + "}", ex);
        } catch (NullPointerException ex) {
            String message = "MLR model cannot be trained because you "
                    + "did not provide the parameter " + ConstantParameters.dataset_uri;
            throw new NullPointerException(message);
        }
        uuid = UUID.randomUUID();
    }

    @SuppressWarnings({"unchecked"})
    public AbstractTrainer(YaqpForm form) throws QSARException{
        if (form == null) throw new NullPointerException("The provided form must not be null");
        this.datasetUri = form.getFirstValue(ConstantParameters.dataset_uri);
        this.predictionFeature = form.getFirstValue(ConstantParameters.prediction_feature);
        if (datasetUri == null ) throw new QSARException(Cause.XQReg500, "The parameter "+ConstantParameters.dataset_uri+" was not specified");
        if (predictionFeature == null ) throw new QSARException(Cause.XQReg501, "The parameter "+ConstantParameters.prediction_feature+" was not specified");
        try {
            putParameter(ConstantParameters.prediction_feature, new AlgorithmParameter(new URI(this.predictionFeature)));
        } catch (URISyntaxException ex) {
            throw new QSARException(Cause.XQReg711, "Invalid URI for prediction feature {" + predictionFeature + "}", ex);
        }
        try {
            putParameter(ConstantParameters.dataset_uri, new AlgorithmParameter(new URI(this.datasetUri)));
        } catch (URISyntaxException ex) {
            throw new QSARException(Cause.XQReg712, "Invalid URI for dataset {" + datasetUri + "}", ex);
        }
        uuid = UUID.randomUUID();
    }
    

    public Map<String, AlgorithmParameter> getParameters() {
        return parameters;
    }

    public void setParameters(final Map<String, AlgorithmParameter> parameters) {
        this.parameters = parameters;
    }

    public void putParameter(String paramName, AlgorithmParameter parameter){
        this.parameters.put(paramName, parameter);
    }

    public abstract Input preprocessData(Input data) throws QSARException;

    public QSARModel process(Input data) throws YaqpException {
        if (data == null) throw new NullPointerException("Cannot training a model if " +
                "no input (training) data are provided");

        return train(preprocessData(data));
    }
}
