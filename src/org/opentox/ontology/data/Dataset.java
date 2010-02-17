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
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.opentox.core.exceptions.Cause;
import org.opentox.core.processors.Pipeline;
import org.opentox.io.processors.InputProcessor;
import org.opentox.io.publishable.OntObject;
import org.opentox.ontology.exceptions.ImproperEntityException;
import org.opentox.ontology.exceptions.YaqpOntException;
import org.opentox.ontology.namespaces.OTClass;
import org.opentox.ontology.namespaces.OTDataTypeProperties;
import org.opentox.ontology.namespaces.OTObjectProperties;
import org.opentox.ontology.processors.InstancesProcessor;
import org.opentox.qsar.processors.filters.AbstractFilter;
import org.opentox.qsar.processors.filters.AttributeCleanup;
import org.opentox.qsar.processors.filters.AttributeCleanup.ATTRIBUTE_TYPE;
import org.opentox.qsar.processors.filters.SimpleMVHFilter;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

/**
 *
 * A set of data which can be used for training or testing a model.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
@SuppressWarnings({"unchecked"}) public class Dataset {

    private OntObject oo = null;
    /**
     * The name of the first attribute in the dataset, corresponding to a
     * unique identifier for the compound.
     */
    private static final String compound_uri = "compound_uri";

    private enum WekaDataTypes {
        string,
        nominal,
        numeric,
        general;
    }

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

        // SOME INITIAL DEFINITIONS:
        Resource _DATAENTRY = OTClass.DataEntry.getOntClass(oo),
                _DATASET = OTClass.Dataset.getOntClass(oo),
                _FEATURE = OTClass.Feature.getOntClass(oo),
                _NUMERIC_FEATURE = OTClass.NumericFeature.getOntClass(oo),
                _NOMINAL_FEATURE = OTClass.NominalFeature.getOntClass(oo),
                _STRING_FEATURE = OTClass.StringFeature.getOntClass(oo);
        FastVector attributes = null;
        Instances data = null;
        StmtIterator dataSetIterator = null,
                featureIterator = null,
                valuesIterator = null,
                dataEntryIterator = null;
        String relationName = null;
        Map<Resource, WekaDataTypes> featureTypes = new HashMap<Resource, WekaDataTypes>();
        Map<Resource, ArrayList<String>> featureNominalValues = new HashMap<Resource, ArrayList<String>>();


        // CHECK IF THE RESOURCE IS A DATASET. IF YES, GET ITS IDENTIFIER AND SET
        // THE RELATION NAME ACCORDINGLY. IF NOT THROW AN ImproperEntityException.
        // ALSO CHECK IF THERE ARE MULTIPLE DATASETS AND IF YES THROW EXCEPTION.
        dataSetIterator =
                oo.listStatements(new SimpleSelector(null, RDF.type, _DATASET));

        if (dataSetIterator.hasNext()) {
            relationName = dataSetIterator.next().getSubject().getURI();
            if (dataSetIterator.hasNext()) {
                throw new YaqpOntException(Cause.XONT518, "More than one datasets found");
            }
        } else {
            // this is not a dataset model
            throw new ImproperEntityException(Cause.XIE2, "Not a dataset");
        }
        dataSetIterator.close();

        System.out.println(relationName);



        //  POPULATE THE MAP WHICH CORRELATES RESOURCES TO WEKA DATA TYPES
        ArrayList<String> nominalValues = new ArrayList<String>();
        featureIterator =
                oo.listStatements(new SimpleSelector(null, RDF.type, _FEATURE));
        while (featureIterator.hasNext()) {
            Resource feature = featureIterator.next().getSubject().as(Resource.class);
            StmtIterator featureTypeIterator =
                    oo.listStatements(new SimpleSelector(feature, RDF.type, (RDFNode) null));
            Set<Resource> featureTypesSet = new HashSet<Resource>();
            while (featureTypeIterator.hasNext()) {
                Resource type = featureTypeIterator.next().getObject().as(Resource.class);
                featureTypesSet.add(type);
            }
            if (featureTypesSet.contains(_NUMERIC_FEATURE)) {
                featureTypes.put(feature, WekaDataTypes.numeric);
            } else if (featureTypesSet.contains(_STRING_FEATURE)) {
                featureTypes.put(feature, WekaDataTypes.string);
            } else if (featureTypesSet.contains(_NOMINAL_FEATURE)) {
                featureTypes.put(feature, WekaDataTypes.nominal);
                StmtIterator acceptValueIterator = oo.listStatements(
                        new SimpleSelector(feature, OTDataTypeProperties.acceptValue.createProperty(oo), (RDFNode)null));
                // GET THE RANGE OF THE FEATURE:   
                while (acceptValueIterator.hasNext()){
                    nominalValues.add(acceptValueIterator.next().getObject().as(Literal.class).getString());
                }
                featureNominalValues.put(feature, nominalValues);
                nominalValues = new ArrayList<String>();                             
            } else {
                assert (featureTypesSet.contains(_FEATURE));
                featureTypes.put(feature, WekaDataTypes.general);
            }
        }

        // GET THE ATTRIBUTES FOR THE DATASET:
        attributes = getAttributes(featureTypes, featureNominalValues);
        data = new Instances(relationName, attributes, 0);

        // ITERATE OVER ALL DATA ENTRIES IN THE DATASET:
        dataEntryIterator =
                oo.listStatements(new SimpleSelector(null, RDF.type, _DATAENTRY));
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

            vals[data.attribute(compound_uri).index()] = data.attribute(compound_uri).addStringValue(compoundName);

            while (valuesIterator.hasNext()) {
                Statement values = valuesIterator.next();

                /*
                 * A pair of the form (AttributeName, AttributeValue) is created.
                 * This will be registered in an Instance-type object which
                 * is turn will be used to update the dataset.
                 */

                // atVal is the value of the attribute
                String atVal = values.getProperty(OTDataTypeProperties.value.createProperty(oo)).getObject().as(Literal.class).getValue().toString();
                // and atName is the name of the corresponding attribute.
                String atName = values.getProperty(OTObjectProperties.feature.createProperty(oo)).getObject().as(Resource.class).getURI();



                if (featureTypes.get(oo.createResource(atName)).equals(WekaDataTypes.numeric)) {
                    try {
                        vals[data.attribute(atName).index()] = Double.parseDouble(atVal);
                        /**
                         * The following catch rule, handles cases where some values are declared
                         * as numeric (double, float etc) but their value cannot be cast as
                         * double.
                         */
                    } catch (NumberFormatException ex) {
                        /* Just don't include this value in the dataset */
                    }
                } else if (featureTypes.get(oo.createResource(atName)).equals(WekaDataTypes.string)) {
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

    private FastVector getAttributes(Map<Resource, WekaDataTypes> featureTypes, Map<Resource, ArrayList<String>> nominalValues) {
        FastVector atts = new FastVector();
        Set<Entry<Resource, WekaDataTypes>> entrySetDatatypes = featureTypes.entrySet();
        // THE EXISTENCE OF THE (STRING) ATTRIBUTE 'COMPOUND_URI' IS MANDATORY FOR ALL
        // DATASETS. THIS IS ALWAYS THE FIRST ATTRIBUTE IN THE LIST.
        atts.addElement(new Attribute(compound_uri, (FastVector) null));
        // ADD NUMERIC AND STRING ATTRIBUTES INTO THE FASTVECTOR:
        for (Entry<Resource, WekaDataTypes> entry : entrySetDatatypes) {
            WekaDataTypes dataType = entry.getValue();
            if (dataType.equals(WekaDataTypes.numeric)) {
                atts.addElement(new Attribute(entry.getKey().getURI()));
            } else if (dataType.equals(WekaDataTypes.string) || dataType.equals(WekaDataTypes.general)) {
                atts.addElement(new Attribute(entry.getKey().getURI(), (FastVector) null));
            }
        }
        // COPE WITH NOMINAL VALUES:
        Set<Entry<Resource, ArrayList<String>>> nominalAttsSet = nominalValues.entrySet();
        for (Entry<Resource, ArrayList<String>> entry : nominalAttsSet){
            FastVector nominalFVec = new FastVector(entry.getValue().size());
            for (String nominalValue : entry.getValue()){
                nominalFVec.addElement(nominalValue);
            }
            atts.addElement(new Attribute(entry.getKey().toString(), nominalFVec));
        }
        return atts;
    }



    public static void main(String[] args) throws Exception {

        InputProcessor<OntObject> p1 = new InputProcessor<OntObject>();
        DatasetBuilder p2 = new DatasetBuilder();
        InstancesProcessor p3 = new InstancesProcessor();
        AbstractFilter filter1 = new AttributeCleanup(new ATTRIBUTE_TYPE[] {ATTRIBUTE_TYPE.string});
        AbstractFilter filter = new SimpleMVHFilter();

        Pipeline pipe = new Pipeline();
        pipe.add(p1);
        pipe.add(p2);
        pipe.add(p3);
        pipe.add(filter1);
        pipe.add(filter);

        Instances data = (Instances) pipe.process(new URI("http://localhost/9"));
        
    }
}

