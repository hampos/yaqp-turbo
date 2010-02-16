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
package org.opentox.db;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.exceptions.DuplicateKeyException;
import org.opentox.db.handlers.WriterHandler;
import org.opentox.db.table.StandardTables;
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
import org.opentox.ontology.components.Algorithm;
import org.opentox.ontology.components.AlgorithmOntology;
import org.opentox.ontology.components.User;
import org.opentox.ontology.components.UserGroup;
import org.opentox.ontology.exceptions.YaqpOntException;
import org.opentox.ontology.namespaces.OTAlgorithmTypes;
import org.opentox.ontology.util.YaqpAlgorithms;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DatabaseJanitor {

    private static DatabaseJanitor instanceOfThis = null;
    public static DatabaseJanitor INSTANCE = getInstance();

    private static DatabaseJanitor getInstance() {
        if (instanceOfThis == null) {
            instanceOfThis = new DatabaseJanitor();
        }
        return instanceOfThis;
    }

    private DatabaseJanitor() {
    }

    /**
     *
     * @throws DbException if the database cannot be initialized, e.g. the database
     * server failed to start, or the tables could not be created.
     */
    public void startUpDatabase() throws DbException {
        TheDbConnector.init();
        populateAlgorithmOntologies();
        populateAlgorithms();
        populateUsers();
    }

    public void reset() throws SQLException {
        // THE CONTENT OF THE TABLES HAS TO BE CLEANED IN A CERTAIN ORDER
        // OTHERWISE WE WILL ENCOUNTER FOREIGN-KEY VIOLATION EXCEPTIONS.
        StandardTables[] tablesForCleanup = StandardTables.values();

        for (int i = tablesForCleanup.length - 1; i >= 0; i--) {
            try {
                Statement statement = TheDbConnector.DB.getConnection().createStatement();
                String sql = "DELETE FROM " + tablesForCleanup[i].getTable().getTableName();
                //System.out.println(sql);
                statement.executeUpdate(sql);
            } catch (SQLException ex) {
                throw ex;
            }
        }
    }

    private void populateAlgorithmOntologies() throws DbException {
        try {
            ArrayList<OTAlgorithmTypes> otlist = OTAlgorithmTypes.getAllAlgorithmTypes();
            for (OTAlgorithmTypes ot : otlist) {
                try {
                    WriterHandler.add(new AlgorithmOntology(ot));
                } catch (DuplicateKeyException ex) {/* do nonthing */ }
            }
        } catch (YaqpOntException ex) {/* do nonthing */ }
    }

    private void populateAlgorithms() throws DbException {
        for (Algorithm alg : YaqpAlgorithms.getAllAlgorithms()) {
            try {
                WriterHandler.add(alg);
            } catch (DuplicateKeyException ex) {/* do nonthing */ } catch (YaqpOntException ex) {/* do nonthing */ }
        }
    }

    private void populateUsers() {
        User u = new User(
                "john", java.util.UUID.randomUUID().toString(), "john", "smith",
                "john@foo.goo.gr", null, "Italy",
                "Roma", "15, Efi Sarri st.", "https://opentox.ntua.gr/abc", null, new UserGroup("JANITOR", 10000, "CCC", "CCC", "CCC" , "CCC", 3000));
    }

    public static void main(String args[]) throws DbException, SQLException {
        DatabaseJanitor.INSTANCE.reset();
    }

    ;
}
