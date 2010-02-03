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
package org.opentox.db.table;

import org.opentox.db.util.SQLDataTypes;
import org.opentox.db.util.TheDbConnector;

/**
 *
 * This is an enumeration of some standard tables in the database of YAQP. All these 
 * tables should exist in the database, otherwise are created on startup. The static
 * method {@link TheDbConnector#init() init()} in the class {@link TheDbConnector }
 * generated these tables if they do not already exist (this is the case when a new
 * database is to be generated)
 *
 * @author Sopasakis Pantelis
 * @author Charalampos Chomenides
 *
 * @see TableCreator create a table
 * @see TableDropper delete a table
 */
public enum StandardTables {

    /**
     * Every algorithm in opentox is an (ontological) instance of some
     * Class in the corresponding ontology. Formally this ontology is described
     * by the OWL file algorithmTypes.owl distributed with this project.     
     */
    ALGORITHM_ONTOLOGIES(AlgorithmOntologies(), true),
    /**
     * Levels of authorization
     */
    USER_AUTH(UserAuth(), true),
    /**
     * Available algorithms
     */
    ALGORITHMS(Algorithms(), true),
    /**
     * User data including username, password (as SHA-512), first and last name, email
     * and other optional information
     */
    USERS(Users(), true),
    /**
     * Table of features. Normally this table contains features which lay on
     * other servers but where used to generate models on this server.
     */
    FEATURES(Features(), true),
    /**
     * The set of prediction models characterized by the name of the model, its URI,
     * its prediction and dependent feature, the corresponding algorithm that was
     * used to produced the model and the user that triggered the model creation.
     */
    PREDICTION_MODELS(PredictionModels(), true),
    /**
     * Table of all MLR models - child of {@link StandardTables#PREDICTION_MODELS Prediction Models }
     */
    MLR_MODELS(MlrModels(), true),
    /**
     * Table of all SVM models - child of {@link StandardTables#PREDICTION_MODELS Prediction Models }
     */
    SVM_MODELS(SvmModels(), true),
    /**
     * Table of all SVC models - child of {@link StandardTables#PREDICTION_MODELS Prediction Models }
     */
    SVC_MODELS(SvcModels(), true),
    /**
     * Relation between independent features and models. This is a many-to-many relation
     * in the sense that one model has lots of independent features and in turn some
     * feature may be an independent feature in a collection of models. In the database
     * this is materialized by a table with two columns with foreign keys - one pointing
     * to a model and the other pointing to a feature.
     *
     */
    INDEPENDENT_FETAURES_RELATION(IndepFeaturesRelation(), true),
    /**
     * Every algorithm is an instance of one or more (ontological) classes/categories.
     * So algorithmsa and algorithm ontologies possess a many-to-many relationship. This is
     * visualized in the database by this table which has two columns each one of which
     * is a foreign key - one pointing to algorithms and one to algorithm ontologies.
     */
    ALGORITHM_ONTOL_RELATION(AlgorithmOntolRelation(), true),
    /**
     * Tasks running on the server. A task is characterized by its name, URI,
     * its status (one of 'running', 'cancelled', 'completed' ), the user that
     * generated the task and the algorithm to which the task is pointing. TMSTMP is
     * the timestamp of the task creation. Using this value, one can estimate the
     * completion time of the task.
     */
    TASKS(Tasks(), true),
    /**
     *
     */
    OMEGA(OmegaModels(), true); /* End of enumeration*/

    /**
     * Whether the table is to be included in the table creation
     */
    private boolean toBeIncluded;
    /**
     * Corresponding Table
     */
    private Table _TABLE;

    private StandardTables(Table table, boolean toBeIncluded) {
        this._TABLE = table;
        this.toBeIncluded = toBeIncluded;
    }

    /**
     * Whether the table is to be included in the database.
     * @return true if the table is to be included in the table creation procedure
     */
    public boolean is2Bincluded() {
        return toBeIncluded;
    }

    public Table getTable() {
        return _TABLE;
    }

    /**
     * "UID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
    + "NAME VARCHAR(40) NOT NULL, "
    + "URI VARCHAR(200) UNIQUE NOT NULL",
     * @return The Table for Algorithm Ontologies
     */
    public static Table AlgorithmOntologies() {
        Table alg_ont_table = new Table("ALGORITHM_ONTOLOGIES");

//        TableColumn uid = new TableColumn("UID");
//            uid.setColumnType(SQLDataTypes.Int());
//            uid.setNotNull(true);
//            uid.setPrimaryKey(true, true);

        TableColumn name = new TableColumn("NAME");
        name.setColumnType(SQLDataTypes.VarChar(40));
        name.setNotNull(true);
        name.setPrimaryKey(true, false);

        TableColumn uri = new TableColumn("URI");
        uri.setColumnType(SQLDataTypes.VarChar(200));
        uri.setUnique(true);

        //   alg_ont_table.addColumn(uid);
        alg_ont_table.addColumn(name);
        alg_ont_table.addColumn(uri);


        return alg_ont_table;
    }

