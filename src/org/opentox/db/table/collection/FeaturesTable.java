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
public final class FeaturesTable {

    private static final String
            _TABLE = "FEATURES",
            _UID = "UID",
            _URI = "URI";

    private static final int URI_SIZE = 255;

    public static final TableColumn UID = uid();
    public static final TableColumn URI = uri();

    public static final Table TABLE = table();

    private static final Table table(){
        Table table = new Table(_TABLE);
        table.addColumn(UID);
        table.addColumn(URI);
        return table;
    }


    private static TableColumn uid(){
        TableColumn uid = new TableColumn(_UID);
        uid.setColumnType(SQLDataTypes.Int());
        uid.setNotNull(true);
        uid.setPrimaryKey(true, true);
        return uid;
    }

    private static TableColumn uri(){
        TableColumn uri = new TableColumn(_URI);
        uri.setColumnType(SQLDataTypes.VarChar(URI_SIZE));
        uri.setUnique(true);
        uri.setNotNull(true);
        return uri;
    }

}