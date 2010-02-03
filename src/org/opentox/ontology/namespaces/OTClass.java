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
package org.opentox.ontology.namespaces;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.rdf.model.Resource;
import java.util.Iterator;
import java.util.Set;
import org.opentox.io.publishable.OntObject;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class OTClass extends YaqpOntEntity {

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
     * 
     * A <code>NumericFeature</code> is a subclass of <code>Feature</code> having
     * numeric values only.
     */
    public static final OTClass NumericFeature =
            new OTClass(_model.createResource(String.format(_NS_OT, "NumericFeature"))) {

                @Override
                public OntClass createOntClass(OntObject model) {
                    OntClass cl = model.createClass(getURI());
                    cl.setSuperClass(Feature.getResource());
                    model.includeOntClass(Feature);
                    return null;
                }
            };
    /**
     *
     * A <code>StringFeature</code> is a subclass of <code>Feature</code> having
     * String values.
     */
    public static final OTClass StringFeature =
            new OTClass(_model.createResource(String.format(_NS_OT, "StringFeature"))) {

                @Override
                public OntClass createOntClass(OntObject model) {
                    OntClass cl = model.createClass(getURI());
                    cl.setSuperClass(Feature.getResource());
                    model.includeOntClass(Feature);
                    return null;
                }
            };
    /**
     *
     * A <code>NominalFeature</code> is a subclass of <code>Feature</code> accepting
     * nominal values, i.e. values in a finite set such as <code>{A,B,C}</code>.
     */
    public static final OTClass NominalFeature =
            new OTClass(_model.createResource(String.format(_NS_OT, "NominalFeature"))) {

                @Override
                public OntClass createOntClass(OntObject model) {
                    OntClass cl = model.createClass(getURI());
                    cl.setSuperClass(Feature.getResource());
                    model.includeOntClass(Feature);
                    return null;
                }
            };
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
