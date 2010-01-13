package org.opentox.test;

import org.opentox.util.logging.Warning;
import org.opentox.util.logging.YaqpLogger;

/**
 *
 * @author chung
 */
public class LogTest extends Object{
       

    public static void main(String[] args) {
        YaqpLogger logger = YaqpLogger.INSTANCE;
        logger.log(new Warning(LogTest.class, "this is a warning!!!!"));
    }
}
