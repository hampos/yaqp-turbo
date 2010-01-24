package org.opentox.db.queries;

import java.util.Iterator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 *
 * @author chung
 */
public class HyperStatementTest {

    public HyperStatementTest() {
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
     * Test of executeQuery method, of class HyperStatement.
     */
    @Test
    public void testExecuteQuery() throws Exception {
        HyperStatement hs = new HyperStatement("SELECT * FROM ALGORITHM_ONTOLOGIES");
        HyperResult hr = hs.executeQuery();


        Iterator<String> it = hr.getRowIterator(1);
        while(it.hasNext()){
            System.out.println(it.next());
        }
        

        Iterator<String> it2 = hr.getRowIterator("ASDF");
        while(it2.hasNext()){
            System.out.println(it2.next());
        }
      //  System.out.println(it.next());


    }

}