    public static Table UserAuth() {
        Table user_auth_table = new Table("USER_AUTH");

        TableColumn name = new TableColumn("NAME");
        name.setPrimaryKey(true, false);
        name.setColumnType(SQLDataTypes.VarChar(40));
        name.setNotNull(true);

        TableColumn user_level = new TableColumn("USER_LEVEL");
        user_level.setColumnType(SQLDataTypes.Int());
        user_level.setDefaultValue("0");


        user_auth_table.addColumn(name);
        user_auth_table.addColumn(user_level);
        return user_auth_table;
    }

    public static Table Users() {
        Table table = new Table("USERS");

        TableColumn username = new TableColumn("USERNAME");
        username.setColumnType(SQLDataTypes.VarChar(20));
        username.setUnique(true);
        username.setNotNull(true);
        table.addColumn(username);

        TableColumn password = new TableColumn("PASS");
        password.setColumnType(SQLDataTypes.VarChar(255));
        password.setNotNull(true);
        table.addColumn(password);

        TableColumn firstname = new TableColumn("FIRSTNAME");
        firstname.setColumnType(SQLDataTypes.VarChar(50));
        firstname.setNotNull(true);
        table.addColumn(firstname);

        TableColumn lastname = new TableColumn("LASTNAME");
        lastname.setColumnType(SQLDataTypes.VarChar(50));
        lastname.setNotNull(true);
        table.addColumn(lastname);

        TableColumn email = new TableColumn("EMAIL");
        email.setColumnType(SQLDataTypes.VarChar(50));
        email.setNotNull(true);
        email.setPrimaryKey(true, false);
        table.addColumn(email);

        TableColumn organization = new TableColumn("ORGANIZATION");
        organization.setColumnType(SQLDataTypes.VarChar(50));
        table.addColumn(organization);

        TableColumn country = new TableColumn("COUNTRY");
        country.setColumnType(SQLDataTypes.VarChar(50));
        table.addColumn(country);


        TableColumn city = new TableColumn("CITY");
        city.setColumnType(SQLDataTypes.VarChar(50));
        table.addColumn(city);

        TableColumn address = new TableColumn("ADDRESS");
        address.setColumnType(SQLDataTypes.VarChar(80));
        table.addColumn(address);

        TableColumn webpage = new TableColumn("WEBPAGE");
        webpage.setColumnType(SQLDataTypes.VarChar(150));
        table.addColumn(webpage);

        TableColumn timestamp = new TableColumn("TMSTMP");
        timestamp.setColumnType(SQLDataTypes.Timestamp());
        timestamp.setDefaultValue("CURRENT TIMESTAMP");
        table.addColumn(timestamp);

        TableColumn role = new TableColumn("ROLE");
        role.setColumnType(SQLDataTypes.VarChar(40));
        role.setForeignKey(UserAuth().getTableName(), "NAME", true);
        table.addColumn(role);

        return table;
    }

    public static Table Algorithms() {
        Table table = new Table("ALGORITHMS");
//
//        TableColumn uid = new TableColumn("UID");
//          uid.setColumnType(SQLDataTypes.Int());
//          uid.setNotNull(true);
//          uid.setPrimaryKey(true, true);
//         table.addColumn(uid);

        TableColumn name = new TableColumn("NAME");
        name.setColumnType(SQLDataTypes.VarChar(40));
        name.setNotNull(true);
        name.setPrimaryKey(true, false);
        table.addColumn(name);

        TableColumn uri = new TableColumn("URI");
        uri.setColumnType(SQLDataTypes.VarChar(150));
        uri.setUnique(true);
        uri.setNotNull(true);
        table.addColumn(uri);

        return table;
    }

    public static Table Features() {
        Table table = new Table("FEATURES");

        TableColumn uid = new TableColumn("UID");
        uid.setColumnType(SQLDataTypes.Int());
        uid.setNotNull(true);
        uid.setPrimaryKey(true, true);
        table.addColumn(uid);

        TableColumn uri = new TableColumn("URI");
        uri.setColumnType(SQLDataTypes.VarChar(150));
        uri.setUnique(true);
        uri.setNotNull(true);
        table.addColumn(uri);

        return table;
    }

