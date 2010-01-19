package org.opentox.db.util;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.processors.Pipeline;
import org.opentox.db.table.TableCreator;
import org.opentox.db.table.StandardTables;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Debug;
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
        
        TheDbConnector db = TheDbConnector.DB;
        TableCreator creator;
        Pipeline pipe = new Pipeline();
        for (StandardTables t : StandardTables.values()) {
            creator = new TableCreator(t);
            pipe.add(creator);
        }

        try {
            pipe.process(null);
            System.out.println(pipe.getStatus());
        } catch (YaqpException ex) {
            YaqpLogger.LOG.log(new Fatal(TheDbConnectorTest.class, ex.toString()));
           fail();
        }

        try {
            ArrayList<String> list = db.getTableNames();
            for (int i=0;i<list.size();i++){
                System.out.println(list.get(i));
            }

        } catch (YaqpException ex) {
            fail();
        }


    }
}
