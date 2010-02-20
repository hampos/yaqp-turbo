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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.opentox.config.ServerFolders;
import org.opentox.core.exceptions.Cause;
import org.opentox.ontology.components.Feature;
import org.opentox.ontology.components.QSARModel;
import org.opentox.ontology.components.QSARModel.ModelStatus;
import org.opentox.ontology.util.AlgorithmParameter;
import org.opentox.ontology.util.YaqpAlgorithms;
import org.opentox.ontology.util.vocabulary.ConstantParameters;
import org.opentox.qsar.exceptions.QSARException;
import org.opentox.qsar.processors.filters.AttributeCleanup;
import org.opentox.qsar.processors.filters.AttributeCleanup.ATTRIBUTE_TYPE;
import org.opentox.qsar.processors.filters.SimpleMVHFilter;
import org.opentox.qsar.processors.trainers.WekaTrainer;
import org.opentox.www.rest.components.YaqpForm;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SVMreg;
import weka.classifiers.functions.supportVector.Kernel;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.functions.supportVector.RBFKernel;
import weka.core.Instances;
import weka.core.converters.ArffSaver;


/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
final public class SVMTrainer extends WekaRegressor {

    /**
     * The parameter gamma
     */
    private double gamma = Double.parseDouble(
            ConstantParameters.SVMParams().get(ConstantParameters.gamma).paramValue.toString());
    /**
     * The cost used in the trainer's cost function
     */
    private double cost = Double.parseDouble(
            ConstantParameters.SVMParams().get(ConstantParameters.cost).paramValue.toString());
    /**
     * Parameter epsilon or e-SVM training algorithm
     */
    private double epsilon = Double.parseDouble(
            ConstantParameters.SVMParams().get(ConstantParameters.epsilon).paramValue.toString());
    /**
     * The bias of the kernel function of the SVM model.
     */
    private double coeff0 = Double.parseDouble(
            ConstantParameters.SVMParams().get(ConstantParameters.coeff0).paramValue.toString());
    /**
     * Maximum cache size.
     */
    private int cacheSize = Integer.parseInt(
            ConstantParameters.SVMParams().get(ConstantParameters.cacheSize).paramValue.toString());
    /**
     * Degree of a polynomial kernel
     */
    private int degree = Integer.parseInt(
            ConstantParameters.SVMParams().get(ConstantParameters.degree).paramValue.toString());
    /**
     * Convergence criterion.
     */
    private double tolerance = Double.parseDouble(
            ConstantParameters.SVMParams().get(ConstantParameters.tolerance).paramValue.toString());
    /**
     * The kernel of the SVM model.
     */
    private String kernel = ConstantParameters.SVMParams().get(ConstantParameters.kernel).paramValue.toString();


