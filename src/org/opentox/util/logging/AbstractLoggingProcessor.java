package org.opentox.util.logging;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.config.Configuration;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.processors.Processor;

/**
 *
 * @author chung
 */
public abstract class AbstractLoggingProcessor<L extends LogObject>
        extends Processor<L, Object>
        implements ILoggingProcessor<L> {

    public AbstractLoggingProcessor() {
        try {
            org.apache.log4j.PropertyConfigurator.configure(Configuration.loadDefaultProperties());
        } catch (YaqpException ex) {
            System.out.println(ex);
        }
        

    }

    public AbstractLoggingProcessor(Properties properties) {
        org.apache.log4j.PropertyConfigurator.configure(properties);
    }

    public Object process(L logobject) throws YaqpException {
        log(logobject);
        return new Object();
    }

    abstract void logSystemProperties();
    /**
     * The method:
     *  void log(L log);
     * is to be implemented by implementations of AbstractLoggingProcessor
     */
}
