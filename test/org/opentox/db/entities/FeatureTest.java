package org.opentox.db.entities;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class FeatureTest {

    public FeatureTest() {
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
     * Test of getModel method, of class Feature.
     */
    @Test
    public void testGetModel() {
        String feature_uri = "http://opentox.ntua.gr:3000/feature/"+java.util.UUID.randomUUID().toString();
        Feature feature = new Feature(feature_uri);
        feature.getModel().printConsole();

    }

}