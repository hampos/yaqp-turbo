package org.opentox.db.interfaces;



import java.util.ArrayList;
import org.opentox.db.util.PrepStmt;
import org.opentox.db.util.QueryType;

/**
 * A Prepared statement with a list of parameters for it.
 * @author Charalampos Chomenides
 */
public interface JQuery {

    /**
     * 
     * @return type of the query, e.g. update, insert etc
     */
     QueryType getQueryType();

     
    /**
     *
     * @return a list of parameters for the prepared statement of the query.
     */
    ArrayList<JQueryParam> getParameters();

    /**
     *
     * @return standard prepared statement for the database
     */
    PrepStmt getPrepStmt();

    /**
     * Set the prepared statement using some statement preparation factory such
     * as {@link PREP_STMT }.
     * @param prepStmt
     */
    void setPrepStmt(PrepStmt prepStmt);

   

    

}
