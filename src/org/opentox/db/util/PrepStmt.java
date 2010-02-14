/*
 *
 * YAQP - Yet Another QSAR Project:
 * Machine Learning algorithms designed for the prediction of toxicological
 * features of chemical compounds become available on the Web. Yaqp is developed
 * under OpenTox (http://opentox.org) which is an FP7-funded EU research project.
 * This project was developed at the Automatic Control Lab in the Chemical Engineering
 * School of National Technical University of Athens. Please read README for more
 * information.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact:
 * Pantelis Sopasakis
 * chvng@mail.ntua.gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 */
package org.opentox.db.util;

import org.opentox.db.interfaces.JPrepStmt;
import org.opentox.db.queries.QueryFood;
import org.opentox.db.queries.QueryParam;
import org.opentox.db.table.collection.AlgOntRelationTable;
import org.opentox.db.table.collection.AlgOntTable;
import org.opentox.db.table.collection.AlgorithmsTable;
import org.opentox.db.table.collection.FeaturesTable;
import org.opentox.db.table.collection.IndFeatRelationTable;
import org.opentox.db.table.collection.OmegaTable;
import org.opentox.db.table.collection.QSARModelsTable;
import org.opentox.db.table.collection.SupportVecTable;
import org.opentox.db.table.collection.TasksTable;
import org.opentox.db.table.collection.UserAuthTable;
import org.opentox.db.table.collection.UsersTable;
import static org.opentox.db.table.StandardTables.*;

/**
 * Prepared statements are used for increased security as it becomes hard for
 * someone to perform SQL injections or other malicious operations and for
 * performance reasons also as these are precompiled in the database. This is a
 * collection (an enumeration) of SQL Strings corresponding to prepared statements.
 *
 * @author Sopasakis Pantelis
 * @author Charalampos Chomenides
 */
public enum PrepStmt implements JPrepStmt {

