/*
 * YAQP - Yet Another QSAR Project: Machine Learning algorithms designed for
 * the prediction of toxicological features of chemical compounds become
 * available on the Web. Yaqp is developed under OpenTox (http://opentox.org)
 * which is an FP7-funded EU research project.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
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
 */
package org.opentox.ontology.namespaces;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import org.opentox.io.publishable.OntObject;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
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
    public Property createProperty(OntObject model) {
        Property p = model.getObjectProperty(getURI());
        return p==null?model.createObjectProperty(getURI()):p;
    }

}
