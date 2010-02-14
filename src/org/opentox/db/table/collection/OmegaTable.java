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
final public class OmegaTable {

    private static final String _TABLE = "OMEGA",
            _UID = "UID", _CODE = "CODE", _CREATED_BY = "CREATED_BY",
            _DATASET = "DATASET_URI", _TIMESTAMP = "TMSTMP";
    private static final int CODE_SIZE = 40, DATASET_SIZE = 150;

    public static final TableColumn UID = uid();
    public static final TableColumn CODE = code();
    public static final TableColumn CREATED_BY = createdBy();
    public static final TableColumn DATASET = dataset();
    public static final TableColumn TIMESTAMP = timestamp();

    public static final Table TABLE = table();

    private static final Table table(){
        Table table = new Table(_TABLE);
        table.addColumn(UID);
        table.addColumn(CODE);
        table.addColumn(CREATED_BY);
        table.addColumn(DATASET);
        table.addColumn(TIMESTAMP);
        return table;
    }

    private static final TableColumn uid() {
        TableColumn uid = new TableColumn(_UID);
        uid.setColumnType(SQLDataTypes.Int());
        uid.setNotNull(true);
        uid.setPrimaryKey(true, true);
        return uid;
    }

    private static final TableColumn code() {
        TableColumn code = new TableColumn(_CODE);
        code.setColumnType(SQLDataTypes.VarChar(CODE_SIZE));
        code.setNotNull(true);
        return code;
    }

    private static final TableColumn createdBy() {
        TableColumn createdBy = new TableColumn(_CREATED_BY);
        createdBy.setColumnType(UsersTable.EMAIL.getColumnType());
        createdBy.setNotNull(true);
        createdBy.setForeignKey(UsersTable.TABLE.getTableName(), UsersTable.EMAIL.getColumnName(), true);
        return createdBy;
    }

    private static final TableColumn dataset() {
        TableColumn dataset = new TableColumn(_DATASET);
        dataset.setColumnType(SQLDataTypes.VarChar(DATASET_SIZE));
        dataset.setNotNull(true);
        return dataset;
    }

    private static final TableColumn timestamp() {
        TableColumn timestamp = new TableColumn(_TIMESTAMP);
        timestamp.setColumnType(SQLDataTypes.Timestamp());
        timestamp.setDefaultValue("CURRENT TIMESTAMP");
        return timestamp;
    }


}
