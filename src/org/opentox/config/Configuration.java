/*
 * YAQP - Yet Another QSAR Project:
 * Machine Learning algorithms designed for the prediction of toxicological
 * features of chemical compounds become available on the Web. Yaqp is developed
 * under OpenTox (http://opentox.org) which is an FP7-funded EU research project.
 * This project was developed at the Automatic Control Lab in the Chemical Engineering
 * School of National Technical University of Athens. Please read README for more
 * information.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
 *
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
 * Contact: 
 * Pantelis Sopasakis
 * chvng@mail.ntua.gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 */
package org.opentox.config;

import java.io.FileInputStream;
import java.util.Properties;
import org.opentox.core.exceptions.YaqpException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Configuration {

    private static Properties properties = null;

    public static Properties getProperties() {
        if (properties == null) {
            try {
                properties = loadDefaultProperties();
            } catch (YaqpException ex) {
                backupProperties();
            }
        }
        return properties;
    }

    // <editor-fold defaultstate="collapsed" desc="load the Default Properies">
    public static Properties loadDefaultProperties() throws YaqpException {
        try {
            properties = new Properties();
            if (System.getProperty("os.name").contains("Linux")) {
                properties.load(new FileInputStream("src/org/opentox/config/server.properties"));
            } else if (System.getProperty("os.name").contains("Mac OS")) {
                properties.load(new FileInputStream("src/org/opentox/config/macos.server.properties"));
            }
            properties.setProperty("log4j.useDefaultFile", "true");
        } catch (final Exception ex) {
            backupProperties();
        } finally {
            if (properties != null) {
                return properties;
            } else {
                String message = "Could not load the standard properties hence could not use the standard "
        + "logger - Using the console instead!";
                throw new YaqpException("XAA12", message);
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="load Properies from a file">
    public static Properties loadProperties(String propertiesFile) throws YaqpException {
        try {
            properties.load(new FileInputStream(propertiesFile));
            properties.setProperty("log4j.useCustomFile", "true");
        } catch (final Exception ex) {
            properties = loadDefaultProperties();
            properties.setProperty("log4j.useCustomFile", "false");
        }

        if (properties != null) {
            return properties;
        } else {
            String message = "Could not load the standard properties hence could not use the standard "
        + "logger - Using the console instead";
            throw new YaqpException("XAA13", message);
        }

    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Configure">
    public static void configure(Properties properties) throws YaqpException {
        if (properties != null) {
            Configuration.properties = properties;
        } else {
            String message = "Could not load the standard properties hence could not use the standard "
        + "logger - Using the console instead";
            throw new YaqpException("XAA5", message);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Backup Properties in case of emergency">
    private static void backupProperties() {
        properties = new Properties();
        properties.setProperty("server.port", "3000");
        properties.setProperty("server.domainName", "opentox.ntua.gr");
        properties.setProperty("database.url", "jdbc:derby://localhost:1527/modelsDb");
        properties.setProperty("database.user", "itsme");
        properties.setProperty("database.driver", "org.apache.derby.jdbc.EmbeddedDriver");
        properties.setProperty("log4j.rootCategory", "WARN, file");
        properties.setProperty("log4j.appender.file", "org.apache.log4j.FileAppender");
        properties.setProperty("log4j.appender.file.File", "yaqp.log");
        properties.setProperty("log4j.appender.file.layout", "org.apache.log4j.PatternLayout");
        properties.setProperty("log4j.appender.file.layout.ConversionPattern", "%d [%t] %-5p %c - %m%n");
        properties.setProperty("log4j.useDefaultFile", "false");
    }// </editor-fold>
}
