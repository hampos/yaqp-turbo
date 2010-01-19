package org.opentox.db.table;

import java.util.ArrayList;
import org.opentox.db.util.SQLDataTypes;

/**
 *
 * This is an enumeration of some standard tables in the database of YAQP. All these 
 * tables should exist in the database, otherwise are created on startup.
 *
 * @author Sopasakis Pantelis
 * @author Charalampos Chomenides
 *
 * @see TableCreator
 */
public enum StandardTables {

    /**
     * Every algorithm in opentox is an (ontological) instance of some
     * Class in the corresponding ontology. Formally this ontology is described
     * by the OWL file algorithmTypes.owl distributed with this project.     
     */
    ALGORITHM_ONTOLOGIES(
    "ALGORITHM_ONTOLOGIES",
    "UID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
    + "NAME VARCHAR(40) NOT NULL, "
    + "URI VARCHAR(200) UNIQUE NOT NULL",
    true),
    /**
     * Levels of authorization
     */
    USER_AUTH(
    "USER_AUTH",
    "UID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
    + "NAME VARCHAR(50) NOT NULL, "
    + "USER_LEVEL INT UNIQUE DEFAULT 0",
    true),
    /**
     * Available algorithms
     */
    ALGORITHMS(
    "ALGORITHMS",
    "UID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
    + "NAME VARCHAR(20) NOT NULL, "
    + "URI VARCHAR(80) UNIQUE NOT NULL",
    true),
    /**
     * User data including username, password (as SHA-512), first and last name, email
     * and other optional information
     */
    USERS(
    "USERS",
    "UID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
    + "USERNAME VARCHAR(20) UNIQUE NOT NULL,"
    + "PASS VARCHAR(255) NOT NULL,"
    + "FIRSTNAME VARCHAR(50) NOT NULL,"
    + "LASTNAME VARCHAR(50) NOT NULL,"
    + "EMAIL VARCHAR(50) UNIQUE NOT NULL,"
    + "ORGANIZATION VARCHAR(50),"
    + "COUNTRY VARCHAR(20), "
    + "CITY VARCHAR(20),"
    + "ADDRESS VARCHAR(80),"
    + "WEBPAGE VARCHAR(150),"
    + "ROLE INT,"
    + "TMSTMP TIMESTAMP DEFAULT CURRENT TIMESTAMP,"
    + "FOREIGN KEY(ROLE) REFERENCES USER_AUTH(UID) ON DELETE CASCADE",
    true),
    /**
     * Table of features. Normally this table contains features which lay on
     * other servers but where used to generate models on this server.
     */
    FEATURES(
    "FEATURES",
    "UID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
    + "URI VARCHAR(255) UNIQUE NOT NULL",
    true),
    /**
     * The set of prediction models characterized by the name of the model, its URI,
     * its prediction and dependent feature, the corresponding algorithm that was
     * used to produced the model and the user that triggered the model creation.
     */
    PREDICTION_MODELS(
    "PREDICTION_MODELS",
    "UID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
    + "NAME VARCHAR(255) UNIQUE NOT NULL, "
    + "URI VARCHAR(255) UNIQUE NOT NULL, "
    + "PREDICTION_FEATURE INT NOT NULL, "
    + "DEPENDENT_FEATURE INT NOT NULL, "
    + "ALGORITHM INT NOT NULL, "
    + "CREATED_BY INT NOT NULL, "
    + "TMSTMP TIMESTAMP DEFAULT CURRENT TIMESTAMP,"
    + "FOREIGN KEY (ALGORITHM) REFERENCES ALGORITHMS(UID) ON DELETE CASCADE, "
    + "FOREIGN KEY (PREDICTION_FEATURE) REFERENCES FEATURES(UID) ON DELETE CASCADE, "
    + "FOREIGN KEY (DEPENDENT_FEATURE) REFERENCES FEATURES(UID) ON DELETE CASCADE, "
    + "FOREIGN KEY (CREATED_BY) REFERENCES USERS(UID) ON DELETE CASCADE",
    true),
    /**
     * Table of all MLR models - child of {@link StandardTables#PREDICTION_MODELS Prediction Models }
     */
    MLR_MODELS(
    "MLR_MODELS",
    "UID INT NOT NULL,"
    + "NAME VARCHAR(255) UNIQUE NOT NULL,"
    + "URI VARCHAR(255) UNIQUE NOT NULL, "
    + "DATASET VARCHAR(255) NOT NULL,"
    + "PRIMARY KEY (UID),"
    + "FOREIGN KEY (UID) REFERENCES PREDICTION_MODELS(UID) ON DELETE CASCADE",
    true),
    /**
     * Table of all SVM models - child of {@link StandardTables#PREDICTION_MODELS Prediction Models }
     */
    SVM_MODELS(
    "SVM_MODELS",
    "UID INT NOT NULL PRIMARY KEY, "
    + "DATASET VARCHAR(255) NOT NULL, "
    + "GAMMA FLOAT DEFAULT 1.50 CONSTRAINT GAMMA_CONSTRAINT CHECK (GAMMA >= 0.000), "
    + "EPSILON FLOAT DEFAULT 0.10 CONSTRAINT EPSILON_CONSTRAINT CHECK (EPSILON > 0.00), "
    + "COST FLOAT DEFAULT 100  CONSTRAINT COST_CONSTRAINT CHECK (COST > 0.00), "
    + "TOLERANCE FLOAT DEFAULT 0.001 CONSTRAINT TOLERANCE_CONSTRAINT CHECK (TOLERANCE > 0.00), "
    + "CACHESIZE INT DEFAULT 50000 CONSTRAINT CACHE_CONSTRAINT CHECK (CACHESIZE < 5000000), "
    + "KERNEL VARCHAR(10) DEFAULT 'RBF' CONSTRAINT KERNEL_CONTRAINT CHECK (KERNEL IN ('RBF', 'POLYNOMIAL', 'SIGMOID', 'LINEAR')), "
    + "DEGREE INT DEFAULT 3 CONSTRAINT DEGREE_CONSTRAINT CHECK (DEGREE >= 1),"
    + "FOREIGN KEY (UID) REFERENCES PREDICTION_MODELS(UID) ON DELETE CASCADE ",
    true),
    /**
     * Table of all SVC models - child of {@link StandardTables#PREDICTION_MODELS Prediction Models }
     */
    SVC_MODELS(
    "SVC_MODELS",
    "UID INT NOT NULL PRIMARY KEY, "
    + "DATASET VARCHAR(255) NOT NULL, "
    + "GAMMA FLOAT DEFAULT 1.50 CONSTRAINT GAMMA_C CHECK (GAMMA >= 0.000), "
    + "COST FLOAT DEFAULT 100  CONSTRAINT COST_C CHECK (COST > 0.00), "
    + "TOLERANCE FLOAT DEFAULT 0.001 CONSTRAINT TOLERANCE_C CHECK (TOLERANCE > 0.00), "
    + "CACHESIZE INT DEFAULT 50000 CONSTRAINT CACHE_C CHECK (CACHESIZE < 5000000), "
    + "KERNEL VARCHAR(10) DEFAULT 'RBF' CONSTRAINT KERNEL_C CHECK (KERNEL IN ('RBF', 'POLYNOMIAL', 'SIGMOID', 'LINEAR')), "
    + "DEGREE INT DEFAULT 3 CONSTRAINT DEGREE_C CHECK (DEGREE >= 1),"
    + "FOREIGN KEY (UID) REFERENCES PREDICTION_MODELS(UID) ON DELETE CASCADE ",
    true),
    /**
     * Relation between independent features and models. This is a many-to-many relation
     * in the sense that one model has lots of independent features and in turn some
     * feature may be an independent feature in a collection of models. In the database
     * this is materialized by a table with two columns with foreign keys - one pointing
     * to a model and the other pointing to a feature.
     *
     */
    INDEPENDENT_FETAURES_RELATION(
    "INDEPENDENT_FETAURES_RELATION",
    "MODEL_UID INT NOT NULL,"
    + "FEATURE_UID INT NOT NULL,"
    + "FOREIGN KEY (MODEL_UID) REFERENCES PREDICTION_MODELS(UID) ON DELETE CASCADE,"
    + "FOREIGN KEY (FEATURE_UID) REFERENCES FEATURES(UID) ON DELETE CASCADE",
    true),
    /**
     * Every algorithm is an instance of one or more (ontological) classes/categories.
     * So algorithmsa and algorithm ontologies possess a many-to-many relationship. This is
     * visualized in the database by this table which has two columns each one of which
     * is a foreign key - one pointing to algorithms and one to algorithm ontologies.
     */
    ALGORITHM_ONTOL_RELATION(
    "ALGORITHM_ONTOL_RELATION",
    "ALGORITHM_UID INT NOT NULL,"
    + "ONTOLOGY_UID INT NOT NULL,"
    + "FOREIGN KEY (ALGORITHM_UID) REFERENCES ALGORITHMS(UID), "
    + "FOREIGN KEY (ONTOLOGY_UID) REFERENCES ALGORITHM_ONTOLOGIES(UID)",
    true),
    /**
     * Tasks running on the server. A task is characterized by its name, URI,
     * its status (one of 'running', 'cancelled', 'completed' ), the user that
     * generated the task and the algorithm to which the task is pointing. TMSTMP is
     * the timestamp of the task creation. Using this value, one can estimate the
     * completion time of the task.
     */
    TASKS(
    "TASKS",
    "UID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
    + "NAME VARCHAR(80) UNIQUE NOT NULL,"
    + "URI VARCHAR(255) UNIQUE NOT NULL,"
    + "STATUS VARCHAR(80) NOT NULL DEFAULT 'RUNNING' CONSTRAINT TASK_STATUS_C CHECK (STATUS IN ('RUNNING', 'COMPLETED','CANCELLED')),"
    + "CREATED_BY INT NOT NULL DEFAULT 1,"
    + "ALGORITHM INT NOT NULL,"
    + "TMSTMP TIMESTAMP DEFAULT CURRENT TIMESTAMP,"
    + "FOREIGN KEY (CREATED_BY) REFERENCES USERS(UID),"
    + "FOREIGN KEY (ALGORITHM) REFERENCES ALGORITHMS(UID) ",
    true);
    private String TABLE_NAME;
    private String TABLE_STRUCTURE;
    private boolean toBeIncluded;
    private Table table;

