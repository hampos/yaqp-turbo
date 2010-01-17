package org.opentox.db.interfaces;

import java.sql.Connection;

/**
 *
 * @author chung
 */
public interface JDbConnector {

    Object getLock();

    Object getLock(String lockName);

    boolean isConnected();

    void connect();

    void disconnect();

    Connection getConnection();

    String getDatabaseName();

    String getDatabaseUrl();

    int getDatabasePort();

    String getDatabaseDriver();



    

}
