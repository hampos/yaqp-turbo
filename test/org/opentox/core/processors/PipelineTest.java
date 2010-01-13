package org.opentox.core.processors;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.core.exceptions.YaqpException;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class PipelineTest {

    // PROCESSOR 1
    private Processor<String, String> p1 = new Processor<String, String>() {

        public String process(String data) throws YaqpException {
            return data + " processed by p1";
        }
    };
    // PROCESSOR 2
    private Processor<String, String> p2 = new Processor<String, String>() {

        public String process(String data) throws YaqpException {
            return data + " --";
        }
    };

    public PipelineTest() {
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
     * Test of process method, of class Pipeline.
     */
    @Test
    public void testProcess() throws Exception {
        Pipeline<String, String, Processor> pipe = new Pipeline<String, String, Processor>();
        pipe.add(p1);
        pipe.add(p2);
        try {
            String out = pipe.process("data ");
            System.out.println(out);
        } catch (YaqpException ex) {
            fail(/*ex.getMessage()*/);
        }
    }

    /**
     * Test of isEnabled method, of class Pipeline.
     */
    @Test
    public void testIsEnabled() {
        Pipeline<String, String, Processor> pipe = new Pipeline<String, String, Processor>();
        pipe.add(p1);
        pipe.add(p2);
        p2.setEnabled(false);
        try {
            String out = pipe.process("data ");
            System.out.println(out);
        } catch (YaqpException ex) {
            fail(/*ex.getMessage()*/);
        }
    }
}
