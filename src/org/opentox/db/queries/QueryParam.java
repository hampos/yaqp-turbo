package org.opentox.db.queries;


/**
 *
 * @author Sopasakis Pantelis
 * @author Charalampos Chomenides
 */
public class QueryParam  {

   
    private final Class valueType;
    private final String parameterName;

    public QueryParam(final String parameterName, final Class type){
        this.parameterName = parameterName;
        this.valueType = type;
    }

  
    public Class getType() {
        return valueType;
    }

    public String getName(){
        return parameterName;
    }

    

    
}
