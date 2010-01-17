package org.opentox.util.logging;


import org.opentox.util.logging.processors.AbstractLoggingProcessor;
import org.opentox.util.logging.logobject.LogObject;
import org.opentox.util.logging.levels.Warning;
import org.apache.log4j.Logger;
import org.opentox.config.Configuration;



/**
 * Default logger used in yaqp.
 * @author chung
 */
public class YaqpLogger extends AbstractLoggingProcessor<LogObject> {

    private static YaqpLogger instanceOfThis = null;

    /**
     * Public & Unique access point for the logger.
     */
    public static YaqpLogger LOG = getInstance();

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
        Logger.getLogger(log.getSource()).log(log.getLevel(), log.getMessage());
    }

    
    
}
