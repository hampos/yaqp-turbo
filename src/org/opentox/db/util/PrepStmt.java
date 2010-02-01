/*
 * YAQP - Yet Another QSAR Project: Machine Learning algorithms designed for
 * the prediction of toxicological features of chemical compounds become
 * available on the Web. Yaqp is developed under OpenTox (http://opentox.org)
 * which is an FP7-funded EU research project.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
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
 */
package org.opentox.db.util;

import org.opentox.db.interfaces.JPrepStmt;
import org.opentox.db.queries.QueryFood;
import org.opentox.db.queries.QueryParam;
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
    "INSERT INTO " + AlgorithmOntologies().getTableName()
    + " (NAME, URI ) VALUES (?,?)",
        //    new QueryParamp[]
    new QueryParam[]    {
                            new QueryParam("NAME", String.class),
                            new QueryParam("URI", String.class)
                        }
    ),

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
    "INSERT INTO " + AlgorithmOntolRelation().getTableName() + " ( ALGORITHM, ONTOLOGY) VALUES (?,?)",
    new QueryParam[]{
                        new QueryParam("ALGORITHM", String.class),
                        new QueryParam("ONTOLOGY", String.class)

                    }
   ),

   /**
    * Add a new user into the database; you need to provide the username, password
    * and other information about the user including the user group.
    *
    * @see PrepStmt#ADD_USER_GROUP Add a UserGroup
    */
    ADD_USER(
    "INSERT INTO " + Users().getTableName()
    + " ( USERNAME, PASS, FIRSTNAME, LASTNAME, EMAIL, ORGANIZATION, COUNTRY, CITY, ADDRESS, WEBPAGE, ROLE) VALUES (?,?,?,?,?,?,?,?,?,?,?)",
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
    ADD_USER_GROUP("INSERT INTO " + UserAuth().getTableName() + "(NAME, USER_LEVEL) VALUES (?,?)",
    new QueryParam[]{
                        new QueryParam("NAME", String.class),
                        new QueryParam("USER_LEVEL", Integer.class),
    }),
    /**
     * Add a new Algorithm in the database.
     */
    ADD_ALGORITHM("INSERT INTO " + Algorithms().getTableName() + " (NAME, URI) VALUES (?,?)",
    new QueryParam[]{
                        new QueryParam("NAME", String.class),
                        new QueryParam("URI", String.class)
                    }
    ),

    /**
     * Add a new prediction model in the database.
     */
    ADD_PRED_MODEL("INSERT INTO " + PredictionModels().getTableName()
    + " (NAME, URI, PREDICTION_FEATURE, DEPENDENT_FEATURE, ALGORITHM, CREATED_BY ) VALUES (?,?,?,?,?,?)",
    new QueryParam[]{
                        new QueryParam("NAME",String.class),
                        new QueryParam("URI",String.class),
                        new QueryParam("PREDICTION_FEATURE", Integer.class),
                        new QueryParam("DEPENDENT_FEATURE",Integer.class),
                        new QueryParam("ALGORITHM",String.class),
                        new QueryParam("CREATED_BY",String.class)
                    }
    ),
    /**
     * Add a new MLR model in the database.
     */
    ADD_MLR_MODEL("INSERT INTO " + MlrModels().getTableName() + " (DATASET) VALUES (?)",
    new QueryParam[]{
                        new QueryParam("DATASET", String.class)
                    }
    ),
    /**
     *
     */
    ADD_SVM_MODEL("INSERT INTO "+SvmModels().getTableName()+
            " (DATASET, GAMMA, EPSILON, COST, BIAS, TOLERANCE, CACHE, KERNEL, DEGREE ) VALUES (?,?,?,?,?,?,?,?,?)",
    new QueryParam[]{
                        new QueryParam("DATASET", String.class),
                        new QueryParam("GAMMA", Double.class),
                        new QueryParam("EPSILON", Double.class),
                        new QueryParam("COST", Double.class),
                        new QueryParam("BIAS", Double.class),
                        new QueryParam("TOLERANCE", Double.class),
                        new QueryParam("CACHE", Double.class),
                        new QueryParam("KERNEL", String.class),
                        new QueryParam("DEGREE", Integer.class)
                    }
    ),
    /**
     *
     */
    ADD_SVC_MODEL("INSERT INTO "+SvcModels().getTableName()+
            " (DATASET, GAMMA, COST, BIAS, TOLERANCE, CACHE, KERNEL, DEGREE ) VALUES (?,?,?,?,?,?,?,?)",
    new QueryParam[]{
                        new QueryParam("DATASET", String.class),
                        new QueryParam("GAMMA", Double.class),
                        new QueryParam("COST", Double.class),
                        new QueryParam("BIAS", Double.class),
                        new QueryParam("TOLERANCE", Double.class),
                        new QueryParam("CACHE", Double.class),
                        new QueryParam("KERNEL", String.class),
                        new QueryParam("DEGREE", Integer.class)
                    }
    ),
    /**
     *
     * Add a new feature in the database. The <code>URI</code> of the feature has to
     * be provided in the <code>QueryFood<code>.
     */
    ADD_FEATURE("INSERT INTO " + Features().getTableName() + " (URI) VALUES (?)",
    new QueryParam[]{
            new QueryParam("URI", String.class)
                    }
    ),
    /**
     *
     * A Prepared Statement to retrieve all users from the database. The SQL command
     * is: <code>SELECT * FROM USERS</code>.
     */
    GET_USERS("SELECT * FROM "+Users().getTableName(), null),
    /**
     *
     * Prepared statement used to retrieve all algorithm ontologies in the database.
     * The SQL command is: <code>SELECT * FROM ALGORITHM_ONTOLOGIES</code>
     */
    GET_ALGORITHM_ONTOLOGIES("SELECT * FROM "+AlgorithmOntologies().getTableName(), null),

    /**
     *
     * For a certain Algorithm, retrieve all algorithm ontologies, i.e. the algorithm
     * types of this algorithm. The SQL Command is: <code>SELECT ALGORITHM_ONTOLOGIES.* FROM
     * ALGORITHM_ONTOLOGIES INNER JOID ALG_ONT_RELATION ON NAME = ONTOLOGY WHERE ALGORITHM
     * = ?</code>.
     */
    GET_ALGORITHM_ONTOLOGY_RELATION("SELECT "+AlgorithmOntologies().getTableName()+".*" +
            " FROM "+AlgorithmOntologies().getTableName()+" INNER JOIN "+AlgorithmOntolRelation().getTableName()+
            " ON NAME=ONTOLOGY"+" WHERE ALGORITHM=?",
    new QueryParam[]{
                        new QueryParam("ALGORITHM",String.class)
                    }
    ),
    /**
     *
     * Retrieve all algorithms that hava a certain ontological type.
     */
    GET_ONTOLOGY_ALGORITHM_RELATION("SELECT "+Algorithms().getTableName()+".*" +
            " FROM "+Algorithms().getTableName()+" INNER JOIN "+AlgorithmOntolRelation().getTableName()+
            " ON NAME=ALGORITHM"+" WHERE ONTOLOGY=?",
    new QueryParam[]{
                        new QueryParam("ONTOLOGY",String.class)
                    }
    ),
    /**
     *
     * Retrieve all algorithm-ontology relations.
     */
    GET_ALGORITHM_ONTOLOGY_RELATIONS("SELECT * FROM "+AlgorithmOntolRelation().getTableName(), null),
    /**
     *
     * Get all user groups.
     */
    GET_USER_GROUPS("SELECT * FROM "+UserAuth().getTableName(), null),
    /**
     *
     * Retrieve all algorithms.
     */
    GET_ALGORITHMS("SELECT * FROM "+Algorithms().getTableName(), new QueryParam[]{}),
    /**
     *
     * Get all prediction models from the database.
     */
    GET_PRED_MODELS("SELECT * FROM "+PredictionModels().getTableName(), null),
    /**
     *
     * Get all MLR models.
     */
    GET_MLR_MODELS("SELECT * FROM "+MlrModels().getTableName(), null),
    /**
     *
     * Get all SVM models
     */
    GET_SVM_MODELS("SELECT * FROM "+SvmModels().getTableName(), null),
    /**
     *
     * Get all SVC models.
     */
    GET_SVC_MODELS("SELECT * FROM "+SvcModels().getTableName(), null),

    GET_FEATURES("SELECT * FROM "+Features().getTableName(), null)

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

    

    

    

