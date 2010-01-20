package org.opentox.db.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.db.exceptions.DbException;
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
    public void testAddUserGroup() {
        try {
            TheDbConnector.init();
        } catch (DbException ex) {
            System.out.println("1-----\n" + ex);
            fail();
        }
        HyperStatement hp = null;
        try {
            System.err.println(PrepSwimmingPool.POOL.take(PrepStmt.ADD_USER_GROUP).toString());
            hp = PrepSwimmingPool.POOL.take(PrepStmt.ADD_USER_GROUP);
        } catch (InterruptedException ex) {
            fail();
            throw new RuntimeException(ex);
        }
        try {
            hp.setString(1, "GROUP_E");
            hp.setInt(2, 199);
            hp.executeUpdate();
        } catch (DbException ex) {
            System.out.println("2-----\n" + ex);
            fail();
        }

    }

    @Test
    public void testAddUser() {
        HyperStatement hp = null;
        try {
            System.err.println(PrepSwimmingPool.POOL.take(PrepStmt.ADD_USER).toString());
            hp = PrepSwimmingPool.POOL.take(PrepStmt.ADD_USER);
        } catch (InterruptedException ex) {
            fail();
            throw new RuntimeException(ex);
        }
        try {
            hp.setString(1, "chung");
            hp.setString(2, "sdlf4o8fsndlfn4otjlkejelfsf");
            hp.setString(3,"Pantelis");
            hp.setString(4,"Sopasakis");
            hp.setString(5,"chvng@mail.ntua.gr");
            hp.setString(6,"NTUA");
            hp.setString(7,"Greece/Hellas");
            hp.setString(8,"Athens/Pireaus");
            hp.setString(9,"My Address 73");
            hp.setString(10,"http://opentox.ntua.gr");
            hp.setInt(11, 1);
            hp.executeUpdate();
        } catch (DbException ex) {
            System.out.println("2-----\n" + ex);
            fail();
        }
    }
}
