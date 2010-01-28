package org.opentox.ontology.namespaces;

import com.hp.hpl.jena.rdf.model.Property;

/**
 *
 * @author chung
 */
public class OTProperties extends YaqpOntEntity{

    public static final Property dataEntry =
            _model.createProperty(String.format(_NS_OT, "dataEntry"));
    public static final Property compound =
            _model.createProperty(String.format(_NS_OT, "compound"));
    public static final Property feature =
            _model.createProperty(String.format(_NS_OT, "feature"));
    public static final Property values =
            _model.createProperty(String.format(_NS_OT, "values"));
    public static final Property hasSource =
            _model.createProperty(String.format(_NS_OT, "hasSource"));
    public static final Property conformer =
            _model.createProperty(String.format(_NS_OT, "conformer"));   
    public static final Property model =
            _model.createProperty(String.format(_NS_OT, "model"));
    public static final Property report =
            _model.createProperty(String.format(_NS_OT, "report"));
    public static final Property algorithm =
            _model.createProperty(String.format(_NS_OT, "algorithm"));
    public static final Property dependentVariables =
            _model.createProperty(String.format(_NS_OT, "dependentVariables"));
    public static final Property independentVariables =
            _model.createProperty(String.format(_NS_OT, "independentVariables"));
    public static final Property predictedVariables =
            _model.createProperty(String.format(_NS_OT, "predictedVariables"));
    public static final Property trainingDataset =
            _model.createProperty(String.format(_NS_OT, "trainingDataset"));
    public static final Property validationReport =
            _model.createProperty(String.format(_NS_OT, "validationReport"));
    public static final Property validation =
            _model.createProperty(String.format(_NS_OT, "validation"));
    public static final Property hasValidationInfo =
            _model.createProperty(String.format(_NS_OT, "hasValidationInfo"));
    public static final Property validationModel =
            _model.createProperty(String.format(_NS_OT, "validationModel"));
    public static final Property validationPredictionDataset =
            _model.createProperty(String.format(_NS_OT, "validationPredictionDataset"));
    public static final Property validationTestDataset =
            _model.createProperty(String.format(_NS_OT, "validationTestDataset"));
    public static final Property value =
            _model.createProperty(String.format(_NS_OT, "value"));
    public static final Property units =
            _model.createProperty(String.format(_NS_OT, "units"));
    public static final Property has3Dstructure =
            _model.createProperty(String.format(_NS_OT, "has3Dstructure"));
    public static final Property hasStatus =
            _model.createProperty(String.format(_NS_OT, "hasStatus"));
    public static final Property paramScope =
            _model.createProperty(String.format(_NS_OT, "paramScope"));
    public static final Property paramValue =
            _model.createProperty(String.format(_NS_OT, "paramValue"));
    public static final Property statisticsSupported =
            _model.createProperty(String.format(_NS_OT, "statisticsSupported"));
    public static final Property parameters =
            _model.createProperty(String.format(_NS_OT, "parameters"));


}
