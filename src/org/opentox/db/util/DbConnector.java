package org.opentox.db.util;

import java.sql.Connection;
import org.opentox.db.interfaces.JDbConnector;

/**
 *
 * @author chung
 */
public class DbConnector implements JDbConnector{

    private static JDbConnector instanceOfthis = null;

    public static DbConnector INSTANCE = getIstance();

    private Connection connection;

    private static DbConnector getIstance(){
        if (instanceOfthis == null){
            instanceOfthis = new DbConnector();
        }
        return (DbConnector) instanceOfthis;
    }

    private DbConnector(){
        connect();
    }

    public Object getLock() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getLock(String lockName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void connect() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void disconnect() {
        // TODO: ...
        instanceOfthis = null;
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getDatabaseName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getDatabaseUrl() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getDatabasePort() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getDatabaseDriver() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isConnected() {
        return (instanceOfthis == null);
    }

    public Connection getConnection() {
        return connection;
    }

}
