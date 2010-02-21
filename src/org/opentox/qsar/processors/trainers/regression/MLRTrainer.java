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
import java.io.File;
import java.io.FileOutputStream;
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
import org.opentox.qsar.exceptions.QSARException;
import org.opentox.qsar.processors.filters.AttributeCleanup;
import org.opentox.qsar.processors.filters.AttributeCleanup.ATTRIBUTE_TYPE;
import org.opentox.qsar.processors.filters.InstancesFilter;
import org.opentox.qsar.processors.filters.SimpleMVHFilter;
import org.opentox.qsar.processors.trainers.WekaTrainer;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Trace;
import org.opentox.www.rest.components.YaqpForm;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

/**
 *
 * This MLR Trainer accepts the training data as instances and produces a model file
 * which is saved in the corresponding folder on the server for weka serialized models.
 * What is more, a PMML file is generated and stored as well. 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
@SuppressWarnings({"unchecked"})
public class MLRTrainer extends WekaRegressor {

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
     * <td>XQReg200</td><td>The <code>prediction_feature</code> you provided is not a valid URI</td>
     * </tr>
     * <tr>
     * <td>XQReg201</td><td>The <code>dataset_uri</code> you provided is not a valid URI</td>
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
        super(form);
    }

    /**
     * <p>This is an auxiliary constructor for MLR Trainer, mainly from testing purposes
     * and is equivalent to {@link MLRTrainer#MLRTrainer(org.opentox.www.rest.components.YaqpForm) }.
     * Construct a new MLR trainer which is initialized with a set of parameters. Some
     * checks are done regarding the consisteny of the parameters. The mandatory parameters
     * one has to specidy are the <code>prediction_feature</code> and the <code>
     * dataset_uri</code> which should be posted by the client to the training resource.
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
     * <td>XQReg200</td><td>The <code>prediction_feature</code> you provided is not a valid URI</td>
     * </tr>
     * <tr>
     * <td>XQReg201</td><td>The <code>dataset_uri</code> you provided is not a valid URI</td>
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
    }

    /**
     *
     * Construct a new MLRTrainer object. No parameters are specified for the trainer.
     * A new <code>UUID</code> is chosen.
     */
    public MLRTrainer() {
        super();
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
     * <td>XQReg1</td><td>Could not train the an model</td>
     * </tr>
     * <tr>
     * <td>XQReg2</td><td>Could not generate PMML representation for the model</td>
     * </tr>
     * <tr>
     * <td>XQReg202</td><td>The prediction feature you provided is not a valid numeric attribute of the dataset</td>
     * </tr>
     * </tbody>
     * </table>
     * </p>
     * @throws NullPointerException
     *      In case the provided training data is null.
     */
    public QSARModel train(Instances data) throws QSARException {

        System.out.println(data);

        // GET A UUID AND DEFINE THE TEMPORARY FILE WHERE THE TRAINING DATA
        // ARE STORED IN ARFF FORMAT PRIOR TO TRAINING.
        final String rand = java.util.UUID.randomUUID().toString();
        final String temporaryFilePath = ServerFolders.temp +"/"+rand+ ".arff";
        final File tempFile = new File(temporaryFilePath);

        // SAVE THE DATA IN THE TEMPORARY FILE
        try {
            ArffSaver dataSaver = new ArffSaver();
            dataSaver.setInstances(data);
            dataSaver.setDestination(new FileOutputStream(tempFile));
            dataSaver.writeBatch();
        }
        catch (final IOException ex) {
            tempFile.delete();
            throw new RuntimeException("Unexpected condition while trying to save the " +
                    "dataset in a temporary ARFF file", ex);
        }



             
        LinearRegression linreg = new LinearRegression();
        String[] linRegOptions = {"-S", "1", "-C"};
        try {
            linreg.setOptions(linRegOptions);
            linreg.buildClassifier(data);
        } catch (final Exception ex) {// illegal options or could not build the classifier!
            String message = "MLR Model could not be trained";
            YaqpLogger.LOG.log(new Trace(getClass(), message + " :: " + ex));
            throw new QSARException(Cause.XQReg1, message, ex);
        }


        try {
            generatePMML(linreg, data);
        } catch (final YaqpIOException ex) {
            String message = "Could not generate PMML representation for MLR model :: " + ex;
            throw new QSARException(Cause.XQReg2, message, ex);
        }

        // PERFORM THE TRAINING
        String[] generalOptions = {
            "-c", Integer.toString(data.classIndex() + 1),
            "-t", temporaryFilePath,
            /// Save the model in the following directory
            "-d", ServerFolders.models_weka + "/" + uuid};
        try {
            Evaluation.evaluateModel(linreg, generalOptions);
        } catch (final Exception ex) {
            tempFile.delete();
            throw new QSARException(Cause.XQReg350, "Unexpected condition while trying to train "
                    + "an SVM model. Possible explanation : {" + ex.getMessage() + "}", ex);
        }


        ArrayList<Feature> independentFeatures = new ArrayList<Feature>();
        for (int i = 0; i < data.numAttributes(); i++) {
            Feature f = new Feature(data.attribute(i).name());
            if (data.classIndex() != i) {
                independentFeatures.add(f);
            }
        }

        Feature dependentFeature = new Feature(data.classAttribute().name());
        Feature predictedFeature = dependentFeature;

        QSARModel model = new QSARModel(
                uuid.toString(), predictedFeature, dependentFeature,
                independentFeatures, YaqpAlgorithms.MLR,
                new User(), null, datasetUri, ModelStatus.UNDER_DEVELOPMENT);
        model.setParams(new HashMap<String, AlgorithmParameter>());

        return model;

    }

    /**
     * Generates the PMML representation of the model and stores in the hard
     * disk.
     * @param coefficients The vector of the coefficients of the MLR model.
     * @param model_id The id of the generated model.
     * TODO: build the XML using some XML editor
     */
    // <editor-fold defaultstate="collapsed" desc="PMML generation routine!">
    private void generatePMML(final LinearRegression wekaModel, final Instances data) throws YaqpIOException {
        final double[] coefficients = wekaModel.coefficients();
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
            throw new YaqpIOException(Cause.XQReg3, "Could not write data to PMML file :" + uuid.toString(), ex);
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
        final WekaRegressor p6 = new MLRTrainer(params);

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
