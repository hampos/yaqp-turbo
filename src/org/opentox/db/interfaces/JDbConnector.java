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
package org.opentox.db.interfaces;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
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
     * @return lock 
     */
    Lock getLock();

    /**
     * A flag telling whether there is an active connection to the
     * database. If not you can start a new connection calling the method
     * {@link JDbConnector#connect() connect() }
     * @return <code>true</code> if there's an active connection to the DB.
     */
    boolean isConnected();

    /**
     * Starts a new connection with the database. The connection
     * URL and username are found at the properties file of the server. If already
     * connected to the database, no action is taken.
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
     * Returns the name of the database.If the name of the database is <code>mydb</code>, the URL is
     * <code>jdbc:derby://localhost:{port}/mydb</code>. You can modify this
     * default options in your server.properties file of macos.server.properties
     * if your OS is Mac OS X.
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

    /**
     * Returns the metadata of the database. The metadata include information
     * about the database's tables, its supported SQL grammar, its stored procedures,
     * the capabilities of this connection, and so on.
     * @return database metadata
     * @throws YaqpException
     */
    DatabaseMetaData getMetaData() throws YaqpException;

    /**
     * A list of the tables in the database, generated by the database user. System
     * tables are excluded from this list.
     * @return list of database tables.
     * @throws YaqpException
     */
    ArrayList<String> getTableNames() throws YaqpException;


    /**
     * Retrieves whether the database connection is properly initialized, i.e.
     * if there is an established connection and the standard tables where
     * created.
     * @return <code>true</code> if the connector is initialized.
     */
    boolean isInitialized();
}