    /**
     * Private constructor for the initialization of a new standard table in the
     * database.
     * @param tableName The name of the table.
     * @param tableStructure Structure of the table written in SQL
     * @param toBeIncluded If the specific table is to be included in the database
     * or is it just design for future use or creation on demand.
     */
    private StandardTables(String tableName, String tableStructure, boolean toBeIncluded) {
        this.TABLE_NAME = tableName;
        this.TABLE_STRUCTURE = tableStructure;
        this.toBeIncluded = toBeIncluded;
    }

    private StandardTables(Table table) {
        this.table = table;
    }

    /**
     *
     * @return name of the table
     */
    public String getTableName() {
        return TABLE_NAME;
    }

    /**
     *
     * @return structure of the table in SQL
     */
    public String getTableStructure() {
        return TABLE_STRUCTURE;
    }

    /**
     * Whether the table is to be included in the database.
     * @return
     */
    public boolean is2Bincluded() {
        return toBeIncluded;
    }

    private static String getSQL(Table talbe) {
        String SQL = "CREATE TABLE " + talbe.getTableName() + "(";
        ArrayList<TableColumn> colList = new ArrayList<TableColumn>();
        colList = talbe.getTableColumns();
        TableColumn temp;
        for (int i = 0; i < colList.size(); i++) {
            temp = colList.get(i);
            SQL = SQL + temp.getColumnName() + " " + temp.getColumnType().toString();
            if (temp.isUnique()) {
                SQL = SQL + " UNIQUE";
            }
            if (temp.isNotNull()) {
                SQL = SQL + " NOT NULL";
            }
            if (temp.isPrimaryKey()) {
                SQL = SQL + " PRIMARY KEY ";
            }


            SQL = SQL + temp.getDefaultValue() ;
            
            
             if (temp.isConstrained()){
            SQL = SQL + temp.getConstraint()+ ",";
            }
            
            if (temp.isForeignKey()) {
                SQL = SQL + "\n" + temp.getForeignKey();
            }
            if (i != colList.size() - 1) {
                SQL = SQL + ",";
            }
        }
        SQL = SQL + ")";
        return SQL;
    }

    public static void main(String[] args){
        TableColumn uid = new TableColumn();
        uid.setColumnName("UID");
        uid.setColumnType(SQLDataTypes.Int());
        uid.setNotNull(true);
        uid.setUnique(true);
        uid.setPrimaryKey(true, true);
        TableColumn name = new TableColumn();
        name.setColumnName("NAME");
        name.setColumnType(SQLDataTypes.VarChar(40));
        name.setNotNull(true);
        ArrayList<TableColumn> listOfCols = new ArrayList<TableColumn>();
        listOfCols.add(uid);
        listOfCols.add(name);
        Table my_table = new Table();
        my_table.setTableColumns(listOfCols);
        my_table.setTableName("MY_TABLE");
        System.out.println(getSQL(my_table));
    }
}
