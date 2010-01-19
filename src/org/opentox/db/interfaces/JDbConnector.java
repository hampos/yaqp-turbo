package org.opentox.db.interfaces;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.concurrent.locks.Lock;
import org.opentox.core.exceptions.YaqpException;

/**
 * Interface for the database connector.
 * @author Sopasakis Pantelis
 * @author Charalampos Chomenides
 */
public interface JDbConnector {

    /**
     * Returns a Reentrant lock from the DbConnector.
     * @return
     */
    Lock getLock();

    /**
     * A flag telling whether there is an active connection to the
     * database. If not you can start a new connection calling the method
     * {@link JDbConnector#connect() connect() }
     * @return
     */
    boolean isConnected();

    /**
     * Starts a new connection with the database. The connection
     * URL and username are found at the properties file of the server.
     */
    void connect();

    /**
     * Disconnects the database.
     */
    void disconnect();

    /**
     * Returns an SQL Connection object.
     * @return connection with the database - null if no connection is alive.
     */
    Connection getConnection();

    /**
     * Returns the name of the database.
     * @return database name
     */
    String getDatabaseName();

    /**
     * Returns the URL of the database
     * @return database url
     */
    String getDatabaseUrl();

    /**
     * The port of the database connection. This the ports at which the derby
     * database server listens.
     * @return port of database connection.
     * @throws YaqpException An exception is thrown if the port
     * specified in the properties file is not an integer or the port is used
     * by some other service or is not available for any reason.
     */
    int getDatabasePort() throws YaqpException;

    /**
     * The user that handles the connection
     * @return database user
     */
    String getDatabaseUser();

    /**
     * The driver used by the connection
     * @return database driver
     */
    String getDatabaseDriver();


    DatabaseMetaData getMetaData() throws YaqpException;



    

}
