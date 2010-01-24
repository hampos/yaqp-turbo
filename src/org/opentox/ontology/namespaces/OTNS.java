package org.opentox.ontology.namespaces;

/**
 * OpenTox Namespace and its basic element: Classes and Properties.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class OTNS {

    private static final String _NS = "http://www.opentox.org/api/1.1#%s";
    public static final String NS = String.format(_NS, "");

    public enum OTClass {

        Algorithm,
        Dataset,
        Feature,
        NumericFeature,
        NominalFeature,
        StringFeature,
        FeatureValue,
        Model,
        Parameter,
        Validation,
        ValidationInfo,
        Task;
    }

    public enum OTProperty {

        dataEntry,
        compound,
        feature,
        values,
        hasSource,
        conformer,
        model,
        parameters,
        report,
        algorithm,
        dependentVariables,
        independentVariables,
        predictedVariables,
        trainingDataset,
        validationReport,
        validation,
        hasValidationInfo,
        validationModel,
        validationPredictionDataset,
        validationTestDataset;
    }
}