    /**
     * Add a new Algorithm Ontology in the database. The <code>NAME</code> and the
     * <code>URI</code> of the algorithm ontology have to be provided.
     *
     * @see PrepStmt#ADD_ALGORITHM Add a new Algorithm
     * @see PrepStmt#ADD_ALGORITHM_ONTOL_RELATION Add an Algorithm-Ontology Relation
     */
    ADD_ALGORITHM_ONTOLOGY(
    "INSERT INTO " + AlgOntTable.TABLE.getTableName()
    + " (NAME, URI ) VALUES (?,?)",
    new QueryParam[]{new QueryParam("NAME", String.class), new QueryParam("URI", String.class)}),
    /**
     * Add a new Algorithm Ontology Relation in the database. Every algorithm
     * bolengs to one ore more ontological classes. This one-to-many relation is
     * depicted in this table where pairs of algorithm and algorithm ontology are
     * stored. The name of the <code>algorithm</code> and the corresponding
     * <code>ontology</code> have to be provided (as <code>QueryFood</code>).
     *
     * @see PrepStmt#ADD_ALGORITHM Add a new Algorithm
     * @see QueryFood
     */
    ADD_ALGORITHM_ONTOL_RELATION(
    "INSERT INTO " + AlgOntRelationTable.TABLE.getTableName() + " ( ALGORITHM, ONTOLOGY) VALUES (?,?)",
    new QueryParam[]{new QueryParam("ALGORITHM", String.class), new QueryParam("ONTOLOGY", String.class)}),
    /**
     * Add a new user into the database; you need to provide the username, password
     * and other information about the user including the user group.
     *
     * @see PrepStmt#ADD_USER_GROUP Add a UserGroup
     */
    ADD_USER(
    "INSERT INTO " + UsersTable.TABLE.getTableName()
    + " ( USERNAME, PASS, FIRSTNAME, LASTNAME, EMAIL, ORGANIZATION, "
    + "COUNTRY, CITY, ADDRESS, WEBPAGE, ROLE) VALUES (?,?,?,?,?,?,?,?,?,?,?)",
    new QueryParam[]{
        new QueryParam("USERNAME", String.class),
        new QueryParam("PASS", String.class),
        new QueryParam("FIRSTNAME", String.class),
        new QueryParam("LASTNAME", String.class),
        new QueryParam("EMAIL", String.class),
        new QueryParam("ORGANIZATION", String.class),
        new QueryParam("COUNTRY", String.class),
        new QueryParam("CITY", String.class),
        new QueryParam("ADDRESS", String.class),
        new QueryParam("WEBPAGE", String.class),
        new QueryParam("ROLE", String.class)
}),
    /**
     * Add a new user group. A user group is characterized by its authorization
     * level which is an integer. Two groups can have the same authorization level.
     * The parameters one has to provide within the <code>QueryFood</code> are
     * the <code>NAME</code> and the <code>USER_LEVEL</code> (integer).
     *
     * @see PrepStmt#ADD_USER Add a new user
     */
    ADD_USER_GROUP("INSERT INTO " + UserAuthTable.TABLE.getTableName() + "(NAME, USER_LEVEL) VALUES (?,?)",
    new QueryParam[]{
        new QueryParam("NAME", String.class),
        new QueryParam("USER_LEVEL", Integer.class)
    }),
    /**
     * Add a new Algorithm in the database.
     */
    ADD_ALGORITHM("INSERT INTO " + AlgorithmsTable.TABLE.getTableName() + " (NAME) VALUES (?)",
    new QueryParam[]{
        new QueryParam("NAME", String.class)
    }),
    /**
     * Add a new prediction model in the database.
     */
    ADD_QSAR_MODEL("INSERT INTO " + QSARModelsTable.TABLE.getTableName()
    + " (CODE, PREDICTION_FEATURE, DEPENDENT_FEATURE, ALGORITHM, CREATED_BY, DATASET_URI, STATUS ) VALUES (?,?,?,?,?,?,?)",
    new QueryParam[]{
        new QueryParam("CODE", String.class),
        new QueryParam("PREDICTION_FEATURE", Integer.class),
        new QueryParam("DEPENDENT_FEATURE", Integer.class),
        new QueryParam("ALGORITHM", String.class),
        new QueryParam("CREATED_BY", String.class),
        new QueryParam("DATASET_URI", String.class),
        new QueryParam("STATUS", String.class)
}),
    ADD_INDEP_FEATURE_RELATION("INSERT INTO " + IndFeatRelationTable.TABLE.getTableName()
    + " (MODEL_UID, FEATURE_UID) VALUES (?,?)",
    new QueryParam[]{
        new QueryParam("MODEL_UID", Integer.class),
        new QueryParam("FEATURE_UID", Integer.class)
}),
    /**
     *
     */
    ADD_SUPPORT_VECTOR("INSERT INTO " + SupportVecTable.TABLE.getTableName()
    + " (UID, GAMMA, EPSILON, COST, COEFF0, TOLERANCE, CACHESIZE, KERNEL, DEGREE ) "
    + "VALUES (?,?,?,?,?,?,?,?,?)",
    new QueryParam[]{
        new QueryParam("UID", Integer.class),
        new QueryParam("GAMMA", Double.class),
        new QueryParam("EPSILON", Double.class),
        new QueryParam("COST", Double.class),
        new QueryParam("COEFF0", Double.class),
        new QueryParam("TOLERANCE", Double.class),
        new QueryParam("CACHESIZE", Double.class),
        new QueryParam("KERNEL", String.class),
        new QueryParam("DEGREE", Integer.class)
    }),
    /**
     *
     * Add a new feature in the database. The <code>URI</code> of the feature has to
     * be provided in the <code>QueryFood<code>.
     */
    ADD_FEATURE("INSERT INTO " + FeaturesTable.TABLE.getTableName() + " (URI) VALUES (?)",
    new QueryParam[]{
        new QueryParam("URI", String.class)
    }),
    ADD_TASK("INSERT INTO " + TasksTable.TABLE.getTableName()
    + " (NAME, CREATED_BY, ALGORITHM, DURATION ) VALUES (?,?,?,?)",
    new QueryParam[]{
        new QueryParam("NAME", String.class),
        new QueryParam("CREATED_BY", String.class),
        new QueryParam("ALGORITHM", String.class),
        new QueryParam("DURATION", Integer.class)
    }),
    /**
     *
     */
    ADD_OMEGA_MODEL("INSERT INTO "+OmegaTable.TABLE.getTableName()+" (CODE, CREATED_BY, DATASET_URI) VALUES (?,?,?)",
    new QueryParam[]{
        new QueryParam("CODE", String.class),
        new QueryParam("CREATED_BY", String.class),
        new QueryParam("DATASET_URI", String.class)
    }),
    /**
     *
     * A Prepared Statement to retrieve all users from the database. The SQL command
     * is: <code>SELECT * FROM USERS</code>.
     */
    GET_ALL_USERS("SELECT USERNAME FROM " + UsersTable.TABLE.getTableName(), null),
    /**
     * Get a set of users for a given search criterion.
     */
    SEARCH_USER("SELECT " + UsersTable.TABLE.getTableName() + ".* FROM " + UsersTable.TABLE.getTableName() + " "
    + "INNER JOIN " + UserAuthTable.TABLE.getTableName() + " "
    + "ON NAME = ROLE WHERE USER_LEVEL >= ? AND USER_LEVEL <= ? "
    + "AND USERNAME LIKE ? "
    + "AND EMAIL LIKE ? "
    + "AND FIRSTNAME LIKE ? "
    + "AND LASTNAME LIKE ? "
    + "AND COUNTRY LIKE ? "
    + "AND CITY LIKE ? "
    + "AND ADDRESS LIKE ? "
    + "AND ORGANIZATION LIKE ? "
    + "AND WEBPAGE LIKE ? "
    + "AND ROLE LIKE ? ",
    new QueryParam[]{
        new QueryParam("USER_LEVEL_MIN", Integer.class),
        new QueryParam("USER_LEVEL_MAX", Integer.class),
        new QueryParam("USERNAME", String.class),
        new QueryParam("EMAIL", String.class),
        new QueryParam("FIRSTNAME", String.class),
        new QueryParam("LASTNAME", String.class),
        new QueryParam("COUNTRY", String.class),
        new QueryParam("CITY", String.class),
        new QueryParam("ADDRESS", String.class),
        new QueryParam("ORGANIZATION", String.class),
        new QueryParam("WEBPAGE", String.class),
        new QueryParam("ROLE", String.class),
    }),
    /**
     *
     * Prepared statement used to retrieve all algorithm ontologies in the database.
     * The SQL command is: <code>SELECT * FROM ALGORITHM_ONTOLOGIES</code>
     */
    GET_ALGORITHM_ONTOLOGIES("SELECT * FROM " + AlgOntTable.TABLE.getTableName(), null),
    /**
     *
     * For a certain Algorithm, retrieve all algorithm ontologies, i.e. the algorithm
     * types of this algorithm. The SQL Command is: <code>SELECT ALGORITHM_ONTOLOGIES.* FROM
     * ALGORITHM_ONTOLOGIES INNER JOID ALG_ONT_RELATION ON NAME = ONTOLOGY WHERE ALGORITHM
     * = ?</code>.
     */
    GET_ALGORITHM_ONTOLOGY_RELATION("SELECT " + AlgOntTable.TABLE.getTableName()+ ".*"
    + " FROM " + AlgOntTable.TABLE.getTableName() + " INNER JOIN " +AlgOntRelationTable.TABLE.getTableName()
    + " ON NAME=ONTOLOGY" + " WHERE ALGORITHM=?",
    new QueryParam[]{
        new QueryParam("ALGORITHM", String.class)
    }),
    /**
     *
     * Retrieve all algorithms that hava a certain ontological type.
     */
    GET_ONTOLOGY_ALGORITHM_RELATION("SELECT " + AlgorithmsTable.TABLE.getTableName() + ".*"
    + " FROM " + AlgorithmsTable.TABLE.getTableName() + " INNER JOIN " + AlgOntRelationTable.TABLE.getTableName()
    + " ON NAME=ALGORITHM" + " WHERE ONTOLOGY=?",
    new QueryParam[]{
        new QueryParam("ONTOLOGY", String.class)
    }),
    /**
     *
     * Retrieve all algorithm-ontology relations.
     */
    GET_ALGORITHM_ONTOLOGY_RELATIONS("SELECT * FROM " + AlgOntRelationTable.TABLE.getTableName(), null),
    /**
     * Get a specific user group.
     */
    GET_USER_GROUP("SELECT * FROM " + UserAuthTable.TABLE.getTableName() + " WHERE NAME=?",
    new QueryParam[]{
        new QueryParam("NAME", String.class)
    }),
    /**
     *
     * Get all user groups.
     */
    GET_USER_GROUPS("SELECT * FROM " + UserAuthTable.TABLE.getTableName(), null),
    /**
     *
     * Retrieve all algorithms.
     */
    GET_ALGORITHMS("SELECT * FROM " + AlgorithmsTable.TABLE.getTableName(), new QueryParam[]{}),
    /**
     *
     * Get all prediction models from the database.
     */
    SEARCH_QSAR_MODELS("SELECT * FROM " + QSARModelsTable.TABLE.getTableName()+" "+
            "WHERE PREDICTION_FEATURE = ? " +
            "AND DEPENDENT_FEATURE = ? " +
            "AND ALGORITHM LIKE ? " +
            "AND CREATED_BY LIKE ? " +
            "AND DATASET_URI LIKE ? ",
            new QueryParam[]{
                new QueryParam("PREDICTION_FEATURE", String.class),
                new QueryParam("DEPENDENT_FEATURE", String.class),
                new QueryParam("ALGORITHM", String.class),
                new QueryParam("CREATED_BY", String.class),
                new QueryParam("DATASET_URI", String.class),
            }),
    /**
     *
     * Get all MLR models.
     */
    //    GET_MLR_MODELS("SELECT "+QSARModels().getTableName()+".* , "+MlrModels().getTableName()+".DATASET FROM "+QSARModels().getTableName()+" INNER JOIN "+MlrModels().getTableName()
    //            +" ON "+QSARModels().getTableName()+".UID="+MlrModels().getTableName()+".UID",null),
    /**
     *
     * Get all SVM models
     */
    GET_SVM_MODELS("SELECT * FROM " + SupportVecTable.TABLE.getTableName(), null),
    /**
     *
     * Get all SVC models.
     */
    //    GET_SVC_MODELS("SELECT * FROM "+SvcModels().getTableName(), null),

