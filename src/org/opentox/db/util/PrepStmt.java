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
     * ****************************************************************************
     * ----------------------------------------------------------------------------
     *                      ADD QUERIES
     * ----------------------------------------------------------------------------
     * ****************************************************************************
     */


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
    ADD_USER_GROUP("INSERT INTO " + UserAuthTable.TABLE.getTableName() 
            + "(NAME, USER_LEVEL, MODEL_AUTH, USER_AUTH, ALGORITHM_AUTH, USER_GROUP_AUTH, MAX_MODELS) " +
            "VALUES (?,?,?,?,?,?,?)",
    new QueryParam[]{
        new QueryParam("NAME", String.class),
        new QueryParam("USER_LEVEL", Integer.class),
        new QueryParam("MODEL_AUTH", String.class),
        new QueryParam("USER_AUTH", String.class),
        new QueryParam("ALGORITHM_AUTH", String.class),
        new QueryParam("USER_GROUP_AUTH", String.class),
        new QueryParam("MAX_MODELS", Integer.class)
    }),
    /**
     * Add a new Algorithm in the database.
     */
    ADD_ALGORITHM("INSERT INTO " + AlgorithmsTable.TABLE.getTableName() 
            + " (NAME) VALUES (?)",
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
    /**
     * Add independent features for a specific model in the database.
     */
    ADD_INDEP_FEATURE_RELATION("INSERT INTO " + IndFeatRelationTable.TABLE.getTableName()
    + " (MODEL_UID, FEATURE_UID) VALUES (?,?)",
    new QueryParam[]{
        new QueryParam("MODEL_UID", Integer.class),
        new QueryParam("FEATURE_UID", Integer.class)
    }),
    /**
     * Add extra SVM or SVC model parameters in the database.
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
    /**
     * Add a new task in the database.
     */
    ADD_TASK("INSERT INTO " + TasksTable.TABLE.getTableName()
    + " (NAME, CREATED_BY, ALGORITHM, DURATION, RESULT ) VALUES (?,?,?,?,?)",
    new QueryParam[]{
        new QueryParam("NAME", String.class),
        new QueryParam("CREATED_BY", String.class),
        new QueryParam("ALGORITHM", String.class),
        new QueryParam("DURATION", Integer.class),
        new QueryParam("RESULT", String.class),
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
     * ****************************************************************************
     * ----------------------------------------------------------------------------
     *                      SEARCH QUERIES
     * ----------------------------------------------------------------------------
     * ****************************************************************************
     */


    /**
     * Searches in the UserGroup table for user groups that comply to the
     * specified parameters. If a parameter is not specified it will be ignored
     * in the search.
     * @return All information available on the specified User Groups
     */
    SEARCH_USER_GROUP("SELECT * FROM " + UserAuthTable.TABLE.getTableName() + " WHERE " +
            "NAME LIKE ? " +
            "AND USER_LEVEL >= ? " +
            "AND USER_LEVEL <= ? " +
            "AND MODEL_AUTH LIKE ? " +
            "AND USER_AUTH LIKE ? " +
            "AND ALGORITHM_AUTH LIKE ? " +
            "AND USER_GROUP_AUTH LIKE ? " +
            "AND MAX_MODELS >= ? " +
            "AND MAX_MODELS <= ? " +
            getPagingQuery(),
            
    new QueryParam[]{
        new QueryParam("NAME", String.class),
        new QueryParam("USER_LEVEL_MIN", Integer.class),
        new QueryParam("USER_LEVEL_MAX", Integer.class),
        new QueryParam("MODEL_AUTH", String.class),
        new QueryParam("USER_AUTH", String.class),
        new QueryParam("ALGORITHM_AUTH", String.class),
        new QueryParam("USER_GROUP_AUTH", String.class),
        new QueryParam("MAX_MODELS_MIN", Integer.class),
        new QueryParam("MAX_MODELS_MAX", Integer.class),
        new QueryParam("OFFSET", Integer.class),
        new QueryParam("ROWS", Integer.class)
    }),

    /**
     * Searches in the UserGroup table for user groups that comply to the
     * specified parameters. If a parameter is not specified it will be ignored
     * in the search.
     * @return Only NAME column of the specified User Groups
     */
    SEARCH_USER_GROUP_SKROUTZ("SELECT NAME FROM " + UserAuthTable.TABLE.getTableName() + " WHERE " +
            "NAME LIKE ? " +
            "AND USER_LEVEL >= ? " +
            "AND USER_LEVEL <= ? " +
            "AND MODEL_AUTH LIKE ? " +
            "AND USER_AUTH LIKE ? " +
            "AND ALGORITHM_AUTH LIKE ? " +
            "AND USER_GROUP_AUTH LIKE ? " +
            "AND MAX_MODELS >= ? " +
            "AND MAX_MODELS <= ? " +
            getPagingQuery(),
    new QueryParam[]{
        new QueryParam("NAME", String.class),
        new QueryParam("USER_LEVEL_MIN", Integer.class),
        new QueryParam("USER_LEVEL_MAX", Integer.class),
        new QueryParam("MODEL_AUTH", String.class),
        new QueryParam("USER_AUTH", String.class),
        new QueryParam("ALGORITHM_AUTH", String.class),
        new QueryParam("USER_GROUP_AUTH", String.class),
        new QueryParam("MAX_MODELS_MIN", Integer.class),
        new QueryParam("MAX_MODELS_MAX", Integer.class),
        new QueryParam("OFFSET", Integer.class),
        new QueryParam("ROWS", Integer.class)
    }),

    /**
     * Searches in the Users table for users that comply to the
     * specified User and UserGroup parameters. If a parameter is not specified it will be ignored
     * in the search.
     * @return All information available on the specified Users
     */
    SEARCH_USER("SELECT " + UsersTable.TABLE.getTableName() + ".* FROM "
            + UsersTable.TABLE.getTableName() + " "
    + "INNER JOIN " + UserAuthTable.TABLE.getTableName() + " "
    + "ON " + UserAuthTable.TABLE.getTableName()+ ".NAME = "+UsersTable.TABLE.getTableName() +".ROLE WHERE " +
            "USER_LEVEL >= ? AND USER_LEVEL <= ? " +
            "AND MODEL_AUTH LIKE ? " +
            "AND USER_AUTH LIKE ? " +
            "AND ALGORITHM_AUTH LIKE ? " +
            "AND USER_GROUP_AUTH LIKE ? " +
            "AND MAX_MODELS >= ? " +
            "AND MAX_MODELS <= ? "
    + "AND USERNAME LIKE ? "
    + "AND EMAIL LIKE ? "
    + "AND FIRSTNAME LIKE ? "
    + "AND LASTNAME LIKE ? "
    + "AND COUNTRY LIKE ? "
    + "AND CITY LIKE ? "
    + "AND ADDRESS LIKE ? "
    + "AND ORGANIZATION LIKE ? "
    + "AND WEBPAGE LIKE ? "
    + "AND ROLE LIKE ? "
    + getPagingQuery(),
    new QueryParam[]{
        new QueryParam("USER_LEVEL_MIN", Integer.class),
        new QueryParam("USER_LEVEL_MAX", Integer.class),
        new QueryParam("MODEL_AUTH", String.class),
        new QueryParam("USER_AUTH", String.class),
        new QueryParam("ALGORITHM_AUTH", String.class),
        new QueryParam("USER_GROUP_AUTH", String.class),
        new QueryParam("MAX_MODELS_MIN", Integer.class),
        new QueryParam("MAX_MODELS_MAX", Integer.class),

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

        new QueryParam("OFFSET", Integer.class),
        new QueryParam("ROWS", Integer.class),
    }),


    /**
     * Searches in the Users table for users that comply to the
     * specified User and UserGroup parameters. If a parameter is not specified it will be ignored
     * in the search.
     * @return Only EMAIL column of the specified Users
     */
    SEARCH_USER_SKROUTZ("SELECT " + UsersTable.TABLE.getTableName() + ".USERNAME FROM "
            + UsersTable.TABLE.getTableName() + " "
    + "INNER JOIN " + UserAuthTable.TABLE.getTableName() + " "
    + "ON " + UserAuthTable.TABLE.getTableName()+ ".NAME = "+UsersTable.TABLE.getTableName() +".ROLE WHERE " +
            "USER_LEVEL >= ? AND USER_LEVEL <= ? " +
            "AND MODEL_AUTH LIKE ? " +
            "AND USER_AUTH LIKE ? " +
            "AND ALGORITHM_AUTH LIKE ? " +
            "AND USER_GROUP_AUTH LIKE ? " +
            "AND MAX_MODELS >= ? " +
            "AND MAX_MODELS <= ? "
    + "AND USERNAME LIKE ? "
    + "AND EMAIL LIKE ? "
    + "AND FIRSTNAME LIKE ? "
    + "AND LASTNAME LIKE ? "
    + "AND COUNTRY LIKE ? "
    + "AND CITY LIKE ? "
    + "AND ADDRESS LIKE ? "
    + "AND ORGANIZATION LIKE ? "
    + "AND WEBPAGE LIKE ? "
    + "AND ROLE LIKE ? "
    + getPagingQuery(),
    new QueryParam[]{
        new QueryParam("USER_LEVEL_MIN", Integer.class),
        new QueryParam("USER_LEVEL_MAX", Integer.class),
        new QueryParam("MODEL_AUTH", String.class),
        new QueryParam("USER_AUTH", String.class),
        new QueryParam("ALGORITHM_AUTH", String.class),
        new QueryParam("USER_GROUP_AUTH", String.class),
        new QueryParam("MAX_MODELS_MIN", Integer.class),
        new QueryParam("MAX_MODELS_MAX", Integer.class),

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
     
        new QueryParam("OFFSET", Integer.class),
        new QueryParam("ROWS", Integer.class),
    }),


    /**
     * Searches in the AlgorithmOntologies table for ontologies that comply to the
     * specified parameters. If a parameter is not specified it will be ignored
     * in the search.
     * @return All information available on the specified Ontologies
     */
    SEARCH_ALGORITHM_ONTOLOGY("SELECT * FROM " + AlgOntTable.TABLE.getTableName() + " " +
            "WHERE NAME LIKE ? " +
            "AND URI LIKE ? " +
            getPagingQuery(),
    new QueryParam[]{
        new QueryParam("NAME", String.class),
        new QueryParam("URI", String.class),
        new QueryParam("OFFSET", Integer.class),
        new QueryParam("ROWS", Integer.class),
    }),

    /**
     * Searches in the AlgorithmOntologies table for ontologies that comply to the
     * specified parameters. If a parameter is not specified it will be ignored
     * in the search.
     * @return Only NAME column of the specified Ontologies
     */
    SEARCH_ALGORITHM_ONTOLOGY_SKROUTZ("SELECT NAME FROM " + AlgOntTable.TABLE.getTableName() + " " +
            "WHERE NAME LIKE ? " +
            "AND URI LIKE ? " +
            getPagingQuery(),
    new QueryParam[]{
        new QueryParam("NAME", String.class),
        new QueryParam("URI", String.class),
        new QueryParam("OFFSET", Integer.class),
        new QueryParam("ROWS", Integer.class),
    }),

    /**
     * Searches in the Features table for features that comply to the
     * specified parameters. If a parameter is not specified it will be ignored
     * in the search.
     * @return All information available on the specified features
     */
    SEARCH_FEATURE("SELECT * FROM " + FeaturesTable.TABLE.getTableName() + " " +
            "WHERE UID >= ? AND UID <= ? " +
            "AND URI LIKE ? " +
            getPagingQuery(),

    new QueryParam[]{
        new QueryParam("UID_MIN", Integer.class),
        new QueryParam("UID_MAX", Integer.class),
        new QueryParam("URI", String.class),
        new QueryParam("OFFSET", Integer.class),
        new QueryParam("ROWS", Integer.class)
    }),

    /**
     * Searches in the Features table for features that comply to the
     * specified parameters. If a parameter is not specified it will be ignored
     * in the search.
     * @return Only UID column of the specified features
     */
    SEARCH_FEATURE_SKROUTZ("SELECT UID FROM " + FeaturesTable.TABLE.getTableName() + " " +
            "WHERE UID >= ? AND UID <= ? " +
            "AND URI LIKE ? " +
            getPagingQuery(),

    new QueryParam[]{
        new QueryParam("UID_MIN", Integer.class),
        new QueryParam("UID_MAX", Integer.class),
        new QueryParam("URI", String.class),
        new QueryParam("OFFSET", Integer.class),
        new QueryParam("ROWS", Integer.class)
    }),


    /**
     * Searches in the QSARModels and SVMModels tables for models that comply to the
     * specified QSARModel parameters. If a parameter is not specified it will be ignored
     * in the search.
     * @return All information available on the specified models
     */
    SEARCH_QSAR_MODEL_ALL(
            "SELECT * FROM " + QSARModelsTable.TABLE.getTableName()
            + " LEFT OUTER JOIN " + SupportVecTable.TABLE.getTableName() + " ON "
            + QSARModelsTable.TABLE.getTableName() + ".UID=" + SupportVecTable.TABLE.getTableName() + ".UID "
            + " WHERE " + QSARModelsTable.TABLE.getTableName() + ".UID BETWEEN ? AND ? "
            + "AND CODE LIKE ? "
            + "AND PREDICTION_FEATURE BETWEEN ? AND ? "
            + "AND DEPENDENT_FEATURE BETWEEN ? AND ? "
            + "AND ALGORITHM LIKE ? "
            + "AND CREATED_BY LIKE ? "
            + "AND DATASET_URI LIKE ? "
            + "AND STATUS LIKE ? "
            + getPagingQuery(),
            new QueryParam[]{
                new QueryParam("UID_MIN", Integer.class),
                new QueryParam("UID_MAX", Integer.class),
                new QueryParam("CODE", String.class),
                new QueryParam("PRED_FEATURE_MIN", Integer.class),
                new QueryParam("PRED_FEATURE_MAX", Integer.class),
                new QueryParam("DEP_FEATURE_MIN", Integer.class),
                new QueryParam("DEP_FEATURE_MAX", Integer.class),
                new QueryParam("ALGORITHM", String.class),
                new QueryParam("CREATED_BY", String.class),
                new QueryParam("DATASET_URI", String.class),
                new QueryParam("STATUS", String.class),

                new QueryParam("OFFSET", Integer.class),
                new QueryParam("ROWS", Integer.class)
            }),

    /**
     * Searches in the QSARModels and SVMModels tables for models that comply to the
     * specified QSARModel parameters. If a parameter is not specified it will be ignored
     * in the search.
     * @return Only UID column of the specified models
     */
    SEARCH_QSAR_MODEL_ALL_SKROUTZ(
            "SELECT " + QSARModelsTable.TABLE.getTableName() + ".UID" +" FROM " + QSARModelsTable.TABLE.getTableName()
            + " LEFT OUTER JOIN " + SupportVecTable.TABLE.getTableName() + " ON "
            + QSARModelsTable.TABLE.getTableName() + ".UID=" + SupportVecTable.TABLE.getTableName() + ".UID "
            + " WHERE " + QSARModelsTable.TABLE.getTableName() + ".UID BETWEEN ? AND ? "
            + "AND CODE LIKE ? "
            + "AND PREDICTION_FEATURE BETWEEN ? AND ? "
            + "AND DEPENDENT_FEATURE BETWEEN ? AND ? "
            + "AND ALGORITHM LIKE ? "
            + "AND CREATED_BY LIKE ? "
            + "AND DATASET_URI LIKE ? "
            + "AND STATUS LIKE ? "
            + getPagingQuery(),
            new QueryParam[]{
                new QueryParam("UID_MIN", Integer.class),
                new QueryParam("UID_MAX", Integer.class),
                new QueryParam("CODE", String.class),
                new QueryParam("PRED_FEATURE_MIN", Integer.class),
                new QueryParam("PRED_FEATURE_MAX", Integer.class),
                new QueryParam("DEP_FEATURE_MIN", Integer.class),
                new QueryParam("DEP_FEATURE_MAX", Integer.class),
                new QueryParam("ALGORITHM", String.class),
                new QueryParam("CREATED_BY", String.class),
                new QueryParam("DATASET_URI", String.class),
                new QueryParam("STATUS", String.class),

                new QueryParam("OFFSET", Integer.class),
                new QueryParam("ROWS", Integer.class)
            }),

    /**
     * Searches in the QSARModels and SVMModels tables for models that comply to the
     * specified QSARModel and SVMModel parameters. If a parameter is not specified
     * it will be ignored in the search.
     * @return All information available on the specified models
     */
    SEARCH_QSAR_MODEL("SELECT * FROM " + QSARModelsTable.TABLE.getTableName() +
            " LEFT OUTER JOIN " + SupportVecTable.TABLE.getTableName() + " ON "
            + QSARModelsTable.TABLE.getTableName() +".UID=" +SupportVecTable.TABLE.getTableName()+".UID "+
           "WHERE " + QSARModelsTable.TABLE.getTableName() + ".UID BETWEEN ? AND ? "
            + "AND CODE LIKE ? "
            + "AND PREDICTION_FEATURE BETWEEN ? AND ? "
            + "AND DEPENDENT_FEATURE BETWEEN ? AND ? "
            + "AND ALGORITHM LIKE ? "
            + "AND CREATED_BY LIKE ? "
            + "AND DATASET_URI LIKE ? "
            + "AND STATUS LIKE ? "

            + "AND GAMMA BETWEEN ? AND ? "
            + "AND EPSILON BETWEEN ? AND ? "
            + "AND COST BETWEEN ? AND ? "
            + "AND COEFF0 BETWEEN ? AND ? "
            + "AND TOLERANCE BETWEEN ? AND ? "
            + "AND CACHESIZE BETWEEN ? AND ? "
            + "AND KERNEL LIKE ? "
            + "AND DEGREE BETWEEN ? AND ? "
            + getPagingQuery(),

            new QueryParam[]{
                new QueryParam("UID_MIN", Integer.class),
                new QueryParam("UID_MAX", Integer.class),
                new QueryParam("CODE", String.class),
                new QueryParam("PRED_FEATURE_MIN", Integer.class),
                new QueryParam("PRED_FEATURE_MAX", Integer.class),
                new QueryParam("DEP_FEATURE_MIN", Integer.class),
                new QueryParam("DEP_FEATURE_MAX", Integer.class),
                new QueryParam("ALGORITHM", String.class),
                new QueryParam("CREATED_BY", String.class),
                new QueryParam("DATASET_URI", String.class),
                new QueryParam("STATUS", String.class),

                new QueryParam("GAMMA_MIN", Double.class),
                new QueryParam("GAMMA_MAX", Double.class),
                new QueryParam("EPSILON_MIN", Double.class),
                new QueryParam("EPSILON_MAX", Double.class),
                new QueryParam("COST_MIN", Double.class),
                new QueryParam("COST_MAX", Double.class),
                new QueryParam("COEFF0_MIN", Double.class),
                new QueryParam("COEFF0_MAX", Double.class),
                new QueryParam("TOLERANCE_MIN", Double.class),
                new QueryParam("TOLERANCE_MAX", Double.class),
                new QueryParam("CACHESIZE_MIN", Integer.class),
                new QueryParam("CACHESIZE_MAX", Integer.class),
                new QueryParam("KERNEL", String.class),
                new QueryParam("DEGREE_MIN", Integer.class),
                new QueryParam("DEGREE_MAX", Integer.class),

                new QueryParam("OFFSET", Integer.class),
                new QueryParam("ROWS", Integer.class)
            }),

     /**
     * Searches in the QSARModels and SVMModels tables for models that comply to the
     * specified QSARModel and SVMModel parameters. If a parameter is not specified
     * it will be ignored in the search.
     * @return Only UID column of the specified models
     */
    SEARCH_QSAR_MODEL_SKROUTZ("SELECT " + QSARModelsTable.TABLE.getTableName() + ".UID" +" FROM " + QSARModelsTable.TABLE.getTableName() +
            " LEFT OUTER JOIN " + SupportVecTable.TABLE.getTableName() + " ON "
            + QSARModelsTable.TABLE.getTableName() +".UID=" +SupportVecTable.TABLE.getTableName()+".UID "+
           "WHERE " + QSARModelsTable.TABLE.getTableName() + ".UID BETWEEN ? AND ? "
            + "AND CODE LIKE ? "
            + "AND PREDICTION_FEATURE BETWEEN ? AND ? "
            + "AND DEPENDENT_FEATURE BETWEEN ? AND ? "
            + "AND ALGORITHM LIKE ? "
            + "AND CREATED_BY LIKE ? "
            + "AND DATASET_URI LIKE ? "
            + "AND STATUS LIKE ? "

            + "AND GAMMA BETWEEN ? AND ? "
            + "AND EPSILON BETWEEN ? AND ? "
            + "AND COST BETWEEN ? AND ? "
            + "AND COEFF0 BETWEEN ? AND ? "
            + "AND TOLERANCE BETWEEN ? AND ? "
            + "AND CACHESIZE BETWEEN ? AND ? "
            + "AND KERNEL LIKE ? "
            + "AND DEGREE BETWEEN ? AND ? "
            + getPagingQuery(),

            new QueryParam[]{
                new QueryParam("UID_MIN", Integer.class),
                new QueryParam("UID_MAX", Integer.class),
                new QueryParam("CODE", String.class),
                new QueryParam("PRED_FEATURE_MIN", Integer.class),
                new QueryParam("PRED_FEATURE_MAX", Integer.class),
                new QueryParam("DEP_FEATURE_MIN", Integer.class),
                new QueryParam("DEP_FEATURE_MAX", Integer.class),
                new QueryParam("ALGORITHM", String.class),
                new QueryParam("CREATED_BY", String.class),
                new QueryParam("DATASET_URI", String.class),
                new QueryParam("STATUS", String.class),

                new QueryParam("GAMMA_MIN", Double.class),
                new QueryParam("GAMMA_MAX", Double.class),
                new QueryParam("EPSILON_MIN", Double.class),
                new QueryParam("EPSILON_MAX", Double.class),
                new QueryParam("COST_MIN", Double.class),
                new QueryParam("COST_MAX", Double.class),
                new QueryParam("COEFF0_MIN", Double.class),
                new QueryParam("COEFF0_MAX", Double.class),
                new QueryParam("TOLERANCE_MIN", Double.class),
                new QueryParam("TOLERANCE_MAX", Double.class),
                new QueryParam("CACHESIZE_MIN", Integer.class),
                new QueryParam("CACHESIZE_MAX", Integer.class),
                new QueryParam("KERNEL", String.class),
                new QueryParam("DEGREE_MIN", Integer.class),
                new QueryParam("DEGREE_MAX", Integer.class),

                new QueryParam("OFFSET", Integer.class),
                new QueryParam("ROWS", Integer.class)
            }),

     /**
     * Searches in the Omega table for models that comply to the
     * specified parameters. If a parameter is not specified
     * it will be ignored in the search.
     * @return All information available on the specified models
     */
    SEARCH_OMEGA("SELECT * FROM " + OmegaTable.TABLE.getTableName() + " " +
            "WHERE UID BETWEEN ? AND ? " +
            "AND CODE LIKE ? " +
            "AND CREATED_BY LIKE ? " +
            "AND DATASET_URI LIKE ? " +
            getPagingQuery(),

            new QueryParam[]{
                new QueryParam("UID_MIN", Integer.class),
                new QueryParam("UID_MAX", Integer.class),
                new QueryParam("CODE", String.class),
                new QueryParam("CREATED_BY", String.class),
                new QueryParam("DATASET_URI", String.class),
                
                new QueryParam("OFFSET", Integer.class),
                new QueryParam("ROWS", Integer.class)
          }),

     /**
     * Searches in the Omega table for models that comply to the
     * specified parameters. If a parameter is not specified
     * it will be ignored in the search.
     * @return Only UID column of the specified models
     */
     SEARCH_OMEGA_SKROUTZ("SELECT UID FROM " + OmegaTable.TABLE.getTableName() + " " +
            "WHERE UID BETWEEN ? AND ? " +
            "AND CODE LIKE ? " +
            "AND CREATED_BY LIKE ? " +
            "AND DATASET_URI LIKE ? " +
            getPagingQuery(),

            new QueryParam[]{
                new QueryParam("UID_MIN", Integer.class),
                new QueryParam("UID_MAX", Integer.class),
                new QueryParam("CODE", String.class),
                new QueryParam("CREATED_BY", String.class),
                new QueryParam("DATASET_URI", String.class),

                new QueryParam("OFFSET", Integer.class),
                new QueryParam("ROWS", Integer.class)
          }),


    /**
     * Searches in the Tasks table for tasks that comply to the
     * specified parameters. If a parameter is not specified
     * it will be ignored in the search.
     * @return All information available on the specified tasks
     */
    SEARCH_TASK("SELECT * FROM " + TasksTable.TABLE.getTableName() +" "+
            "WHERE NAME LIKE ? " +
            "AND STATUS LIKE ? " +
            "AND CREATED_BY LIKE ? " +
            "AND ALGORITHM LIKE ? " +
            "AND HTTPSTATUS BETWEEN ? AND ? " +
            "AND RESULT LIKE ? " +
            "AND DURATION BETWEEN ? AND ? " +
            getPagingQuery(),

       new QueryParam[]{
                new QueryParam("NAME", String.class),
                new QueryParam("STATUS", String.class),
                new QueryParam("CREATED_BY", String.class),
                new QueryParam("ALGORITHM", String.class),
                new QueryParam("HTTPSTATUS_MIN", Integer.class),
                new QueryParam("HTTPSTATUS_MAX", Integer.class),
                new QueryParam("RESULT", String.class),
                new QueryParam("DURATION_MIN", Integer.class),
                new QueryParam("DURATION_MAX", Integer.class),

                new QueryParam("OFFSET", Integer.class),
                new QueryParam("ROWS", Integer.class)
          }),

     /**
     * Searches in the Tasks table for tasks that comply to the
     * specified parameters. If a parameter is not specified
     * it will be ignored in the search.
     * @return Only NAME column of the specified tasks
     */
     SEARCH_TASK_SKROUTZ("SELECT NAME FROM " + TasksTable.TABLE.getTableName() +" "+
            "WHERE NAME LIKE ? " +
            "AND STATUS LIKE ? " +
            "AND CREATED_BY LIKE ? " +
            "AND ALGORITHM LIKE ? " +
            "AND HTTPSTATUS BETWEEN ? AND ? " +
            "AND RESULT LIKE ? " +
            "AND DURATION BETWEEN ? AND ? " +
            getPagingQuery(),

       new QueryParam[]{
                new QueryParam("NAME", String.class),
                new QueryParam("STATUS", String.class),
                new QueryParam("CREATED_BY", String.class),
                new QueryParam("ALGORITHM", String.class),
                new QueryParam("HTTPSTATUS_MIN", Integer.class),
                new QueryParam("HTTPSTATUS_MAX", Integer.class),
                new QueryParam("RESULT", String.class),
                new QueryParam("DURATION_MIN", Integer.class),
                new QueryParam("DURATION_MAX", Integer.class),

                new QueryParam("OFFSET", Integer.class),
                new QueryParam("ROWS", Integer.class)
          }),



    /**
     * ****************************************************************************
     * ----------------------------------------------------------------------------
     *                      GET QUERIES
     * ----------------------------------------------------------------------------
     * ****************************************************************************
     */
    
    
    /**
     *
     * For a certain Algorithm, retrieve all algorithm ontologies, i.e. the algorithm
     * types of this algorithm. The SQL Command is: <code>SELECT ALGORITHM_ONTOLOGIES.* FROM
     * ALGORITHM_ONTOLOGIES INNER JOID ALG_ONT_RELATION ON NAME = ONTOLOGY WHERE ALGORITHM
     * = ?</code>.
     */
    GET_ALGORITHM_ONTOLOGY_RELATION("SELECT " + AlgOntTable.TABLE.getTableName()+ ".*"
            + " FROM " + AlgOntTable.TABLE.getTableName()
            + " INNER JOIN " +AlgOntRelationTable.TABLE.getTableName()
            + " ON NAME=ONTOLOGY" + " WHERE ALGORITHM=?"
            + getPagingQuery(),
    new QueryParam[]{
        new QueryParam("ALGORITHM", String.class),
        new QueryParam("OFFSET", Integer.class),
        new QueryParam("ROWS", Integer.class)
    }),
    /**
     *
     * Retrieve all algorithms that have a certain ontological type.
     */
    GET_ONTOLOGY_ALGORITHM_RELATION("SELECT " + AlgorithmsTable.TABLE.getTableName() + ".*"
            + " FROM " + AlgorithmsTable.TABLE.getTableName()
            + " INNER JOIN " + AlgOntRelationTable.TABLE.getTableName()
            + " ON NAME=ALGORITHM"
            + " WHERE ONTOLOGY=?"
            + getPagingQuery(),
    new QueryParam[]{
        new QueryParam("ONTOLOGY", String.class),
        new QueryParam("OFFSET", Integer.class),
        new QueryParam("ROWS", Integer.class)
    }),
    
    /**
     *
     * Retrieve all algorithms.
     */
    GET_ALGORITHMS("SELECT * FROM " + AlgorithmsTable.TABLE.getTableName(), new QueryParam[]{}),
    

    /**
     * Retrieve all Independent Features for a given model uid.
     */
    GET_INDEP_FEATURES("SELECT " + FeaturesTable.TABLE.getTableName() + ".*"
    + " FROM " + FeaturesTable.TABLE.getTableName() + " INNER JOIN " + IndFeatRelationTable.TABLE.getTableName()
    + " ON UID=FEATURE_UID" + " WHERE MODEL_UID=?",
    new QueryParam[]{
        new QueryParam("MODEL_UID", Integer.class)
    });

    
    /**
     * The SQL command for the preparation of the statement.
     */
    private String sql;
    private QueryParam[] parameters;

    /**
     * The paging query extension adds to every query the ability to
     * get a specific page from the database, given an Offset and a
     * number of Rows to get.
     */
    private static final String pagingQuery = " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";

    private PrepStmt(String SQL, QueryParam[] pl) {
        this.sql = SQL;
        this.parameters = pl;
    }

    private static String getPagingQuery(){
        return pagingQuery;
    }

    public String getSql() {
        return this.sql;
    }

    public QueryParam[] getParameters() {
        return this.parameters;
    }
}

    

    

    

