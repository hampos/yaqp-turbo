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
package org.opentox.qsar.processors.trainers.regression;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.opentox.config.Configuration;
import org.opentox.config.ServerFolders;
import org.opentox.core.exceptions.Cause;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.io.exceptions.YaqpIOException;
import org.opentox.core.processors.Pipeline;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.handlers.WriterHandler;
import org.opentox.db.util.TheDbConnector;
import org.opentox.io.processors.InputProcessor;
import org.opentox.io.util.ServerList;
import org.opentox.ontology.components.Feature;
import org.opentox.ontology.components.QSARModel;
import org.opentox.ontology.components.QSARModel.ModelStatus;
import org.opentox.ontology.components.User;
import org.opentox.ontology.data.DatasetBuilder;
import org.opentox.ontology.processors.InstancesProcessor;
import org.opentox.ontology.util.AlgorithmParameter;
import org.opentox.ontology.util.YaqpAlgorithms;
import org.opentox.ontology.util.vocabulary.ConstantParameters;
import org.opentox.qsar.exceptions.QSARException;
import org.opentox.qsar.processors.filters.AttributeCleanup;
import org.opentox.qsar.processors.filters.AttributeCleanup.ATTRIBUTE_TYPE;
import org.opentox.qsar.processors.filters.InstancesFilter;
import org.opentox.qsar.processors.filters.SimpleMVHFilter;
import org.opentox.qsar.processors.trainers.WekaTrainer;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Trace;
import org.opentox.www.rest.components.YaqpForm;
import weka.classifiers.functions.LinearRegression;
import weka.core.Instances;

