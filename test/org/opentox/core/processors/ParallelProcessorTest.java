/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.core.processors;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Fatal;
import org.opentox.util.logging.logobject.LogObject;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class ParallelProcessorTest {

    public ParallelProcessorTest() {
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
     * Test of isfailSensitive method, of class ParallelProcessor.
     */
    @Test
    public void testIsfailSensitive() {
        ParallelProcessor pp = new ParallelProcessor();
        
        
        ArrayList<LogObject> list = new ArrayList<LogObject>();
        
        for (int j=0;j<1000;j++){
            pp.add(YaqpLogger.INSTANCE);
            list.add(new Fatal(ParallelProcessor.class, "abc"));
        }
        try {
            pp.process(list);
        } catch (Exception ex) {
            fail();
        }
        System.out.println(pp.getStatus());
    }
}
