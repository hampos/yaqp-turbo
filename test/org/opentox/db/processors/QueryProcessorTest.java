package org.opentox.db.processors;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.processors.Pipeline;
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
        try {
            TheDbConnector.init();
        } catch (DbException ex) {
        }

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
     * Test of execute method, of class QueryProcessor. Insert 100 lines in the table
     * Algorithm_ontologies.
     */
    @Test
    public void testExecute() throws DbException {
        System.out.println("-- testing normal execute --");

        for (int i = 0; i < 100; i++) {
            QueryProcessor pr = new QueryProcessor(PrepStmt.ADD_ALGORITHM_ONTOLOGY);
            HyperStatement hs = null;
            QueryFood food = new QueryFood();
            food.add("NAME", java.util.UUID.randomUUID().toString());
            food.add("URI", java.util.UUID.randomUUID().toString());
            try {
                hs = pr.process(food);
                hs.executeUpdate();
                hs.flush();
            } catch (DbException ex) {
                System.out.println("** You've already put this in the DB **");
            } finally {
                PrepSwimmingPool.POOL.recycle(hs);
            }
        }

    }

    /**
     * See what happens if a parameter of the query was not set
     * in the query food.
     * @throws DbException A DbException should be thrown
     */
    @Test
    public void oneIsMissing() throws DbException {
        System.out.println("-- one is missing --");
        QueryProcessor pr = new QueryProcessor(PrepStmt.ADD_ALGORITHM_ONTOLOGY);
        HyperStatement hs = null;
        QueryFood food = new QueryFood();
        food.add("NAME", "abcd");
        try {
            hs = pr.process(food);
            hs.executeUpdate();
            fail("UNABLE TO HANDLE MISSING VALUES (SOME)");
        } catch (DbException ex) {
            System.out.println("EX --> " + ex);
        } finally {
            PrepSwimmingPool.POOL.recycle(hs);
        }
    }

    /**
     * Food is provided with no parameters.
     * @throws DbException  A DbException should be thrown
     */
    @Test
    public void allAreMissing() throws DbException {
        System.out.println("-- all are missing --");
        QueryProcessor pr = new QueryProcessor(PrepStmt.ADD_ALGORITHM_ONTOLOGY);
        HyperStatement hs = null;
        QueryFood food = new QueryFood();
        try {
            hs = pr.process(food);
            hs.executeUpdate();
            fail("UNABLE TO HANDLE MISSING VALUES (ALL)");
        } catch (DbException ex) {
            System.out.println("EXCEPTION --> " + ex);
        } finally {
            PrepSwimmingPool.POOL.recycle(hs);
        }
    }

    /**
     * Do the job with two processors in a pipeline. The first is the
     * QueryProcessor that feeds a PrepStmt object with QueryFood and the second
     * applies the query to the database.
     */
    @Test
    public void TwoInAPipeline() {
        System.out.println("-- Two Processors in a pipeline do the job  --");
        Pipeline pipe = new Pipeline();
        QueryProcessor qp = new QueryProcessor(PrepStmt.ADD_ALGORITHM_ONTOLOGY);
        DbProcessor dbp = new DbProcessor();
        /**
         * Put these processors in a pipeline like this:
         * (food) --> [qp] --> (HyperStatement) --> [dbp] --> (HyperResult)
         */
        pipe.add(qp);
        pipe.add(dbp);

        QueryFood food = new QueryFood();
        food.add("NAME", java.util.UUID.randomUUID().toString());
        food.add("URI", java.util.UUID.randomUUID().toString());
        try {
            pipe.process(food);
        } catch (YaqpException ex) {
            System.out.println(pipe.getStatus());
            fail();
        }
    }

    /**
     * Construct the QueryFood on the fly.
     */
    @Test
    public void queryFoodOnTheFly() {
        System.out.println("-- Query Food on the fly --");
        Pipeline pipe = new Pipeline();
        QueryProcessor qp = new QueryProcessor(PrepStmt.ADD_ALGORITHM_ONTOLOGY);
        DbProcessor dbp = new DbProcessor();
        pipe.add(qp);
        pipe.add(dbp);
        String name = java.util.UUID.randomUUID().toString();
        String uri = java.util.UUID.randomUUID().toString();
        QueryFood food = new QueryFood(new String[][]{{"NAME", name}, {"URI", uri}, {"OTHER"}});
        assertEquals(food.getValue("NAME"), name);
        assertEquals(food.getValue("URI"), uri);
        try {
            pipe.process(food);
        } catch (YaqpException ex) {
            System.out.println("Exception while trying to add an existing value "
                    + "at a unique column:: " + ex);
        }
    }


    @Test
    public void duplicateKey(){

    }
}
