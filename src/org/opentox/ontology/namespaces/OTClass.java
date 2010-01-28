package org.opentox.ontology.namespaces;


import com.hp.hpl.jena.rdf.model.Resource;

/**
 *
 * @author chung
 */
public class OTClass extends YaqpOntEntity{

     public OTClass(Resource resource) {
        super(resource);
    }

     /**
      * A Chemical Compound
      */
    public static final OTClass Compound =
            new OTClass(_model.createResource(String.format(_NS_OT, "Compound")));
    /**
     * A conformer of a chemical compound
     */
    public static final OTClass Conformer =
            new OTClass(_model.createResource(String.format(_NS_OT, "Conformer")));
    /**
     * A set of compounds along with their features and values for them.
     */
    public static final OTClass Dataset =
            new OTClass(_model.createResource(String.format(_NS_OT, "Dataset")));
    /**
     * An entry in a {@link OTClass#Dataset dataset} consists of three components, the
     * {@link OTClass#Compound compound}, the {@link OTClass#Feature feature} and the
     * {@link OTClass#FeatureValue feature value}.
     */
    public static final OTClass DataEntry =
            new OTClass(_model.createResource(String.format(_NS_OT, "DataEntry")));
    /**
     * A physicochemical or other property related to a chemical compound.
     */
    public static final OTClass Feature =
            new OTClass(_model.createResource(String.format(_NS_OT, "Feature")));
    /**
     * The value of a {@link OTClass#Feature feature} for some {@link OTClass#Compound compound}.
     */
    public static final OTClass FeatureValue =
            new OTClass(_model.createResource(String.format(_NS_OT, "FeatureValue")));
    /**
     * An algorithm of any type.
     */
    public static final OTClass Algorithm =
            new OTClass(_model.createResource(String.format(_NS_OT, "Algorithm")));
    /**
     * A (predictive or other type) model.
     */
    public static final OTClass Model =
            new OTClass(_model.createResource(String.format(_NS_OT, "Model")));
    /**
     * A validation routine.
     */
    public static final OTClass Validation =
            new OTClass(_model.createResource(String.format(_NS_OT, "Validation")));
    /**
     * Information produced by a validation procedure.
     */
    public static final OTClass ValidationInfo =
            new OTClass(_model.createResource(String.format(_NS_OT, "ValidationInfo")));
    /**
     * A Parameter of an algorithm of of a Model.
     */
    public static final OTClass Parameter =
            new OTClass(_model.createResource(String.format(_NS_OT, "Parameter")));
 

}
