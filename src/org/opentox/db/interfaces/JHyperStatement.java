package org.opentox.db.interfaces;

import java.sql.ResultSet;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.util.SQLDataTypes;



/**
 * This is a wrapper(proxy) for java.sql.PreparedStatement
 * @author chung
 */
public interface JHyperStatement {

    void setInt(int index, int value) throws DbException;
    void setString(int index, String value) throws DbException;
    void setObject(int index, Object value) throws DbException;
    void setObject(int index, String value, SQLDataTypes datatype) throws DbException;
    int executeUpdate() throws DbException;
    ResultSet executeQuery() throws DbException;
    @Override
    String toString();
    void flush() throws DbException;

}