    public static Table PredictionModels() {
        Table PredictioModels_table = new Table("PREDICTION_MODELS");

        TableColumn uid = new TableColumn("UID");
        uid.setColumnType(SQLDataTypes.Int());
        uid.setNotNull(true);
        uid.setPrimaryKey(true, true);
        PredictioModels_table.addColumn(uid);

        TableColumn name = new TableColumn("NAME");
        name.setColumnType(SQLDataTypes.VarChar(40));
        name.setNotNull(true);
        PredictioModels_table.addColumn(name);

        TableColumn uri = new TableColumn("URI");
        uri.setColumnType(SQLDataTypes.VarChar(150));
        uri.setUnique(true);
        uri.setNotNull(true);
        PredictioModels_table.addColumn(uri);

        TableColumn prediction_feature = new TableColumn("PREDICTION_FEATURE");
        prediction_feature.setColumnType(SQLDataTypes.Int());
        prediction_feature.setNotNull(true);
        prediction_feature.setForeignKey(Features().getTableName(), "UID", true);
        PredictioModels_table.addColumn(prediction_feature);

        TableColumn dependent_feature = new TableColumn("DEPENDENT_FEATURE");
        dependent_feature.setColumnType(SQLDataTypes.Int());
        dependent_feature.setNotNull(true);
        dependent_feature.setForeignKey(Features().getTableName(), "UID", true);
        PredictioModels_table.addColumn(dependent_feature);

        TableColumn algorithm = new TableColumn("ALGORITHM");
        algorithm.setColumnType(SQLDataTypes.VarChar(40));
        algorithm.setNotNull(true);
        algorithm.setForeignKey(Algorithms().getTableName(), "NAME", true);
        PredictioModels_table.addColumn(algorithm);

        TableColumn createdBy = new TableColumn("CREATED_BY");
        createdBy.setColumnType(SQLDataTypes.VarChar(50));
        createdBy.setNotNull(true);
        createdBy.setForeignKey(Users().getTableName(), "EMAIL", true);
        PredictioModels_table.addColumn(createdBy);

        TableColumn timestamp = new TableColumn("TMSTMP");
        timestamp.setColumnType(SQLDataTypes.Timestamp());
        timestamp.setDefaultValue("CURRENT TIMESTAMP");
        PredictioModels_table.addColumn(timestamp);

        return PredictioModels_table;
    }

    public static Table MlrModels() {
        Table table = new Table("MLR_MODELS");

        TableColumn uid = new TableColumn("UID");
        uid.setColumnType(SQLDataTypes.Int());
        uid.setNotNull(true);
        uid.setPrimaryKey(true, false);
        uid.setForeignKey(PredictionModels().getTableName(), "UID", true);
        table.addColumn(uid);

        TableColumn dataset = new TableColumn("DATASET");
        dataset.setColumnType(SQLDataTypes.VarChar(150));
        dataset.setUnique(true);
        dataset.setNotNull(true);
        table.addColumn(dataset);

        return table;
    }

