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
public class QSARModel extends YaqpOntComponent {

    private int id;
    private String name;
    private String uri;
    private Feature predictionFeature;
    private Feature dependentFeature;
    private Algorithm algorithm;
    private User user;
    private String timestamp;
    private ArrayList<Feature> independentFeatures;

    public QSARModel() {
        super();
    }

    public QSARModel(TurboOntModel model) {
        super(model);
    }

    
    public QSARModel(int id, String name, String uri,
            Feature predictionFeature, Feature dependentFeature,
            Algorithm algorithm, User user, String timestamp) {
        this.id = id;
        this.name = name;
        this.uri = uri;
        this.predictionFeature = predictionFeature;
        this.dependentFeature = dependentFeature;
        this.algorithm = algorithm;
        this.user = user;
        this.timestamp = timestamp;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public Feature getDependentFeature() {
        return dependentFeature;
    }

    public void setDependentFeature(Feature dependentFeature) {
        this.dependentFeature = dependentFeature;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Feature> getIndependentFeatures() {
        return independentFeatures;
    }

    public void setIndependentFeatures(ArrayList<Feature> independentFeatures) {
        this.independentFeatures = independentFeatures;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Feature getPredictionFeature() {
        return predictionFeature;
    }

    public void setPredictionFeature(Feature predictionFeature) {
        this.predictionFeature = predictionFeature;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        String predModel = "-- PREDICTION MODEL --\n";
        predModel += "ID          : " + getId() + "\n";
        predModel += "URI         : " + getUri() + "\n";
        predModel += "NAME        : " + getName() + "\n";
        predModel += "PRED FEATURE: " + "\n" + getPredictionFeature() + "\n";
        predModel += "DEP FEATURE : " + "\n" + getDependentFeature() + "\n";
        predModel += "ALGORITHM   : " + "\n" + getAlgorithm() + "\n";
        predModel += "USER        : " + "\n" + getUser() + "\n";
        predModel += "TIMESTAMP   : " + getTimestamp() + "\n";
        predModel += "IND FEATURES: " + getId() + "\n";
        Iterator<Feature> it = independentFeatures.iterator();
        while (it.hasNext()) {
            predModel += it.next();
        }
        return predModel;

    }
}
