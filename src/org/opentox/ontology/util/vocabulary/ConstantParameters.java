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
package org.opentox.ontology.util.vocabulary;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import java.util.ArrayList;
import org.opentox.ontology.util.AlgorithmParameter;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ConstantParameters {

    public static final AlgorithmParameter<String> TARGET = TARGET("http://someserver.com/feature/100");
    public static final AlgorithmParameter<String> KERNEL = KERNEL("rbf");
    public static final AlgorithmParameter<Double> COST = COST(10.0);
    public static final AlgorithmParameter<Double> EPSILON = EPSILON(0.10);
    public static final AlgorithmParameter<Double> GAMMA = GAMMA(1.0);
    public static final AlgorithmParameter<Double> COEFF0 = COEFF0(0.0);
    public static final AlgorithmParameter<Integer> DEGREE = DEGREE((int) 3);
    public static final AlgorithmParameter<Double> TOLERANCE = TOLERANCE(0.0001);
    public static final AlgorithmParameter<Integer> CACHESIZE = CACHESIZE((int) 250007);

    /**
     * The collection of tuning parameters for the SVC training algorithm as
     * an <code>ArrayList</code> of <code>AlgorithmParameter</code> objects.
     * @return Array List of Algorithm Parameters.
     */
    public static final ArrayList<AlgorithmParameter> SVC_BUNDLE(){
        ArrayList<AlgorithmParameter> svm_bundle = new ArrayList<AlgorithmParameter>();
        svm_bundle.add(CACHESIZE);
        svm_bundle.add(COEFF0);
        svm_bundle.add(COST);
        svm_bundle.add(DEGREE);
        svm_bundle.add(GAMMA);
        svm_bundle.add(KERNEL);
        svm_bundle.add(TARGET);
        svm_bundle.add(TOLERANCE);
        return svm_bundle;
    }

    /**
     * The collection of tuning parameters for the SVC training algorithm as
     * an <code>ArrayList</code> of <code>AlgorithmParameter</code> objects.
     * @return Array List of Algorithm Parameters.
     */
    public static final ArrayList<AlgorithmParameter> SVM_BUNDLE(){
        ArrayList<AlgorithmParameter> svm_bundle = SVC_BUNDLE();
        svm_bundle.add(EPSILON);
        return svm_bundle;
    }

    public static final AlgorithmParameter<String> TARGET(String value) {
        return new AlgorithmParameter<String>("prediction_feature",
                XSDDatatype.XSDanyURI, value, "mandatory");
    }

    public static final AlgorithmParameter<String> KERNEL(String value) {
        return new AlgorithmParameter<String>("kernel",
                XSDDatatype.XSDstring, value, "optional");
    }

    public static final AlgorithmParameter<Double> COST(double value) {
        return new AlgorithmParameter<Double>("cost", XSDDatatype.XSDdouble,
                10.0, "optional");
    }

    public static final AlgorithmParameter<Double> EPSILON(double value) {
        return new AlgorithmParameter<Double>("epsilon", XSDDatatype.XSDdouble, value, "optional");
    }

    public static final AlgorithmParameter<Double> GAMMA(double value) {
        return new AlgorithmParameter<Double>("gamma", XSDDatatype.XSDdouble, value, "optional");
    }

    public static final AlgorithmParameter<Double> COEFF0(double value) {
        return new AlgorithmParameter<Double>("coeff0", XSDDatatype.XSDdouble, value, "optional");
    }

    public static final AlgorithmParameter<Integer> DEGREE(int value) {
        return new AlgorithmParameter<Integer>("degree",
                XSDDatatype.XSDpositiveInteger, value, "optional");
    }

    private static final AlgorithmParameter<Double> TOLERANCE(double value) {
        return new AlgorithmParameter<Double>("tolerance",
                XSDDatatype.XSDdouble, value, "optional");
    }

    private static final AlgorithmParameter<Integer> CACHESIZE(int value) {
        return new AlgorithmParameter<Integer>("cacheSize",
                XSDDatatype.XSDnonNegativeInteger, value, "optional");
    }
}
