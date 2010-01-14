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
                Thread.sleep(100000);
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
    public void mytest() throws Exception {
        JMultiProcessor pp = new ParallelProcessor<Processor>();

        ArrayList<String> list = new ArrayList<String>();


        

        pp.setfailSensitive(true);

        pp.add(p1);
        pp.add(p2);
        

        list.add("1");
        list.add("2");
        

        try {
            ArrayList result = (ArrayList) pp.process(list);
            System.out.println(result.get(1));
        } catch (Exception ex) {
            System.out.println(ex);
//            fail();
        }

        System.out.println(pp.getStatus());
    }
}
