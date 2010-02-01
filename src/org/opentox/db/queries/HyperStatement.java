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
 * 
 * @author Pantelis Sopasakis
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
