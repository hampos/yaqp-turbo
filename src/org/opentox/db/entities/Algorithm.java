/*
 * YAQP - Yet Another QSAR Project: Machine Learning algorithms designed for
 * the prediction of toxicological features of chemical compounds become
 * available on the Web. Yaqp is developed under OpenTox (http://opentox.org)
 * which is an FP7-funded EU research project.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
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
 */
package org.opentox.db.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Algorithm implements Serializable {

    public static final long serialVersionUID = -18477218378326540L;
    private ArrayList<AlgorithmOntology> ontologies = null;
    private String name, uri;

    public Algorithm() {
    }

    public Algorithm(String name, String uri, ArrayList<AlgorithmOntology> ontologies) {
        setName(name);
        setUri(uri);
        setOntologies(ontologies);
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    public ArrayList<AlgorithmOntology> getOntologies() {
        return ontologies;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setOntologies(ArrayList<AlgorithmOntology> ontologies) {
        this.ontologies = ontologies;
    }

    @Override
    public String toString() {
        String algorithm = "-- ALGORITHM -- \n";
        algorithm += "NAME    :" + getName() + "\n";
        algorithm += "URI     :" + getUri() + "\n";
        if (ontologies != null) {
            algorithm += "ONTOLOGIES:\n";
            Iterator<AlgorithmOntology> it = ontologies.iterator();
            while (it.hasNext()) {
                algorithm += "* "+ it.next().toString();
                algorithm += "\n";
            }
        }
        return algorithm;
    }
}
