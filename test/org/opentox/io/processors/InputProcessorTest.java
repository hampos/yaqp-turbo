package org.opentox.io.processors;

import java.net.URI;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.ontology.YaqpOntModel;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class InputProcessorTest {

    public InputProcessorTest() {
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
     * Test of process method, of class InputProcessor.
     */
    @Test
    public void testProcess(){
        try{
        InputProcessor p = new InputProcessor();
        YaqpOntModel y = p.process(new URI("http://localhost/ds.rdf"));
        y.printConsole();
        }catch (Exception e){
            fail();
        }
    }

}