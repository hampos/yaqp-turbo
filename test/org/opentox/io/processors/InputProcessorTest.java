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

            //  URI uri = new URI("http://opentox.ntua.gr:3000/algorithm");
            URI uri = new URI(ServerList.ambit.getBaseURI() + "/dataset/6");

            p.handle(uri);

            long start = System.currentTimeMillis();
            for (int i = 0; i < 20; i++) {
                p.handle(uri);
            }
            System.out.println(Double.parseDouble(Long.toString( System.currentTimeMillis() - start)) / 20);

        } catch (Exception e) {
            System.out.println(e);
            fail(e.toString());
        }
    }
}
