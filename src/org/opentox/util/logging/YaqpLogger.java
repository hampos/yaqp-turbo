package org.opentox.util.logging;

import org.opentox.util.logging.processors.AbstractLoggingProcessor;
import org.opentox.util.logging.logobject.LogObject;
import org.opentox.util.logging.levels.Warning;
import org.opentox.util.logging.levels.Debug;
import org.apache.log4j.Logger;
import org.opentox.config.Configuration;


/**
 *
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
        log(new Debug(YaqpLogger.class, "os.name        : " + System.getProperty("os.name")));
        log(new Debug(YaqpLogger.class, "os.version     : " + System.getProperty("os.version")));
        log(new Debug(YaqpLogger.class, "os.arch        : " + System.getProperty("os.arch")));
        log(new Debug(YaqpLogger.class, "java.version   : " + System.getProperty("java.version")));
        log(new Debug(YaqpLogger.class, "java.vendor    : " + System.getProperty("java.vendor")));
    }
}
