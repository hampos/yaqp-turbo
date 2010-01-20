/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.db.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.db.exceptions.DbException;

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
    public void testAddUserGroup() {
        try {
            TheDbConnector.init();
        } catch (DbException ex) {
            System.out.println("1-----\n" + ex);
        }
        HyperStatement hp = null;
        try {
            System.err.println(PrepSwimmingPool.POOL.take(PrepStmt.ADD_USER_GROUP));
            hp = PrepSwimmingPool.POOL.take(PrepStmt.ADD_USER_GROUP);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        try {
            hp.setString(1, "GROUP_A");
            hp.setInt(2, 17);
            hp.executeUpdate();
        } catch (DbException ex) {
            System.out.println("2-----\n"+ex);
        }

        
    }

   
}
