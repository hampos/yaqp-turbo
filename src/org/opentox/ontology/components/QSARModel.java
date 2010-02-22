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

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.vocabulary.DC;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.core.exceptions.Cause;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.io.exceptions.YaqpIOException;
import org.opentox.io.publishable.JSONObject;
import org.opentox.io.publishable.PDFObject;
import org.opentox.io.publishable.RDFObject;
import org.opentox.io.publishable.UriListObject;
import org.opentox.io.util.YaqpIOStream;
import org.opentox.ontology.namespaces.OTClass;
import org.opentox.ontology.namespaces.OTObjectProperties;
import org.opentox.ontology.util.AlgorithmParameter;
import org.opentox.ontology.util.Meta;
import org.opentox.ontology.util.YaqpAlgorithms;
import org.opentox.ontology.util.vocabulary.ConstantParameters;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class QSARModel extends YaqpComponent {

    //private ArrayList<AlgorithmParameter> tuningParams = new ArrayList<AlgorithmParameter>();
    private Map<String, AlgorithmParameter> params = new HashMap<String, AlgorithmParameter>();
    private int id = 0;
    private int _minId = Integer.MIN_VALUE, _maxId = Integer.MAX_VALUE;
    private String code = null;
    private Feature predictionFeature = new Feature();
    private Feature dependentFeature = new Feature();
    private ArrayList<Feature> independentFeatures = new ArrayList<Feature>();
    private Algorithm algorithm = new Algorithm();
    private User user = new User();
    private String timestamp = null;
    private String dataset = null;
    private ModelStatus modelStatus = null;

    private boolean hasVec = false;
    private final double doubleMax = (double)Integer.MAX_VALUE;


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
                return "UNDER_DEVELOPMENT";
            }

            ;
        }
    }

    public QSARModel() {
        super();
    }

    private QSARModel(int id) {
        this.id = id;
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
        if( modelStatus != null){
            this.modelStatus = modelStatus;
        }else{
            this.modelStatus = ModelStatus.UNDER_DEVELOPMENT;
        }
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
        this._maxId = id;
        this._minId = id;
        this.params = checkParams(tuningParams);
    }

    

    public int getMaxId() {
        return _maxId;
    }

    public void setMaxId(int _maxId) {
        this._maxId = _maxId;
    }

    public int getMinId() {
        return _minId;
    }

    public void setMinId(int _minId) {
        this._minId = _minId;
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
        this._maxId = id;
        this._minId = id;
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
        this.params = checkParams(params);
    }

    public boolean hasVec(){
        return hasVec;
    }

