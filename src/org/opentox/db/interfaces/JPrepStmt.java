package org.opentox.db.interfaces;

import org.opentox.db.queries.QueryParam;


/**
 *
 * Interface for the prepared statements used in YAQP. Prepared statements are
 * used for increased security as it becomes hard for someone to perform SQL
 * injections or other malicious operations and for performance reasons also
 * as these are precompiled in the database.
 * 
 * @author Sopasakis Pantelis
 * @author Charalampos Chomenides
 */
public interface JPrepStmt {

    /**
     * String representation of the SQL command corresponding to the prepared
     * statement.
     * @return SQL command
     */
    String getSql();
    /**
     * The set of parameters the client needs to provide to the prepared statement
     * before executing it.
     * @return Set of parameters
     * @see QueryParam
     */
    QueryParam[] getParameters();
   

}
