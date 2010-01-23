package org.opentox.db.table;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.db.util.TheDbConnector;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class TableDropperTest {

    public TableDropperTest() {
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
     * Test of execute method, of class TableDropper.
     */
    @Test
    public void testExecute() throws YaqpException {
        System.out.println("execute");
        TheDbConnector db = TheDbConnector.DB;
        System.out.println(db.isConnected());
    }

}