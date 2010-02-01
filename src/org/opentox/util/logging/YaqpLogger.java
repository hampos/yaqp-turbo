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
package org.opentox.util.logging;


import org.opentox.util.logging.processors.AbstractLoggingProcessor;
import org.opentox.util.logging.logobject.LogObject;
import org.opentox.util.logging.levels.Warning;
import org.apache.log4j.Logger;
import org.opentox.config.Configuration;



/**
 * Default logger used in yaqp.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
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
