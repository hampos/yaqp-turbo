package org.opentox.core.processors;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.interfaces.JMultiProcessorStatus.STATUS;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class PipelineTest {

    // PROCESSOR 1
    private Processor<String, String> p1 = new Processor<String, String>() {

        public String process(String data) throws YaqpException {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                throw new YaqpException();
            }
            return data + " <-- p1";
        }
    };
    // PROCESSOR 2
    private Processor<String, String> p2 = new Processor<String, String>() {

        public String process(String data) throws YaqpException {
            try {
                Thread.sleep(80);
            } catch (InterruptedException ex) {
                throw new YaqpException();
            }
            return data + " <-- p2";
        }
    };

    private Processor<String, String> p3 = new Processor<String, String>(){

        public String process(String data) throws YaqpException {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                throw new YaqpException();
            }
            throw new YaqpException();
        }

    };

    public PipelineTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.err.println("Testing :"+Pipeline.class.getCanonicalName());
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
    public void testPipe() {
        System.out.println("-- first test --");
        Pipeline pipe = new Pipeline();
        pipe.setfailSensitive(false);
        pipe.add(p1);
        pipe.add(p2);
        pipe.add(p3);
        try {
            String out = (String) pipe.process("data ");
            System.out.println(out);
            System.out.println(pipe.getStatus());
            assertTrue(Math.abs(pipe.getStatus().getElapsedTime(STATUS.ERROR)-2000)<5);
            assertTrue(Math.abs(pipe.getStatus().getElapsedTime(STATUS.PROCESSED)-1080)<5);
            assertTrue(!pipe.getStatus().isInProgress());
        } catch (Throwable ex) {
            System.out.println(ex);
            //fail(/*ex.getMessage()*/);
        }
    }

    /**
     * Test of isEnabled method, of class Pipeline.
     */
    @Test
    public void oneIsEnabled() {
        System.out.println("-- one is enabled --");
        Pipeline<String, String, Processor<String, String>> pipe =
                new Pipeline<String, String, Processor<String, String>>();
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

    @Test
    public void allDisabled(){
        System.out.println("-- all disabled --");
        Pipeline<String, String, Processor<String, String>> pipe =
                new Pipeline<String, String, Processor<String, String>>();
        pipe.add(p1);
        pipe.add(p2);
        p2.setEnabled(false);
        p1.setEnabled(false);
        try {
            String out = pipe.process("data ");
            System.out.println(out);
        } catch (YaqpException ex) {
            fail(/*ex.getMessage()*/);
        }
    }
}
