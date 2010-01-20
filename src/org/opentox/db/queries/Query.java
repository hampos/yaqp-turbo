package org.opentox.db.queries;


import java.util.ArrayList;
import org.opentox.db.interfaces.JQuery;
import org.opentox.db.interfaces.JQueryParam;
import org.opentox.db.util.PrepStmt;
import org.opentox.db.util.QueryType;

/**
 *
 * @author chung
 */
public class Query implements JQuery {

    private PrepStmt  prepStmt;
    private ArrayList<JQueryParam> list;
    private QueryType qt;

    public QueryType getQueryType() {
        return this.qt;
    }

    public void setQueryType(QueryType query_type){
        this.qt = query_type;
    }

    public ArrayList<JQueryParam> getParameters() {
        return this.list;
    }

    public void setParameters(ArrayList<JQueryParam> parameters){
        this.list = parameters;
    }


    public PrepStmt getPrepStmt() {
        return this.prepStmt;
    }

    public void setPrepStmt(PrepStmt prepStmt) {
        this.prepStmt = prepStmt;
    }



    
   

}
