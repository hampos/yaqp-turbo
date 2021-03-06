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
package org.opentox.ontology.util;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.opentox.config.Configuration;
import org.opentox.ontology.components.Algorithm;
import org.opentox.ontology.namespaces.OTAlgorithmTypes;
import org.opentox.ontology.util.vocabulary.Audience;
import org.opentox.ontology.util.vocabulary.ConstantParameters;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Warning;
import org.restlet.data.MediaType;

/**
 * <p>In this class, all available algorithms from YAQP are listed with their meta
 * data. RDF (and all other) representations of algorithms are generated usign these
 * meta data listed here. If you need to start developing a new algorithm for YAQP,
 * first of all update this class with the algorithm's meta information. For examlpe, if
 * you are developing an algorithm called <code>'abc'</code> here is an example of
 * what you should add in this file:</p>
 * <p>
 * <code>
 * public static final YaqpAlgorithms MyAlgorithm = new YaqpAlgorithms("abc") {<br/>
 * <br/>
 * AlgorithmMeta meta = new AlgorithmMeta(getUri());<br/>
 * meta.title = "ABC Algorithm - User Defined";<br/>
 * // Populate the meta data...<br/>
 * }<br/>
 * </code>
 * </p>
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class YaqpAlgorithms {

    protected YaqpAlgorithms() {
    }
    /**
     * Multiple Linear Regression Algorithm
     */
    public static final Algorithm MLR = new Algorithm(mlr_metadata());
    public static final Algorithm SVM = new Algorithm(svm_metadata());
    public static final Algorithm SVC = new Algorithm(svc_metadata());
    public static final Algorithm NAIVE_BAYES = new Algorithm(naiveBayes_metadata());
    public static final Algorithm CLEAN_UP = new Algorithm(cleanup_meta());
    public static final Algorithm FCBF = new Algorithm(fcbf_meta());

    /**
     * Returns an algorithm (from the set of algorithms listed in {@link YaqpAlgorithms }
     * given its name. The search is not case sensitive, so <code>getByName("mlr")</code>
     * will return the same as <code>getByName("MLR")</code>.
     * @param name
     *      The name of the algorithm (not case-sensitive).
     * @return
     *      The algorithm as a componente, that is an object containing all metadata
     *      concerning the algorithm entity into consideration. Returns <code>null</code>
     *      if the algorithm is not found.
     */
    public static final Algorithm getByName(String name){
        YaqpAlgorithms o = new YaqpAlgorithms();
        Field[] fields =  o.getClass().getFields();
        for (int i=0;i<fields.length;i++){
            try {
                Algorithm a = (Algorithm) fields[i].get(o);
                if (a.getMeta().getName().equalsIgnoreCase(name)) return a;
            } catch (IllegalArgumentException ex) {
                //Logger.getLogger(YaqpAlgorithms.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
               // Logger.getLogger(YaqpAlgorithms.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    
    /**
     * Get a complete list of available algorithms on the server.
     * @return
     */
    public static final ArrayList<Algorithm> getAllAlgorithms(){
        ArrayList<Algorithm> list  = new ArrayList<Algorithm>();
        YaqpAlgorithms o = new YaqpAlgorithms();
        Field[] fields =  o.getClass().getFields();
        for (int i=0;i<fields.length;i++){
            try {
                list.add((Algorithm) fields[i].get(o));
            } catch (IllegalArgumentException ex) {
                // Not an algorithm! (Take no action)
            } catch (IllegalAccessException ex) {
                // Not an algorithm! (Take no action)
            }
        }
        return list;
    }

    public static AlgorithmMeta mlr_metadata() {
        String name = "mlr";
        String uri = "http://" + Configuration.getProperties().getProperty("server.domainName") + ":"
                + Configuration.getProperties().getProperty("server.port") + "/algorithm/" + name;

        AlgorithmMeta meta = new AlgorithmMeta(uri);
        meta.setParameters(new HashMap<String, AlgorithmParameter>());

        meta.setName(name);
        meta.title = "Multiple Linear Regression Training Algorithm";
        meta.subject = "Regression, Linear, Training, Multiple Linear Regression, Machine Learning, "
                + "Single Target, Eager Learning, Weka";
        meta.description = "Training algorithm for multiple linear regression models. Applies "
                + "on datasets which contain exclusively numeric data entries. The algorithm is an "
                + "implementation of LinearRegression of Weka. More information about Linear Regression "
                + "you will find at http://en.wikipedia.org/wiki/Linear_regression. The weka API "
                + "for Linear Regression Training is located at "
                + "http://weka.sourceforge.net/doc/weka/classifiers/functions/LinearRegression.html .";
        meta.format.add(MediaType.APPLICATION_RDF_XML);
        meta.format.add(MediaType.APPLICATION_RDF_TURTLE);
        meta.format.add(MediaType.TEXT_RDF_N3);
        meta.format.add(MediaType.TEXT_RDF_NTRIPLES);
        meta.format.add(MediaType.APPLICATION_XML);
        meta.identifier = uri;
        meta.type = "http://purl.org/dc/dcmitype/Service";
        meta.audience.addAll(Audience.AllExpert);
        meta.provenance = "Updated vesrion from yaqp version 1.3.6 to yaqp-turbo version 1.0";
        meta.setAlgorithmType(OTAlgorithmTypes.RegressionEagerSingleTarget);

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            meta.date = formatter.parse("2010-01-01");
        } catch (ParseException ex) {
            YaqpLogger.LOG.log(new Warning(YaqpAlgorithms.class, "(MLR) Wrong date : " + ex));
            meta.date = new Date(System.currentTimeMillis());
        }

        return meta;
    }

    public static AlgorithmMeta svm_metadata() {
        String name = "svm";
        String uri = "http://" + Configuration.getProperties().getProperty("server.domainName") + ":"
                + Configuration.getProperties().getProperty("server.port") + "/algorithm/" + name;        
        AlgorithmMeta meta = new AlgorithmMeta(uri);
        meta.setParameters(ConstantParameters.SVMParams());
        meta.setName(name);
        meta.title = "Support Vector Machine Training Algorithm (Regression)";
        // TODO: Provide a reference for SVM Regression
        meta.description =
                "Algorithm for training regression models using the Support Vector "
                + "Machine Learning Algorithm. The training is based on the Weka implementation of "
                + "SVM and specifically the class weka.classifiers.functions.SVMreg. A comprehensive introductory text "
                + "is provided by John Shawe-Taylor & Nello Cristianin in the book 'Support Vector Machines', "
                + "Cambridge University Press, 2000";
        meta.subject =
                "Support Vector Machine, SVM, Regression, Nonlinear, Single Target,"
                + "Eager Learning, Training Algorithm, Machine Learning, Weka";
        meta.format.add(MediaType.APPLICATION_RDF_XML);
        meta.format.add(MediaType.APPLICATION_RDF_TURTLE);
        meta.format.add(MediaType.TEXT_RDF_N3);
        meta.format.add(MediaType.TEXT_RDF_NTRIPLES);
        meta.format.add(MediaType.APPLICATION_XML);
        meta.format.add(MediaType.APPLICATION_PDF);
        meta.identifier = uri;
        meta.type = "http://purl.org/dc/dcmitype/Service";
        meta.audience.addAll(Audience.AllExpert);
        meta.provenance = "Updated vesrion from yaqp version 1.3.6 to yaqp-turbo version 1.0";
        meta.setAlgorithmType(OTAlgorithmTypes.RegressionEagerSingleTarget);


        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            meta.date = formatter.parse("2010-01-01");
        } catch (ParseException ex) {
            YaqpLogger.LOG.log(new Warning(YaqpAlgorithms.class, "(" + uri + ") Wrong date : " + ex));
            meta.date = new Date(System.currentTimeMillis());
        }

        return meta;
    }

    public static AlgorithmMeta svc_metadata() {
        String name = "svc";
        String uri = "http://" + Configuration.getProperties().getProperty("server.domainName") + ":"
                + Configuration.getProperties().getProperty("server.port") + "/algorithm/" + name;
        AlgorithmMeta meta = new AlgorithmMeta(uri);
        meta.setName(name);
        meta.setParameters(ConstantParameters.SVCParams());
        meta.title = "Support Vector Machine Training Algorithm (Regression)";
        // TODO: Provide a reference for SVM Regression
        meta.description =
                "Algorithm for training classification models using the Support Vector "
                + "Machine Learning algorithm. The training is based on the Weka implementation of "
                + "SVM and specifically the class weka.classifiers.functions.SMO. A comprehensive introductory text "
                + "is provided by John Shawe-Taylor & Nello Cristianin in the book 'Support Vector Machines', "
                + "Cambridge University Press, 2000";
        meta.subject =
                "Support Vector Machine, SVM, Classification, Nonlinear, Single Target,"
                + "Eager Learning, Training Algorithm, Machine Learning, Weka";
        meta.format.add(MediaType.APPLICATION_RDF_XML);
        meta.format.add(MediaType.APPLICATION_RDF_TURTLE);
        meta.format.add(MediaType.TEXT_RDF_N3);
        meta.format.add(MediaType.TEXT_RDF_NTRIPLES);
        meta.format.add(MediaType.APPLICATION_XML);
        meta.format.add(MediaType.APPLICATION_PDF);
        meta.identifier = uri;
        meta.type = "http://purl.org/dc/dcmitype/Service";
        meta.audience.addAll(Audience.AllExpert);
        meta.provenance = "Updated vesrion from yaqp version 1.3.6 to yaqp-turbo version 1.0";
        meta.setAlgorithmType(OTAlgorithmTypes.ClassificationEagerSingleTarget);

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            meta.date = formatter.parse("2010-01-01");
        } catch (ParseException ex) {
            YaqpLogger.LOG.log(new Warning(YaqpAlgorithms.class, "(" + uri + ") Wrong date : " + ex));
            meta.date = new Date(System.currentTimeMillis());
        }

        return meta;
    }


    public static AlgorithmMeta naiveBayes_metadata() {
        String name = "naiveBayes";
        String uri = "http://" + Configuration.getProperties().getProperty("server.domainName") + ":"
                + Configuration.getProperties().getProperty("server.port") + "/algorithm/" + name;
        AlgorithmMeta meta = new AlgorithmMeta(uri);
        meta.setName(name);
        meta.setParameters(new HashMap<String, AlgorithmParameter>());
        meta.title = "Naive Bayes Classifier";
        // TODO: Provide a reference for SVM Regression
        meta.description =
                "Naive Bayes Classifier is a simple probabilistic classifier based on "
                + "applying Bayes' theorem with strong (naive) independence assumptions."
                + "A more descriptive term for the underlying probability model would be \"independent feature model\""
                + "In simple terms, a naive Bayes classifier assumes that the presence (or absence) "
                + "of a particular feature of a class is unrelated to the presence (or absence) "
                + "of any other feature. Even though these features depend on the existence of the other features, "
                + "a naive Bayes classifier considers all of these properties to independently contribute to the "
                + "probability that this fruit is an apple." 
                + "This implementation relies on the weka implementation for the Naive Bayes Classifier " +
                "(weka.classifiers.bayes.NaiveBayes).";
        meta.subject =
                "bayes classifier, independent feature model, ";
        meta.format.add(MediaType.APPLICATION_RDF_XML);
        meta.format.add(MediaType.APPLICATION_RDF_TURTLE);
        meta.format.add(MediaType.TEXT_RDF_N3);
        meta.format.add(MediaType.TEXT_RDF_NTRIPLES);
        meta.format.add(MediaType.APPLICATION_XML);
        meta.format.add(MediaType.APPLICATION_PDF);
        meta.identifier = uri;
        meta.type = "http://purl.org/dc/dcmitype/Service";
        meta.audience.addAll(Audience.AllExpert);
        meta.provenance = "Updated vesrion from yaqp version 1.3.6 to yaqp-turbo version 1.0";
        meta.setAlgorithmType(OTAlgorithmTypes.Classification);

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            meta.date = formatter.parse("2010-02-01");
        } catch (ParseException ex) {
            YaqpLogger.LOG.log(new Warning(YaqpAlgorithms.class, "(" + uri + ") Wrong date : " + ex));
            meta.date = new Date(System.currentTimeMillis());
        }

        return meta;
    }


    public static AlgorithmMeta cleanup_meta(){
         String name = "cleanup";
        String uri = Configuration.BASE_URI + "/algorithm/" + name;
        AlgorithmMeta meta = new AlgorithmMeta(uri);
        meta.setName(name);
        meta.setParameters(ConstantParameters.CleanUpParams());
        meta.title = "Feature clean-up service";
        // TODO: Provide a reference for SVM Regression
        meta.description =
                "Removes all features of certain type from a dataset (e.g. removes all string features) and " +
                "ouputs the URI of a new cleaned-up dataset. Useful for all training algorithms as a preprocessing " +
                "service.";
        meta.subject =
                "data cleanup, attribute cleanup, data filtering, data preprocessing, unsupervised";
        meta.format.add(MediaType.APPLICATION_RDF_XML);
        meta.format.add(MediaType.APPLICATION_RDF_TURTLE);
        meta.format.add(MediaType.TEXT_RDF_N3);
        meta.format.add(MediaType.TEXT_RDF_NTRIPLES);
        meta.format.add(MediaType.APPLICATION_PDF);
        meta.identifier = uri;
        meta.type = "http://purl.org/dc/dcmitype/Service";
        meta.audience.addAll(Audience.AllExpert);
        meta.provenance = "Newly added algorithm on Feb 22, 2010";
        meta.setAlgorithmType(OTAlgorithmTypes.DataCleanup);

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            meta.date = formatter.parse("2010-02-22");
        } catch (ParseException ex) {
            YaqpLogger.LOG.log(new Warning(YaqpAlgorithms.class, "(" + uri + ") Wrong date : " + ex));
            meta.date = new Date(System.currentTimeMillis());
        }

        return meta;
    }


    public static AlgorithmMeta fcbf_meta(){
        String name = "fcbf";
        String uri = Configuration.BASE_URI + "/algorithm/" + name;
        AlgorithmMeta meta = new AlgorithmMeta(uri);
        meta.setName(name);
        meta.setParameters(ConstantParameters.CleanUpParams());
        meta.title = "Feature clean-up service";
        // TODO: Provide a reference for SVM Regression
        meta.description =
                "An attribute selection algorithm based on the publication by Lei Yu, Huan Liu: Feature Selection for " +
                "High-Dimensional Data: A Fast Correlation-Based Filter Solution. In: Proceedings of the Twentieth " +
                "International Conference on Machine Learning, 856-863, 2003. This service is based on WEKA's implementation " +
                "for FCBF";
        meta.subject =
                "attribute selection, dimensionality reduction, feature selection, correlation, relevance, redundancy";
        meta.format.add(MediaType.APPLICATION_RDF_XML);
        meta.format.add(MediaType.APPLICATION_RDF_TURTLE);
        meta.format.add(MediaType.TEXT_RDF_N3);
        meta.format.add(MediaType.TEXT_RDF_NTRIPLES);
        meta.format.add(MediaType.APPLICATION_PDF);
        meta.identifier = uri;
        meta.type = "http://purl.org/dc/dcmitype/Service";
        meta.audience.addAll(Audience.AllExpert);
        meta.provenance = "Newly added algorithm on Feb 22, 2010";
        meta.setAlgorithmType(OTAlgorithmTypes.DataCleanup);

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            meta.date = formatter.parse("2010-02-22");
        } catch (ParseException ex) {
            YaqpLogger.LOG.log(new Warning(YaqpAlgorithms.class, "(" + uri + ") Wrong date : " + ex));
            meta.date = new Date(System.currentTimeMillis());
        }

        return meta;
    }
//    public static void main(String[] args) throws IOException {
//        Algorithm alg = YaqpAlgorithms.MLR;
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.writeValue(new File("/home/chung/Desktop/abc.json"), (Object) alg);
//    }
}
