package org.opentox.db.util;

import org.opentox.db.table.StandardTables;


/**
 *
 * @author chung
 */
public enum PrepStmt {

    // TODO: populate the list of statements
    //add_user(""),
    add_algorithm_ontology("INSERT INTO "+StandardTables.ALGORITHM_ONTOLOGIES.getTable().getTableName()+" (NAME, URI) VALUES (?,?)"),   
    //add_user_role("")
    ;


    private String sql;

    private PrepStmt(String SQL) {
        this.sql = SQL;
    }

    public String getSql(){
        return this.sql;
    }
    
}

    

    

    

