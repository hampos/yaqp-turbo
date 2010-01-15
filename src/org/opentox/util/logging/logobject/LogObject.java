package org.opentox.util.logging.logobject;

import org.apache.log4j.Level;

/**
 * LogObjects contain information about a log event which will be handled by some
 * {@link org.opentox.util.logging.processors.AbstractLoggingProcessor Logging Processor }.
 * @author Sopasakis Pantelis
 * @author Charalampos Chomenides
 */
public interface LogObject {

    

    /**
     * @return the message to be logged.
     */
    String getMessage();

    /**
     * Declares the message to be logged.
     * @param message log message.
     */
    void setMessage(String message);

    /**
     * Return the type of the logging object.
     * @return type of log object.
     */
    Level getLevel();

    /**
     * Declare the type of the log object.
     * @param logType
     */
    void setLogType(Level logType);

    /**
     * Declare the source of the log, that is the
     * class that sends the logging message.
     * @param clash Reference to class calling the logger.
     */
    void setSource(Class clash);


    /**
     * 
     * @return the class that calls the logger.
     */
    Class getSource();


}