/**
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
@SuppressWarnings({"unchecked"})
public class MLRTrainer extends WekaTrainer {

    /**
     * The URI of the prediction feature (class attribute or target attribute) accoeding
     * to which the training is carried out.
     */
    private String predictionFeature = null;
    /**
     * URI of the training dataset
     */
    private String datasetUri = null;
    /**
     * Uniformly Unique Identifier used to identify the file path of the produced
     * model. This ID is stored in the database as well.
     */
    private UUID uuid;
    private static final String PMMLIntro =
            "<PMML version=\"3.2\" "
            + " xmlns=\"http://www.dmg.org/PMML-3_2\"  "
            + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
            + " <Header copyright=\"Copyleft (c) OpenTox - An Open Source "
            + "Predictive Toxicology Framework, http://www.opentox.org, 2009\" />\n";


    /**
     * <p>Construct a new MLR trainer which is initialized with a set of parameters as
     * posted by the client to the resource (instance of {@link YaqpForm }. Some
     * checks are done regarding the consisteny of the parameters. The mandatory parameters
     * one has to specidy are the <code>prediction_feature</code> and the <code>
     * dataset_uri</code> which should be posted by the client to the training resoruce.
     * </p>
     * @param parameters
     *      Set of parameters as an instance of {@link YaqpForm }
     * @throws QSARException
     *      In case there are some inconsistencies with the posted parameters.
     * <p>
     * <table>
     * <thead>
     * <tr>
     * <td><b>Code</b></td><td><b>Explanation</b></td>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td>XQM200</td><td>The <code>prediction_feature</code> you provided is not a valid URI</td>
     * </tr>
     * <tr>
     * <td>XQM201</td><td>The <code>dataset_uri</code> you provided is not a valid URI</td>
     * </tr>
     * </tbody>
     * </table>
     * </p>
     * @throws NullPointerException
     *      In case some mandatory parameters are not specified or
     *      are set to null (For example if one does not specify the
     *      <code>prediction_feature</code>).
     */
    public MLRTrainer(YaqpForm form) throws QSARException {
        this.datasetUri = form.getFirstValue(ConstantParameters.dataset_uri);
        this.predictionFeature = form.getFirstValue(ConstantParameters.prediction_feature);
        System.out.println(predictionFeature);
        if (datasetUri == null ) throw new QSARException(Cause.XQM500, "The parameter "+ConstantParameters.dataset_uri+" was not specified");
        if (predictionFeature == null ) throw new QSARException(Cause.XQM501, "The parameter "+ConstantParameters.prediction_feature+" was not specified");
        Map<String, AlgorithmParameter> m = new HashMap<String, AlgorithmParameter>(2);
        try {
            m.put(ConstantParameters.prediction_feature, new AlgorithmParameter(new URI(this.predictionFeature)));
        } catch (URISyntaxException ex) {
            throw new QSARException(Cause.XQM711, "Invalid URI for prediction feature {" + predictionFeature + "}", ex);
        }
        try {
            m.put(ConstantParameters.dataset_uri, new AlgorithmParameter(new URI(this.datasetUri)));
        } catch (URISyntaxException ex) {
            throw new QSARException(Cause.XQM712, "Invalid URI for dataset {" + datasetUri + "}", ex);
        }
        uuid = UUID.randomUUID();
    }

    /**
     * <p>Construct a new MLR trainer which is initialized with a set of parameters. Some
     * checks are done regarding the consisteny of the parameters. The mandatory parameters
     * one has to specidy are the <code>prediction_feature</code> and the <code>
     * dataset_uri</code> which should be posted by the client to the training resoruce.
     * </p>
     * @param parameters
     *      Set of parameters as a Map&lt;String,{@link  AlgorithmParameter }&gt;.
     * @throws QSARException
     *      In case there are some inconsistencies with the posted parameters.
     * <p>
     * <table>
     * <thead>
     * <tr>
     * <td><b>Code</b></td><td><b>Explanation</b></td>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td>XQM200</td><td>The <code>prediction_feature</code> you provided is not a valid URI</td>
     * </tr>
     * <tr>
     * <td>XQM201</td><td>The <code>dataset_uri</code> you provided is not a valid URI</td>
     * </tr>
     * </tbody>
     * </table>
     * </p>
     * @throws NullPointerException
     *      In case some mandatory parameters are not specified or
     *      are set to null (For example if one does not specify the
     *      <code>prediction_feature</code>).
     */
    public MLRTrainer(Map<String, AlgorithmParameter> parameters) throws QSARException {
        super(parameters);
        /* Retrieve the parameter prediction-feature */
        try {
            predictionFeature = getParameters().get(ConstantParameters.prediction_feature).paramValue.toString();
            new URI(predictionFeature);
        } catch (URISyntaxException ex) {
            throw new QSARException(Cause.XQM200, "The prediction feature you provided is not a valid URI : {" + predictionFeature + "}", ex);
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
            throw new QSARException(Cause.XQM201, "The dataset_uri parameter you provided is not a valid URI {" + datasetUri + "}", ex);
        } catch (NullPointerException ex) {
            String message = "MLR model cannot be trained because you "
                    + "did not provide the parameter " + ConstantParameters.dataset_uri;
            throw new NullPointerException(message);
        }
        uuid = UUID.randomUUID();
    }

    /**
     *
     * Construct a new MLRTrainer object. No parameters are specified for the trainer.
     * A new <code>UUID</code> is chosen.
     */
    public MLRTrainer() {
        super();
        uuid = UUID.randomUUID();
    }

    /**
     * Trains the MLR model given an Instances object with the training data. The prediction
     * feature (class attributre) is specified in the constructor of the class.
     * @param data The training data as <code>weka.core.Instances</code> object.
     * @return The QSARModel corresponding to the trained model.
     * @throws QSARException In case the model cannot be trained
     * <p>
     * <table>
     * <thead>
     * <tr>
     * <td><b>Code</b></td><td><b>Explanation</b></td>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td>XQM1</td><td>Could not train the an model</td>
     * </tr>
     * <tr>
     * <td>XQM2</td><td>Could not generate PMML representation for the model</td>
     * </tr>
     * <tr>
     * <td>XQM202</td><td>The prediction feature you provided is not a valid numeric attribute of the dataset</td>
     * </tr>
     * </tbody>
     * </table>
     * </p>
     * @throws NullPointerException
     *      In case the provided training data is null.
     */
    public QSARModel train(Instances data) throws QSARException {
        if (data == null) {
            throw new NullPointerException("Cannot train an "
                    + "MLR model without a training dataset");
        }
        if (data.attribute(predictionFeature) == null) {
            throw new QSARException(Cause.XQM202,
                    "The prediction feature you provided is not a valid numeric attribute of the dataset :{"
                    + predictionFeature + "}");
        }
        /* The incoming dataset always has the first attribute set to 
        'compound_uri' which is of type "String". This is removed at the
        begining of the training procedure */
        AttributeCleanup filter = new AttributeCleanup(ATTRIBUTE_TYPE.string);
        // NOTE: Removal of string attributes should be always performed prior to any kind of training!
        data = filter.filter(data);
        SimpleMVHFilter fil = new SimpleMVHFilter();
        data = fil.filter(data);
        data.setClass(data.attribute(predictionFeature.toString()));
        LinearRegression linreg = new LinearRegression();
        String[] linRegOptions = {"-S", "1", "-C"};
        try {
            linreg.setOptions(linRegOptions);
            linreg.buildClassifier(data);
        } catch (Exception ex) {// illegal options or could not build the classifier!
            String message = "MLR Model could not be trained";
            YaqpLogger.LOG.log(new Trace(getClass(), message + " :: " + ex));
            throw new QSARException(Cause.XQM1, message, ex);
        }
        try {
            generatePMML(linreg, data);
        } catch (YaqpIOException ex) {
            String message = "Could not generate PMML representation for MLR model :: " + ex;
            throw new QSARException(Cause.XQM2, message, ex);
        }


        ArrayList<Feature> independentFeatures = new ArrayList<Feature>();
        for (int i = 0; i < data.numAttributes(); i++) {
            Feature f = new Feature(data.attribute(i).name());
            try {
                f = (Feature) WriterHandler.add(f);
            } catch (YaqpException ex) {
            }
            if (data.classIndex() != i) {
                independentFeatures.add(f);
            }
        }
        Feature dependentFeature = new Feature(data.classAttribute().name());
        Feature predictedFeature = dependentFeature;
        try {
            WriterHandler.add(dependentFeature);
        } catch (YaqpException ex) {
        }

        try {
            User u = new User();
            u.setEmail("makis@foo.goo.gr");
            QSARModel model = new QSARModel(
                    uuid.toString(), predictedFeature, dependentFeature,
                    independentFeatures, YaqpAlgorithms.MLR,
                    u, null, datasetUri, ModelStatus.UNDER_DEVELOPMENT);
            model.setParams(new HashMap<String, AlgorithmParameter>());
            try {
                model = (QSARModel) WriterHandler.add(model);
            } catch (DbException ex) {
                System.out.println("Exception Caught!" + ex);
            }
            return model;
        } catch (YaqpException ex) {
            throw new QSARException();
        }

    }

    /**
     * Generates the PMML representation of the model and stores in the hard
     * disk.
     * @param coefficients The vector of the coefficients of the MLR model.
     * @param model_id The id of the generated model.
     * TODO: build the XML using some XML editor
     */
    // <editor-fold defaultstate="collapsed" desc="PMML generation routine!">
    private void generatePMML(LinearRegression wekaModel, Instances data) throws YaqpIOException {
        double[] coefficients = wekaModel.coefficients();
        StringBuilder pmml = new StringBuilder();
        pmml.append("<?xml version=\"1.0\" ?>");
        pmml.append(PMMLIntro);
        pmml.append("<Model ID=\"" + uuid.toString() + "\" Name=\"MLR Model\">\n");
        pmml.append("<AlgorithmID href=\"" + Configuration.baseUri + "/algorithm/mlr\"/>\n");
        pmml.append("<DatasetID href=\"" + datasetUri + "\"/>\n");
        pmml.append("<AlgorithmParameters />\n");
        pmml.append("<FeatureDefinitions>\n");
        for (int k = 1; k <= data.numAttributes(); k++) {
            pmml.append("<link href=\"" + data.attribute(k - 1).name() + "\"/>\n");
        }
        pmml.append("<target index=\"" + data.attribute(predictionFeature).index() + "\" name=\""
                + predictionFeature + "\"/>\n");
        pmml.append("</FeatureDefinitions>\n");
        pmml.append("<Timestamp>" + java.util.GregorianCalendar.getInstance().getTime() + "</Timestamp>\n");
        pmml.append("</Model>\n");

        pmml.append("<DataDictionary numberOfFields=\"" + data.numAttributes() + "\" >\n");
        for (int k = 0; k
                <= data.numAttributes() - 1; k++) {
            pmml.append("<DataField name=\"" + data.attribute(k).name()
                    + "\" optype=\"continuous\" dataType=\"double\" />\n");
        }
        pmml.append("</DataDictionary>\n");
        // RegressionModel
        pmml.append("<RegressionModel modelName=\"" + uuid.toString() + "\""
                + " functionName=\"regression\""
                + " modelType=\"linearRegression\""
                + " algorithmName=\"linearRegression\""
                + " targetFieldName=\"" + data.classAttribute().name() + "\""
                + ">\n");
        // RegressionModel::MiningSchema
        pmml.append("<MiningSchema>\n");
        for (int k = 0; k <= data.numAttributes() - 1; k++) {
            if (k != data.classIndex()) {
                pmml.append("<MiningField name=\""
                        + data.attribute(k).name() + "\" />\n");
            }
        }
        pmml.append("<MiningField name=\""
                + data.attribute(data.classIndex()).name() + "\" "
                + "usageType=\"predicted\"/>\n");
        pmml.append("</MiningSchema>\n");
        // RegressionModel::RegressionTable
        pmml.append("<RegressionTable intercept=\"" + coefficients[coefficients.length - 1] + "\">\n");

        for (int k = 0; k
                <= data.numAttributes() - 1; k++) {

            if (!(predictionFeature.equals(data.attribute(k).name()))) {
                pmml.append("<NumericPredictor name=\""
                        + data.attribute(k).name() + "\" "
                        + " exponent=\"1\" "
                        + "coefficient=\"" + coefficients[k] + "\"/>\n");
            }
        }
        pmml.append("</RegressionTable>\n");
        pmml.append("</RegressionModel>\n");
        pmml.append("</PMML>\n\n");
        try {
            FileWriter fwriter = new FileWriter(ServerFolders.models_pmml + "/" + uuid.toString());
            BufferedWriter writer = new BufferedWriter(fwriter);
            writer.write(pmml.toString());
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            throw new YaqpIOException(Cause.XQM3, "Could not write data to PMML file :" + uuid.toString(), ex);
        }
    }
    // </editor-fold>

    public static void main(String args[]) throws QSARException, YaqpException, URISyntaxException {
        TheDbConnector.init();
        final InputProcessor p1 = new InputProcessor();
        final DatasetBuilder p2 = new DatasetBuilder();
        final InstancesProcessor p3 = new InstancesProcessor();
        final InstancesFilter p4 = new SimpleMVHFilter();
        final InstancesFilter p5 = new AttributeCleanup(new ATTRIBUTE_TYPE[]{ATTRIBUTE_TYPE.string, ATTRIBUTE_TYPE.nominal});
        final Map<String, AlgorithmParameter> params = new HashMap<String, AlgorithmParameter>();
        params.put("prediction_feature", new AlgorithmParameter<String>(ServerList.ambit + "/feature/11954"));
        params.put("dataset_uri", new AlgorithmParameter<String>("http://localhost/6"));
        final WekaTrainer p6 = new MLRTrainer(params);

        final Pipeline pipe = new Pipeline();
        pipe.add(p1);
        pipe.add(p2);
        pipe.add(p3);
        pipe.add(p4);
        pipe.add(p5);
        pipe.add(p6);

        URI u = new URI("http://localhost/6");

        pipe.setfailSensitive(true);
        final QSARModel model = (QSARModel) pipe.process(u);
        System.out.println(model.getCode());
        System.out.println(model.getId());
        System.out.println(pipe.getStatus());
    }
}
