package org.opentox.db.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.opentox.config.Configuration;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.exceptions.YaqpIOException;
import org.opentox.db.interfaces.JDbConnector;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Fatal;
import org.opentox.util.logging.levels.Info;
import org.opentox.util.logging.levels.ScrewedUp;

/**
 *
 * @author Sopasakis Pantelis
 * @author Charalampos Chomenides
 */
public class TheDbConnector implements JDbConnector {

    private static TheDbConnector instanceOfthis = null;
    public static TheDbConnector DB = getInstance();
    private Connection connection;
    private Properties properties = Configuration.getProperties();
    private String database_name =
            properties.getProperty("database.name", "yaqp/yaqpdb");
    private String database_port =
            properties.getProperty("database.port", "1527");
    private String database_url =
            properties.getProperty("database.urlbase", "jdbc:derby://localhost") + ":"
            + database_port + "/" + database_name;
    private String database_user =
            properties.getProperty("database.user", "itsme");
    private String database_driver =
            properties.getProperty("database.driver", "org.apache.derby.jdbc.EmbeddedDriver");
    private boolean isConnected = false;

    private static TheDbConnector getInstance() {
        
        if (instanceOfthis == null) {
            try {
                instanceOfthis = new TheDbConnector();
            } catch (Throwable e) {
                System.out.println(e);
                YaqpLogger.LOG.log(new Fatal(TheDbConnector.class, "Unable to connect to "
                        + DB.database_url+" :: "+e));
            }
        }
        return (TheDbConnector) instanceOfthis;
    }

    private TheDbConnector() throws YaqpException {
        try {
            YaqpLogger.LOG.log(new Info(TheDbConnector.class, "Starting the derby server..."));
            String[] command = {
                properties.getProperty("javaCommand", "java"),
                properties.getProperty("javaOptions", "-Djava.net.preferIPv4Stack=true"),
                "-jar",
                properties.getProperty("derbyHome", "/usr/local/sges-v3/javadb")+"/lib/derbyrun.jar",
                "server",
                "start",
                "-p",
                properties.getProperty("database.port", "1527"),
            };
            

            Process derby_server = Runtime.getRuntime().exec(command);

            /**
             * Wait a little to let the derby server to start...
             * 500ms seems to be adequate.
             */
            Thread.sleep(500);
            
            YaqpLogger.LOG.log(new Info(TheDbConnector.class, "Derby server started; listening and ready" +
                    " to accept connections on port "+getDatabasePort()));
            connect();
        } catch (Throwable ex) {
            YaqpLogger.LOG.log(new Fatal(TheDbConnector.class, "Unable to connect to "
                        + DB.database_url+" :: "+ex));
            throw new YaqpException();
        }
    }

    public Object getLock() {
        return (Object) TheDbConnector.DB;
    }

    public Object getLock(String lockName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void connect() {
        loadDriver();
        establishConnection();
        // loadTables();
    }

    public void disconnect() {
        // TODO: ...
        instanceOfthis = null;
        if (connection != null) {
            try {
                connection.close();
                isConnected = false;
            } catch (SQLException ex) {
                YaqpLogger.LOG.log(new ScrewedUp(TheDbConnector.class, "Connection "
                        + database_url + " cannot close!"));
            }
        }

    }

    public String getDatabaseName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getDatabaseUrl() {
        return database_url;
    }

    public int getDatabasePort() throws YaqpException {
        try {
            return Integer.parseInt(database_port);
        } catch (NumberFormatException nfe) {
            YaqpLogger.LOG.log(new Fatal(TheDbConnector.class, "Not acceptable port :" + database_port));
            throw new YaqpException("Wrong port declaration :" + database_port);
        }

    }

    public String getDatabaseDriver() {
        return database_driver;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public Connection getConnection() {
        return connection;
    }

    public String getDatabaseUser() {
        return database_user;
    }

    /**
     * Load the JDBC driver specified in the properties section
     * @see Configuration server configuration
     * @see Configuration#getProperties() current properties
     * @see Configuration#loadDefaultProperties() default properties
     */
    private void loadDriver() {
        try {
            Driver myDriver = (Driver) Class.forName(database_driver).newInstance();
            YaqpLogger.LOG.log(new Info(TheDbConnector.class, "The driver " + database_driver
                    + " was successfully loaded."));
            assert (myDriver.jdbcCompliant());
        } catch (Exception exc) {
            YaqpLogger.LOG.log(new Fatal(TheDbConnector.class, "Error while loading the JDBC driver ::"+exc));
        }
    }

    /**
     * Establushes a connection with the database or creates a new database
     * if the specified is not found.
     */
    private void establishConnection() {
        try {
            connection = DriverManager.getConnection(database_url, database_user, "letmein");
            isConnected = true;
            YaqpLogger.LOG.log(new Info(TheDbConnector.class, "Database Connection established at "
                    + database_url + " by " + database_user + " - Now Connected!"));
        } catch (SQLException e) {
            if (e.getErrorCode() == 40000) {
                YaqpLogger.LOG.log(new Info(TheDbConnector.class, "Database " + database_url
                        + " was not found -- creating..."));
                createDataBase();
            } else {
                // TODO: log
            }
        }catch (Exception e){
            YaqpLogger.LOG.log(new Fatal(TheDbConnector.class, "Unable to connect to "
                        + DB.database_url+"--"+e));
        }
    }

    /**
     *
     */
    private void createDataBase() {
        try {
            connection = DriverManager.getConnection(database_url + ";create=true", database_user, "letmein");
            YaqpLogger.LOG.log(new Info(TheDbConnector.class, "New database was generated at "
                    + database_url + " by " + database_user));
            if (connection != null) {
                isConnected = true;
            }
        } catch (SQLException ex) {
            YaqpLogger.LOG.log(new Fatal(TheDbConnector.class, "Encountered an error while trying to " +
                    "create a new database :: "+ex));
        }
    }

    
    private void loadTables(){
        
    }


}