    public SVMTrainer(final YaqpForm form) throws QSARException {
        super(form);
        
        
        // CHECK GAMMA
        try {
            if (form.getFirstValue(ConstantParameters.gamma) != null) {
                this.gamma = Double.parseDouble(form.getFirstValue(ConstantParameters.gamma));
            }
            if (gamma <= 0) {
                throw new QSARException(
                        Cause.XQReg3002, "The parameter gamma must be strictly positive. "
                        + "You provided the illegal value: {" + gamma + "}");
            }
        } catch (final NumberFormatException ex) {
            throw new QSARException(Cause.XQReg3001, "Parameter gamma should be numeric. "
                    + "You provided the illegal value : {" + form.getFirstValue(ConstantParameters.gamma) + "}", ex);
        }
        putParameter(ConstantParameters.gamma, new AlgorithmParameter((double)gamma));

        // CHECK COST
        try {
            if (form.getFirstValue(ConstantParameters.cost) != null) {
                this.cost = Double.parseDouble(form.getFirstValue(ConstantParameters.cost));
            }
            if (cost <= 0) {
                throw new QSARException(
                        Cause.XQReg3004, "The parameter " + ConstantParameters.cost + " must be strictly positive. "
                        + "You provided the illegal value: {" + cost + "}");
            }
        } catch (final NumberFormatException ex) {
            throw new QSARException(Cause.XQReg3003, "Parameter " + ConstantParameters.cost + " should be numeric. "
                    + "You provided the illegal "
                    + "value : {" + form.getFirstValue(ConstantParameters.cost) + "}", ex);
        }
        putParameter(ConstantParameters.cost, new AlgorithmParameter(cost));

        // CHECK EPSILON
        try {
            if (form.getFirstValue(ConstantParameters.epsilon) != null) {
                this.epsilon = Double.parseDouble(form.getFirstValue(ConstantParameters.epsilon));
            }
            if (epsilon <= 0) {
                throw new QSARException(
                        Cause.XQReg3006, "The parameter " + ConstantParameters.epsilon + " must be strictly positive. "
                        + "You provided the illegal value: {"
                        + epsilon + "}");
            }
        } catch (final NumberFormatException ex) {
            throw new QSARException(Cause.XQReg3005, "Parameter " + ConstantParameters.epsilon + " should be numeric. "
                    + "You provided the illegal "
                    + "value : {" + form.getFirstValue(ConstantParameters.epsilon) + "}", ex);
        }
        putParameter(ConstantParameters.epsilon, new AlgorithmParameter(epsilon));


        // CHECK COEFF_0
        try {
            if (form.getFirstValue(ConstantParameters.coeff0) != null) {
                this.coeff0 = Double.parseDouble(form.getFirstValue(ConstantParameters.coeff0));
            }
        } catch (final NumberFormatException ex) {
            throw new QSARException(Cause.XQReg3007, "Parameter " + ConstantParameters.coeff0 + " should be numeric. "
                    + "You provided the illegal "
                    + "value : {" + form.getFirstValue(ConstantParameters.coeff0) + "}", ex);
        }
        putParameter(ConstantParameters.coeff0, new AlgorithmParameter(coeff0));


        // CHECK CACHE SIZE
        try {
            if (form.getFirstValue(ConstantParameters.cacheSize) != null) {
                this.cacheSize = Integer.parseInt(form.getFirstValue(ConstantParameters.cacheSize));
            }
        } catch (final NumberFormatException ex) {
            throw new QSARException(Cause.XQReg3008, "Parameter " + ConstantParameters.cacheSize + " should be integer. "
                    + "You provided the illegal "
                    + "value : {" + form.getFirstValue(ConstantParameters.cacheSize) + "}", ex);
        }
        putParameter(ConstantParameters.cacheSize, new AlgorithmParameter(cacheSize));


        // CHECK DEGREE
        try {
            if (form.getFirstValue(ConstantParameters.degree) != null) {
                this.degree = Integer.parseInt(form.getFirstValue(ConstantParameters.degree));
            }
        } catch (final NumberFormatException ex) {
            throw new QSARException(Cause.XQReg3009, "Parameter " + ConstantParameters.degree + " should be integer. "
                    + "You provided the illegal "
                    + "value : {" + form.getFirstValue(ConstantParameters.degree) + "}", ex);
        }
        putParameter(ConstantParameters.degree, new AlgorithmParameter(degree));


        // CHECK TOLERANCE
        try {
            if (form.getFirstValue(ConstantParameters.tolerance) != null) {
                this.tolerance = Double.parseDouble(form.getFirstValue(ConstantParameters.tolerance));
            }
            if (tolerance < 1E-6) {
                throw new QSARException(
                        Cause.XQReg3011, "The parameter " + ConstantParameters.tolerance + " must be greater that 1E-6. "
                        + "You provided the illegal value: {"
                        + tolerance + "}");
            }
        } catch (final NumberFormatException ex) {
            throw new QSARException(Cause.XQReg3010, "Parameter " + ConstantParameters.tolerance + " should be numeric. "
                    + "You provided the illegal value : {" + form.getFirstValue(ConstantParameters.tolerance) + "}", ex);
        }
        putParameter(ConstantParameters.tolerance, new AlgorithmParameter(tolerance));



        // CHECK KERNEL
        if (form.getFirstValue(ConstantParameters.kernel) != null) {
            this.kernel = form.getFirstValue(ConstantParameters.kernel).toUpperCase();
            if (!kernel.equals("RBF") && !kernel.equals("LINEAR") && !kernel.equals("POLYNOMIAL")) {
                throw new QSARException(Cause.XQReg3012, "The available kernels are [RBF; LINEAR; POLYNOMIAL]. Note that "
                        + "this parameter is not case-sensitive, i.e. rbf is the same as RbF. However you provided " +
                        "the illegal value : {"+kernel+"}");
            }
        }
        putParameter(ConstantParameters.kernel, new AlgorithmParameter(kernel));


    }

    public SVMTrainer() {
        super();
    }
    /**
     *
     * @param data
     * @return
     * @throws QSARException
     */
    public QSARModel train(Instances data) throws QSARException {

        // NOTE: The checks (check if data is null and if the prediction feature is
        //       acceptable are found in WekaRegressor. The method preprocessData(Instances)
        //       does this job.        


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
        

        // INITIALIZE THE REGRESSOR
        SVMreg regressor = new SVMreg();
        final String[] regressorOptions = {
            "-P", Double.toString(epsilon),
            "-T", Double.toString(tolerance)
        };


        Kernel svm_kernel = null;
                if (kernel.equalsIgnoreCase("rbf")) {
                    RBFKernel rbf_kernel = new RBFKernel();
                    rbf_kernel.setGamma(Double.parseDouble(Double.toString(gamma)));
                    rbf_kernel.setCacheSize(Integer.parseInt(Integer.toString(cacheSize)));
                    svm_kernel = rbf_kernel;
                } else if (kernel.equalsIgnoreCase("polynomial")) {
                    PolyKernel poly_kernel = new PolyKernel();
                    poly_kernel.setExponent(Double.parseDouble(Integer.toString(degree)));
                    poly_kernel.setCacheSize(Integer.parseInt(Integer.toString(cacheSize)));
                    poly_kernel.setUseLowerOrder(true);
                    svm_kernel = poly_kernel;
                } else if (kernel.equalsIgnoreCase("linear")) {
                    PolyKernel poly_kernel = new PolyKernel();
                    poly_kernel.setExponent((double) 1.0);
                    poly_kernel.setCacheSize(Integer.parseInt(Integer.toString(cacheSize)));
                    poly_kernel.setUseLowerOrder(true);
                    svm_kernel = poly_kernel;
                }
                regressor.setKernel(svm_kernel);
        try {
            regressor.setOptions(regressorOptions);
        } catch (final Exception ex) {
            tempFile.delete();
            throw new IllegalArgumentException("Bad options in SVM trainer for epsilon = {"+epsilon+"} or " +
                    "tolerance = {"+tolerance+"}.", ex);
        }
        
        // PERFORM THE TRAINING
        String[] generalOptions = {
            "-c", Integer.toString(data.classIndex() + 1),
            "-t", temporaryFilePath,
            /// Save the model in the following directory
            "-d", ServerFolders.models_weka + "/" + uuid};
        try {
            Evaluation.evaluateModel(regressor, generalOptions);
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