    public static Table SvmModels() {
        Table table = new Table("SVM_MODELS");

        TableColumn uid = new TableColumn("UID");
        uid.setColumnType(SQLDataTypes.Int());
        uid.setNotNull(true);
        uid.setPrimaryKey(true, false);
        uid.setForeignKey(PredictionModels().getTableName(), "UID", true);
        table.addColumn(uid);

        TableColumn dataset = new TableColumn("DATASET");
        dataset.setColumnType(SQLDataTypes.VarChar(150));
        dataset.setUnique(true);
        dataset.setNotNull(true);
        table.addColumn(dataset);

        TableColumn gamma = new TableColumn("GAMMA");
        gamma.setColumnType(SQLDataTypes.Float());
        gamma.setDefaultValue("1.50");
        gamma.setConstraint("GAMMA_CONSTRAINT", gamma.getColumnName() + " >=0");
        table.addColumn(gamma);

        TableColumn epsilon = new TableColumn("EPSILON");
        epsilon.setColumnType(SQLDataTypes.Float());
        epsilon.setDefaultValue("0.1");
        epsilon.setConstraint("EPSILON_CONSTRAINT", epsilon.getColumnName() + " > 0.0");
        table.addColumn(epsilon);

        TableColumn cost = new TableColumn("COST");
        cost.setColumnType(SQLDataTypes.Float());
        cost.setDefaultValue("100");
        cost.setConstraint("COST_CONSTRAINT", cost.getColumnName() + " > 0.0");
        table.addColumn(cost);


        TableColumn bias = new TableColumn("BIAS");
        bias.setColumnType(SQLDataTypes.Float());
        bias.setDefaultValue("0");
        table.addColumn(bias);

        TableColumn tolerance = new TableColumn("TOLERANCE");
        tolerance.setColumnType(SQLDataTypes.Float());
        tolerance.setDefaultValue("0.0001");
        tolerance.setConstraint("TOLERANCE_CONSTRAINT", tolerance.getColumnName() + " > 0");
        table.addColumn(tolerance);

        TableColumn cache = new TableColumn("CACHE");
        cache.setColumnType(SQLDataTypes.Int());
        cache.setDefaultValue("50000");
        cache.setConstraint("CACHE_CONSTRAINT", cache.getColumnName() + " < 5000000");
        table.addColumn(cache);

        TableColumn kernel = new TableColumn("KERNEL");
        kernel.setColumnType(SQLDataTypes.VarChar(10));
        kernel.setDefaultValue("'RBF'");
        kernel.setConstraint("KERNEL_CONSTRAINT", kernel.getColumnName() + " IN ('RBF', 'LINEAR', 'POLYNOMIAL', 'SIGMOID')");
        table.addColumn(kernel);

        TableColumn degree = new TableColumn("DEGREE");
        degree.setColumnType(SQLDataTypes.Int());
        degree.setDefaultValue("3");
        degree.setConstraint("DEGREE_CONSTRAINT", "DEGREE > 0");
        table.addColumn(degree);

        return table;
    }

    public static Table SvcModels() {
        Table table = new Table("SVC_MODELS");

        TableColumn uid = new TableColumn("UID");
        uid.setColumnType(SQLDataTypes.Int());
        uid.setNotNull(true);
        uid.setPrimaryKey(true, false);
        uid.setForeignKey(PredictionModels().getTableName(), "UID", true);
        table.addColumn(uid);

        TableColumn dataset = new TableColumn("DATASET");
        dataset.setColumnType(SQLDataTypes.VarChar(150));
        dataset.setUnique(true);
        dataset.setNotNull(true);
        table.addColumn(dataset);

        TableColumn gamma = new TableColumn("GAMMA");
        gamma.setColumnType(SQLDataTypes.Float());
        gamma.setDefaultValue("1.50");
        gamma.setConstraint("GAMMA_C", gamma.getColumnName() + " >=0");
        table.addColumn(gamma);

        TableColumn cost = new TableColumn("COST");
        cost.setColumnType(SQLDataTypes.Float());
        cost.setDefaultValue("100");
        cost.setConstraint("COST_C", cost.getColumnName() + " > 0.0");
        table.addColumn(cost);

        TableColumn bias = new TableColumn("BIAS");
        bias.setColumnType(SQLDataTypes.Float());
        bias.setDefaultValue("0");
        table.addColumn(bias);

        TableColumn tolerance = new TableColumn("TOLERANCE");
        tolerance.setColumnType(SQLDataTypes.Float());
        tolerance.setDefaultValue("0.0001");
        tolerance.setConstraint("TOLERANCE_C", tolerance.getColumnName() + " > 0");
        table.addColumn(tolerance);

        TableColumn cache = new TableColumn("CACHE");
        cache.setColumnType(SQLDataTypes.Int());
        cache.setDefaultValue("50000");
        cache.setConstraint("CACHE_C", cache.getColumnName() + " < 5000000");
        table.addColumn(cache);

        TableColumn kernel = new TableColumn("KERNEL");
        kernel.setColumnType(SQLDataTypes.VarChar(10));
        kernel.setDefaultValue("'RBF'");
        kernel.setConstraint("KERNEL_C", kernel.getColumnName() + " IN ('RBF', 'LINEAR', 'POLYNOMIAL', 'SIGMOID')");
        table.addColumn(kernel);

        TableColumn degree = new TableColumn("DEGREE");
        degree.setColumnType(SQLDataTypes.Int());
        degree.setDefaultValue("3");
        degree.setConstraint("DEGREE_C", degree.getColumnName() + " > 0");
        table.addColumn(degree);

        return table;
    }

    public static Table IndepFeaturesRelation() {
        Table table = new Table("INDEPENDENT_FETAURES_RELATION");

        TableColumn model_uid = new TableColumn("MODEL_UID");
        model_uid.setColumnType(SQLDataTypes.Int());
        model_uid.setForeignKey(PredictionModels().getTableName(), "UID", true);
        table.addColumn(model_uid);

        TableColumn feature_uid = new TableColumn("FEATURE_UID");
        feature_uid.setColumnType(SQLDataTypes.Int());
        feature_uid.setForeignKey(Features().getTableName(), "UID", true);
        table.addColumn(feature_uid);
        return table;
    }

