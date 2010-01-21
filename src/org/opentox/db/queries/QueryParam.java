package org.opentox.db.queries;

import org.opentox.db.interfaces.JQueryParam;
import org.opentox.db.util.SQLDataTypes;

/**
 *
 * @author Sopasakis Pantelis
 * @author Charalampos Chomenides
 */
@Deprecated
public class QueryParam implements JQueryParam {

    protected SQLDataTypes type;
    protected String value;
    protected String parameterName;

    public QueryParam(String name, String value, SQLDataTypes type){

    }

    public void setType(SQLDataTypes type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public SQLDataTypes getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    

    
}
