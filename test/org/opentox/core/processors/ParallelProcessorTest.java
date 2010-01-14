/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.core.processors;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.interfaces.JMultiProcessor;
import org.opentox.core.interfaces.JProcessor;
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
    public void firstTest()  {
        System.out.println("-- first test --");
        JMultiProcessor pp = new ParallelProcessor<Processor>();
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
    public void largeLoad(){
        System.out.println("-- largeLoad test --");
        final int load = 20;
        JMultiProcessor pp = new ParallelProcessor<Processor>(5, 20);
        pp.setfailSensitive(true);
        ArrayList<String> list = new ArrayList<String>(2);
        for (int i=0;i<load;i++){
            pp.add(p1);
            list.add("in"+i);
        }
        try {
            ArrayList result = (ArrayList) pp.process(list);
            for (int i=0;i<load;i++){
                assertEquals(result.get(i), "in"+i+" <-- p1");
            }
            System.out.println(pp.getStatus());
        } catch (Exception ex) {
            fail();
        }

    }

    @Test
    public void noInput(){
        System.out.println("-- No input to processors --");
        JMultiProcessor pp = new ParallelProcessor<Processor>();
        pp.add(p1);
        pp.setfailSensitive(true);
        try {
            pp.process(null);
        } catch (YaqpException ex) {
            System.out.println(ex);
            assertTrue(ex instanceof YaqpException);
        }
    }

    @Test
    public void stackOverFlow(){
        System.out.println("-- No processors --");
        JMultiProcessor pp = new ParallelProcessor<Processor>();
        pp.setfailSensitive(true);
        try {
            Object o = pp.process(pp);

        } catch (YaqpException ex) {
            Logger.getLogger(ParallelProcessorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