    public static Table AlgorithmOntolRelation() {
        Table table = new Table("ALG_ONT_RELATION");

//        TableColumn algorithm_uid = new TableColumn("ALGORITHM_UID");
//          algorithm_uid.setColumnType(SQLDataTypes.Int());
//          algorithm_uid.setForeignKey(Algorithms().getTableName(), "UID", true);
//         table.addColumn(algorithm_uid);

        TableColumn algorithm_name = new TableColumn("ALGORITHM");
        algorithm_name.setColumnType(SQLDataTypes.VarChar(40));
        algorithm_name.setForeignKey(Algorithms().getTableName(), "NAME", true);
        table.addColumn(algorithm_name);

        TableColumn ontology_name = new TableColumn("ONTOLOGY");
        ontology_name.setColumnType(SQLDataTypes.VarChar(40));
        ontology_name.setForeignKey(AlgorithmOntologies().getTableName(), "NAME", true);
        table.addColumn(ontology_name);
//        TableColumn ontology_uid = new TableColumn("ONTOLOGY_UID");
//          ontology_uid.setColumnType(SQLDataTypes.Int());
//          ontology_uid.setForeignKey(AlgorithmOntologies().getTableName(), "UID", true);
//         table.addColumn(ontology_uid);
        return table;
    }

    public static Table OmegaModels() {
        Table table = new Table("OMEGA");

        TableColumn name = new TableColumn("NAME");
        name.setColumnType(SQLDataTypes.VarChar(40));
        name.setNotNull(true);
        name.setPrimaryKey(true, false);
        table.addColumn(name);

        TableColumn uri = new TableColumn("URI");
        uri.setColumnType(SQLDataTypes.VarChar(200));
        uri.setUnique(true);
        table.addColumn(uri);

        TableColumn createdBy = new TableColumn("CREATED_BY");
        createdBy.setColumnType(SQLDataTypes.VarChar(50));
        createdBy.setNotNull(true);
        createdBy.setForeignKey(Users().getTableName(), "EMAIL", true);
        table.addColumn(createdBy);

        TableColumn timestamp = new TableColumn("TMSTMP");
        timestamp.setColumnType(SQLDataTypes.Timestamp());
        timestamp.setDefaultValue("CURRENT TIMESTAMP");
        table.addColumn(timestamp);



        return table;
    }

    // TODO: Add new columns: httpStatus(INT) and result(varchar 255)
    public static Table Tasks() {
        Table table = new Table("TASKS");

        TableColumn uid = new TableColumn("UID");
        uid.setColumnType(SQLDataTypes.Int());
        uid.setNotNull(true);
        uid.setPrimaryKey(true, true);
        table.addColumn(uid);

        TableColumn name = new TableColumn("NAME");
        name.setColumnType(SQLDataTypes.VarChar(40));
        name.setNotNull(true);
        table.addColumn(name);

        TableColumn uri = new TableColumn("URI");
        uri.setColumnType(SQLDataTypes.VarChar(200));
        uri.setUnique(true);
        table.addColumn(uri);

        TableColumn status = new TableColumn("STATUS");
        status.setColumnType(SQLDataTypes.VarChar(80));
        status.setNotNull(true);
        status.setDefaultValue("'RUNNING'");
        status.setConstraint("STATUS_CONSTRAINT", "STATUS IN ('RUNNING', 'COMPLETED', 'CANCELLED')");
        table.addColumn(status);

        TableColumn createdBy = new TableColumn("CREATED_BY");
        createdBy.setColumnType(SQLDataTypes.VarChar(50));
        createdBy.setNotNull(true);
        createdBy.setForeignKey(Users().getTableName(), "EMAIL", true);
        table.addColumn(createdBy);


        TableColumn algorithm = new TableColumn("ALGORITHM");
        algorithm.setColumnType(SQLDataTypes.VarChar(40));
        algorithm.setNotNull(true);
        algorithm.setForeignKey(Algorithms().getTableName(), "NAME", true);
        table.addColumn(algorithm);

        TableColumn timestamp = new TableColumn("TMSTMP");
        timestamp.setColumnType(SQLDataTypes.Timestamp());
        timestamp.setDefaultValue("CURRENT TIMESTAMP");
        table.addColumn(timestamp);

        return table;
    }
}

