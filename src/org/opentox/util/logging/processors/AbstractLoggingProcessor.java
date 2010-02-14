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
package org.opentox.util.logging.processors;

import org.opentox.util.logging.logobject.LogObject;
import java.util.Properties;
import org.opentox.config.Configuration;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.processors.Processor;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Trace;

/**
 * 
 * This is an abstract entity intended to be subclassed by all loggers in the 
 * system. Such a processor can be bundled in a Pipeline or in general a batch processor
 * for sequential logging.
 * @param <L> Subinterface of LogObject
 * @author Sopasakis Pantelis
 */
@SuppressWarnings({"unchecked"})
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
    public void logSystemProperties() {
        
        log((L)new Trace(YaqpLogger.class, "os.name        : " + System.getProperty("os.name")));
        log((L)new Trace(YaqpLogger.class, "os.version     : " + System.getProperty("os.version")));
        log((L)new Trace(YaqpLogger.class, "os.arch        : " + System.getProperty("os.arch")));
        log((L)new Trace(YaqpLogger.class, "java.version   : " + System.getProperty("java.version")));
        log((L)new Trace(YaqpLogger.class, "java.vendor    : " + System.getProperty("java.vendor")));
    }
}
