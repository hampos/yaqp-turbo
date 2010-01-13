package org.opentox.db.processors;

import java.sql.Connection;
import java.sql.SQLException;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.interfaces.IDbAccess;
import org.opentox.db.interfaces.IDbProcessor;
import org.opentox.core.processors.Processor;

/**
 * This abstract class holds and manipulates the connection to the database.
 * @author chung
 */
public abstract class AbstractDbProcessor<Query, Result>
        extends Processor<Query, Result>
        implements IDbProcessor<Query, Result>, IDbAccess {

    protected Connection connection;

    public AbstractDbProcessor(){
        super();
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) throws DbException {
        if ((this.connection != null) && (this.connection != connection)) {
            try {
                close();
            } catch (SQLException x) {
                //logger.error(x);
            }
        }
        this.connection = connection;
    }

    public void close() throws SQLException {
        if ((connection != null) && (!connection.isClosed())) {
            connection.close();
        }
        connection = null;
    }
}
