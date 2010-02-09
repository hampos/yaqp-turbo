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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.io.publishable.JSONObject;
import org.opentox.io.publishable.PDFObject;
import org.opentox.io.publishable.RDFObject;
import org.opentox.ontology.exceptions.ImproperEntityException;
import org.opentox.ontology.namespaces.OTClass;
import org.opentox.ontology.util.AlgorithmParameter;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class QSARModel extends YaqpComponent {

    //private ArrayList<AlgorithmParameter> tuningParams = new ArrayList<AlgorithmParameter>();
    private Map<String, AlgorithmParameter> params = new HashMap<String, AlgorithmParameter>();

    private int id = 0;
    private String code = null;
    private Feature predictionFeature = null;
    private Feature dependentFeature = null;
    private ArrayList<Feature> independentFeatures = new ArrayList<Feature>();
    private Algorithm algorithm = null;
    private User user = null;
    private String timestamp = null;
    private String dataset = null;
    private ModelStatus modelStatus = ModelStatus.UNDER_DEVELOPMENT;

    public enum ModelStatus {

        APPROVED {

            @Override
            public String toString() {
                return "APPROVED";
            }

            ;
        },
        UNDER_DEVELOPMENT {

            @Override
            public String toString() {
                return "UNDER DEVELOPMENT";
            }

            ;
        }
    }

    public QSARModel() {
        super();
    }

    public QSARModel(
            String code,
            Feature predictionFeature,
            Feature dependentFeature,
            ArrayList<Feature> independentFeatures,
            Algorithm algorithm,
            User user,
            String timestamp,
            String dataset,
            ModelStatus modelStatus) {
        this();
        this.code = code;
        this.predictionFeature = predictionFeature;
        this.dependentFeature = dependentFeature;
        this.independentFeatures = independentFeatures;
        this.algorithm = algorithm;
        this.user = user;
        this.timestamp = timestamp;
        this.dataset = dataset;
        this.modelStatus = modelStatus;
    }

    public QSARModel(
            int id,
            String code,
            Feature predictionFeature,
            Feature dependentFeature,
            ArrayList<Feature> independentFeatures,
            Algorithm algorithm,
            User user,
            String timestamp,
            String dataset,
            ModelStatus modelStatus,
            Map<String, AlgorithmParameter> tuningParams) {
        this(code, predictionFeature, dependentFeature, independentFeatures, algorithm, user, timestamp, dataset, modelStatus);
        this.id = id;
        this.params = tuningParams;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
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

    public ModelStatus getModelStatus() {
        return modelStatus;
    }

    public void setModelStatus(ModelStatus modelStatus) {
        this.modelStatus = modelStatus;
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

    public Map<String, AlgorithmParameter> getParams() {
        return params;
    }

    public void setParams(Map<String, AlgorithmParameter> params) {
        this.params = params;
    }

//    public ArrayList<AlgorithmParameter> getTuningParams() {
//        return tuningParams;
//    }
//
//    public void setTuningParams(ArrayList<AlgorithmParameter> tuningParams) {
//        this.tuningParams = tuningParams;
//    }

    

    @Override
    public PDFObject getPDF() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RDFObject getRDF() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public JSONObject getJson() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected String getTag() {
        return "model";
    }

    @Override
    public URI uri() throws YaqpException {
        String superUri = super.uri().toString();
        try{
        return new URI(superUri+"/"+getId());
        } catch (URISyntaxException ex){
            throw new YaqpException("XGL80", "Improper URI", ex);
        }
    }
}
