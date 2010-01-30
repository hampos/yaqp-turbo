package org.opentox.db.util;

import org.opentox.db.interfaces.JPrepStmt;
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
     * Add a new Algorithm Ontology in the database.
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
     * stored.
     *
     * @see PrepStmt#ADD_ALGORITHM Add a new Algorithm
     */
    ADD_ALGORITHM_ONTOL_RELATION(
    "INSERT INTO " + AlgorithmOntolRelation().getTableName() + " (ALGORITHM_NAME, ONTOLOGY_NAME ) VALUES (?,?)",
    new QueryParam[]{
                        new QueryParam("ALGORITHM_NAME", String.class),
                        new QueryParam("ONTOLOGY_NAME", String.class)

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
     * Add a new feature in the database.
     */
    ADD_FEATURE("INSERT INTO " + Features().getTableName() + " (URI) VALUES (?)",
    new QueryParam[]{
            new QueryParam("URI", String.class)
                    }
    ),


            
    GET_USERS("SELECT * FROM "+Users().getTableName(), null),

    GET_ALGORITHM_ONTOLOGIES("SELECT * FROM "+AlgorithmOntologies().getTableName(), null),

    GET_ALGORITHM_ONTOLOGY_RELATION("SELECT "+AlgorithmOntologies().getTableName()+".*" +
            " FROM "+AlgorithmOntologies().getTableName()+" INNER JOIN "+AlgorithmOntolRelation().getTableName()+
            " ON NAME=ONTOLOGY_NAME"+" WHERE ALGORITHM_NAME=?",
    new QueryParam[]{
                        new QueryParam("ALGORITHM_NAME",String.class)
                    }
    ),

    GET_ONTOLOGY_ALGORITHM_RELATION("SELECT "+Algorithms().getTableName()+".*" +
            " FROM "+Algorithms().getTableName()+" INNER JOIN "+AlgorithmOntolRelation().getTableName()+
            " ON NAME=ALGORITHM_NAME"+" WHERE ONTOLOGY_NAME=?",
    new QueryParam[]{
                        new QueryParam("ONTOLOGY_NAME",String.class)
                    }
    ),

    GET_ALGORITHM_ONTOLOGY_RELATIONS("SELECT * FROM "+AlgorithmOntolRelation().getTableName(), null),

    GET_USER_GROUPS("SELECT * FROM "+UserAuth().getTableName(), null),

    GET_ALGORITHMS("SELECT * FROM "+Algorithms().getTableName(), null),

    GET_PRED_MODELS("SELECT * FROM "+PredictionModels().getTableName(), null),

    GET_MLR_MODELS("SELECT * FROM "+MlrModels().getTableName(), null),

    GET_SVM_MODELS("SELECT * FROM "+SvmModels().getTableName(), null),

    GET_SVC_MODELS("SELECT * FROM "+SvcModels().getTableName(), null),

    GET_FEATURES("SELECT * FROM "+Features().getTableName(), null);

//    GET_USERS("SELECT * FROM "+Users().getTableName(), null),
//    GET_USERS("SELECT * FROM "+Users().getTableName(), null),
//    GET_USERS("SELECT * FROM "+Users().getTableName(), null),
//    GET_USERS("SELECT * FROM "+Users().getTableName(), null)


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

    

    

    

