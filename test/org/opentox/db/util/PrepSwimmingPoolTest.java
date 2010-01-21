/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.db.util;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.processors.BatchProcessor;
import org.opentox.core.processors.Processor;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class PrepSwimmingPoolTest {

    public PrepSwimmingPoolTest() {
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
     * Test of take method, of class PrepSwimmingPool.
     */
    @Test
    public void testTake() throws Exception {
        TheDbConnector.init();
        
        Processor<Integer, Object> p = new Processor<Integer, Object>() {            
            
            public Object process(Integer data) throws YaqpException {
                HyperStatement hp = null;
                try {
                    hp = PrepSwimmingPool.POOL.take(PrepStmt.add_algorithm_ontology);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                hp.setString(1, "name_" + data);
                hp.setString(2, "http://sth.com/t/" + data);
                hp.executeUpdate();
                PrepSwimmingPool.POOL.put(hp);
                return new Object();
            }
        };


        final int _SIZE = 100;
        BatchProcessor<Integer, Object, Processor<Integer, Object>> bp = new BatchProcessor<Integer, Object, Processor<Integer, Object>>(p, 50, 90);
        ArrayList<Integer> jobs = new ArrayList<Integer>(_SIZE);
        for (int i = 0; i < _SIZE; i++) {
            jobs.add(new Integer(i));
        }
        try {
            bp.process(jobs);
        } catch (YaqpException ex) {
            fail(ex.toString());
        }




    }
}
