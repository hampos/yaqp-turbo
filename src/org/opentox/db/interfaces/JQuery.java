package org.opentox.db.interfaces;



import java.util.ArrayList;
import org.opentox.db.util.PrepStmtList.PREP_STMT;
import org.opentox.db.util.QueryType;

/**
 * A Prepared statement with a list of parameters for it.
 * @author Charalampos Chomenides
 */
public interface JQuery<T> {

    /**
     * 
     * @return type of the query, e.g. update, insert etc
     */
     QueryType getQueryType();

     
    /**
     *
     * @return a list of parameters for the prepared statement of the query.
     */
    ArrayList<JQueryParam<T>> getParameters();

    /**
     *
     * @return standard prepared statement for the database
     */
    PREP_STMT getPrepStmt();

    /**
     * Set the prepared statement using some statement preparation factory such
     * as {@link PREP_STMT }.
     * @param prepStmt
     */
    void setPrepStmt(PREP_STMT prepStmt);

   

    

}
