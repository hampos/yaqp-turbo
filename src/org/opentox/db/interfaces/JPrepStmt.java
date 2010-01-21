package org.opentox.db.interfaces;

import org.opentox.db.queries.QueryParam;
import org.opentox.db.util.QueryType;

/**
 *
 * @author chung
 */
public interface JPrepStmt {

    String getSql();
    QueryParam[] getParameters();
   

}
