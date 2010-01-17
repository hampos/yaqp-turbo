package org.opentox.db.queries;


import java.util.ArrayList;
import org.opentox.db.interfaces.JQuery;
import org.opentox.db.interfaces.JQueryParam;
import org.opentox.db.util.PrepStmtList.PREP_STMT;
import org.opentox.db.util.QueryType;

/**
 *
 * @author chung
 */
public class Query<T> implements JQuery<T> {

    private PREP_STMT  prepStmt;
    private ArrayList<JQueryParam<T>> list;
    private QueryType qt;

    public QueryType getQueryType() {
        return this.qt;
    }

    public void setQueryType(QueryType query_type){
        this.qt = query_type;
    }

    public ArrayList<JQueryParam<T>> getParameters() {
        return this.list;
    }

    public void setParameters(ArrayList<JQueryParam<T>> parameters){
        this.list = parameters;
    }


    public PREP_STMT getPrepStmt() {
        return this.prepStmt;
    }

    public void setPrepStmt(PREP_STMT prepStmt) {
        this.prepStmt = prepStmt;
    }



    
   

}
