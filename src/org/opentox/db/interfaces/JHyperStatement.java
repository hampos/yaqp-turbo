package org.opentox.db.interfaces;

import org.opentox.db.exceptions.DbException;
import org.opentox.db.queries.HyperResult;
import org.opentox.db.util.QueryType;



/**
 * This is a wrapper(proxy) for java.sql.PreparedStatement
 * @author chung
 */
public interface JHyperStatement {

    void setInt(int index, int value) throws DbException;
    void setString(int index, String value) throws DbException;
    void setDouble(int index, double value) throws DbException;
    int executeUpdate() throws DbException;
    HyperResult executeQuery() throws DbException;
    @Override
    String toString();
    void flush() throws DbException;
    QueryType getType();

}
