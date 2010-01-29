package org.opentox.ontology.namespaces;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import org.opentox.ontology.TurboOntModel;

/**
 *
 * @author chung
 */
public class OTObjectProperties extends YaqpOntEntity{


    public OTObjectProperties(Resource resource) {
        super(resource);
    }
    

    /**
     *
     * A Data Entry in a Dataset
     */
    public static final OTObjectProperties dataEntry =
            new OTObjectProperties(_model.createObjectProperty(String.format(_NS_OT, "dataEntry")));
    /**
     *
     * Denotes that a resource has a certain compound. Applies on data entries.
     */
    public static final OTObjectProperties compound =
            new OTObjectProperties(_model.createObjectProperty(String.format(_NS_OT, "compound")));
    /**
     *
     * Denotes that a resource has a certain feature.
     */
    public static final OTObjectProperties feature =
            new OTObjectProperties(_model.createObjectProperty(String.format(_NS_OT, "feature")));
    /**
     *
     *
     */
    public static final OTObjectProperties values =
            new OTObjectProperties(_model.createObjectProperty(String.format(_NS_OT, "values")));
    /**
     *
     *
     */
    public static final OTObjectProperties hasSource =
            new OTObjectProperties(_model.createObjectProperty(String.format(_NS_OT, "hasSource")));
    /**
     *
     *
     */
    public static final OTObjectProperties conformer =
            new OTObjectProperties(_model.createObjectProperty(String.format(_NS_OT, "conformer")));
    /**
     *
     *
     */
    public static final OTObjectProperties model =
            new OTObjectProperties(_model.createObjectProperty(String.format(_NS_OT, "model")));
    /**
     *
     *
     */
    public static final OTObjectProperties report =
            new OTObjectProperties(_model.createObjectProperty(String.format(_NS_OT, "report")));
    /**
     *
     *
     */
    public static final OTObjectProperties algorithm =
            new OTObjectProperties(_model.createObjectProperty(String.format(_NS_OT, "algorithm")));
    /**
     *
     *
     * Denotes that a resource has a certain dependent variable. Applies on Models.
     */
    public static final OTObjectProperties dependentVariables =
            new OTObjectProperties(_model.createObjectProperty(String.format(_NS_OT, "dependentVariables")));
    /**
     *
     *
     * Denotes that a resource has a certain independent variable. Applies on Models.
     */
    public static final OTObjectProperties independentVariables =
            new OTObjectProperties(_model.createObjectProperty(String.format(_NS_OT, "independentVariables")));
    /**
     *
     *
     * Denotes that a resource has a certain predicted variable. Applies on Models.
     */
    public static final OTObjectProperties predictedVariables =
            new OTObjectProperties(_model.createObjectProperty(String.format(_NS_OT, "predictedVariables")));
    /**
     *
     *
     *
     */
    public static final OTObjectProperties trainingDataset =
            new OTObjectProperties(_model.createObjectProperty(String.format(_NS_OT, "trainingDataset")));
    /**
     *
     *
     */
    public static final OTObjectProperties validationReport =
            new OTObjectProperties(_model.createObjectProperty(String.format(_NS_OT, "validationReport")));
    /**
     *
     *
     */
    public static final OTObjectProperties validation =
            new OTObjectProperties(_model.createObjectProperty(String.format(_NS_OT, "validation")));
    /**
     *
     *
     */
    public static final OTObjectProperties hasValidationInfo =
            new OTObjectProperties(_model.createObjectProperty(String.format(_NS_OT, "hasValidationInfo")));
    /**
     *
     *
     */
    public static final OTObjectProperties validationModel =
            new OTObjectProperties(_model.createObjectProperty(String.format(_NS_OT, "validationModel")));
    /**
     *
     *
     */
    public static final OTObjectProperties validationPredictionDataset =
            new OTObjectProperties(_model.createObjectProperty(String.format(_NS_OT, "validationPredictionDataset")));
    /**
     *
     *
     */
    public static final OTObjectProperties validationTestDataset =
            new OTObjectProperties(_model.createObjectProperty(String.format(_NS_OT, "validationTestDataset")));
    /**
     *
     *
     */
    public static final OTObjectProperties parameters =
            new OTObjectProperties(_model.createObjectProperty(String.format(_NS_OT, "parameters")));

    @Override
    public Property createProperty(TurboOntModel model) {
        Property p = model.getObjectProperty(getURI());
        return p==null?model.createObjectProperty(getURI()):p;
    }

}
