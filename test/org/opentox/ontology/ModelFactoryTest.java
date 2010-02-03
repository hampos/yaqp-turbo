/*
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
package org.opentox.ontology;

import org.opentox.io.publishable.OntObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.vocabulary.DC;
import org.opentox.ontology.namespaces.OTAlgorithmTypes;
import org.opentox.ontology.namespaces.OTClass;
import org.opentox.ontology.namespaces.OTDataTypeProperties;

/**
 *
 * @author chung
 */
public class ModelFactoryTest {

    public ModelFactoryTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }


    /**
     * Test of main method, of class ModelFactory.
     */
    @Test
    public void testMain() {
         OntObject mm = ModelFactory.createTurboOntModel();

        /**
         *
         * Which classes are intended to be used in your ontological model..
         */
        mm.includeOntClass(OTAlgorithmTypes.ClassificationEagerMultipleTargets);
        /**
         *
         * Which non-OWL properties you're gonna use in your model....
         */
        mm.createAnnotationProperty(DC.title.getURI());
        mm.createAnnotationProperty(DC.description.getURI());

        /**
         *
         * Declare your first indicidual....
         */
        // introduction....
        Individual algorithm = mm.createIndividual("http://sth.com/alg1",OTClass.Algorithm.getOntClass(mm));
        // type (Instance of some above-declared class)
        algorithm.addRDFType(OTAlgorithmTypes.ClassificationEagerMultipleTargets.getResource());
        // various properties.....
        algorithm.addLiteral(DC.title,mm.createTypedLiteral("my algorithm",XSDDatatype.XSDstring));
        algorithm.addLiteral(DC.description,mm.createTypedLiteral("This is my first test",XSDDatatype.XSDstring));

        Individual a = mm.createIndividual("http://sth.com/compound/123", OTClass.Compound.getOntClass(mm));
        
        algorithm.addProperty(OTDataTypeProperties.hasStatus.createProperty(mm), mm.createLiteral("haha"));

        mm.printConsole();
    }

}