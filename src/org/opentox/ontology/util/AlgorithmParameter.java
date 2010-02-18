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

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class AlgorithmParameter<E extends Object> implements Serializable {

    public enum SCOPE {

        optional,
        mandatory;
    }
    /**
     * Parameter datatype according to the <a href="http://www.w3.org/TR/xmlschema-2/">
     * XSD</a> specifications.
     */
    public XSDDatatype dataType;
    /**
     * The default value of the parameter.
     */
    public E paramValue;
    /**
     * The scope of the parameter which is either "optional" or
     * "mandatory".
     */
    public SCOPE paramScope;
        

    public AlgorithmParameter(
            XSDDatatype dataType,
            E paramValue, SCOPE paramScope) {
        this.dataType = dataType;
        this.paramValue = paramValue;
        this.paramScope = paramScope;
    }

    public AlgorithmParameter(E paramValue) {
        this(XSDDatatype.XSDstring, paramValue, SCOPE.optional);
        dataType = javaXsdRelation(paramValue);
    }

    public AlgorithmParameter(E paramValue, SCOPE paramScope) {
        this(paramValue);
        this.paramScope = paramScope;
    }

    public AlgorithmParameter updateParamValue(E paramValue){
        this.paramValue = paramValue;
        return this;
    }

    
    private XSDDatatype javaXsdRelation(E o) {
        
            if (o instanceof Double) {
                return XSDDatatype.XSDdouble;
            } else if (o instanceof Float) {
                return XSDDatatype.XSDfloat;
            } else if (o instanceof Integer) {
                return XSDDatatype.XSDinteger;
            } else if (o instanceof String) {
                return XSDDatatype.XSDstring;
            } else if (o instanceof Long) {
                return XSDDatatype.XSDlong;
            } else if (o instanceof URI || o instanceof URL){
                return XSDDatatype.XSDanyURI;
            }
        
        return XSDDatatype.XSDstring;
    }
}
