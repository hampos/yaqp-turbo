package org.opentox.db.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.interfaces.JHyperStatement;



/**
 *
 * @author chung
 */
public class HyperStatement implements JHyperStatement{

    private String sql;
    private PreparedStatement preparedStatement;


    public HyperStatement(final String sql) throws DbException{
        try {
            this.preparedStatement = TheDbConnector.DB.getConnection().prepareStatement(sql);
            this.sql = sql;
        } catch (SQLException ex) {
            throw new DbException(ex);
        }
    }
    

    public void setInt(int index, int value) throws DbException {
        try {
            preparedStatement.setInt(index, value);
        } catch (SQLException ex) {
            throw new DbException(ex);
        }
    }

    public void setString(int index, String value) throws DbException {
        try {
            preparedStatement.setString(index, value);
        } catch (SQLException ex) {
            throw new DbException(ex);
        }
    }

    public void setObject(int index, Object value) throws DbException{
        try {
            preparedStatement.setObject(index, value);
        } catch (SQLException ex) {
            throw new DbException(ex);
        }
    }

    public void setObject(int index, String value, SQLDataTypes datatype) throws DbException{
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int executeUpdate() throws DbException{
        try {
            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DbException(ex);
        }
    }

    public ResultSet executeQuery() throws DbException {
        try {
            return preparedStatement.executeQuery();
        } catch (SQLException ex) {
            throw new DbException(ex);
        }
    }

    @Override
    public String toString(){
        return this.sql;
    }

    public void flush() throws DbException {
        try {
            this.preparedStatement.clearParameters();
        } catch (SQLException ex) {
            throw new DbException(ex);
        }
    }

}
