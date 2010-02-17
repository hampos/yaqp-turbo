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
import java.util.Map;
import org.opentox.config.ServerFolders;
import org.opentox.core.exceptions.Cause;
import org.opentox.ontology.components.QSARModel;
import org.opentox.ontology.util.AlgorithmParameter;
import org.opentox.ontology.util.vocabulary.ConstantParameters;
import org.opentox.qsar.exceptions.QSARException;
import org.opentox.qsar.processors.filters.AttributeCleanup;
import org.opentox.qsar.processors.filters.AttributeCleanup.ATTRIBUTE_TYPE;
import org.opentox.qsar.processors.filters.SimpleMVHFilter;
import org.opentox.qsar.processors.trainers.WekaTrainer;
import org.opentox.www.rest.components.YaqpForm;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public final class SVCTrainer extends WekaTrainer {

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

    public SVCTrainer() {
        super();
    }

    public SVCTrainer(final YaqpForm form) throws QSARException {
        super(form);


        // CHECK GAMMA
        try {
            if (form.getFirstValue(ConstantParameters.gamma) != null) {
                this.gamma = Double.parseDouble(form.getFirstValue(ConstantParameters.gamma));
            }
            if (gamma <= 0) {
                throw new QSARException(
                        Cause.XQSVM3002, "The parameter gamma must be strictly positive. "
                        + "You provided the illegal value: {" + gamma + "}");
            }
        } catch (final NumberFormatException ex) {
            throw new QSARException(Cause.XQSVM3001, "Parameter gamma should be numeric. "
                    + "You provided the illegal value : {" + form.getFirstValue(ConstantParameters.gamma) + "}", ex);
        }

        // CHECK COST
        try {
            if (form.getFirstValue(ConstantParameters.cost) != null) {
                this.cost = Double.parseDouble(form.getFirstValue(ConstantParameters.cost));
            }
            if (cost <= 0) {
                throw new QSARException(
                        Cause.XQSVM3004, "The parameter " + ConstantParameters.cost + " must be strictly positive. "
                        + "You provided the illegal value: {" + cost + "}");
            }
        } catch (final NumberFormatException ex) {
            throw new QSARException(Cause.XQSVM3003, "Parameter " + ConstantParameters.cost + " should be numeric. "
                    + "You provided the illegal "
                    + "value : {" + form.getFirstValue(ConstantParameters.cost) + "}", ex);
        }


        // CHECK COEFF_0
        try {
            if (form.getFirstValue(ConstantParameters.coeff0) != null) {
                this.coeff0 = Double.parseDouble(form.getFirstValue(ConstantParameters.coeff0));
            }
        } catch (final NumberFormatException ex) {
            throw new QSARException(Cause.XQSVM3007, "Parameter " + ConstantParameters.coeff0 + " should be numeric. "
                    + "You provided the illegal "
                    + "value : {" + form.getFirstValue(ConstantParameters.coeff0) + "}", ex);
        }


        // CHECK CACHE SIZE
        try {
            if (form.getFirstValue(ConstantParameters.cacheSize) != null) {
                this.cacheSize = Integer.parseInt(form.getFirstValue(ConstantParameters.cacheSize));
            }
        } catch (final NumberFormatException ex) {
            throw new QSARException(Cause.XQSVM3008, "Parameter " + ConstantParameters.cacheSize + " should be integer. "
                    + "You provided the illegal "
                    + "value : {" + form.getFirstValue(ConstantParameters.cacheSize) + "}", ex);
        }


        // CHECK DEGREE
        try {
            if (form.getFirstValue(ConstantParameters.degree) != null) {
                this.degree = Integer.parseInt(form.getFirstValue(ConstantParameters.degree));
            }
        } catch (final NumberFormatException ex) {
            throw new QSARException(Cause.XQSVM3009, "Parameter " + ConstantParameters.degree + " should be integer. "
                    + "You provided the illegal "
                    + "value : {" + form.getFirstValue(ConstantParameters.degree) + "}", ex);
        }


        // CHECK TOLERANCE
        try {
            if (form.getFirstValue(ConstantParameters.tolerance) != null) {
                this.tolerance = Double.parseDouble(form.getFirstValue(ConstantParameters.tolerance));
            }
            if (tolerance < 1E-6) {
                throw new QSARException(
                        Cause.XQSVM3011, "The parameter " + ConstantParameters.tolerance + " must be greater that 1E-6. "
                        + "You provided the illegal value: {"
                        + tolerance + "}");
            }
        } catch (final NumberFormatException ex) {
            throw new QSARException(Cause.XQSVM3010, "Parameter " + ConstantParameters.tolerance + " should be numeric. "
                    + "You provided the illegal value : {" + form.getFirstValue(ConstantParameters.tolerance) + "}", ex);
        }



        // CHECK KERNEL
        if (form.getFirstValue(ConstantParameters.kernel) != null) {
            this.kernel = form.getFirstValue(ConstantParameters.kernel).toUpperCase();
            if (!kernel.equals("RBF") && !kernel.equals("LINEAR") && !kernel.equals("POLYNOMIAL")) {
                throw new QSARException(Cause.XQSVM3012, "The available kernels are [RBF; LINEAR; POLYNOMIAL]. Note that "
                        + "this parameter is not case-sensitive, i.e. rbf is the same as RbF. However you provided " +
                        "the illegal value : {"+kernel+"}");
            }
        }
    }
    

    public QSARModel train(Instances data) throws QSARException {
        // Will be ready on Wed Feb 17...
        //
        // An extra check should be performed on whether the provided prediction
        // feature is NOMINAL. Datasets for classification purposes should have
        // at least one nominal feature. Maybe a message should be returned if the
        // dataset has no nominal features ('This dataset is improper for building
        // a classification model because it does not contain any nominal features').
        // Or in case a client choses a non-nominal feature for the classifier,
        // provide a list of some available nominal features.

        if (data == null) throw new NullPointerException("Cannot train an SVC model without data");

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
        if (data.checkForAttributeType(Attribute.NOMINAL)){
            throw new QSARException(Cause.XQSVC4040, "Improper dataset! The dataset you provided has no " +
                    "nominal features therefore classification models cannot be built.");
        }

        // CHECK OF THE CLASS ATTRIBUTE IS NOMINAL
        if (!classAttribute.isNominal()){
            throw new QSARException(Cause.XQSVC4041, "The prediction feature you provided " +
                    "is not a nominal");
        }

        // SET THE CLASS ATTRIBUTE OF THE DATASET
        data.setClass(classAttribute);

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
        } catch (final IOException ex) {/*
                                         * The content of the dataset cannot be
                                         * written to the destination file due to
                                         * some communication issue.
                                         */
            tempFile.delete();
            throw new RuntimeException("Unexpected condition while trying to save the " +
                    "dataset in a temporary ARFF file", ex);
        }

        throw new UnsupportedOperationException("Not supported yet.");
    }








}
