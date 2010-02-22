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
import java.util.HashMap;
import java.util.Map;
import org.opentox.ontology.util.AlgorithmParameter;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ConstantParameters {

    public static final String
            kernel = "kernel",
            degree = "degree",
            cost = "cost",
            gamma = "gamma",
            coeff0 = "coeff0",
            tolerance = "tolerance",
            cacheSize = "cacheSize",
            epsilon = "epsilon",
            prediction_feature = "prediction_feature",
            dataset_uri="dataset_uri",
            attribure_type="attribute_type";


    public static Map<String, AlgorithmParameter> SVCParams() {
        Map<String, AlgorithmParameter> map = new HashMap<String, AlgorithmParameter>();
        map.put(kernel, new AlgorithmParameter<String>(XSDDatatype.XSDstring, "RBF", AlgorithmParameter.SCOPE.optional));
        map.put(degree, new AlgorithmParameter<Integer>(XSDDatatype.XSDpositiveInteger, 3, AlgorithmParameter.SCOPE.optional));
        map.put(cost, new AlgorithmParameter<Double>(XSDDatatype.XSDdouble, 100.0, AlgorithmParameter.SCOPE.optional));
        map.put(gamma, new AlgorithmParameter<Double>(XSDDatatype.XSDdouble, 1.50, AlgorithmParameter.SCOPE.optional));
        map.put(coeff0, new AlgorithmParameter<Double>(XSDDatatype.XSDdouble, 0.0, AlgorithmParameter.SCOPE.optional));
        map.put(tolerance, new AlgorithmParameter<Double>(XSDDatatype.XSDdouble, 0.0001, AlgorithmParameter.SCOPE.optional));
        map.put(cacheSize, new AlgorithmParameter<Integer>(XSDDatatype.XSDpositiveInteger, 250007, AlgorithmParameter.SCOPE.optional));
        return map;
    }

    public static Map<String, AlgorithmParameter> SVMParams() {
        Map<String, AlgorithmParameter> map = new HashMap<String, AlgorithmParameter>();
        map.putAll(SVCParams());
        map.put(epsilon, new AlgorithmParameter<Double>(XSDDatatype.XSDdouble, 0.100, AlgorithmParameter.SCOPE.optional));
        return map;
    }

    public static Map<String, AlgorithmParameter> CleanUpParams(){
         Map<String, AlgorithmParameter> map = new HashMap<String, AlgorithmParameter>();
         map.put(attribure_type, new AlgorithmParameter(XSDDatatype.XSDstring, "string", AlgorithmParameter.SCOPE.optional));
         return map;
    }
}
