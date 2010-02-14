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
public final class SupportVecTable {

    private static final String _UID = "UID",
            _TABLE = "SVM_MODELS",
            _GAMMA = "GAMMA",
            _EPSILON = "EPSILON",
            _COST = "COST",
            _COEFF0 = "COEFF0",
            _TOLERANCE = "TOLERANCE",
            _CACHESIZE = "CACHESIZE",
            _KERNEL = "KERNEL",
            _DEGREE = "DEGREE";
    private static double
            DEFAULT_GAMMA = 1.50,
            DEFAULT_EPSILON = 0.1,
            DEFAULT_COST = 100;

    public static final TableColumn UID = uid();
    public static final TableColumn GAMMA = gamma();
    public static final TableColumn EPSILON = epsilon();
    public static final TableColumn COST = cost();
    public static final TableColumn COEFF0 = coeff0();
    public static final TableColumn TOLERANCE = tol();
    public static final TableColumn CACHESIZE = cacheSize();
    public static final TableColumn KERNEL = kernel();
    public static final TableColumn DEGREE = degree();

    public static final Table TABLE = table();

    private static final Table table(){
        Table table = new Table(_TABLE);
        table.addColumn(UID);
        table.addColumn(GAMMA);
        table.addColumn(EPSILON);
        table.addColumn(COST);
        table.addColumn(COEFF0);
        table.addColumn(TOLERANCE);
        table.addColumn(CACHESIZE);
        table.addColumn(KERNEL);
        table.addColumn(DEGREE);
        return table;
    }


    private static final TableColumn uid() {
        TableColumn uid = new TableColumn(_UID);
        uid.setColumnType(SQLDataTypes.Int());
        uid.setNotNull(true);
        uid.setPrimaryKey(true, false);
        uid.setForeignKey(QSARModelsTable.TABLE.getTableName(), QSARModelsTable.UID.getColumnName(), true);
        return uid;
    }

    private static final TableColumn gamma() {
        TableColumn gamma = new TableColumn(_GAMMA);
        gamma.setColumnType(SQLDataTypes.Float());
        gamma.setDefaultValue(Double.toString(DEFAULT_GAMMA));
        gamma.setConstraint("GAMMA_CONSTRAINT", gamma.getColumnName() + " >=0");
        return gamma;
    }

    private static final TableColumn epsilon() {
        TableColumn epsilon = new TableColumn(_EPSILON);
        epsilon.setColumnType(SQLDataTypes.Float());
        epsilon.setDefaultValue(Double.toString(DEFAULT_EPSILON));
        epsilon.setConstraint("EPSILON_CONSTRAINT", epsilon.getColumnName() + " > 0.0");
        return epsilon;
    }

    private static final TableColumn cost() {
        TableColumn cost = new TableColumn(_COST);
        cost.setColumnType(SQLDataTypes.Float());
        cost.setDefaultValue(Double.toString(DEFAULT_COST));
        cost.setConstraint("COST_CONSTRAINT", cost.getColumnName() + " > 0.0");
        return cost;
    }

    private static final TableColumn coeff0() {
        TableColumn coeff0 = new TableColumn(_COEFF0);
        coeff0.setColumnType(SQLDataTypes.Float());
        coeff0.setDefaultValue("0");
        return coeff0;
    }

    private static final TableColumn tol() {
        TableColumn tolerance = new TableColumn(_TOLERANCE);
        tolerance.setColumnType(SQLDataTypes.Float());
        tolerance.setDefaultValue("0.0001");
        tolerance.setConstraint("TOLERANCE_CONSTRAINT", tolerance.getColumnName() + " > 0");
        return tolerance;
    }

    private static final TableColumn cacheSize() {
        TableColumn cache = new TableColumn(_CACHESIZE);
        cache.setColumnType(SQLDataTypes.Int());
        cache.setDefaultValue("50000");
        cache.setConstraint("CACHE_CONSTRAINT", cache.getColumnName() + " < 5000000");
        return cache;
    }

    private static final TableColumn kernel() {
        TableColumn kernel = new TableColumn(_KERNEL);
        kernel.setColumnType(SQLDataTypes.VarChar(10));
        kernel.setDefaultValue("'RBF'");
        kernel.setConstraint("KERNEL_CONSTRAINT", kernel.getColumnName() + " IN ('RBF', 'LINEAR', 'POLYNOMIAL', 'SIGMOID')");
        return kernel;
    }

    private static final TableColumn degree() {
        TableColumn degree = new TableColumn(_DEGREE);
        degree.setColumnType(SQLDataTypes.Int());
        degree.setDefaultValue("3");
        degree.setConstraint("DEGREE_CONSTRAINT", "DEGREE > 0");
        return degree;
    }
}
