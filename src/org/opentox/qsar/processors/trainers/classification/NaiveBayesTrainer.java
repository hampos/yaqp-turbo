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
public class NaiveBayesTrainer extends WekaClassifier {

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
            throw new QSARException(Cause.XQReg350, "Unexpected condition while trying to train "
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
