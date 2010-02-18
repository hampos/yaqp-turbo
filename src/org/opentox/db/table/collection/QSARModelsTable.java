/*
 *
 * YAQP - Yet Another QSAR Project:
 * Machine Learning algorithms designed for the prediction of toxicological
 * features of chemical compounds become available on the Web. Yaqp is developed
 * under OpenTox (http://opentox.org) which is an FP7-funded EU research project.
 * This project was developed at the Automatic Control Lab in the Chemical Engineering
 * School of the National Technical University of Athens. Please read README for more
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
package org.opentox.db.table.collection;

import org.opentox.db.table.Table;
import org.opentox.db.table.TableColumn;
import org.opentox.db.util.SQLDataTypes;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public final class QSARModelsTable {

    private static final String _TABLE = "QSAR_MODELS",
            _UID = "UID",
            _CODE = "CODE",
            _PREDICTION = "PREDICTION_FEATURE",
            _DEPENDENT = "DEPENDENT_FEATURE",
            _ALGORITHM = "ALGORITHM",
            _CREATED_BY = "CREATED_BY",
            _TIMESTAMP = "TMSTMP",
            _DATASET = "DATASET_URI",
            _STATUS = "STATUS",   DEFAULT_STATUS = "'UNDER DEVELOPMENT'",
                            CONSTRAINT_STATUS = "MOD_STATUS_CONSTRAINT";

    private static final int
            DATASET_SIZE = 150,
            STATUS_SIZE = 50,
            CODE_SIZE = 40;

    public static final TableColumn UID = uid();
    public static final TableColumn CODE = code();
    public static final TableColumn PREDICTION_FEATURE = pred();
    public static final TableColumn DEPENDENT_FEATURE = dep();
    public static final TableColumn ALGORITHM = algorithm();
    public static final TableColumn CREATED_BY = createdBy();
    public static final TableColumn TIMESTAMP = timestamp();
    public static final TableColumn DATASET = datasetUri();
    public static final TableColumn STATUS = status();


    public static final Table TABLE = table();

    private static final Table table(){
        Table talbe = new Table(_TABLE);
        talbe.addColumn(UID);
        talbe.addColumn(CODE);
        talbe.addColumn(PREDICTION_FEATURE);
        talbe.addColumn(DEPENDENT_FEATURE);
        talbe.addColumn(ALGORITHM);
        talbe.addColumn(CREATED_BY);
        talbe.addColumn(TIMESTAMP);
        talbe.addColumn(DATASET);                        
        talbe.addColumn(STATUS);
        
        return talbe;
    }

    private static TableColumn uid() {
        TableColumn uid = new TableColumn(_UID);
        uid.setColumnType(SQLDataTypes.Int());
        uid.setNotNull(true);
        uid.setPrimaryKey(true, true);
        return uid;
    }

    private static TableColumn code() {
        TableColumn code = new TableColumn(_CODE);
        code.setColumnType(SQLDataTypes.VarChar(CODE_SIZE));
        code.setNotNull(true);
        return code;
    }

    private static TableColumn pred() {
        TableColumn prediction_feature = new TableColumn(_PREDICTION);
        prediction_feature.setColumnType(SQLDataTypes.Int());
        prediction_feature.setNotNull(true);
        prediction_feature.setForeignKey(FeaturesTable.TABLE.getTableName(), FeaturesTable.UID.getColumnName(), true);
        return prediction_feature;
    }

    private static TableColumn dep() {
        TableColumn dependent_feature = new TableColumn(_DEPENDENT);
        dependent_feature.setColumnType(SQLDataTypes.Int());
        dependent_feature.setNotNull(true);
        dependent_feature.setForeignKey(FeaturesTable.TABLE.getTableName(), FeaturesTable.UID.getColumnName(), true);
        return dependent_feature;
    }

    private static TableColumn algorithm() {
        TableColumn algorithm = new TableColumn(_ALGORITHM);
        algorithm.setColumnType(AlgorithmsTable.NAME.getColumnType());
        algorithm.setNotNull(true);
        algorithm.setForeignKey(AlgorithmsTable.TABLE.getTableName(), AlgorithmsTable.NAME.getColumnName(), true);
        return algorithm;
    }

    private static TableColumn createdBy() {
        TableColumn createdBy = new TableColumn(_CREATED_BY);
        createdBy.setColumnType(UsersTable.EMAIL.getColumnType());
        createdBy.setNotNull(true);
        createdBy.setForeignKey(UsersTable.TABLE.getTableName(), UsersTable.EMAIL.getColumnName(), true);
        return createdBy;
    }

    private static TableColumn timestamp() {
        TableColumn timestamp = new TableColumn(_TIMESTAMP);
        timestamp.setColumnType(SQLDataTypes.Timestamp());
        timestamp.setDefaultValue("CURRENT TIMESTAMP");
        return timestamp;
    }

    private static TableColumn datasetUri() {
        TableColumn dataset = new TableColumn(_DATASET);
        dataset.setColumnType(SQLDataTypes.VarChar(DATASET_SIZE));
        dataset.setNotNull(true);
        return dataset;
    }

    private static TableColumn status() {
        TableColumn status = new TableColumn(_STATUS);
        status.setColumnType(SQLDataTypes.VarChar(STATUS_SIZE));
        status.setDefaultValue(DEFAULT_STATUS);
        status.setConstraint(CONSTRAINT_STATUS, _STATUS + " IN ('UNDER_DEVELOPMENT','APPROVED')");
        return status;
    }
}
