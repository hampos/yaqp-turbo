package org.opentox.db.interfaces;

import java.sql.Connection;
import java.sql.SQLException;
import org.opentox.db.exceptions.DbException;

/**
 *
 * @author chung
 */
public interface IDbAccess {
    void setConnection(Connection connection) throws DbException;
    void close() throws SQLException;
    void open() throws DbException;
}
