/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.core.processors;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.interfaces.JMultiProcessor;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class ParallelProcessorTest {

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
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                throw new YaqpException();
            }
            return data + " <-- p2";
        }
    };
    private Processor<String, String> fails_after_long = new Processor<String, String>() {

        public String process(String data) throws YaqpException {

            try {
                Thread.sleep(10000);
                throw new YaqpException();
            } catch (InterruptedException ex) {
                throw new YaqpException();
            }
        }
    };

    public ParallelProcessorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.err.println("Testing :"+ParallelProcessor.class.getCanonicalName());
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
     * Test of isfailSensitive method, of class ParallelProcessor.
     */
    @Test
    public void firstTest() {
        System.out.println("-- first test --");
        JMultiProcessor pp =
                new ParallelProcessor<String, String, Processor<ArrayList<String>, ArrayList<String>>>();
        ArrayList<String> list = new ArrayList<String>(2);
        pp.setfailSensitive(true);

        pp.add(p1);
        list.add("1");
        pp.add(p2);
        list.add("2");

        try {
            ArrayList result = (ArrayList) pp.process(list);
            System.out.println(result.get(1));
        } catch (Exception ex) {
            fail();
        }

        System.out.println(pp.getStatus());
    }

    @Test
    public void largeLoad() {
        System.out.println("-- largeLoad test --");
        final int load = 20;
        ParallelProcessor pp =
                new ParallelProcessor<String, String, Processor<ArrayList<String>, ArrayList<String>>>(5, 20);
        pp.setfailSensitive(false);

        ArrayList<String> list = new ArrayList<String>(load);

        for (int i = 0; i < load; i++) {
            pp.add(p1);
            list.add("in" + i);
        }

        try {
            ArrayList result = (ArrayList) pp.process(list);
            for (int i = 0; i < load; i++) {
                System.out.println(result.get(i));
            }
            System.out.println(pp.getStatus());
        } catch (Throwable ex) {
            fail(ex.toString());
        }
    }

    @Test
    public void timeOut() {
        System.out.println("-- timeout test --");
        final int load = 20;
        final int timeout = 1600;
        ParallelProcessor pp =
                new ParallelProcessor<String, String, Processor<ArrayList<String>, ArrayList<String>>>(5, 20);
        pp.setfailSensitive(false);
        pp.setTimeOut(timeout, TimeUnit.MILLISECONDS);

        ArrayList<String> list = new ArrayList<String>(load);

        for (int i = 0; i < load; i++) {
            pp.add(p1);
            list.add("in" + i);
        }

        try {
            ArrayList result = (ArrayList) pp.process(list);
            for (int i = 0; i < load; i++) {
                System.out.println(result.get(i));
            }
            System.out.println(pp.getStatus());
        } catch (Throwable ex) {
            fail(ex.toString());
        }
    }

    @Test
    public void noInput() {
        System.out.println("-- No input to processors --");
        JMultiProcessor pp =
                new ParallelProcessor<String, String, Processor<ArrayList<String>, ArrayList<String>>>();
        pp.add(p1);
        pp.setfailSensitive(true);
        try {
            pp.process(null);
        } catch (YaqpException ex) {
            System.out.println(ex + "\n");
            assertTrue(ex instanceof YaqpException);
        }
    }

    @Test
    public void noProcessorsFound() {
        System.out.println("-- No processors --");
        JMultiProcessor pp =
                new ParallelProcessor<String, String, Processor<ArrayList<String>, ArrayList<String>>>();
        pp.setfailSensitive(false);
        try {
            Object o = pp.process(pp);
        } catch (YaqpException ex) {
            System.out.println(ex + "\n");
        }
    }
}
