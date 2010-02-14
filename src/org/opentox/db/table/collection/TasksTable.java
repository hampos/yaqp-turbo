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
final public class TasksTable {

    private static final String _TABLE = "TASKS",
            _NAME = "NAME",
            _STATUS = "STATUS",
            _CREATED_BY = "CREATED_BY",
            _ALGORITHM = "ALGORITHM",
            _HTTPSTATUS = "HTTPSTATUS",
            _RESULT = "RESULT",
            _STARTSTAMP = "STARTSTAMP",
            _ENDSTAMP = "ENDSTAMP",
            _DURATION = "DURATION";

    private static final int NAME_SIZE = 40, RESULT_SIZE=255;

    public static final TableColumn NAME = name();
    public static final TableColumn STATUS = status();
    public static final TableColumn CREATED_BY = createdBy();
    public static final TableColumn ALGORITHM = algorithm();
    public static final TableColumn HTTP_STATUS = httpStatus();
    public static final TableColumn RESULT = result();
    public static final TableColumn STARTSTAMP = start();
    public static final TableColumn ENDSTAMP = end();
    public static final TableColumn DURATION = duration();

    public static final Table TABLE = table();

    private static final Table table(){
        Table table = new Table(_TABLE);
        table.addColumn(NAME);
        table.addColumn(STATUS);
        table.addColumn(CREATED_BY);
        table.addColumn(ALGORITHM);
        table.addColumn(HTTP_STATUS);
        table.addColumn(RESULT);
        table.addColumn(STARTSTAMP);
        table.addColumn(ENDSTAMP);
        table.addColumn(DURATION);
        return table;
    }

    private static final TableColumn name() {
        TableColumn name = new TableColumn(_NAME);
        name.setColumnType(SQLDataTypes.VarChar(NAME_SIZE));
        name.setPrimaryKey(true, false);
        return name;
    }

    private static final TableColumn status() {
        TableColumn status = new TableColumn(_STATUS);
        status.setColumnType(SQLDataTypes.VarChar(15));
        status.setNotNull(true);
        status.setDefaultValue("'RUNNING'");
        status.setConstraint("STATUS_CONSTRAINT", "STATUS IN ('RUNNING', 'COMPLETED', 'CANCELLED')");
        return status;
    }

    private static final TableColumn createdBy() {
        TableColumn createdBy = new TableColumn(_CREATED_BY);
        createdBy.setColumnType(UsersTable.EMAIL.getColumnType());
        createdBy.setNotNull(true);
        createdBy.setForeignKey(UsersTable.TABLE.getTableName(), UsersTable.EMAIL.getColumnName(), true);
        return createdBy;
    }

    private static final TableColumn algorithm() {
        TableColumn algorithm = new TableColumn(_ALGORITHM);
        algorithm.setColumnType(SQLDataTypes.VarChar(40));
        algorithm.setNotNull(true);
        algorithm.setForeignKey(AlgorithmsTable.TABLE.getTableName(), AlgorithmsTable.NAME.getColumnName(), true);
        return algorithm;
    }

    private static final TableColumn httpStatus() {
        TableColumn httpStatus = new TableColumn(_HTTPSTATUS);
        httpStatus.setColumnType(SQLDataTypes.Int());
        httpStatus.setNotNull(true);
        httpStatus.setDefaultValue("202");
        return httpStatus;
    }

    private static final TableColumn result() {
        TableColumn result = new TableColumn(_RESULT);
        result.setColumnType(SQLDataTypes.VarChar(RESULT_SIZE));
        return result;
    }

    private static final TableColumn start() {
        TableColumn startTimestamp = new TableColumn(_STARTSTAMP);
        startTimestamp.setColumnType(SQLDataTypes.Timestamp());
        startTimestamp.setDefaultValue("CURRENT TIMESTAMP");
        return startTimestamp;
    }

    private static final TableColumn end() {
        TableColumn endTimestamp = new TableColumn(_ENDSTAMP);
        endTimestamp.setColumnType(SQLDataTypes.Timestamp());
        return endTimestamp;
    }

    private static final TableColumn duration() {
        TableColumn duration = new TableColumn(_DURATION);
        duration.setColumnType(SQLDataTypes.Int());
        return duration;
    }
}

