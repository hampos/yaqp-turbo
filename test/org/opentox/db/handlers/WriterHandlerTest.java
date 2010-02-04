/*
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
package org.opentox.db.handlers;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.db.exceptions.DbException;
import org.opentox.ontology.components.*;
import org.opentox.db.exceptions.BadEmailException;
import org.opentox.db.exceptions.DuplicateKeyException;
import org.opentox.db.util.TheDbConnector;
import static org.junit.Assert.*;
import org.opentox.ontology.exceptions.YaqpOntException;
import org.opentox.ontology.namespaces.OTAlgorithmTypes;
import org.opentox.ontology.util.YaqpAlgorithms;

/**
 *
 * @author Sopasakis Pantelis
 * @author Charalampos Chomenides
 */
public class WriterHandlerTest {

    public WriterHandlerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        TheDbConnector.init();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of addUserGroup method, of class WriterHandler.
     */

     @Test
    public void testAddUserGroup() throws Exception {
        WriterHandler.addUserGroup(new UserGroup("MYGROUP5", 60));
        WriterHandler.addUserGroup(new UserGroup("MYGROUP9", 70));
        WriterHandler.addUserGroup(new UserGroup("MYGROUP10", 80));
        WriterHandler.addUserGroup(new UserGroup("ADMIN", 10));
    }

    /**
     * Test of addAlgorithmOntology method, of class WriterHandler.
     */
    @Test
    public void testAddAlgorithmOntology() throws YaqpOntException {
        try {
//            OTAlgorithmTypes o ;
//            [] = o.getClass().getFields();
            ArrayList<OTAlgorithmTypes> otlist = OTAlgorithmTypes.getAllAlgorithmTypes();
            for (OTAlgorithmTypes ot : otlist) {
                WriterHandler.addAlgorithmOntology(new AlgorithmOntology(ot.getResource().getLocalName()));

            }
//            WriterHandler.addAlgorithmOntology(new AlgorithmOntology("Regression"));
//            WriterHandler.addAlgorithmOntology(new AlgorithmOntology("Classification"));
//            WriterHandler.addAlgorithmOntology(new AlgorithmOntology("ClassificationEagerMultipleTargets"));
//            WriterHandler.addAlgorithmOntology(new AlgorithmOntology("RegressionEagerMultipleTargets"));
//            WriterHandler.addAlgorithmOntology(new AlgorithmOntology("RegressionEagerSingleTarget"));
        } catch (DuplicateKeyException ex) {
            Logger.getLogger(WriterHandlerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * It seems users are being successfully added in the database.
     * @throws BadEmailException
     */
     @Test
    public void testAddUser() throws BadEmailException {
        try {
            for (int i = 0; i < 100; i++) {
                WriterHandler.addUser(
                        new User(
                        "vser_" + i, "patss" + i, "firstname" + i, "lastname" + i,
                        "makis" + i + "@mailntua.gr", "NTUA", "Greece",
                        "Athens", "Al. Papan. 50", "https://opentox.ntua.gr/new", null, new UserGroup("ADMIN", 0)));
            }
        } catch (DuplicateKeyException ex) {
            System.out.println(ex);
            Logger.getLogger(WriterHandlerTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            System.out.println(ex);
            fail(ex.toString());
        }
    }

    @Test
    public void testAddAlgorithm() throws Exception {

        WriterHandler.addAlgorithm(YaqpAlgorithms.MLR);
        WriterHandler.addAlgorithm(YaqpAlgorithms.SVM);
        WriterHandler.addAlgorithm(YaqpAlgorithms.SVC);
    }

        
    @Test
    public void testAddFeature() throws DbException {
        for(int i=1; i<=100; i++){
            WriterHandler.addFeature(new Feature("url"+i));
        }
    }

    @Test
    public void testAddMLRModel() throws DuplicateKeyException {
        ArrayList<Feature> features = ReaderHandler.getFeatures();
        System.err.println(ReaderHandler.getUser("vser_1"));
        MLRModel model = new MLRModel(0, "asdf", "//asdf",
                features.get(1), features.get(2), YaqpAlgorithms.MLR, ReaderHandler.getUser("vser_1"), "", "//dataset");
//                QSARModel qsar = new QSARModel(1,"asdf", "//asdf5",
//                features.get(1), features.get(2), YaqpAlgorithms.MLR, ReaderHandler.getUser("vser_1"), null) {
//
//            @Override
//            public PDFObject getPDF() {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public RDFObject getRDF() {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public TurtleObject getTurtle() {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public JSONObject getJson() {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//        };
//        System.out.println(WriterHandler.addQSARModel(qsar));
        WriterHandler.addMLRModel(model);
    }
}