    GET_FEATURES("SELECT * FROM " + FeaturesTable.TABLE.getTableName(), null),

    SEARCH_FEATURE("SELECT * FROM " + FeaturesTable.TABLE.getTableName() + " WHERE UID >= ? AND UID <= ? AND URI LIKE ?",

    new QueryParam[]{
        new QueryParam("UID_MIN", Integer.class),
        new QueryParam("UID_MAX", Integer.class),
        new QueryParam("URI", String.class)
    }),
    GET_INDEP_FEATURES("SELECT " + FeaturesTable.TABLE.getTableName() + ".*"
    + " FROM " + FeaturesTable.TABLE.getTableName() + " INNER JOIN " + IndFeatRelationTable.TABLE.getTableName()
    + " ON UID=FEATURE_UID" + " WHERE MODEL_UID=?",
    new QueryParam[]{
        new QueryParam("MODEL_UID", Integer.class)
    }),
    GET_TASKS("SELECT * FROM " + TasksTable.TABLE.getTableName(), null);

    /**
     * The SQL command for the preparation of the statement.
     */
    private String sql;
    private QueryParam[] parameters;

    private PrepStmt(String SQL, QueryParam[] pl) {
        this.sql = SQL;
        this.parameters = pl;
    }

    public String getSql() {
        return this.sql;
    }

    public QueryParam[] getParameters() {
        return this.parameters;
    }
}

    

    

    

