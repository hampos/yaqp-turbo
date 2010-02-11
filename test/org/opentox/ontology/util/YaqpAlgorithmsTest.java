package org.opentox.ontology.util;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.io.util.YaqpIOStream;
import org.opentox.ontology.components.Algorithm;

/**
 *
 * @author chung
 */
public class YaqpAlgorithmsTest {

    public YaqpAlgorithmsTest() {
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

   

    @Test
    public void testGetAllAlgorithms() throws Exception {
        ArrayList<Algorithm> list = YaqpAlgorithms.getAllAlgorithms();
        for (Algorithm alg : list) {
            alg.getTurtle().publish(new YaqpIOStream(System.out));
        }
    }
}
