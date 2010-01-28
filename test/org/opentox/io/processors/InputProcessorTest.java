package org.opentox.io.processors;

import java.net.URI;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.io.util.ServerList;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class InputProcessorTest {

    public InputProcessorTest() {
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
     * Test of process method, of class InputProcessor.
     */
    @Test
    public void testProcess() {
        try {
            InputProcessor p = new InputProcessor();

            //URI uri = new URI("http://opentox.ntua.gr/big.rdf");
            URI uri = new URI(ServerList.ambit.getBaseURI() + "/dataset/6");

            //p.handle(uri).printConsole();

            //double start = 0, duration = 0, sum = 0;
            final int N = 10;
            for (int i = 0; i < N; i++) {
              //  start = System.currentTimeMillis();
                p.handle(uri);
                //duration = System.currentTimeMillis() - start;
              //  System.out.println(duration);
                //sum += duration;
            }
            //System.out.println("Average :"+(double)sum/(double)N);
            

        } catch (Exception e) {
            System.out.println(e);
            fail(e.toString());
        }
    }
}
