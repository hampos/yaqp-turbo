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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.core.exceptions.Cause;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.db.DatabaseJanitor;
import org.opentox.db.exceptions.DbException;
import org.opentox.ontology.components.*;
import org.opentox.db.exceptions.BadEmailException;
import org.opentox.db.exceptions.DuplicateKeyException;
import org.opentox.db.table.StandardTables;
import org.opentox.db.table.Table;
import org.opentox.db.table.TableDropper;
import org.opentox.db.util.TheDbConnector;
import static org.junit.Assert.*;
import org.opentox.ontology.exceptions.ImproperEntityException;
import org.opentox.ontology.exceptions.YaqpOntException;
import org.opentox.ontology.namespaces.OTAlgorithmTypes;
import org.opentox.ontology.util.AlgorithmMeta;
import org.opentox.ontology.util.AlgorithmParameter;
import org.opentox.ontology.util.YaqpAlgorithms;
import org.opentox.ontology.util.vocabulary.ConstantParameters;

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
        DatabaseJanitor.INSTANCE.reset();
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

    @Test
    public void testAddUserGroup() throws ImproperEntityException {
        System.out.println("user group - test 1");
        try {
            UserGroup guestGroup = (UserGroup) WriterHandler.add(new UserGroup("GUEST", 60));
            assertTrue(guestGroup.getName().equals("GUEST") && guestGroup.getLevel() == 60);
            UserGroup adminGroup = WriterHandler.addUserGroup(new UserGroup("ADMINISTRATOR", 10));
            assertTrue(adminGroup.getLevel() == 10 && adminGroup.getName().equals("ADMINISTRATOR"));
        } catch (DuplicateKeyException ex) {
            fail();
        } catch (DbException ex) {
            fail();
        }
    }

    @Test
    public void addBadUserGroup() {
        System.out.println("user group - test 2");
        UserGroup badGroup = new UserGroup(null, 10);
        boolean failed = false;
        try {
            WriterHandler.add(badGroup);
        } catch (DbException ex) {
            assertTrue(ex.getCode() == Cause.XDB321);
            failed = true;
        } catch (ImproperEntityException ex) {
            fail("Improper Entity ?!");
        }
        if (!failed) {
            fail("SHOULD HAVE FAILED!");
        }
    }

    @Test
    public void addDuplicateUserGroup() {
        System.out.println("user group - test 3");
        UserGroup some = new UserGroup("ROCKETS", 512);
        try {
            WriterHandler.add(some);
        } catch (DbException ex) {
        } catch (ImproperEntityException ex) {
            fail("Improper Entity ?!");
        }
        try {
            WriterHandler.add(some);
            fail("SHOULD HAVE FAILED!");
        } catch (DbException ex) {
            assertTrue(ex instanceof DuplicateKeyException);
        } catch (ImproperEntityException ex) {
            fail("Improper Entity ?!");
        }
    }

    @Test
    public void addNullUserGroup() throws DbException {
        System.out.println("user group - test 4");
        try {
            WriterHandler.addUserGroup(null);
            fail("SHOULD HAVE FAILED!");
        } catch (NullPointerException ex) {
            assertTrue(ex.toString().contains("null user group"));
        }
    }

    @Test
    public void addBigNameUserGroup() {
        System.out.println("user group - test 5");
        UserGroup ug = new UserGroup("ababababababababababababababababababababababa", 10);
        try {
            WriterHandler.add(ug);
            fail("SHOULD HAVE FAILED!");
        } catch (DbException ex) {
            assertTrue(ex.getCode() == Cause.XDB490);
        } catch (ImproperEntityException ex) {
            fail();
        }
    }

    @Test
    public void testAddAlgorithmOntology() throws YaqpOntException, DbException, YaqpException {
        System.out.println("algorithm ontology - test 1");
        try {
            ArrayList<OTAlgorithmTypes> otlist = OTAlgorithmTypes.getAllAlgorithmTypes();
            for (OTAlgorithmTypes ot : otlist) {
                AlgorithmOntology algOnt = new AlgorithmOntology(ot.getResource().getLocalName());
                assertTrue(algOnt == WriterHandler.addAlgorithmOntology(algOnt));
            }
        } catch (DuplicateKeyException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    @Test
    public void duplicateAddOntology() {
        System.out.println("algorithm ontology - test 2");
        AlgorithmOntology algOnt = new AlgorithmOntology(OTAlgorithmTypes.Classification);
        try {
            WriterHandler.add(algOnt);
            fail("SHOULD HAVE FAILED!");
        } catch (DbException ex) {
            assertTrue(ex instanceof DuplicateKeyException);
        } catch (ImproperEntityException ex) {
            fail("Improper Entity ?!");
        }
    }

    @Test
    public void addProperUser() throws BadEmailException {
        System.out.println("user - test 1");
        try {
            WriterHandler.add(
                    new User(
                    "john", java.util.UUID.randomUUID().toString(), "john", "smith",
                    "john@foo.goo.gr", null, "Italy",
                    "Roma", "15, Efi Sarri st.", "https://opentox.ntua.gr/abc", null, new UserGroup("GUEST", 0)));

        } catch (DuplicateKeyException ex) {
            System.out.println(ex);
            Logger.getLogger(WriterHandlerTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void noUsername() {
        System.out.println("user - test 2");
        try {
            WriterHandler.add(new User(null, java.util.UUID.randomUUID().toString(), "john", "smith", "john@foo.goo.gr", null, "Italy", "Roma", "15, Efi Sarri st.", "https://opentox.ntua.gr/abc", null, new UserGroup("GUEST", 0)));
            fail("SHOULD HAVE FAILED!");
        } catch (DbException ex) {
            assertTrue(ex.getCode() == Cause.XDB5870);
        } catch (ImproperEntityException ex) {
            Logger.getLogger(WriterHandlerTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Test
    public void noPassWord() {
        System.out.println("user - test 3");
        try {
            WriterHandler.add(new User("john", null, "john", "smith", "john@foo.goo.gr", null, "Italy", "Roma", "15, Efi Sarri st.", "https://opentox.ntua.gr/abc", null, new UserGroup("GUEST", 0)));
            fail("SHOULD HAVE FAILED!");
        } catch (DbException ex) {
            assertTrue(ex.getCode() == Cause.XDB5872);
        } catch (ImproperEntityException ex) {
            Logger.getLogger(WriterHandlerTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Test
    public void noAddress() {
        System.out.println("user - test 4");
        try {
            WriterHandler.add(new User("mike", java.util.UUID.randomUUID().toString(), "mike", "williams", "mike@foo.goo.gr", null, "Greece", "Larisa", null, "https://opentox.ntua.gr/abc", null, new UserGroup("GUEST", 0)));
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void duplicateUser() throws BadEmailException {
        System.out.println("user - test 5");
        try {
            WriterHandler.add(
                    new User(
                    "john", java.util.UUID.randomUUID().toString(), "john", "smith",
                    "john@foo.goo.gr", null, "Italy",
                    "Roma", "15, Efi Sarri st.", "https://opentox.ntua.gr/abc", null, new UserGroup("GUEST", 0)));
            fail("SHOULD HAVE FAILED!");
        } catch (DbException ex) {
            assertTrue(ex instanceof DuplicateKeyException);
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void badEMailUser() {
        System.out.println("user - test 6");
        try {
            WriterHandler.add(
                    new User(
                    "flash", java.util.UUID.randomUUID().toString(), "flash", "gordon",
                    "iDontGiveYouMyEmail", null, "Chung",
                    null, null, null, null, new UserGroup("GUEST", 0)));
            fail("SHOULD HAVE FAILED!");
        } catch (DbException ex) {
            assertTrue(ex instanceof BadEmailException);
            assertTrue(ex.getCode() == Cause.XDH103);
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void addProperAlgorithm() {
        System.out.println("algorithm - test 1");
        try {
            WriterHandler.add(YaqpAlgorithms.MLR);
            WriterHandler.add(YaqpAlgorithms.SVM);
            WriterHandler.add(YaqpAlgorithms.SVC);
        } catch (Throwable thr) {
            fail("SHOULD NOT HAVE FAILED!");
        }
    }

    @Test
    public void duplicateAlgorithm() {
        System.out.println("algorithm - test 2");
        try {
            WriterHandler.add(YaqpAlgorithms.SVM);
        } catch (DbException ex) {
            //assertTrue(ex.getCode()==Cause);
            assertTrue(ex instanceof DuplicateKeyException);
        } catch (ImproperEntityException ex) {
            fail("Improper Entity ?!");
        }
    }

    @Test
    public void algorithmNoOntologies() {
        System.out.println("algorithm - test 3");
        Algorithm a = new Algorithm();
        a.setMeta(new AlgorithmMeta());
        try {
            WriterHandler.add(a);
            fail("SHOULD HAVE FAILED!");
        } catch (DbException ex) {
            assertTrue(ex.getCode() == Cause.XDB3235);
        } catch (ImproperEntityException ex) {
            Logger.getLogger(WriterHandlerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void voidAlgorithm() {
        System.out.println("algorithm - test 4");
        Algorithm a = new Algorithm();
        try {
            WriterHandler.add(a);
            fail("SHOULD HAVE FAILED!");
        } catch (Exception ex) {
            assertTrue(ex instanceof NullPointerException);
            assertTrue(ex.getMessage().contains("algorithm with unknown metadata"));
        }
    }

    @Test
    public void addProperFeature() {
        System.out.println("feature - test 1");
        for (int i = 5; i <= 20; i++) {
            try {
                System.out.println(WriterHandler.addFeature(new Feature("http://sth.com/feature/" + i)).getID());
            } catch (DbException ex) {
                fail("SHOULDN'T HAVE FAILED!");
            }
        }
    }

    @Test
    public void addDuplicateFeature() {
        System.out.println("feature - test 2");
        try {
            String feature_uri = "http://sth.com/feature/10";
            Feature feature = WriterHandler.addFeature(new Feature(feature_uri));
            assertTrue(feature.getID() > 0);
            assertTrue(feature.getURI().equals(feature_uri));
        } catch (DbException ex) {
            fail("SHOULD NOT HAVE FAILED!");
        }
    }

    @Test
    public void addNullFeature() {
        System.out.println("feature - test 3");
        try {
            WriterHandler.addFeature(null);
            fail("SHOULD HAVE FAILED!");
        } catch (Exception ex) {
            assertTrue(ex instanceof NullPointerException);
        }
    }

    @Test
    public void addBadURIFeature() {
        System.out.println("feature - test 4");
        Feature bad = new Feature("asdf wqeryt *&^");
        try {
            WriterHandler.addFeature(bad);
            fail("SHOULD HAVE FAILED!");
        } catch (DbException ex) {
            assertTrue(ex.getCode() == Cause.XDB3237);
        }
    }

    @Test
    public void addVoidFeature() {
        System.out.println("feature - test 5");
        Feature bad = new Feature();
        try {
            WriterHandler.addFeature(bad);
            fail("SHOULD HAVE FAILED!");
        } catch (DbException ex) {
            assertTrue(ex.getCode() == Cause.XDB3236);
        }
    }

    @Test
    public void addProperTask() {
        System.out.println("task - test 1");
        try {
            User prot = new User();
            prot.setUserName("%ik%");            
            User u = ReaderHandler.searchUsers(prot).get(0);
            Task t = new Task(java.util.UUID.randomUUID().toString(), u, YaqpAlgorithms.SVC, 1000);
            assertEquals(WriterHandler.addTask(t), t);
        } catch (DbException ex) {
            fail();
        }
    }

    @Test
    public void addTaskUnknownUser() {
        System.out.println("task - test 2");
        try {
            User u = new User();
            u.setEmail("a@bc.d");
            Task t = new Task(java.util.UUID.randomUUID().toString(), u, YaqpAlgorithms.SVC, 1000);
            assertEquals(WriterHandler.addTask(t), t);
        } catch (DbException ex) {
            assertTrue(ex.getCode() == Cause.XDH110);
        }
    }

    @Test
    public void addTaskUndefinedAlgorithm() {
        System.out.println("task - test 3");
        try {
            User prot = new User();
            User u = ReaderHandler.searchUsers(prot).get(1);
            Task t = new Task(java.util.UUID.randomUUID().toString(), u, null, 1000);
            assertEquals(WriterHandler.addTask(t), t);
        } catch (DbException ex) {
            assertEquals(ex.getCode(), Cause.XDB4003);
        }
    }

    @Test
    public void addTaskZeroDuration() {
        System.out.println("task - test 4");
        try {
            User prot = new User();
            User u = ReaderHandler.searchUsers(prot).get(1);
            Task t = new Task(java.util.UUID.randomUUID().toString(), u, YaqpAlgorithms.SVC, 0);
            assertEquals(WriterHandler.addTask(t), t);
        } catch (DbException ex) {
            assertEquals(ex.getCode(), Cause.XDB4001);
        }
    }

    @Test
    public void addQSARModel() {
        System.out.println("QSAR Model - test 1");
        try {
            User u = ReaderHandler.searchUsers(new User()).get(0);
            Feature f = ReaderHandler.searchFeature(new Feature(-1, null));
            System.err.println(f);
            ArrayList<Feature> lf = new ArrayList<Feature>();
            lf.add(f);
            QSARModel m = new QSARModel(java.util.UUID.randomUUID().toString(), f, f, lf, YaqpAlgorithms.MLR, u, null, "dataset1", QSARModel.ModelStatus.UNDER_DEVELOPMENT);
            WriterHandler.addQSARModel(m);
            //System.out.println(model.getId());
        } catch (DbException ex) {
            fail("SHOULDN'T HAVE FAILED!");
        }
    }











    //  @Test
    public void addsvmModel() throws Exception {
        User u = ReaderHandler.searchUsers(new User()).get(1);
        Feature f = ReaderHandler.searchFeature(new Feature(-1, "http://sth.com/feature/1"));
        ArrayList<Feature> lf = new ArrayList<Feature>();
        lf.add(f);
        QSARModel m = new QSARModel(java.util.UUID.randomUUID().toString(), f, f, lf, YaqpAlgorithms.SVM, u, null, "dataset1", null);
        Map<String, AlgorithmParameter> params = new HashMap<String, AlgorithmParameter>();
        params.putAll(ConstantParameters.SVMParams());
        m.setParams(params);
        WriterHandler.add(m);
    }

    //  @Test
    public void addmlrModel() throws DbException, ImproperEntityException, YaqpException {
        User u = ReaderHandler.searchUsers(new User()).get(7);
        u.setEmail("ann10@foo.goo.gr");
        Feature f = ReaderHandler.searchFeature(new Feature(-1, "http://sth.com/feature/1"));
        ArrayList<Feature> lf = new ArrayList<Feature>();
        lf.add(f);
        QSARModel m = new QSARModel(java.util.UUID.randomUUID().toString(), f, f, lf, YaqpAlgorithms.MLR, u, null, "dataset1", null);
        ArrayList<AlgorithmParameter> tps = new ArrayList<AlgorithmParameter>();
        System.out.println(WriterHandler.add(m));
    }

    //  @Test
    public void addOmega() throws DbException, ImproperEntityException, YaqpException {
        User prot = new User();
        prot.setEmail("ann12%");
        User u = ReaderHandler.searchUsers(prot).get(1);
        OmegaModel om = new OmegaModel("dset50", java.util.UUID.randomUUID().toString(), u);
        System.out.println(WriterHandler.addOmega(om));

    }
}

