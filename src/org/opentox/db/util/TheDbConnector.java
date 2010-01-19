package org.opentox.db.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.opentox.config.Configuration;
import org.opentox.core.exceptions.YaqpException;
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
    /**
     * Public access point to the {@link TheDbConnector DB Connection Singleton }.
     * Use this static connection to the database to perform any operations.
     */
    public static TheDbConnector DB = getInstance();
    /**
     * SQL connection to the database
     */
    private Connection connection;
    /**
     * Database Properties
     */
    private final Properties properties = Configuration.getProperties();
    /**
     * Name of the database.
     */
    private final String database_name =
            properties.getProperty("database.name", "yaqp/yaqpdb");
    /**
     * Database port
     */
    private final String database_port =
            properties.getProperty("database.port", "1527");
    /**
     * URL of the database
     */
    private final String database_url =
            properties.getProperty("database.urlbase", "jdbc:derby://localhost") + ":"
            + database_port + "/" + database_name;
    /**
     * User/Schema of the database
     */
    private final String database_user =
            properties.getProperty("database.user", "itsme");
    /**
     * Driver used to connect to the database. By default this is
     * org.apache.derby.jdbc.EmbeddedDriver. Note that derby.jar should
     * be in thy classpath.
     */
    private final String database_driver =
            properties.getProperty("database.driver", "org.apache.derby.jdbc.EmbeddedDriver");
    /**
     * Java command. You are adviced to use the SUN variant (version 6 or higher).
     */
    private final String javacmd =
            properties.getProperty("javaCommand", "java");
    /**
     * Options/Directives to the java virtual machine.
     */
    private final String javaOptions =
            properties.getProperty("javaOptions", "-Djava.net.preferIPv4Stack=true");
    /**
     * Home directory for DERBY
     */
    private final String derbyHome =
            properties.getProperty("derbyHome", "/usr/local/sges-v3/javadb");
    private static ArrayList<String> TABLE_LIST = null;
    /**
     * Connection Flag.
     */
    private boolean isConnected = false;
    private final Lock lock = new ReentrantLock();

    private static TheDbConnector getInstance() {
        if (instanceOfthis == null) {
            try {
                instanceOfthis = new TheDbConnector();
            } catch (Throwable e) {
                YaqpLogger.LOG.log(new Fatal(TheDbConnector.class, "Unable to connect to "
                        + DB.database_url + " :: " + e));
            }
        }
        return (TheDbConnector) instanceOfthis;
    }

    private TheDbConnector() throws YaqpException {
        connect();
    }

    public Lock getLock() {
        return lock;
    }

    /**
     * If there is no connection to the database, attempts a
     * new connection.
     */
    public void connect() {
        if (!isConnected()) {
            try {
                startDerbyServer();
                loadDriver();
                establishConnection();
            } catch (Throwable ex) {
                YaqpLogger.LOG.log(new Fatal(TheDbConnector.class, "Unable to connect to "
                        + DB.database_url + " :: " + ex));
                throw new RuntimeException(ex);

            }
        }
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                instanceOfthis = null;
                isConnected = false;
            } catch (SQLException ex) {
                YaqpLogger.LOG.log(new ScrewedUp(TheDbConnector.class, "Connection "
                        + database_url + " cannot close!"));
            }
        }

    }

    public String getDatabaseName() {
        return database_name;
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

    public DatabaseMetaData getMetaData() throws YaqpException {
        if (connection != null && isConnected()) {
            try {
                return connection.getMetaData();
            } catch (SQLException ex) {
                YaqpLogger.LOG.log(new Fatal(TheDbConnector.class, "No connection to the database :" + database_name));
                throw new YaqpException("No connection to the database");
            }
        } else {
            YaqpLogger.LOG.log(new Fatal(TheDbConnector.class, "No connection to the database :" + database_name));
            throw new YaqpException("No connection to the database");
        }

    }

    public ArrayList<String> getTableNames() throws YaqpException {
        if (TABLE_LIST == null) {
            try {
                TABLE_LIST = new ArrayList<String>();
                DatabaseMetaData md = getMetaData();
                String[] rsOps = {"TABLE"};
                ResultSet rs = md.getTables(null, null, "%", rsOps);
                while (rs.next()) {
                    TABLE_LIST.add(rs.getString(3));
                }
            } catch (SQLException ex) {
                throw new YaqpException(ex);
            }
        }
        return TABLE_LIST;
    }

    private void startDerbyServer() throws Exception {
        String[] derby_start_command = {
            javacmd, javaOptions,
            "-jar", derbyHome + "/lib/derbyrun.jar", "server", "start",
            "-p", database_port
        };

        String[] derby_ping_command = {
            javacmd, javaOptions,
            "-jar", derbyHome + "/lib/derbyrun.jar", "server", "ping",
            "-p", database_port};


        Runtime runtime = Runtime.getRuntime();

        YaqpLogger.LOG.log(new Info(TheDbConnector.class, "Attempting connection to the derby server..."));
        // Check if the server is started:
        boolean is_server_started = false;
        Process derby_ping = runtime.exec(derby_ping_command);
        BufferedReader br = new BufferedReader(new InputStreamReader(derby_ping.getInputStream()));
        if (br.readLine().contains("Connection obtained")) {
            is_server_started = true;
        }

        if (!is_server_started) {
            YaqpLogger.LOG.log(new Info(TheDbConnector.class, "Derby server is down. Now starting..."));
            Process derby_start = runtime.exec(derby_start_command);
            // Wait until the derby server is started.
            derby_ping = runtime.exec(derby_ping_command);
            br = new BufferedReader(new InputStreamReader(derby_ping.getInputStream()));
            while (!br.readLine().contains("Connection obtained")) {
                derby_ping = runtime.exec(derby_ping_command);
                br = new BufferedReader(new InputStreamReader(derby_ping.getInputStream()));
            }
        }


        YaqpLogger.LOG.log(new Info(TheDbConnector.class, "Derby server is up, listening and ready"
                + " to accept connections on port " + getDatabasePort()));

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
            YaqpLogger.LOG.log(new Fatal(TheDbConnector.class, "Error while loading the JDBC driver ::" + exc));
        }
    }

    /**
     * Establushes a connection with the database or creates a new database
     * if the specified is not found.
     */
    private void establishConnection() {
        YaqpLogger.LOG.log(new Info(TheDbConnector.class, "Attempting connection to :" + database_url));
        try {
            connection = DriverManager.getConnection(database_url, database_user, "letmein");
            isConnected = true;
            YaqpLogger.LOG.log(new Info(TheDbConnector.class, "Database Connection established at "
                    + database_url + " by " + database_user + " - Now Connected!"));
        } catch (SQLException e) {
            if (e.getErrorCode() == 40000) {
                YaqpLogger.LOG.log(new Info(TheDbConnector.class, "Database " + database_url + " was not found -- creating..."));
                createDataBase();
            } else {
                YaqpLogger.LOG.log(new Fatal(TheDbConnector.class, "Unexpected condition while connecting to the database :: " + e));
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            YaqpLogger.LOG.log(new Fatal(TheDbConnector.class, "Unable to connect to "
                    + DB.database_url + "--" + e));
        }
    }

    /**
     * This method is called when the specified database does not exist and it is
     * created. The directive <code>create=true</code> is used within the URL
     * of the database
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
            YaqpLogger.LOG.log(new Fatal(TheDbConnector.class, "Encountered an error while trying to "
                    + "create a new database :: " + ex));
        }
    }
}
