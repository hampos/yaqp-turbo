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
import org.opentox.core.interfaces.JMultiProcessorStatus.STATUS;
import static org.junit.Assert.*;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Fatal;
import org.opentox.util.logging.levels.Info;
import org.opentox.util.logging.levels.ScrewedUp;
import org.opentox.util.logging.levels.Warning;
import org.opentox.util.logging.logobject.LogObject;

/**
 *
 * @author chung
 */
public class BatchProcessorTest {

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
    // PROCESSOR 1
    private Processor<String, Integer> p2 = new Processor<String, Integer>() {

        public Integer process(String data) throws YaqpException {
            long sleeptime = 0;
            try {
                sleeptime = Long.parseLong(data);
            } catch (NumberFormatException ex) {
                sleeptime = 1000;
            }
            try {
                Thread.sleep(sleeptime);
            } catch (InterruptedException ex) {
                Logger.getLogger(BatchProcessorTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            return new Integer(1234);
        }
    };

    public BatchProcessorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.err.println("Testing :" + BatchProcessor.class.getCanonicalName());
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

    @Test
    public void batchLogging() throws Exception {
        System.out.println("-- core test --");
        YaqpLogger.LOG.log(new Info(getClass(), "Initializing new Batch Processor"));
        BatchProcessor bp = new BatchProcessor(YaqpLogger.LOG);
        ArrayList<LogObject> list = new ArrayList<LogObject>();
        list.add(new Warning(BatchProcessorTest.class));
        list.add(new Warning(BatchProcessorTest.class));
        list.add(new Warning(BatchProcessorTest.class));
        list.add(new ScrewedUp(BatchProcessorTest.class));
        list.add(new ScrewedUp(BatchProcessorTest.class));
        list.add(new ScrewedUp(BatchProcessorTest.class));
        list.add(new Fatal(BatchProcessorTest.class));
        YaqpLogger.LOG.log(new Info(getClass(), "Batch Processor is ready. Now processing"));
        bp.process(list);
        YaqpLogger.LOG.log(new Info(getClass(), "BatchProcessing Completed"));
        assertTrue(bp.isEnabled());
        System.out.println(bp.getStatus());
        YaqpLogger.LOG.log(new Info(getClass(), "Success!"));
    }

    @Test
    public void turboBatch() {
        System.out.println("-- testing using p1 --");
        YaqpLogger.LOG.log(new Info(getClass(), "Turbo Batch Processor"));
        BatchProcessor bp = new BatchProcessor(p1, 10, 12);
        ArrayList<String> listOfJobs = new ArrayList<String>();
        // all these will run in parallel
        listOfJobs.add("1");
        listOfJobs.add("2");
        listOfJobs.add("3");
        listOfJobs.add("4");
        listOfJobs.add("5");
        listOfJobs.add("6");
        listOfJobs.add("7");
        listOfJobs.add("8");
        listOfJobs.add("9");
        listOfJobs.add("10");
        try {
            ArrayList<String> result = bp.process(listOfJobs);
            for (int i = 0; i < result.size(); i++) {
                assertEquals(result.get(i), (i+1)+" <-- p1");
            }
            System.out.println(bp.getStatus());
            assertTrue(Math.abs(bp.getStatus().getElapsedTime(STATUS.PROCESSED) - 1000) < 50);
            assertTrue(!bp.getStatus().isInProgress());
            YaqpLogger.LOG.log(new Info(getClass(), "Turbo Batch Processor Completed"));
        } catch (YaqpException ex) {
            fail();
        }

    }

    @Test
    public void timeOutTest() {
        System.out.println("-- timeout test --");
        YaqpLogger.LOG.log(new Info(getClass(), "Batch Processor TimeOut"));
        BatchProcessor<String, Integer, Processor<String, Integer>> bp =
                new BatchProcessor<String, Integer, Processor<String, Integer>>(p2, 2, 2);
        assertTrue(bp.getStatus().isInProgress());
        bp.setTimeOut(1000, TimeUnit.MILLISECONDS);
        ArrayList listOfJobs = new ArrayList();
        // all these will run in parallel
        listOfJobs.add("100");
        listOfJobs.add("20000");
        try {
            ArrayList<Integer> result = bp.process(listOfJobs);
            for (int i = 0; i < result.size(); i++) {
                System.out.println(result.get(i));
            }
            assertEquals(result.get(0), new Integer(1234));
            assertEquals(result.get(1), null);
            YaqpLogger.LOG.log(new Info(getClass(), "Batch Processor timeout test completed"));
        } catch (YaqpException ex) {
            fail();
        }

    }


    @Test
    public void synch(){
        System.out.println("-- synchronization test --");
        YaqpLogger.LOG.log(new Info(getClass(), "Batch Processor Synch Test"));
        p1.setSynchronized(true);
        BatchProcessor bp = new BatchProcessor(p1, 10, 12);
        ArrayList<String> listOfJobs = new ArrayList<String>();
        // all these will run in parallel
        listOfJobs.add("1");
        listOfJobs.add("2");
        listOfJobs.add("3");
        listOfJobs.add("4");
        listOfJobs.add("5");
        listOfJobs.add("6");
        listOfJobs.add("7");
        listOfJobs.add("8");
        listOfJobs.add("9");
        listOfJobs.add("10");
        try {
            ArrayList<String> result = bp.process(listOfJobs);
            System.out.println(bp.getStatus());
            YaqpLogger.LOG.log(new Info(getClass(), "Synch Test completed"));
        } catch (YaqpException ex) {
            fail();
        }

    }
}

