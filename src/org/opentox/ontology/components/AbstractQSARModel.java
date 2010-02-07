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
package org.opentox.ontology.components;


import java.util.ArrayList;
import java.util.Iterator;
import org.opentox.io.publishable.PDFObject;
import org.opentox.io.publishable.RDFObject;
import org.opentox.io.publishable.TurtleObject;
import org.opentox.ontology.util.AlgorithmParameter;

/**
 * TODO: Update QSARModel and its subclasses according to the corresponding db tables
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class AbstractQSARModel extends YaqpComponent {

    private int id;
    private String code;
    private Feature predictionFeature;
    private Feature dependentFeature;
    private ArrayList<Feature> independentFeatures;
    private Algorithm algorithm;
    private User user;
    private String timestamp;
    private String dataset;

    public AbstractQSARModel() {
        super();
    }

    public AbstractQSARModel(
            String code,
            Feature predictionFeature,
            Feature dependentFeature,
            ArrayList<Feature> independentFeatures,
            Algorithm algorithm,
            User user,
            String timestamp,
            String dataset) {
        this.code = code;
        this.predictionFeature = predictionFeature;
        this.dependentFeature = dependentFeature;
        this.independentFeatures = independentFeatures;
        this.algorithm = algorithm;
        this.user = user;
        this.timestamp = timestamp;
        this.dataset = dataset;
    }

    public abstract ArrayList<AlgorithmParameter> getTuningParams();

    @Override
    public PDFObject getPDF() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RDFObject getRDF() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TurtleObject getTurtle() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public org.opentox.io.publishable.JSONObject getJson() {
        throw new UnsupportedOperationException("Not supported yet.");
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public ArrayList<Feature> getIndependentFeatures() {
        return independentFeatures;
    }

    public void setIndependentFeatures(ArrayList<Feature> independentFeatures) {
        this.independentFeatures = independentFeatures;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        String predModel = "-- PREDICTION MODEL --\n";
        predModel += "ID                    : " + getId() + "\n";
        predModel += "CODE                  : " + getCode() + "\n";
        predModel += "DATASET               : " + getCode() + "\n";
        predModel += "PRED FEATURE          : " + "\n" + getPredictionFeature() + "\n";
        predModel += "DEP FEATURE           : " + "\n" + getDependentFeature() + "\n";
        predModel += "ALGORITHM             : " + getAlgorithm().getMeta().name + "\n";
        predModel += "USER                  : " + getUser().getEmail() + "\n";
        predModel += "TIMESTAMP             : " + getTimestamp() + "\n";
        predModel += "INDEPENDENT FEATURES  : \n";
        Iterator<Feature> it = independentFeatures.iterator();
        while (it.hasNext()) {
            predModel += it.next() + "\n";
        }
        return predModel;

    }
}
