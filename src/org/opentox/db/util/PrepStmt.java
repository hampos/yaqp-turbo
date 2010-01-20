package org.opentox.db.util;

import static org.opentox.db.table.StandardTables.*;

/**
 *
 * @author chung
 */
public enum PrepStmt {

    ADD_ALGORITHM_ONTOLOGY("INSERT INTO " + ALGORITHM_ONTOLOGIES.getTable().getTableName() + " (NAME, URI) VALUES (?,?)"),

    ADD_ALGORITHM_ONTOL_RELATION("INSERT INTO " + ALGORITHM_ONTOL_RELATION.getTable().getTableName() + " (ALGORITHM_UID, ONTOLOGY_UID ) VALUES (?,?)"),

    ADD_USER("INSERT INTO " + USERS.getTable().getTableName()
    + " ( USERNAME, PASS, FIRSTNAME, LASTNAME, EMAIL, ORGANIZATION, COUNTRY, CITY, ADDRESS, WEBPAGE, ROLE) VALUES (?,?,?,?,?,?,?,?,?,?,?)"),

    ADD_USER_GROUP("INSERT INTO " + USER_AUTH.getTable().getTableName() + "(NAME, USER_LEVEL) VALUES (?,?)"),

    ADD_ALGORITHM("INSERT INTO " + ALGORITHMS.getTable().getTableName() + " (NAME, URI) VALUES (?,?)"),

    ADD_PRED_MODEL("INSERT INTO " + PREDICTION_MODELS.getTable().getTableName()
    + " (NAME, URI, PREDICTION_FEATURE, DEPENDENT_FEATURE, ALGORITHM, CREATED_BY ) VALUES (?,?,?,?,?,?)"),

    ADD_MODEL_MLR("INSERT INTO " + MLR_MODELS.getTable().getTableName() + " (DATASET) VALUES (?)"),

    ADD_FEATURE("INSERT INTO "+FEATURES.getTable().getTableName()+" (URI) VALUES (?)");

    private String sql;

    private PrepStmt(String SQL) {
        this.sql = SQL;
    }

    public String getSql() {
        return this.sql;
    }
}

    

    

    

