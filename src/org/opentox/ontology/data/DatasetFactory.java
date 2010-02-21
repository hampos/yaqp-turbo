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
package org.opentox.ontology.data;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.vocabulary.DC;
import java.util.Enumeration;
import org.opentox.config.Configuration;
import org.opentox.io.publishable.OntObject;
import org.opentox.io.publishable.RDFObject;
import org.opentox.ontology.namespaces.OTClass;
import org.opentox.ontology.namespaces.OTDataTypeProperties;
import org.opentox.ontology.namespaces.OTObjectProperties;
import weka.core.Attribute;
import weka.core.Instances;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DatasetFactory {

    public static Dataset getDataset(final OntObject data) {
        return new Dataset(data);
    }

    /**
     * Creates a {@link Dataset dataset object} from a weka <code>Instances</code> one.
     * @param data
     *      The data in a weka <code>Instances</code> object.
     * @return
     *      Instance of {@link Dataset }
     * @see DatasetFactory#getDataset(org.opentox.io.publishable.OntObject) create form ontological object.
     */
    public static Dataset getDataset(final Instances data) {

        final int NUM_COMPOUNDS = data.numInstances();
        final int NUM_FEATURES = data.numAttributes();

        Attribute compoundAttribute = data.attribute(0);
        Attribute predictionAttribute = data.attribute(1);


        OntObject oo = new RDFObject();
        oo.includeOntClasses(OTClass.Compound, OTClass.Feature, OTClass.NominalFeature, OTClass.NumericFeature, OTClass.FeatureValue);

        Individual predictionIndiv = null;
        int targetType = predictionAttribute.type();
        if (targetType == Attribute.NUMERIC) {
            predictionIndiv = oo.createIndividual(predictionAttribute.name(), OTClass.NumericFeature.getOntClass(oo));
        } else if (targetType == Attribute.NOMINAL) {
            predictionIndiv = oo.createIndividual(predictionAttribute.name(), OTClass.NominalFeature.getOntClass(oo));
            Enumeration nominalValues = predictionAttribute.enumerateValues();
            while (nominalValues.hasMoreElements()) {
                predictionIndiv.addProperty(OTDataTypeProperties.acceptValue.createProperty(oo), oo.createTypedLiteral(nominalValues.nextElement().toString()));
            }
        }

        Individual dataset;
        dataset = oo.createIndividual(OTClass.Dataset.getOntClass(oo));
        dataset.addProperty(oo.createAnnotationProperty(DC.title.getURI()), oo.createTypedLiteral("Predicted values for the feature " + data.attribute(1).name()));
        dataset.addProperty(oo.createAnnotationProperty(DC.creator.getURI()), oo.createTypedLiteral(Configuration.BASE_URI, XSDDatatype.XSDanyURI));
        dataset.addProperty(
                oo.createAnnotationProperty(DC.description.getURI()),
                oo.createTypedLiteral("A dataset containing the predicted values for the feature :" + data.attribute(1).name()));
        dataset.addProperty(oo.createAnnotationProperty(DC.creator.getURI()), oo.createTypedLiteral(Configuration.BASE_URI));



        Individual dataEntry = null;
        Individual compound = null;
        Individual featureValue = null;


        for (int i = 0; i < NUM_COMPOUNDS; i++) {

            dataEntry = oo.createIndividual(OTClass.DataEntry.getOntClass(oo));
            compound = oo.createIndividual(data.instance(i).stringValue(compoundAttribute), OTClass.Compound.getOntClass(oo));
            dataEntry.addProperty(OTObjectProperties.compound.createProperty(oo), compound);

            featureValue = oo.createIndividual(OTClass.FeatureValue.getOntClass(oo));
            featureValue.addProperty(OTObjectProperties.feature.createProperty(oo), predictionIndiv);
            try {

                if (targetType == Attribute.NUMERIC) {
                    double value = data.instance(i).value(predictionAttribute);
                    featureValue.addLiteral(OTDataTypeProperties.value.createProperty(oo), oo.createTypedLiteral(value, XSDDatatype.XSDdouble));
                } else if (targetType == Attribute.NOMINAL) {
                    String value = predictionAttribute.value((int) data.instance(i).value(predictionAttribute));
                    featureValue.addLiteral(OTDataTypeProperties.value.createProperty(oo), oo.createTypedLiteral(value, XSDDatatype.XSDstring));
                }

            } catch (Exception ex) {
                System.out.println(ex);
            }

            dataEntry.addProperty(OTObjectProperties.values.createProperty(oo), featureValue);

            dataset.addProperty(OTObjectProperties.dataEntry.createProperty(oo), dataEntry);
        }

        dataset.addProperty(OTObjectProperties.dataEntry.createProperty(oo), dataEntry);

        return getDataset(oo);
    }
}
