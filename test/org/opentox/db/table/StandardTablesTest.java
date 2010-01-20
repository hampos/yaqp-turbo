package org.opentox.db.table;

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
public class StandardTablesTest {

    public StandardTablesTest() {
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
     * Test of is2Bincluded method, of class StandardTables.
     */
    @Test
    public void testIs2Bincluded() {
        System.out.println("-- is2Bincluded --");
        for (StandardTables t : StandardTables.values()) {
            assertTrue(t.is2Bincluded());
        }
    }

    /**
     * Test of getTable method, of class StandardTables.
     */
    @Test
    public void testGetTable() {
        System.out.println("-- getTable --");
        for (StandardTables t : StandardTables.values()) {
            System.out.println("**" + t.getTable().getTableName());
        }
    }

    /**
     * Test of AlgorithmOntologies method, of class StandardTables.
     */
    @Test
    public void testSQL() {
        System.out.println("-- AlgorithmOntologies --");
        for (StandardTables t : StandardTables.values()) {
            System.out.println(t.getTable().getCreationSQL());
        }
    }
}
