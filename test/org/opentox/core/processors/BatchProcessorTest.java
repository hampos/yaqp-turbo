package org.opentox.core.processors;


import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Fatal;
import org.opentox.util.logging.levels.ScrewedUp;
import org.opentox.util.logging.levels.Warning;
import org.opentox.util.logging.logobject.LogObject;

/**
 *
 * @author chung
 */
public class BatchProcessorTest {

    public BatchProcessorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.err.println("Testing :"+BatchProcessor.class.getCanonicalName());
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
        BatchProcessor bp = new BatchProcessor(YaqpLogger.INSTANCE);
        ArrayList<LogObject> list = new ArrayList<LogObject>();
        list.add(new Warning(BatchProcessorTest.class));
        list.add(new Warning(BatchProcessorTest.class));
        list.add(new Warning(BatchProcessorTest.class));
        list.add(new ScrewedUp(BatchProcessorTest.class));
        list.add(new ScrewedUp(BatchProcessorTest.class));
        list.add(new ScrewedUp(BatchProcessorTest.class));
        list.add(new Fatal(BatchProcessorTest.class));
        bp.process(list);
        System.out.println(bp.getStatus());
    
    }

    

}