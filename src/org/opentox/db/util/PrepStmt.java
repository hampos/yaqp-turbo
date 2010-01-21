package org.opentox.db.util;

import org.opentox.db.interfaces.JPrepStmt;
import org.opentox.db.queries.QueryParam;
import static org.opentox.db.table.StandardTables.*;

/**
 *
 * TODO: To be updated, add new statements...
 * @author chung
 */
public enum PrepStmt implements JPrepStmt {

    /**
     *
     */
    ADD_ALGORITHM_ONTOLOGY(
    "INSERT INTO " + ALGORITHM_ONTOLOGIES.getTable().getTableName()
    + " (NAME, URI ) VALUES (?,?)",
        //    new QueryParamp[]
    new QueryParam[]    {
                            new QueryParam("NAME", String.class),
                            new QueryParam("URI", String.class)
                        }
    ),

    /**
     *
     */
    ADD_ALGORITHM_ONTOL_RELATION(
    "INSERT INTO " + ALGORITHM_ONTOL_RELATION.getTable().getTableName() + " (ALGORITHM_UID, ONTOLOGY_UID ) VALUES (?,?)",
    new QueryParam[]{
                        new QueryParam("ALGORITHM_UID", Integer.class),
                        new QueryParam("ONTOLOGY_UID", Integer.class)

                    }
    ),

//    ADD_USER(
//    "INSERT INTO " + USERS.getTable().getTableName()
//    + " ( USERNAME, PASS, FIRSTNAME, LASTNAME, EMAIL, ORGANIZATION, COUNTRY, CITY, ADDRESS, WEBPAGE, ROLE) VALUES (?,?,?,?,?,?,?,?,?,?,?)",
//    new String[]{"USERNAME", "PASS", "FIRSTNAME", "LASTNAME", "EMAIL", "ORGANIZATION", "COUNTRY", "CITY", "ADDRESS", "WEBPAGE", "ROLE"}),
//
//    ADD_USER_GROUP("INSERT INTO " + USER_AUTH.getTable().getTableName() + "(NAME, USER_LEVEL) VALUES (?,?)",
//    new String[]{"NAME", "USER_LEVEL"}),
//
//    ADD_ALGORITHM("INSERT INTO " + ALGORITHMS.getTable().getTableName() + " (NAME, URI) VALUES (?,?)",
//    new String[]{"NAME", "URI"}),
//
//    ADD_PRED_MODEL("INSERT INTO " + PREDICTION_MODELS.getTable().getTableName()
//    + " (NAME, URI, PREDICTION_FEATURE, DEPENDENT_FEATURE, ALGORITHM, CREATED_BY ) VALUES (?,?,?,?,?,?)",
//    new String[]{"NAME", "URI", "PREDICTION_FEATURE", "DEPENDENT_FEATURE", "ALGORITHM", "CREATED_BY"}),
//
//    ADD_MODEL_MLR("INSERT INTO " + MLR_MODELS.getTable().getTableName() + " (DATASET) VALUES (?)", new String[]{"DATASET"}),
//
//    ADD_FEATURE("INSERT INTO " + FEATURES.getTable().getTableName() + " (URI) VALUES (?)", new String[]{"URI"}),
            ;





    private String sql;
    private QueryParam[] parameters;
   
    private PrepStmt(String SQL, QueryParam[] pl){
        this.sql = SQL;
        this.parameters = pl;
    }

    public String getSql() {
        return this.sql;
    }

   
    public QueryParam[] getParameters(){
        return this.parameters;
    }

   
}

    

    

    

