package org.opentox.db.util;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.interfaces.JProcessor;
import org.opentox.core.processors.BatchProcessor;
import org.opentox.core.processors.Pipeline;
import org.opentox.db.table.TableCreator;
import org.opentox.db.table.StandardTables;
import org.opentox.db.table.Table;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Fatal;

/**
 *
 * @author chung
 */
public class TheDbConnectorTest {

    public TheDbConnectorTest() {
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
     * Test of getLock method, of class DbConnector.
     */
    @Test
    public void InstantiationTest() {
        System.out.println("-- instantiation test --");

        
        TheDbConnector.init();
        
        // TheDbConnector.INIT.init();

//        TableCreator creator = new TableCreator();
//
//        BatchProcessor<Table, Object, JProcessor<Table, Object>> bp =
//                new BatchProcessor<Table, Object, JProcessor<Table, Object>>(creator, 1, 1);
//
//        ArrayList<Table> tableToBeCreated = new ArrayList<Table>();
//        for (StandardTables t : StandardTables.values()) {
//            tableToBeCreated.add(t.getTable());
//        }
//
//        try {
//            System.out.println("creating tables");
//            bp.process(tableToBeCreated);
//            System.out.println("done");
//        } catch (YaqpException ex) {
//            YaqpLogger.LOG.log(new Fatal(TheDbConnector.class, ex.toString()));
//        }


    }
}
