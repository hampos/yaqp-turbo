/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.ontology;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.core.exceptions.YaqpIOException;
import org.opentox.io.processors.InputProcessor;
import static org.junit.Assert.*;

/**
 *
 * @author hampos
 */
public class YaqpOntModelTest {

    public YaqpOntModelTest() {
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
     * Test of getType method, of class YaqpOntModel.
     */
    @Test
    public void testGetType() {
        URI uri;
        try {
            uri = new URI("http://ambit.uni-plovdiv.bg:8080/ambit2/dataset/6");

            InputProcessor p = new InputProcessor();

            TurboOntModel m = p.handle(uri);
        //    m.printConsole("RDF/XML");
        //    System.out.println(m.toString());

        } catch (Exception ex) {
            Logger.getLogger(YaqpOntModelTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
