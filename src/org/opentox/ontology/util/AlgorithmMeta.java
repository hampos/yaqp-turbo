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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.opentox.ontology.namespaces.OTAlgorithmTypes;

/**
 *
 * Metainformation about algorithms include Dublin Core metadata plus a set of
 * algorithm parameters. An algorithm parameter consists of its name, (default)
 * value, XSD type and scope (optional/mandatory). For more information see {@link
 * AlgorithmParameter }.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class AlgorithmMeta extends Meta {

    /**
     * Link of the algorithm with some ontology. This parameter is declared as transient
     * because <code>OTAlgorithmType</code> contains references to <code>Resource</code>
     * which could not be serialized.
     */
    private transient OTAlgorithmTypes algorithmType = OTAlgorithmTypes.AlgorithmType;
    /**
     * List of parameters for the algorithm
     */
    private Map<String, AlgorithmParameter> parameters = new HashMap<String, AlgorithmParameter>();
    /**
     * The name of the algorithm
     */
    private String name = "";

    public AlgorithmMeta() {
        super();
    }

    public AlgorithmMeta(String about) {
        this();
        this.identifier = about;
    }

    public Map<String, AlgorithmParameter> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, AlgorithmParameter> parameters) {
        this.parameters = parameters;
    }

    public OTAlgorithmTypes getAlgorithmType() {
        return algorithmType;
    }

    public void setAlgorithmType(OTAlgorithmTypes algorithmType) {
        this.algorithmType = algorithmType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
