/*
 * YAQP - Yet Another QSAR Project: Machine Learning algorithms designed for
 * the prediction of toxicological features of chemical compounds become
 * available on the Web. Yaqp is developed under OpenTox (http://opentox.org)
 * which is an FP7-funded EU research project.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
