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
public final class IndFeatRelationTable {

    private static final String 
            _TABLE = "INDEPENDENT_FEATURES_RELATION",
            _MODEL_fk = "MODEL_UID",
            _FEATURES_fk = "FEATURE_UID";

    public static final TableColumn MODEL_UID = mod();
    public static final TableColumn FEATURE_UID = feat();

    public static final Table TABLE = table();

    private static final Table table(){
        Table table = new Table(_TABLE);
        table.addColumn(MODEL_UID);
        table.addColumn(FEATURE_UID);
        return table;
    }

    private static final TableColumn mod() {
        TableColumn model_uid = new TableColumn(_MODEL_fk);
        model_uid.setColumnType(SQLDataTypes.Int());
        model_uid.setForeignKey(QSARModelsTable.TABLE.getTableName(), QSARModelsTable.UID.getColumnName(), true);
        return model_uid;
    }

    private static final TableColumn feat() {
        TableColumn feature_uid = new TableColumn(_FEATURES_fk);
        feature_uid.setColumnType(SQLDataTypes.Int());
        feature_uid.setForeignKey(FeaturesTable.TABLE.getTableName(), FeaturesTable.UID.getColumnName(), true);
        return feature_uid;
    }
}
