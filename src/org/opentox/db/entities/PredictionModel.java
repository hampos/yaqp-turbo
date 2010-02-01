package org.opentox.db.entities;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author hampos
 */
public abstract class PredictionModel {
    private int id;
    private String name;
    private String uri;
    private Feature predictionFeature;
    private Feature dependentFeature;
    private Algorithm algorithm;
    private User user;
    private String timestamp;

    private ArrayList<Feature> independentFeatures;

    public PredictionModel(int id, String name, String uri,
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
    public String toString(){
        String predModel = "-- PREDICTION MODEL --\n";
        predModel += "ID          : "+getId()+"\n";
        predModel += "URI         : "+getUri()+"\n";
        predModel += "NAME        : "+getName()+"\n";
        predModel += "PRED FEATURE: "+"\n"+getPredictionFeature()+"\n";
        predModel += "DEP FEATURE : "+"\n"+getDependentFeature()+"\n";
        predModel += "ALGORITHM   : "+"\n"+getAlgorithm()+"\n";
        predModel += "USER        : "+"\n"+getUser()+"\n";
        predModel += "TIMESTAMP   : "+getTimestamp()+"\n";
        predModel += "IND FEATURES: "+getId()+"\n";
        Iterator<Feature> it = independentFeatures.iterator();
        while(it.hasNext()){
            predModel += it.next();
        }
        return predModel;

    }
}
