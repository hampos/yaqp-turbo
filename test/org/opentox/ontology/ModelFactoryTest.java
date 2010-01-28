package org.opentox.ontology;

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
         TurboOntModel mm = ModelFactory.createTurboOntModel();

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

        mm.printConsole();
    }

}