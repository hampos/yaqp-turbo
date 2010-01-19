package org.opentox.util.logging;

import org.opentox.util.logging.levels.ScrewedUp;
import org.opentox.util.logging.levels.Fatal;
import org.opentox.util.logging.levels.Trace;
import org.opentox.util.logging.levels.Warning;
import org.opentox.util.logging.levels.Info;
import org.opentox.util.logging.levels.Debug;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class YaqpLoggerTest {

    public YaqpLoggerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.err.println("Testing :"+YaqpLogger.class.getCanonicalName());
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
     * Test of log method, of class YaqpLogger.
     */
    @Test
    public void testLogger() {
        try{
        YaqpLogger.LOG.log(new Trace(YaqpLoggerTest.class));
        YaqpLogger.LOG.log(new Debug(YaqpLoggerTest.class));
        YaqpLogger.LOG.log(new Info(YaqpLoggerTest.class));
        YaqpLogger.LOG.log(new Warning(YaqpLoggerTest.class));
        YaqpLogger.LOG.log(new ScrewedUp(YaqpLoggerTest.class));
        YaqpLogger.LOG.log(new Fatal(YaqpLoggerTest.class));
        }catch(Throwable ex){
            System.out.println(ex);
            fail();
        }
    }

    
}