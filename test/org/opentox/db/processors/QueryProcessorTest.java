/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.db.processors;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.db.exceptions.DbException;
import static org.junit.Assert.*;
import org.opentox.db.queries.HyperStatement;
import org.opentox.db.queries.QueryFood;
import org.opentox.db.util.PrepStmt;
import org.opentox.db.util.PrepSwimmingPool;
import org.opentox.db.util.TheDbConnector;

/**
 *
 * @author chung
 */
public class QueryProcessorTest {

    public QueryProcessorTest() {
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
     * Test of execute method, of class QueryProcessor.
     */
    @Test
    public void testExecute() {
        System.out.println("execute");
        try {
            TheDbConnector.init();
        } catch (DbException ex) {
            System.out.println("1-----\n" + ex);
            fail();
        }
        for (int i = 0; i < 100; i++) {
            QueryProcessor pr = new QueryProcessor(PrepStmt.ADD_ALGORITHM_ONTOLOGY);
            QueryFood food = new QueryFood();
            food.add("URI", "uri"+i);
            food.add("NAME", "name"+i);
            try {
                HyperStatement hs = pr.process(food);
                hs.executeUpdate();
                hs.flush();
                PrepSwimmingPool.POOL.put(hs);

            } catch (Exception ex) {
                Logger.getLogger(QueryProcessorTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
