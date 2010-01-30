package org.opentox.ontology.namespaces;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import org.opentox.ontology.TurboOntModel;

/**
 *
 * @author chung
 */
public class OTDataTypeProperties extends YaqpOntEntity{

    public OTDataTypeProperties(Resource resource){
        super(resource);
    }
        

    /**
     *
     * A value
     */
    public static final OTDataTypeProperties value =
            new OTDataTypeProperties(_model.createDatatypeProperty(String.format(_NS_OT, "value")));
    /**
     *
     *
     */
    public static final OTDataTypeProperties units =
            new OTDataTypeProperties(_model.createDatatypeProperty(String.format(_NS_OT, "units")));
    /**
     *
     *
     */
    public static final OTDataTypeProperties has3Dstructure =
            new OTDataTypeProperties(_model.createDatatypeProperty(String.format(_NS_OT, "has3Dstructure")));
    /**
     *
     *
     */
    public static final OTDataTypeProperties hasStatus =
            new OTDataTypeProperties(_model.createDatatypeProperty(String.format(_NS_OT, "hasStatus")));
    /**
     *
     *
     */
    public static final OTDataTypeProperties paramScope =
            new OTDataTypeProperties(_model.createDatatypeProperty(String.format(_NS_OT, "paramScope")));
    /**
     *
     *
     */
    public static final OTDataTypeProperties paramValue =
            new OTDataTypeProperties(_model.createDatatypeProperty(String.format(_NS_OT, "paramValue")));
    /**
     *
     *
     */
    public static final OTDataTypeProperties percentageCompleted =
            new OTDataTypeProperties(_model.createDatatypeProperty(String.format(_NS_OT, "paramValue")));


    @Override
    public Property createProperty(TurboOntModel model) {
        Property p = model.getObjectProperty(getURI());
        return p==null?model.createDatatypeProperty(getURI()):p;
    }

    
}
