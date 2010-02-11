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

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.exceptions.DuplicateKeyException;
import org.opentox.db.handlers.WriterHandler;
import org.opentox.db.table.StandardTables;
import org.opentox.db.table.TableDropper;
import org.opentox.db.util.TheDbConnector;
import org.opentox.ontology.components.Algorithm;
import org.opentox.ontology.components.AlgorithmOntology;
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

    public void reset() throws DbException {
        try {
            StandardTables o = null;
            Field[] tables = StandardTables.class.getFields();
            for (Field table : tables) {
                StandardTables t = (StandardTables) table.get(o);
                try {
                    Statement statement = TheDbConnector.DB.getConnection().createStatement();
                    statement.executeUpdate("DELETE FROM "+t.name());
                } catch (SQLException ex) {
                    throw new DbException();
                }
                t.getTable().getTableName();
            }
        } catch (IllegalArgumentException ex) {
            //Logger.getLogger(DatabaseJanitor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
           // Logger.getLogger(DatabaseJanitor.class.getName()).log(Level.SEVERE, null, ex);
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
    }

    public static void main(String args[]) throws DbException{
        DatabaseJanitor.INSTANCE.reset();
    };
}
