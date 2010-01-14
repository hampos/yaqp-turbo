package org.opentox.util.logging;

import org.apache.log4j.Appender;
import org.opentox.util.logging.processors.AbstractLoggingProcessor;
import org.opentox.util.logging.logobject.LogObject;
import org.opentox.util.logging.levels.Warning;
import org.opentox.util.logging.levels.Debug;
import org.apache.log4j.Logger;
import org.opentox.config.Configuration;
import org.opentox.util.logging.levels.Trace;


/**
 * Default logger used in yaqp.
 * @author chung
 */
public class YaqpLogger extends AbstractLoggingProcessor<LogObject> {

    private Logger logger;
    private static YaqpLogger instanceOfThis = null;
    public static YaqpLogger INSTANCE = getInstance();

    private static YaqpLogger getInstance() {
        if (instanceOfThis == null) {
            instanceOfThis = new YaqpLogger();
        }
        return instanceOfThis;
    }

    private YaqpLogger() {
        super();
        try {
            if (Configuration.getProperties().getProperty("log4j.useDefaultFile").equals("false")) {
                log(new Warning(YaqpLogger.class, "Properties File not found - using defaults instead."));
            }
       } catch (Throwable exception) {
            if (
                    (!(exception instanceof ExceptionInInitializerError)) &&
                    (!(exception instanceof NullPointerException))
            ){
                throw new RuntimeException(exception);
            }
        }
        logSystemProperties();
    }

    public void log(LogObject log) {
        logger = Logger.getLogger(log.getSource());
        logger.log(log.getLevel(), log.getMessage());
    }

    @Override
    public void logSystemProperties() {
        log(new Trace(YaqpLogger.class, "os.name        : " + System.getProperty("os.name")));
        log(new Trace(YaqpLogger.class, "os.version     : " + System.getProperty("os.version")));
        log(new Trace(YaqpLogger.class, "os.arch        : " + System.getProperty("os.arch")));
        log(new Trace(YaqpLogger.class, "java.version   : " + System.getProperty("java.version")));
        log(new Trace(YaqpLogger.class, "java.vendor    : " + System.getProperty("java.vendor")));
    }
}
