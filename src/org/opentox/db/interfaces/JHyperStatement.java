package org.opentox.db.interfaces;

import org.opentox.db.exceptions.DbException;
import org.opentox.db.queries.HyperResult;
import org.opentox.db.util.QueryType;



/**
 * This is a wrapper(proxy) for java.sql.PreparedStatement. A HyperStatement can be
 * initialized with an SQL String for the prepared statement like for example
 * <code>INSERT INTO {TABLE NAME} (A,B) VALUES (?,?)</code>.
 * @author chung
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
    int executeUpdate() throws DbException;
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
