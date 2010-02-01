package org.opentox.db.entities;

/**
 *
 * @author hampos
 */
public class MlrModel extends PredictionModel {

    private String dataset;

    public MlrModel(int id, String name, String uri,
            Feature predictionFeature, Feature dependentFeature,
            Algorithm algorithm, User user, String timestamp, String dataset) {
        super(id, name, uri, predictionFeature, dependentFeature, algorithm, user, timestamp);
        this.dataset = dataset;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }


    @Override
    public String toString(){
       String mlrModel = "--- MLR MODEL ---\n";
       mlrModel += super.toString();
       mlrModel += "DATASET     : "+getDataset()+"\n";
       mlrModel += "\n---------------------\n";

       return mlrModel;
    }




}
