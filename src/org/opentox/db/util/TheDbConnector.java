/*
 *
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
import org.opentox.core.interfaces.JProcessor;
import org.opentox.core.processors.BatchProcessor;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.interfaces.JDbConnector;
import org.opentox.db.table.StandardTables;
import org.opentox.db.table.Table;
import org.opentox.db.table.TableCreator;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.*;

/**
 * This Singleton Class manages the connection to the database.
 * @author Sopasakis Pantelis
 * @author Charalampos Chomenides
 */
public class TheDbConnector implements JDbConnector {

    private static TheDbConnector instanceOfthis = null;
    /**
     * Public access point to the {@link TheDbConnector DB Connection Singleton }.
     * Use this static connection to the database to perform any operations.
     * The first time this field is retrieved, a new DB connection is started.
     *
     * @see TheDbConnector#init() DB initialization.
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
    private boolean isInitialized = false;
    private final Lock lock = new ReentrantLock();

    private synchronized static TheDbConnector getInstance() {
        if (instanceOfthis == null) {
            try {
                instanceOfthis = new TheDbConnector();
            } catch (Throwable e) {
                YaqpLogger.LOG.log(new Fatal(TheDbConnector.class, "XAE317 - Unable to connect to "
                        + DB.database_url + " :: " + e));
            }
        }
        return (TheDbConnector) instanceOfthis;
    }

    /**
     * Initialize the Connection to the database. Initializes the standard tables of
     * the database. Check if the database is initialized usign the method {@link
     * TheDbConnector#isInitialized() }. If the database connection is already
     * initialized, no action is taken.
     *
     * @throws DbException If the database could not be initialized properly.
     */
    public synchronized static void init() throws DbException {

        if (!instanceOfthis.isInitialized()) {
            TableCreator creator = new TableCreator();

            BatchProcessor<Table, Object, JProcessor<Table, Object>> bp =
                    new BatchProcessor<Table, Object, JProcessor<Table, Object>>(creator, 1, 1);

            ArrayList<Table> tableToBeCreated = new ArrayList<Table>();
            for (StandardTables t : StandardTables.values()) {
                tableToBeCreated.add(t.getTable());
            }

            try {
                YaqpLogger.LOG.log(new Info(TheDbConnector.class, "Attempting to create the standard tables..."));
                bp.process(tableToBeCreated);
                YaqpLogger.LOG.log(new Info(TheDbConnector.class, "Standard tables were initialized..."));
                DB.isInitialized = true;
                // TODO; Check if the tables were really created!
            } catch (YaqpException ex) {
                YaqpLogger.LOG.log(new Fatal(TheDbConnector.class, "XAE318 - " + ex.toString()));
            }
        } else {
            YaqpLogger.LOG.log(new Trace(TheDbConnector.class, "Database is Already Initialized"));
        }

    }

    /**
     * Retrieves whether the database connection is properly initialized, i.e.
     * if there is an established connection and the standard tables where
     * created.
     * @return true if the database is initialized
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * Private constructor.
     * @throws YaqpException If a connection cannot be established. In such a
     * case, check again your server.properties file.
     */
    private TheDbConnector() throws YaqpException {
        connect();
    }

    public Lock getLock() {
        return lock;
    }

    public void connect() {
        if (!isConnected()) {
            try {
                startDerbyServer();
                loadDriver();
                establishConnection();
            } catch (Throwable ex) {
                YaqpLogger.LOG.log(new Fatal(TheDbConnector.class, "XAE319 - Unable to connect to "
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
                YaqpLogger.LOG.log(new ScrewedUp(TheDbConnector.class, "XAE320 - Connection "
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
            YaqpLogger.LOG.log(new Fatal(TheDbConnector.class, "XAE321 - Not acceptable port :" + database_port));
            throw new YaqpException("XAE321 - Wrong port declaration :" + database_port);
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
                YaqpLogger.LOG.log(new Fatal(TheDbConnector.class, "XAE323 - No connection to the database :" + database_name));
                throw new DbException("XAE323 - No connection to the database");
            }
        } else {
            String message = "XAE324 - No connection to the database :" + database_name;
            YaqpLogger.LOG.log(new Fatal(TheDbConnector.class, message));
            throw new DbException(message);
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
                throw new DbException("XAE325 - ", ex);
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
     *
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
            YaqpLogger.LOG.log(new Fatal(TheDbConnector.class, "XAF335 - Error while loading the JDBC driver ::" + exc));
        }
    }

    /**
     * Establishes a connection with the database or creates a new database
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
                YaqpLogger.LOG.log(new Fatal(TheDbConnector.class, "XAG612 - Unexpected condition while connecting to the database :: " + e));
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            YaqpLogger.LOG.log(new Fatal(TheDbConnector.class, "XAG703 - Unable to connect to "
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
            YaqpLogger.LOG.log(new Fatal(TheDbConnector.class, "XAG846 - Encountered an error while trying to "
                    + "create a new database :: " + ex));
        }
    }
}
