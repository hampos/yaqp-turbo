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
    ALGORITHM_ONTOLOGIES(AlgOntTable.TABLE, true),
    /**
     * Levels of authorization
     */
    USER_AUTH(UserAuthTable.TABLE, true),
    /**
     * Available algorithms
     */
    ALGORITHMS(AlgorithmsTable.TABLE, true),
    /**
     * User data including username, password (as SHA-512), first and last name, email
     * and other optional information
     */
    USERS(UsersTable.TABLE, true),
    /**
     * Table of features. Normally this table contains features which lay on
     * other servers but where used to generate models on this server.
     */
    FEATURES(FeaturesTable.TABLE, true),
    /**
     * The set of prediction models characterized by the name of the model, its URI,
     * its prediction and dependent feature, the corresponding algorithm that was
     * used to produced the model and the user that triggered the model creation.
     */
    QSAR_MODELS(QSARModelsTable.TABLE, true),
    /**
     * Table of all SVM models - child of {@link StandardTables#QSAR_MODELS Prediction QSARModels }
     */
    SUPPORT_VECTOR(SupportVecTable.TABLE, true),
    /**
     * Relation between independent features and models. This is a many-to-many relation
     * in the sense that one model has lots of independent features and in turn some
     * feature may be an independent feature in a collection of models. In the database
     * this is materialized by a table with two columns with foreign keys - one pointing
     * to a model and the other pointing to a feature.
     *
     */
    INDEPENDENT_FETAURES_RELATION(IndFeatRelationTable.TABLE, true),
    /**
     * Every algorithm is an instance of one or more (ontological) classes/categories.
     * So algorithmsa and algorithm ontologies possess a many-to-many relationship. This is
     * visualized in the database by this table which has two columns each one of which
     * is a foreign key - one pointing to algorithms and one to algorithm ontologies.
     */
    ALGORITHM_ONTOL_RELATION(AlgOntRelationTable.TABLE, true),
    /**
     * Tasks running on the server. A task is characterized by its name, URI,
     * its status (one of 'running', 'cancelled', 'completed' ), the user that
     * generated the task and the algorithm to which the task is pointing. TMSTMP is
     * the timestamp of the task creation. Using this value, one can estimate the
     * completion time of the task.
     */
    TASKS(TasksTable.TABLE, true),
    /**
     *
     */
    OMEGA(OmegaTable.TABLE, true); /* End of enumeration*/

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
                       

    

    


   

}

