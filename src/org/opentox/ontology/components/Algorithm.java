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
package org.opentox.ontology.components;

import java.util.ArrayList;
import java.util.Iterator;
import org.opentox.ontology.TurboOntModel;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Algorithm extends YaqpOntComponent {

    public static final long serialVersionUID = -18477218378326540L;
    private AlgorithmOntology ontology = null;
    private String name, uri;

    

    public Algorithm() {
        super();
    }

    public Algorithm(String name, String uri, AlgorithmOntology ontology) {
        super();
        setName(name);
        setUri(uri);
        setOntology(ontology);
    }

    public Algorithm(TurboOntModel model) {
        super(model);
    }



    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    public AlgorithmOntology getOntology() {
        return ontology;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setOntology(AlgorithmOntology ontology) {
        this.ontology = ontology;
    }

    @Override
    public String toString() {
        String algorithm = "-- ALGORITHM -- \n";
        algorithm += "NAME       :" + getName() + "\n";
        algorithm += "URI        :" + getUri() + "\n";
        if (ontology != null) {
        algorithm += "ONTOLOGY   :"+getOntology();
            
        }
        return algorithm;
    }
}
