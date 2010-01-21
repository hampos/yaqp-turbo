package org.opentox.db.util;

import org.opentox.db.interfaces.JPrepStmt;
import org.opentox.db.queries.QueryParam;
import static org.opentox.db.table.StandardTables.*;

/**
 *
 * @author chung
 */
public enum PrepStmt implements JPrepStmt {

    ADD_ALGORITHM_ONTOLOGY(
    "INSERT INTO " + ALGORITHM_ONTOL_RELATION.getTable().getTableName()
    + " (ALGORITHM_UID, ONTOLOGY_UID ) VALUES (?,?)",
    new String[]{"NAME", "URI"}),

    ADD_ALGORITHM_ONTOL_RELATION(
    "INSERT INTO " + ALGORITHM_ONTOL_RELATION.getTable().getTableName() + " (ALGORITHM_UID, ONTOLOGY_UID ) VALUES (?,?)",
    new String[]{"ALGORITHM_UID", "ONTOLOGY_UID"}),

    ADD_USER(
    "INSERT INTO " + USERS.getTable().getTableName()
    + " ( USERNAME, PASS, FIRSTNAME, LASTNAME, EMAIL, ORGANIZATION, COUNTRY, CITY, ADDRESS, WEBPAGE, ROLE) VALUES (?,?,?,?,?,?,?,?,?,?,?)",
    new String[]{"USERNAME", "PASS", "FIRSTNAME", "LASTNAME", "EMAIL", "ORGANIZATION", "COUNTRY", "CITY", "ADDRESS", "WEBPAGE", "ROLE"}),

    ADD_USER_GROUP("INSERT INTO " + USER_AUTH.getTable().getTableName() + "(NAME, USER_LEVEL) VALUES (?,?)",
    new String[]{"NAME", "USER_LEVEL"}),

    ADD_ALGORITHM("INSERT INTO " + ALGORITHMS.getTable().getTableName() + " (NAME, URI) VALUES (?,?)",
    new String[]{"NAME", "URI"}),

    ADD_PRED_MODEL("INSERT INTO " + PREDICTION_MODELS.getTable().getTableName()
    + " (NAME, URI, PREDICTION_FEATURE, DEPENDENT_FEATURE, ALGORITHM, CREATED_BY ) VALUES (?,?,?,?,?,?)",
    new String[]{"NAME", "URI", "PREDICTION_FEATURE", "DEPENDENT_FEATURE", "ALGORITHM", "CREATED_BY"}),

    ADD_MODEL_MLR("INSERT INTO " + MLR_MODELS.getTable().getTableName() + " (DATASET) VALUES (?)", new String[]{"DATASET"}),

    ADD_FEATURE("INSERT INTO " + FEATURES.getTable().getTableName() + " (URI) VALUES (?)", new String[]{"URI"}),;





    private String sql;
    private String[] listOfParameters;

    private PrepStmt(String SQL, String[] x) {
        this.sql = SQL;
        this.listOfParameters = x;
    }

    private PrepStmt(String SQL, QueryParam[] list) {
        this.sql = SQL;
    }

    public String getSql() {
        return this.sql;
    }

    public String[] getParameterSequence() {
        return this.listOfParameters;
    }
}

    

    

    

