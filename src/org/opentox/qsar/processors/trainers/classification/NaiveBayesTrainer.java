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
package org.opentox.qsar.processors.trainers.classification;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;
import org.opentox.config.ServerFolders;
import org.opentox.core.exceptions.Cause;
import org.opentox.ontology.components.Feature;
import org.opentox.ontology.components.QSARModel;
import org.opentox.ontology.components.QSARModel.ModelStatus;
import org.opentox.ontology.util.AlgorithmParameter;
import org.opentox.ontology.util.YaqpAlgorithms;
import org.opentox.qsar.exceptions.QSARException;
import org.opentox.qsar.processors.filters.AttributeCleanup;
import org.opentox.qsar.processors.filters.AttributeCleanup.ATTRIBUTE_TYPE;
import org.opentox.qsar.processors.filters.SimpleMVHFilter;
import org.opentox.qsar.processors.trainers.WekaTrainer;
import org.opentox.www.rest.components.YaqpForm;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class NaiveBayesTrainer extends WekaTrainer {

    public NaiveBayesTrainer(final YaqpForm form) throws QSARException {
        super(form);
    }

    public NaiveBayesTrainer(final Map<String, AlgorithmParameter> parameters) throws QSARException {
        super(parameters);
    }

    public NaiveBayesTrainer() {
        super();
    }

    public QSARModel train(Instances data) throws QSARException {
        if (data == null) {
            throw new NullPointerException("Cannot train a "
                    + "Naive Bayes classification model without a training dataset");
        }

        /*
         * TODO: In case a client choses a non-nominal feature for the classifier,
         * provide a list of some available nominal features.
         */

        if (data == null) {
            throw new NullPointerException("Cannot train an SVC model without data");
        }

        /* The incoming dataset always has the first attribute set to
        'compound_uri' which is of type "String". This is removed at the
        begining of the training procedure */
        AttributeCleanup filter = new AttributeCleanup(ATTRIBUTE_TYPE.string);
        // NOTE: Removal of string attributes should be always performed prior to any kind of training!
        data = filter.filter(data);

        SimpleMVHFilter fil = new SimpleMVHFilter();
        data = fil.filter(data);

        // CHECK IF THE GIVEN URI IS AN ATTRIBUTE OF THE DATASET
        Attribute classAttribute = data.attribute(predictionFeature);
        if (classAttribute == null) {
            throw new QSARException(Cause.XQM202,
                    "The prediction feature you provided is not a valid numeric attribute of the dataset :{"
                    + predictionFeature + "}");
        }

        // CHECK IF THE DATASET CONTAINS ANY NOMINAL ATTRIBUTES
        if (!data.checkForAttributeType(Attribute.NOMINAL)) {
            throw new QSARException(Cause.XQSVC4040, "Improper dataset! The dataset you provided has no "
                    + "nominal features therefore classification models cannot be built.");
        }

        // CHECK WHETHER THE CLASS ATTRIBUTE IS NOMINAL
        if (!classAttribute.isNominal()) {
            StringBuilder list_of_nominal_features = new StringBuilder();

            int j = 0;
            for (int i = 0; i < data.numAttributes() && j < 10; i++) {
                if (data.attribute(i).isNominal()) {
                    j++;
                    list_of_nominal_features.append(data.attribute(i).name() + "\n");
                }
                System.out.println(data.attribute(i).type());
            }

            throw new QSARException(Cause.XQSVC4041, "The prediction feature you provided "
                    + "is not a nominal. Here is a list of some nominal features in the dataset you might "
                    + "be interested in :\n" + list_of_nominal_features.toString());
        }

        // CHECK IF THE RANGE OF THE CLASS ATTRIBUTE IS NON-UNARY
        Enumeration nominalValues = classAttribute.enumerateValues();
        String v = nominalValues.nextElement().toString();
        if (!nominalValues.hasMoreElements()) {
            throw new QSARException(Cause.XQSVC4042, "This classifier cannot handle unary nominal classes, that is "
                    + "nominal class attributes whose range includes only one value. Singleton value : {" + v + "}");
        }

        // SET THE CLASS ATTRIBUTE OF THE DATASET
        data.setClass(classAttribute);

        // GET A UUID AND DEFINE THE TEMPORARY FILE WHERE THE TRAINING DATA
        // ARE STORED IN ARFF FORMAT PRIOR TO TRAINING.
        final String rand = java.util.UUID.randomUUID().toString();
        final String temporaryFilePath = ServerFolders.temp + "/" + rand + ".arff";
        final File tempFile = new File(temporaryFilePath);

        // SAVE THE DATA IN THE TEMPORARY FILE
        try {
            ArffSaver dataSaver = new ArffSaver();
            dataSaver.setInstances(data);
            dataSaver.setDestination(new FileOutputStream(tempFile));
            dataSaver.writeBatch();
            if (!tempFile.exists()) {
                throw new IOException("Temporary File was not created");
            }
        } catch (final IOException ex) {/*
             * The content of the dataset cannot be
             * written to the destination file due to
             * some communication issue.
             */
            tempFile.delete();
            throw new RuntimeException("Unexpected condition while trying to save the "
                    + "dataset in a temporary ARFF file", ex);
        }

        NaiveBayes classifier = new NaiveBayes();

        String[] generalOptions = {
            "-c", Integer.toString(data.classIndex() + 1),
            "-t", temporaryFilePath,
            /// Save the model in the following directory
            "-d", ServerFolders.models_weka + "/" + uuid};

        try {
            Evaluation.evaluateModel(classifier, generalOptions);
        } catch (final Exception ex) {
            tempFile.delete();
            throw new QSARException(Cause.XQSVM350, "Unexpected condition while trying to train "
                    + "an SVM model. Possible explanation : {" + ex.getMessage() + "}", ex);
        }

        QSARModel model = new QSARModel();

        model.setParams(getParameters());
        model.setCode(uuid.toString());
        model.setAlgorithm(YaqpAlgorithms.SVM);
        model.setDataset(datasetUri);
        model.setModelStatus(ModelStatus.UNDER_DEVELOPMENT);

        ArrayList<Feature> independentFeatures = new ArrayList<Feature>();
        for (int i = 0; i < data.numAttributes(); i++) {
            Feature f = new Feature(data.attribute(i).name());
            if (data.classIndex() != i) {
                independentFeatures.add(f);
            }
        }

        Feature dependentFeature = new Feature(data.classAttribute().name());
        Feature predictedFeature = dependentFeature;
        model.setDependentFeature(dependentFeature);
        model.setIndependentFeatures(independentFeatures);
        model.setPredictionFeature(predictedFeature);
        tempFile.delete();
        return model;
    }
}
