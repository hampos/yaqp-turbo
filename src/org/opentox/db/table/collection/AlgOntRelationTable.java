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

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class AlgOntRelationTable {

    private static final String
            _TABLE = "ALG_ONT_RELATION",
            _ALGORITHM = "ALGORITHM",
            _ONTOLOGY = "ONTOLOGY";

    public static final TableColumn ALGORITHM = algorithm();
    public static final TableColumn ONTOLOGY = ontology();

    public static final Table TABLE = table();

    private static final Table table() {
        Table table = new Table(_TABLE);
        table.addColumn(ALGORITHM);
        table.addColumn(ONTOLOGY);
        return table;
    }

    private static final TableColumn algorithm() {
        TableColumn algorithm_name = new TableColumn(_ALGORITHM);
        algorithm_name.setColumnType(AlgorithmsTable.NAME.getColumnType());
        algorithm_name.setForeignKey(AlgorithmsTable.TABLE.getTableName(), AlgorithmsTable.NAME.getColumnName(), true);
        return algorithm_name;
    }

    private static final TableColumn ontology() {
        TableColumn ontology_name = new TableColumn(_ONTOLOGY);
        ontology_name.setColumnType(AlgOntTable.NAME.getColumnType());
        ontology_name.setForeignKey(AlgOntTable.TABLE.getTableName(), AlgOntTable.NAME.getColumnName(), true);
        return ontology_name;
    }
}
