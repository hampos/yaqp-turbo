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
        System.out.println("YaqpLogger.log");
        YaqpLogger.INSTANCE.log(new Trace(YaqpLoggerTest.class));
        YaqpLogger.INSTANCE.log(new Debug(YaqpLoggerTest.class));
        YaqpLogger.INSTANCE.log(new Info(YaqpLoggerTest.class));
        YaqpLogger.INSTANCE.log(new Warning(YaqpLoggerTest.class));
        YaqpLogger.INSTANCE.log(new ScrewedUp(YaqpLoggerTest.class));
        YaqpLogger.INSTANCE.log(new Fatal(YaqpLoggerTest.class));
    }

    
}