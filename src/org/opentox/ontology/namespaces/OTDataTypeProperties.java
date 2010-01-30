package org.opentox.ontology.namespaces;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import org.opentox.ontology.TurboOntModel;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class OTDataTypeProperties extends YaqpOntEntity{

    public OTDataTypeProperties(Resource resource){
        super(resource);
    }
        

    /**
     *
     * A value.
     */
    public static final OTDataTypeProperties value =
            new OTDataTypeProperties(_model.createDatatypeProperty(String.format(_NS_OT, "value")));
    /**
     *
     * The units of a parameter or other measure.
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
     * The status of an entity. For example for a Task, the range of this variable
     * is <code>Running, Cancelled, Completed</code>.
     */
    public static final OTDataTypeProperties hasStatus =
            new OTDataTypeProperties(_model.createDatatypeProperty(String.format(_NS_OT, "hasStatus")));
    /**
     *
     * Scope of a parameter (optional/mandatory).
     */
    public static final OTDataTypeProperties paramScope =
            new OTDataTypeProperties(_model.createDatatypeProperty(String.format(_NS_OT, "paramScope")));
    /**
     *
     * Value of a parameter.
     */
    public static final OTDataTypeProperties paramValue =
            new OTDataTypeProperties(_model.createDatatypeProperty(String.format(_NS_OT, "paramValue")));
    /**
     *
     * Percentage of completion of a running task.
     */
    public static final OTDataTypeProperties percentageCompleted =
            new OTDataTypeProperties(_model.createDatatypeProperty(String.format(_NS_OT, "percentageCompleted")));


    @Override
    public Property createProperty(TurboOntModel model) {
        Property p = model.getObjectProperty(getURI());
        return p==null?model.createDatatypeProperty(getURI()):p;
    }

    
}
