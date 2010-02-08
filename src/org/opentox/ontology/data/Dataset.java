/*
 *
 * YAQP - Yet Another QSAR Project:
 * Machine Learning algorithms designed for the prediction of toxicological
 * features of chemical compounds become available on the Web. Yaqp is developed
 * under OpenTox (http://opentox.org) which is an FP7-funded EU research project.
 * This project was developed at the Automatic Control Lab in the Chemical Engineering
 * School of National Technical University of Athens. Please read README for more
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
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import java.net.URI;
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.opentox.core.processors.Pipeline;
import org.opentox.io.processors.InputProcessor;
import org.opentox.io.publishable.OntObject;
import org.opentox.ontology.exceptions.ImproperEntityException;
import org.opentox.ontology.exceptions.YaqpOntException;
import org.opentox.ontology.namespaces.OTClass;
import org.opentox.ontology.namespaces.OTDataTypeProperties;
import org.opentox.ontology.namespaces.OTObjectProperties;
import org.opentox.ontology.processors.InstancesProcessor;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * A set of data which can be used for training or testing a model.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Dataset {

    private OntObject oo = null;

    /**
     * A dataset is instantiated providing an OntObject, which is an ontological model.
     * The class {@link OntObject } is in fact an extension of <code>OntModelImpl</code>
     * of jena. Such an object (OntObject) can be retrieved from a remote dataset server,
     * or from a local resource (e.g. file) using the <code>InputProcessor</code>.
     * @param oo An ontological object holding a representation of a dataset. If an improper
     * ontological entity is provided to construct the Dataset, methods like
     * {@link Dataset#getInstances() getInstances()} are not likely to work, so you have to
     * chack that the resource you provided is a dataset resource.
     * @see DatasetBuilder
     */
    public Dataset(OntObject oo) {
        this.oo = oo;
    }

    /**
     * The dataset as <code>Instances</code>. These objects are used by weka as
     * input/output object to most algorithms (training, data preprocessing etc).
     * The Instances equivalent of the dataset may contain three different types of
     * <code>attributes</code>: numeric, nominal and/or string ones. The first attribute
     * is always a string one corresponding to the compound of the dataentry while  
     * acting as an identifier for it. The name of this attribute is <code>compound_uri</code>
     * and is unique among all data entries. 
     * @return Instances object for the dataset.
     * @throws YaqpOntException In case something goes wrong with the provided
     * representation (e.g. it does not correspond to a valid dataset).
     */
    public Instances getInstances() throws YaqpOntException {

        /*
         *
         * Some initial definitions:
         */
        Resource _DataEntry = OTClass.DataEntry.getOntClass(oo),
                _Dataset = OTClass.Dataset.getOntClass(oo),
                _Feature = OTClass.Feature.getOntClass(oo),
                _NumericFeature = OTClass.NumericFeature.getOntClass(oo),
                _NominalFeature = OTClass.NominalFeature.getOntClass(oo),
                _StringFeature =  OTClass.StringFeature.getOntClass(oo);

        FastVector attributes = null;

        Instances data = null;

        StmtIterator dataSetIterator = null,
                featureIterator = null,
                valuesIterator = null,
                dataEntryIterator = null;

        String relationName = null;

        /*
         *
         * Iterate over all nodes in the dataset having type 'ot:Dataset'.
         */
        dataSetIterator =
                oo.listStatements(new SimpleSelector(null, RDF.type, _Dataset));

        if (dataSetIterator.hasNext()) {
            relationName = dataSetIterator.next().getSubject().getURI();
            if (dataSetIterator.hasNext()) {
                throw new YaqpOntException("XN311","More than one datasets found");
            }
        } else {
            // this is not a dataset model
            throw new ImproperEntityException("XN312","Not a dataset");
        }
        dataSetIterator.close();

        System.out.println(relationName);

        /**
         * Create a Map<String, String> such that its first entry is a feature in
         * the dataset and the second is its datatype.
         */
        Map<Resource, String> featureTypeMap = new HashMap<Resource, String>();


        //  A3. Iterate over all Features.
        featureIterator =
                oo.listStatements(new SimpleSelector(null, RDF.type, _StringFeature));
        while (featureIterator.hasNext()) {
            Statement feature = featureIterator.next();

            // A4. For every single feature in the dataset, pick a "values" node.
            valuesIterator =
                    oo.listStatements(
                     new SimpleSelector(null, OTObjectProperties.feature.createProperty(oo),
                      feature.getSubject())  );
            if (valuesIterator.hasNext()) {
                Statement values = valuesIterator.next();

                // A5. For this values node, get the value
                StmtIterator valueInValuesIter =
                        oo.listStatements(new SimpleSelector(values.getSubject(), OTDataTypeProperties.value.createProperty(oo), (Resource) null));
                if (valueInValuesIter.hasNext()) {
                    featureTypeMap.put(feature.getSubject(), valueInValuesIter.next().getLiteral().getDatatypeURI());
                }
            }
            valuesIterator.close();
        }
        featureIterator.close();


        /**
         * A6. Now update the attributes of the dataset.
         */
        attributes = getAttributes(featureTypeMap);
        data = new Instances(relationName, attributes, 0);



        /**
         * B1. Iterate over all dataentries
         */
        dataEntryIterator =
                oo.listStatements(new SimpleSelector(null, RDF.type, _DataEntry));
        while (dataEntryIterator.hasNext()) {
            Statement dataEntry = dataEntryIterator.next();



            /**
             * B2. For every dataEntry, iterate over all values nodes.
             */
            Instance temp = null;
            valuesIterator =
                    oo.listStatements(new SimpleSelector(dataEntry.getSubject(), OTObjectProperties.values.createProperty(oo), (Resource) null));

            double[] vals = new double[data.numAttributes()];
            for (int i = 0; i < data.numAttributes(); i++) {
                vals[i] = Instance.missingValue();
            }

            StmtIterator compoundNamesIterator =
                    oo.listStatements(new SimpleSelector(dataEntry.getSubject(), OTObjectProperties.compound.createProperty(oo), (Resource) null));
            String compoundName = null;
            if (compoundNamesIterator.hasNext()) {
                compoundName = compoundNamesIterator.next().getObject().as(Resource.class).getURI();
            }

            vals[data.attribute("compound_uri").index()] = data.attribute("compound_uri").addStringValue(compoundName);

            while (valuesIterator.hasNext()) {
                Statement values = valuesIterator.next();

                /*
                 * B3. A pair of the form (AttributeName, AttributeValue) is created.
                 * This will be registered in an Instance-type object which
                 * is turn will be used to update the dataset.
                 */

                // atVal is the value of the attribute
                String atVal = values.getProperty(OTDataTypeProperties.value.createProperty(oo)).getObject().as(Literal.class).getValue().toString();
                // and atName is the name of the corresponding attribute.
                String atName = values.getProperty(OTObjectProperties.feature.createProperty(oo)).getObject().as(Resource.class).getURI();



                if (numericXSDtypes().contains(featureTypeMap.get(oo.createResource(atName)))) {
                    try {
                        vals[data.attribute(atName).index()] = Double.parseDouble(atVal);
                        /**
                         * The following catch rule, handles cases where some values are declared
                         * as numeric (double, float etc) but their value cannot be cast as
                         * double.
                         */
                    } catch (NumberFormatException ex) {
                    }
                } else if (stringXSDtypes().contains(featureTypeMap.get(oo.createResource(atName)))) {
                    vals[data.attribute(atName).index()] = data.attribute(atName).addStringValue(atVal);
                } else if (XSDDatatype.XSDdate.getURI().equals(atName)) {
                    try {
                        vals[data.attribute(atName).index()] = data.attribute(atName).parseDate(atVal);
                    } catch (ParseException ex) {
                        System.out.println(ex);
                        //Logger.getLogger(Dataset.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }



            }
            temp = new Instance(1.0, vals);

            // Add the Instance only if its compatible with the dataset!
            if (data.checkInstance(temp)) {
                data.add(temp);
            } else {
                System.err.println("Warning! The instance " + temp + " is not compatible with the dataset!");
            }


        }
        dataEntryIterator.close();

        return data;

    }

      private FastVector getAttributes(Map<Resource, String> featureTypeMap) {
        // atts is the FastVector containing all attributes of the dataset:
        FastVector atts = new FastVector();
        Iterator<Map.Entry<Resource, String>> mapIterator = featureTypeMap.entrySet().iterator();
        Map.Entry<Resource, String> entry;
        // All datasets must have an attribute called 'compound_uri'
        atts.addElement(new Attribute("compound_uri", (FastVector) null));
        while (mapIterator.hasNext()) {
            entry = mapIterator.next();
            String dataType = entry.getValue();
            if (numericXSDtypes().contains(dataType)) {
                atts.addElement(new Attribute(entry.getKey().getURI()));
            } else if (stringXSDtypes().contains(dataType)) {
                atts.addElement(new Attribute(entry.getKey().getURI(), (FastVector) null));
            }
        }

        return atts;
    }

    /**
     * The set of XSD data types that should be cast as numeric.
     * @return the set of XSD datatypes that should be considered as numeric.
     */
    private static Set<String> numericXSDtypes() {
        Set<String> numericXSDtypes = new HashSet<String>();
        numericXSDtypes.add(XSDDatatype.XSDdouble.getURI());
        numericXSDtypes.add(XSDDatatype.XSDfloat.getURI());
        numericXSDtypes.add(XSDDatatype.XSDint.getURI());
        numericXSDtypes.add(XSDDatatype.XSDinteger.getURI());
        numericXSDtypes.add(XSDDatatype.XSDnegativeInteger.getURI());
        numericXSDtypes.add(XSDDatatype.XSDnonNegativeInteger.getURI());
        numericXSDtypes.add(XSDDatatype.XSDpositiveInteger.getURI());
        numericXSDtypes.add(XSDDatatype.XSDnonPositiveInteger.getURI());
        numericXSDtypes.add(XSDDatatype.XSDlong.getURI());
        numericXSDtypes.add(XSDDatatype.XSDshort.getURI());
        numericXSDtypes.add(XSDDatatype.XSDlong.getURI());
        numericXSDtypes.add(XSDDatatype.XSDunsignedInt.getURI());
        numericXSDtypes.add(XSDDatatype.XSDunsignedLong.getURI());
        numericXSDtypes.add(XSDDatatype.XSDunsignedShort.getURI());
        return numericXSDtypes;
    }

    /**
     * The set of XSD data types that should be cast as string.
     * @return the set of XSD datatypes that should be considered as strings.
     */
    private static Set<String> stringXSDtypes() {
        Set<String> stringXSDtypes = new HashSet<String>();
        stringXSDtypes.add(XSDDatatype.XSDstring.getURI());
        stringXSDtypes.add(XSDDatatype.XSDanyURI.getURI());
        return stringXSDtypes;
    }

    public static void main(String[] args) throws Exception {

        InputProcessor<OntObject> p1 = new InputProcessor<OntObject>();
        DatasetBuilder p2 = new DatasetBuilder();
        InstancesProcessor p3 = new InstancesProcessor();
        Pipeline pipe = new Pipeline();
        pipe.add(p1);
        pipe.add(p2);
        pipe.add(p3);

        Instances data = (Instances) pipe.process(new URI("http://localhost/6"));
        System.out.println(data);
    }



}
