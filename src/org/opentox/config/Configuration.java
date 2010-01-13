package org.opentox.config;

import java.io.FileInputStream;
import java.util.Properties;
import org.opentox.core.exceptions.YaqpException;

/**
 *
 * @author chung
 */
public class Configuration {

    private static Properties properties = null;

    public static Properties getProperties() {
        return properties;
    }

    public static Properties loadDefaultProperties() throws YaqpException {
        try {
            properties = new Properties();
            properties.load(new FileInputStream("src/org/opentox/config/serwver.properties"));
            properties.setProperty("log4j.useDefaultFile", "true");
        } catch (final Exception ex) {
            properties.setProperty("server.port", "3000");
            properties.setProperty("server.domainName", "opentox.ntua.gr");
            properties.setProperty("database.name", "modelsDb");
            properties.setProperty("database.user", "itsme");
            properties.setProperty("database.driver", "org.apache.derby.jdbc.EmbeddedDriver");
            properties.setProperty("log4j.rootCategory", "WARN, file");
            properties.setProperty("log4j.appender.file", "org.apache.log4j.FileAppender");
            properties.setProperty("log4j.appender.file.File", "yaqp.log");
            properties.setProperty("log4j.appender.file.layout", "org.apache.log4j.PatternLayout");
            properties.setProperty("log4j.appender.file.layout.ConversionPattern", "%d [%t] %-5p %c - %m%n");
            properties.setProperty("log4j.useDefaultFile", "false");
        } finally {
            if (properties != null) {
                return properties;
            } else {
                throw new YaqpException(YaqpException.CAUSE.could_not_load_properties);
            }
        }




    }

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
            throw new YaqpException(YaqpException.CAUSE.could_not_load_properties);
        }

    }

    public void configure(Properties properties) throws YaqpException {
        if (properties != null) {
            Configuration.properties = properties;
        }else{
            throw new YaqpException(YaqpException.CAUSE.could_not_load_properties);
        }

    }
}