//    public ArrayList<AlgorithmParameter> getTuningParams() {
//        return tuningParams;
//    }
//
//    public void setTuningParams(ArrayList<AlgorithmParameter> tuningParams) {
//        this.tuningParams = tuningParams;
//    }
    // TODO: Implement this method ASAP (due for Feb 17)!
    @Override
    public PDFObject getPDF() {
        PDFObject pdf = new PDFObject();
        return pdf;
    }

    // TODO: Implement this method ASAP (due for Feb 17)!

    /**
     * As fas as the model parameters are concerned, a set of individuals will be created
     * for the data model each one of which corresponds to an entry of the parameters map
     * which is available through {@link QSARModel#getParams() }. You have to specify
     * correctly these parameters in order to avoid issued with the RDF representation of
     * the QSARModel.
     * @return
     */
    @Override
    public RDFObject getRDF() {
        RDFObject rdf = new RDFObject();
        

        // SOME ONTOLOGICAL CLASES ARE CONTAINED IN THE DATASET
        rdf.includeOntClasses(OTClass.Dataset, OTClass.Algorithm, OTClass.Feature, OTClass.Model, OTClass.User, OTClass.UserGroup, OTClass.Parameter);

        // THE WHOLE REPRESENTATION OF THE ALGORITHM IS INCLUDED IN THE DATA MODEL
        // OF THE QSAR MODEL.
        rdf = new RDFObject( rdf.union(getAlgorithm().getRDF())   );
        
        try {
            Individual qsarModel = rdf.createIndividual(uri().toString(), rdf.createOntResource(OTClass.Model.getURI()));
            Individual algIndiv = rdf.createIndividual(getAlgorithm().uri().toString(), rdf.createOntResource(OTClass.Algorithm.getURI()));
            qsarModel.addProperty(OTObjectProperties.algorithm.createProperty(rdf), algIndiv);

            // STANDARD META INFORMATION ABOUT THE MODEL LIKE THE PHBLISHER, THE AUDIENCE AND SO ON
            // SOME OF THESE ARE THE GLOBAL META DATA FOR ALL YAQP ENTITIES (LIKE THE PUBLISHER). FOR OTHER, LIKE
            // THE AUDIENCES, META INFO ARE ADAPTED FROM THOSE OF THE TRAINING ALGORITHM.
            Meta standardeModelMeta = new Meta();
            /* title */
            qsarModel.addLiteral(
                    rdf.createAnnotationProperty(DC.title.getURI()),
                    rdf.createTypedLiteral(
                    "Predictive QSAR Model generated by the algorithm " + getAlgorithm().getMeta().getName(),
                    XSDDatatype.XSDstring));
            /* description */
            qsarModel.addLiteral(
                    rdf.createAnnotationProperty(DC.description.getURI()),
                    rdf.createTypedLiteral(
                    "Predictive QSAR Model generated by the algorithm "
                    + getAlgorithm().getMeta().getName() + " for the prediction of the feature " + getDependentFeature().getURI(),
                    XSDDatatype.XSDstring));
            /* identifier */
            qsarModel.addLiteral(rdf.createAnnotationProperty(DC.identifier.getURI()), rdf.createTypedLiteral(uri().toString(), XSDDatatype.XSDanyURI));
            /* publisher */
            qsarModel.addLiteral(rdf.createAnnotationProperty(DC.publisher.getURI()), rdf.createTypedLiteral(standardeModelMeta.publisher, XSDDatatype.XSDstring));
            /* */
            qsarModel.addLiteral(rdf.createAnnotationProperty(DC.date.getURI()), rdf.createTypedLiteral(standardeModelMeta.date, XSDDatatype.XSDdate));
            /* language */
            qsarModel.addLiteral(rdf.createAnnotationProperty(DC.language.getURI()), rdf.createTypedLiteral(standardeModelMeta.language, XSDDatatype.XSDstring));
            /* rights */
            qsarModel.addLiteral(rdf.createAnnotationProperty(DC.rights.getURI()), rdf.createTypedLiteral(standardeModelMeta.rights, XSDDatatype.XSDstring));

            /* training dataset */
            Individual datasetIndiv = rdf.createIndividual(getDataset(), rdf.createOntResource(OTClass.Dataset.getURI()));
            datasetIndiv.addComment("The dataset used to train the model", Locale.ENGLISH.getLanguage());
            qsarModel.addProperty(rdf.createAnnotationProperty(OTObjectProperties.trainingDataset.getURI()), datasetIndiv);

            /* */
            // Add all parameters:
            Individual iparam;
            
            
            
        } catch (YaqpException ex) { /* What should be done? */  }
        return rdf;
    }

    public static void main(String... args) throws YaqpIOException{
        QSARModel m = new QSARModel("sdfsdfsdf", new Feature(), new Feature(), new ArrayList<Feature>(), YaqpAlgorithms.MLR, new User(), "", null, ModelStatus.APPROVED);
        m.setParams(new HashMap<String, AlgorithmParameter>());
        m.setDataset("http://someserver.com/dataset/10");
        m.getRDF().publish(new YaqpIOStream(System.out));
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
        try {
            return new URI(superUri + "/" + getId());
        } catch (URISyntaxException ex) {
            throw new YaqpException(Cause.XTC743, "Improper URI", ex);
        }
    }



    private Map<String, AlgorithmParameter> initParams() {
        Map<String,AlgorithmParameter> newParams = new HashMap<String,AlgorithmParameter>();
        newParams.put(ConstantParameters.gamma+"_min", new AlgorithmParameter(0.0));
        newParams.put(ConstantParameters.gamma+"_max", new AlgorithmParameter(doubleMax));

        newParams.put(ConstantParameters.epsilon+"_min", new AlgorithmParameter(0.0));
        newParams.put(ConstantParameters.epsilon+"_max", new AlgorithmParameter(doubleMax));

        newParams.put(ConstantParameters.cost+"_min", new AlgorithmParameter(0.0));
        newParams.put(ConstantParameters.cost+"_max", new AlgorithmParameter(doubleMax));

        newParams.put(ConstantParameters.coeff0+"_min", new AlgorithmParameter(0.0));
        newParams.put(ConstantParameters.coeff0+"_max", new AlgorithmParameter(doubleMax));

        newParams.put(ConstantParameters.tolerance+"_min", new AlgorithmParameter(0.0));
        newParams.put(ConstantParameters.tolerance+"_max", new AlgorithmParameter(doubleMax));

        newParams.put(ConstantParameters.cacheSize+"_min", new AlgorithmParameter(0));
        newParams.put(ConstantParameters.cacheSize+"_max", new AlgorithmParameter(Integer.MAX_VALUE));

        newParams.put(ConstantParameters.kernel, new AlgorithmParameter("%%"));

        newParams.put(ConstantParameters.degree+"_min", new AlgorithmParameter(0));
        newParams.put(ConstantParameters.degree+"_max", new AlgorithmParameter(Integer.MAX_VALUE));

        return newParams;
    }

    private Map<String,AlgorithmParameter> checkParams(Map<String,AlgorithmParameter> params){
        Map<String,AlgorithmParameter> newParams = initParams();

        if ((params.get(ConstantParameters.gamma) != null && params.get(ConstantParameters.gamma).paramValue != null)){
            newParams.put(ConstantParameters.gamma, params.get(ConstantParameters.gamma));
            newParams.put(ConstantParameters.gamma+"_min", params.get(ConstantParameters.gamma));
            newParams.put(ConstantParameters.gamma+"_max", params.get(ConstantParameters.gamma));
            hasVec = true;
        }else{
            if ((params.get(ConstantParameters.gamma+"_min") != null && params.get(ConstantParameters.gamma+"_min").paramValue != null)){
                newParams.put(ConstantParameters.gamma+"_min", params.get(ConstantParameters.gamma+"_min"));
                hasVec = true;
            }
            if ((params.get(ConstantParameters.gamma+"_max") != null && params.get(ConstantParameters.gamma+"_max").paramValue != null)){
                newParams.put(ConstantParameters.gamma+"_max", params.get(ConstantParameters.gamma+"_max"));
                hasVec = true;
            }
        }

        if ((params.get(ConstantParameters.epsilon) != null && params.get(ConstantParameters.epsilon).paramValue != null)){
            newParams.put(ConstantParameters.epsilon, params.get(ConstantParameters.epsilon));
            newParams.put(ConstantParameters.epsilon+"_min", params.get(ConstantParameters.epsilon));
            newParams.put(ConstantParameters.epsilon+"_max", params.get(ConstantParameters.epsilon));
            hasVec = true;
        }else{
            if ((params.get(ConstantParameters.epsilon+"_min") != null && params.get(ConstantParameters.epsilon+"_min").paramValue != null)){
                newParams.put(ConstantParameters.epsilon+"_min", params.get(ConstantParameters.epsilon+"_min"));
                hasVec = true;
            }
            if ((params.get(ConstantParameters.epsilon+"_max") != null && params.get(ConstantParameters.epsilon+"_max").paramValue != null)){
                newParams.put(ConstantParameters.epsilon+"_max", params.get(ConstantParameters.epsilon+"_max"));
                hasVec = true;
            }
        }

        if ((params.get(ConstantParameters.cost) != null && params.get(ConstantParameters.cost).paramValue != null)){
            newParams.put(ConstantParameters.cost, params.get(ConstantParameters.cost));
            newParams.put(ConstantParameters.cost+"_min", params.get(ConstantParameters.cost));
            newParams.put(ConstantParameters.cost+"_max", params.get(ConstantParameters.cost));
            hasVec = true;
        }else{
            if ((params.get(ConstantParameters.cost+"_min") != null && params.get(ConstantParameters.cost+"_min").paramValue != null)){
                newParams.put(ConstantParameters.cost+"_min", params.get(ConstantParameters.cost+"_min"));
                hasVec = true;
            }
            if ((params.get(ConstantParameters.cost+"_max") != null && params.get(ConstantParameters.cost+"_max").paramValue != null)){
                newParams.put(ConstantParameters.cost+"_max", params.get(ConstantParameters.cost+"_max"));
                hasVec = true;
            }
        }

        if ((params.get(ConstantParameters.coeff0) != null && params.get(ConstantParameters.coeff0).paramValue != null)){
            newParams.put(ConstantParameters.coeff0, params.get(ConstantParameters.coeff0));
            newParams.put(ConstantParameters.coeff0+"_min", params.get(ConstantParameters.coeff0));
            newParams.put(ConstantParameters.coeff0+"_max", params.get(ConstantParameters.coeff0));
            hasVec = true;
        }else{
            if ((params.get(ConstantParameters.coeff0+"_min") != null && params.get(ConstantParameters.coeff0+"_min").paramValue != null)){
                newParams.put(ConstantParameters.coeff0+"_min", params.get(ConstantParameters.coeff0+"_min"));
                hasVec = true;
            }
            if ((params.get(ConstantParameters.coeff0+"_max") != null && params.get(ConstantParameters.coeff0+"_max").paramValue != null)){
                newParams.put(ConstantParameters.coeff0+"_max", params.get(ConstantParameters.coeff0+"_max"));
                hasVec = true;
            }
        }

        if ((params.get(ConstantParameters.cacheSize) != null && params.get(ConstantParameters.cacheSize).paramValue != null)){
            newParams.put(ConstantParameters.cacheSize, params.get(ConstantParameters.cacheSize));
            newParams.put(ConstantParameters.cacheSize+"_min", params.get(ConstantParameters.cacheSize));
            newParams.put(ConstantParameters.cacheSize+"_max", params.get(ConstantParameters.cacheSize));
            hasVec = true;
        }else{
            if ((params.get(ConstantParameters.cacheSize+"_min") != null && params.get(ConstantParameters.cacheSize+"_min").paramValue != null)){
                newParams.put(ConstantParameters.cacheSize+"_min", params.get(ConstantParameters.cacheSize+"_min"));
                hasVec = true;
            }
            if ((params.get(ConstantParameters.cacheSize+"_max") != null && params.get(ConstantParameters.cacheSize+"_max").paramValue != null)){
                newParams.put(ConstantParameters.cacheSize+"_max", params.get(ConstantParameters.cacheSize+"_max"));
                hasVec = true;
            }
        }

        if ((params.get(ConstantParameters.tolerance) != null && params.get(ConstantParameters.tolerance).paramValue != null)){
            newParams.put(ConstantParameters.tolerance, params.get(ConstantParameters.tolerance));
            newParams.put(ConstantParameters.tolerance+"_min", params.get(ConstantParameters.tolerance));
            newParams.put(ConstantParameters.tolerance+"_max", params.get(ConstantParameters.tolerance));
            hasVec = true;
        }else{
            if ((params.get(ConstantParameters.tolerance+"_min") != null && params.get(ConstantParameters.tolerance+"_min").paramValue != null)){
                newParams.put(ConstantParameters.tolerance+"_min", params.get(ConstantParameters.tolerance+"_min"));
                hasVec = true;
            }
            if ((params.get(ConstantParameters.tolerance+"_max") != null && params.get(ConstantParameters.tolerance+"_max").paramValue != null)){
                newParams.put(ConstantParameters.tolerance+"_max", params.get(ConstantParameters.tolerance+"_max"));
                hasVec = true;
            }
        }

        if ((params.get(ConstantParameters.degree) != null && params.get(ConstantParameters.degree).paramValue != null)){
            newParams.put(ConstantParameters.degree, params.get(ConstantParameters.degree));
            newParams.put(ConstantParameters.degree+"_min", params.get(ConstantParameters.degree));
            newParams.put(ConstantParameters.degree+"_max", params.get(ConstantParameters.degree));
            hasVec = true;
        }else{
            if ((params.get(ConstantParameters.degree+"_min") != null && params.get(ConstantParameters.degree+"_min").paramValue != null)){
                newParams.put(ConstantParameters.degree+"_min", params.get(ConstantParameters.degree+"_min"));
                hasVec = true;
            }
            if ((params.get(ConstantParameters.degree+"_max") != null && params.get(ConstantParameters.degree+"_max").paramValue != null)){
                newParams.put(ConstantParameters.degree+"_max", params.get(ConstantParameters.degree+"_max"));
                hasVec = true;
            }
        }

        if ((params.get(ConstantParameters.kernel) != null && params.get(ConstantParameters.kernel).paramValue != null)){
            newParams.put(ConstantParameters.kernel, params.get(ConstantParameters.kernel));
            hasVec = true;
        }

        return newParams;
    }


    @Override
    public UriListObject getUriList() {
        ArrayList<URI> uriList = new ArrayList<URI>(1);
        try {
            uriList.add(uri());
            return new UriListObject(uriList);
        } catch (YaqpException ex) {
            return null;
        }
    }

    @Override
    public QSARModel getSkroutz(){
        return new QSARModel(this.getId());
    }

    @Override
    public boolean equals(Object obj){
        if(obj.getClass() == this.getClass()){
            QSARModel model = (QSARModel) obj;
            return (this.getId() == model.getId());
        }else{
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + this.id;
        return hash;
    }



}
