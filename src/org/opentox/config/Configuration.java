package org.opentox.config;

import java.util.Properties;



/**
 *
 * @author chung
 */
public class Configuration {

    private static Properties properties = null;

    public static Properties loadDefaultProperties(){
        throw new UnsupportedOperationException();
    }

    public static Properties loadProperties(String properties){
        throw new UnsupportedOperationException();
    }

    public void configure(Properties properties){
        Configuration.properties = properties;
    }

}
