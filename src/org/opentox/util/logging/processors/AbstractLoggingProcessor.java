package org.opentox.util.logging.processors;

import org.opentox.util.logging.logobject.LogObject;
import java.util.Properties;
import org.opentox.config.Configuration;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.processors.Processor;

/**
 * This is an abstract entity intended to be subclassed by all loggers in the 
 * system. Such a processor can be bundled in a Pipeline or in general a batch processor
 * for sequential logging.
 * @param <L> Subinterface of LogObject
 * @author Sopasakis Pantelis
 */
public abstract class AbstractLoggingProcessor<L extends LogObject>
        extends Processor<L, L>
        implements ILoggingProcessor<L> {

    /**
     * Constructor for an abstract logger. The logger is configured with the default
     * configurations defined in the file log4j.properties. If an exception is thrown
     * during this procedure, some default properties are used which are defined in
     * {@link org.opentox.config.Configuration } and if still there is problem, the
     * System console is used for logging.
     */
    public AbstractLoggingProcessor() {
        try {
            org.apache.log4j.PropertyConfigurator.configure(Configuration.loadDefaultProperties());
        } catch (YaqpException ex) {
            System.out.println(ex);
        }
        

    }

    /**
     * A logger is constructed with given propertied.
     * @param properties logging properties.
     */
    public AbstractLoggingProcessor(Properties properties) {
        org.apache.log4j.PropertyConfigurator.configure(properties);
    }

    /**
     * 
     * @param logobject
     * @return the logobject it self is return while logged.
     * @throws YaqpException
     */
    public L process(L logobject) throws YaqpException {
        log(logobject);
        return logobject;
    }

    /**
     * Write the properties of the system to the log target (console or file)
     */
    public abstract void logSystemProperties();
    /**
     * The method:
     *  void log(L log);
     * is to be implemented by implementations of AbstractLoggingProcessor
     */
}
