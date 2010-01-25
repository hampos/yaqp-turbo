package org.opentox.db.queries;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.exceptions.DuplicateKeyException;
import org.opentox.db.interfaces.JHyperStatement;
import org.opentox.db.processors.DbProcessor;
import org.opentox.db.util.QueryType;
import org.opentox.db.util.TheDbConnector;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Warning;

/**
 *
 * This is a proxy for {@link java.sql.PreparedStatement Java PreparedStatement }
 * containing the set of necessary methods to prepare a statement. HyperStatements
 * are fed into a {@link DbProcessor DataBase Processor } to produce {@link
 * HyperResult Hyper Results }.
 * @author Sopasakis Pantelis
 * @author Chomenides Charalampos
 */
public class HyperStatement implements JHyperStatement {

    private String sql;
    private PreparedStatement preparedStatement;

    public HyperStatement(final String sql) throws DbException {

        try {
            this.preparedStatement = TheDbConnector.DB.getConnection().prepareStatement(sql);
            this.sql = sql;
        } catch (SQLException ex) {
            throw new DbException("Cannot prepare a statement in the database. Reproducing SQLException :" + ex, ex);
        }
    }

    public void setInt(int index, int value) throws DbException {
        try {
            preparedStatement.setInt(index, value);
        } catch (SQLException ex) {
            throw new DbException("Cannot set a parameteter with index " + index + " to the integer value : " + value, ex);
        }
    }

    public void setString(int index, String value) throws DbException {
        try {
            preparedStatement.setString(index, value);
        } catch (SQLException ex) {
            throw new DbException("Cannot set a parameteter with index " + index + " to String value : " + value, ex);
        }
    }

    public void setDouble(int index, double value) throws DbException {
        try {
            preparedStatement.setDouble(index, value);
        } catch (SQLException ex) {
            throw new DbException("Cannot set a parameteter with index " + index + " to double value : " + value, ex);
        }
    }

    public int executeUpdate() throws DbException {
        try {
            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            if (ex.toString().contains("duplicate key")) {
                YaqpLogger.LOG.log(new Warning(getClass(), "Duplicate Key Exception while executing update"));
                throw new DuplicateKeyException();
            }
            throw new DbException("Error while executing update... ", ex);
        }
    }

    public HyperResult executeQuery() throws DbException {
        HyperResult result = new HyperResult();
        try {
            ResultSet rs = preparedStatement.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();

            ArrayList<String> row = new ArrayList<String>();

            for (int col_index = 0; col_index < rsmd.getColumnCount(); col_index++) {
                result.addColName(rsmd.getColumnName(col_index+1), col_index+1);
            }
            while (rs.next()) {
                for (int col_index = 0; col_index < rsmd.getColumnCount(); col_index++) {
                    row.add(rs.getString(col_index + 1));
                }
                result.addRow(row);
                row = new ArrayList<String>();
            }
            
            rs.close();
            return result;
        } catch (SQLException ex) {
            throw new DbException("Exception while executing a database query through HyperStatement#executeQuery() ", ex);
        }
    }

    @Override
    public String toString() {
        return this.sql;
    }

    public void flush() throws DbException {
        try {
            this.preparedStatement.clearParameters();
        } catch (SQLException ex) {
            throw new DbException("Cannot clear the parameters from a prepared statements ", ex);
        }
    }

    public QueryType getType() {
        if (sql.contains("SELECT")) {
            return QueryType.SELECT;
        } else {
            return QueryType.UPDATE;
        }
    }
}
