package org.opentox.db.interfaces;

import java.sql.Connection;
import org.opentox.core.exceptions.YaqpException;

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

    int getDatabasePort() throws YaqpException;

    String getDatabaseUser();

    String getDatabaseDriver();



    

}
