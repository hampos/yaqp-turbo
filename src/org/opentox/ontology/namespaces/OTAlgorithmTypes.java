package org.opentox.ontology.namespaces;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.rdf.model.Resource;
import org.opentox.ontology.TurboOntModel;

/**
 *
 *
 * A collection of algorithm types used in OpenTox.
 * @author Pantelis Soapsakis
 * @author Charalampos Chomenides
 */
public class OTAlgorithmTypes extends YaqpOntEntity {

    public OTAlgorithmTypes() {
    }

    /**
     *
     * Construct a new Algorithm Type given a resource.
     * @param resource
     */
    public OTAlgorithmTypes(Resource resource) {
        super(resource);
    }
    /**
     *
     * Classification Algorithms
     */
    public static final OTAlgorithmTypes Classification =
            new OTAlgorithmTypes(_model.createResource(String.format(_NS_AlgorithmTypes, "Classification"))) {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass();
                    cl.setSuperClass(Learning.getResource());
                    return cl;
                }
            };
    /**
     *
     * Eager Classification Algorithms with many targets.
     */
    public static final OTAlgorithmTypes ClassificationEagerMultipleTargets =
            new OTAlgorithmTypes(_model.createResource(String.format(_NS_AlgorithmTypes, "ClassificationEagerMultipleTargets"))) {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass();
                    cl.setRDFType(Classification.getResource());
                    cl.setRDFType(EagerLearning.getResource());
                    cl.setRDFType(MultipleTargets.getResource());
                    return cl;
                }
            };
    /**
     *
     * Eager Regression Algorithms with many targets.
     */
    public static final OTAlgorithmTypes RegressionEagerMultipleTargets =
            new OTAlgorithmTypes(_model.createResource(String.format(_NS_AlgorithmTypes, "RegressionEagerMultipleTargets"))) {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass();
                    cl.setRDFType(Regression.getResource());
                    cl.setRDFType(EagerLearning.getResource());
                    cl.setRDFType(MultipleTargets.getResource());
                    return cl;
                }
            };
    /**
     *
     * Eager Regression Algorithms with one single targer.
     */
    public static final OTAlgorithmTypes RegressionEagerSingleTarget =
            new OTAlgorithmTypes(_model.createResource(String.format(_NS_AlgorithmTypes, "RegressionEagerSingleTarget"))) {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass();
                    cl.setRDFType(Regression.getResource());
                    cl.setRDFType(EagerLearning.getResource());
                    cl.setRDFType(SingleTarget.getResource());
                    return cl;
                }
            };
    /**
     *
     * Eager Classification Algorithms with one single targer.
     */
    public static final OTAlgorithmTypes ClassificationEagerSingleTarget =
            new OTAlgorithmTypes(_model.createResource(String.format(_NS_AlgorithmTypes, "ClassificationEagerSingleTarget"))) {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass();
                    cl.setRDFType(Classification.getResource());
                    cl.setRDFType(EagerLearning.getResource());
                    cl.setRDFType(SingleTarget.getResource());
                    return cl;
                }
            };
    /**
     *
     * Eager Learning Algorithms.
     */
    public static final OTAlgorithmTypes EagerLearning =
            new OTAlgorithmTypes(_model.createResource(String.format(_NS_AlgorithmTypes, "EagerLearning"))) {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass();
                    cl.setSuperClass(Learning.getResource());
                    cl.setDisjointWith(LazyLearning.getResource());
                    return cl;
                }
            };
    /**
     * Lazy Learning Algorithms.
     */
    public static final OTAlgorithmTypes LazyLearning =
            new OTAlgorithmTypes(_model.createResource(String.format(_NS_AlgorithmTypes, "LazyLearning"))) {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass();
                    cl.setSuperClass(Learning.getResource());
                    return cl;
                }
            };
    /**
     *
     * Regression Machine Learning Algorithms.
     */
    public static final OTAlgorithmTypes Regression =
            new OTAlgorithmTypes(_model.createResource(String.format(_NS_AlgorithmTypes, "Regression"))) {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass();
                    cl.setSuperClass(Learning.getResource());
                    cl.setDisjointWith(Classification.getResource());
                    return cl;
                }
            };
    /**
     *
     * Any kind of supervised algorithms.
     */
    public static final OTAlgorithmTypes Supervised =
            new OTAlgorithmTypes(_model.createResource(String.format(_NS_AlgorithmTypes, "Supervised"))) {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass();
                    cl.setSuperClass(AlgorithmType.getResource());
                    return cl;
                }
            };
    /**
     *
     * Learning algorithms of any type.
     */
    public static final OTAlgorithmTypes Learning =
            new OTAlgorithmTypes(_model.createResource(String.format(_NS_AlgorithmTypes, "Learning"))) {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass();
                    cl.setSuperClass(MSDMTox.getResource());
                    return cl;
                }
            };
    /**
     *
     * Learning algorithms for a single target.
     */
    public static final OTAlgorithmTypes SingleTarget =
            new OTAlgorithmTypes(_model.createResource(String.format(_NS_AlgorithmTypes, "SingleTarget"))) {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass();
                    cl.setSuperClass(MSDMTox.getResource());
                    return cl;
                }
            };
    /**
     *
     * Learning algorithms for multiple targets.
     */
    public static final OTAlgorithmTypes MultipleTargets =
            new OTAlgorithmTypes(_model.createResource(String.format(_NS_AlgorithmTypes, "MultipleTargets"))) {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass();
                    cl.setSuperClass(MSDMTox.getResource());
                    cl.setDisjointWith(SingleTarget.getResource());
                    return cl;
                }
            };
    /**
     *
     * MSDMTOx Algorithms
     */
    public static final OTAlgorithmTypes MSDMTox =
            new OTAlgorithmTypes(_model.createResource(String.format(_NS_AlgorithmTypes, "MSDMTox"))) {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass();
                    cl.setSuperClass(AlgorithmType.getResource());
                    return cl;
                }
            };
    /**
     *
     * Preprocessing algorithms.
     */
    public static final OTAlgorithmTypes Preprocessing =
            new OTAlgorithmTypes(_model.createResource(String.format(_NS_AlgorithmTypes, "Preprocessing"))) {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass();
                    cl.setSuperClass(AlgorithmType.getResource());
                    return cl;
                }
            };
    /**
     *
     * Datacleanup algorithms (Preparation of a dataset for training).
     */
    public static final OTAlgorithmTypes DataCleanup =
            new OTAlgorithmTypes(_model.createResource(String.format(_NS_AlgorithmTypes, "DataCleanup"))) {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass();
                    cl.setSuperClass(Preprocessing.getResource());
                    return cl;
                }
            };
    /**
     *
     * Algorithms for the calculation of molecular descriptors.
     */
    public static final OTAlgorithmTypes DescriptorCalculation =
            new OTAlgorithmTypes(_model.createResource(String.format(_NS_AlgorithmTypes, "DescriptorCalculation")));
    /**
     *
     * Normalization Algorithms.
     */
    public static final OTAlgorithmTypes Normalization =
            new OTAlgorithmTypes(_model.createResource(String.format(_NS_AlgorithmTypes, "Normalization"))) {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass();
                    cl.setSuperClass(Preprocessing.getResource());
                    return cl;
                }
            };
    /**
     *
     * Unsupervised Filtering routines.
     */
    public static final OTAlgorithmTypes Unsupervised =
            new OTAlgorithmTypes(_model.createResource(String.format(_NS_AlgorithmTypes, "Unsupervised"))) {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass();
                    cl.setSuperClass(AlgorithmType.getResource());
                    return cl;
                }
            };
    /**
     *
     * Feature selection.
     */
    public static final OTAlgorithmTypes FeatureSelection =
            new OTAlgorithmTypes(_model.createResource(String.format(_NS_AlgorithmTypes, "FeatureSelection"))) {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass();
                    cl.setSuperClass(Preprocessing.getResource());
                    return cl;
                }
            };
    /**
     *
     * Supervised feature selection.
     */
    public static final OTAlgorithmTypes FeatureSelectionSupervised =
            new OTAlgorithmTypes(_model.createResource(String.format(_NS_AlgorithmTypes, "FeatureSelectionSupervised"))) {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass();
                    cl.setRDFType(FeatureSelection.getResource());
                    cl.setRDFType(Supervised.getResource());
                    return cl;
                }
            };
    /**
     *
     * Unsupervised feature selection.
     */
    public static final OTAlgorithmTypes FeatureSelectionUnsupervised =
            new OTAlgorithmTypes(_model.createResource(String.format(_NS_AlgorithmTypes, "FeatureSelectionUnsupervised"))) {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass();
                    cl.setRDFType(FeatureSelection.getResource());
                    cl.setRDFType(Unsupervised.getResource());
                    return cl;
                }
            };
    /**
     *
     * Algorithm of any type.
     */
    public static final OTAlgorithmTypes AlgorithmType =
            new OTAlgorithmTypes(_model.createResource(String.format(_NS_AlgorithmTypes, "AlgorithmType")));
}
