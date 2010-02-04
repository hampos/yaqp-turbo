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
package org.opentox.db.interfaces;

import org.opentox.db.exceptions.DbException;
import org.opentox.db.queries.HyperResult;
import org.opentox.db.util.QueryType;



/**
 * This is a wrapper(proxy) for java.sql.PreparedStatement. A HyperStatement can be
 * initialized with an SQL String for the prepared statement like for example
 * <code>INSERT INTO {TABLE NAME} (A,B) VALUES (?,?)</code>.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface JHyperStatement {

    /**
     * Set an integer value.
     * @param index parameter's index (Start with 1)
     * @param value the parameter's value
     * @throws DbException If a database connection/access occurs or the index is
     * out of bound of there is a datatype conflict.
     */
    void setInt(int index, int value) throws DbException;
    /**
     * Set a String value to a parameter of a prepared statement.
     * @param index parameter's index (Start with 1)
     * @param value the parameter's value
     * @throws DbException If a database connection/access occurs or the index is
     * out of bound of there is a datatype conflict.
     */
    void setString(int index, String value) throws DbException;
    /**
     * Set a double value to a parameter of a prepared statement
     * @param index parameter's index (Start with 1)
     * @param value the parameter's value
     * @throws DbException If a database connection/access occurs or the index is
     * out of bound of there is a datatype conflict.
     */
    void setDouble(int index, double value) throws DbException;
    /**
     * Execute an update query in the database
     * @return either the row count for SQL Data Manipulation Language
     * statements or <code>0</code> for SQL statements that return nothing
     * @throws DbException If the statement is not well-prepared or some
     * database connection/access issue occurs.
     */
    HyperResult executeUpdate() throws DbException;
    /**
     * Execute a database select-type query.
     * @return The result of the query as an instance of <code>HyperResult</code>
     * @throws DbException If the statement is not well-prepared or some
     * database connection/access issue occurs.
     */
    HyperResult executeQuery() throws DbException;
    /**
     * String representation of the prepared statement
     * @return the SQL command
     */
    @Override
    String toString();
    /**
     * Clear the parameters of the prepared statement.
     * @throws DbException if a database connection/access issue occurs.
     */
    void flush() throws DbException;
    /**
     * The type of the HyperStatement.
     * @return Statement Type as an instance of <code>QueryType</code>
     */
    QueryType getType();

}
