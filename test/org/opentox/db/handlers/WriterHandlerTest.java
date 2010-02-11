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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.db.exceptions.DbException;
import org.opentox.ontology.components.*;
import org.opentox.db.exceptions.BadEmailException;
import org.opentox.db.exceptions.DuplicateKeyException;
import org.opentox.db.util.TheDbConnector;
import static org.junit.Assert.*;
import org.opentox.ontology.exceptions.ImproperEntityException;
import org.opentox.ontology.exceptions.YaqpOntException;
import org.opentox.ontology.namespaces.OTAlgorithmTypes;
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
//    @Test
    public void testAddUserGroup() throws ImproperEntityException {
        try{
        UserGroup guestGroup = (UserGroup ) WriterHandler.add(new UserGroup("GUEST", 60));
        assertTrue(guestGroup.getName().equals("GUEST") && guestGroup.getLevel()==60);
        UserGroup adminGroup = WriterHandler.addUserGroup(new UserGroup("ADMINISTRATOR", 10));
        assertTrue(adminGroup.getLevel()==10 && adminGroup.getName().equals("ADMINISTRATOR"));
        } catch (DuplicateKeyException ex){}
        catch (DbException ex) {
            //
        }
    }

//    @Test
    public void addBadUserGroup() {
        UserGroup badGroup = new UserGroup(null, 10);
        boolean dbExcThrown = false;
        try {
            WriterHandler.add(badGroup);
        } catch (DbException ex) {
            dbExcThrown= true;
        } catch (ImproperEntityException ex) {
            fail("Improper Entity ?!");
        }
        if (!dbExcThrown){
            fail("A bad user group seeme to be added!!!");
        }
        
    }

//    @Test
    public void addDuplicateUserGroup(){
        UserGroup some = new UserGroup("ROCKETS", 512);
        try {
            WriterHandler.add(some);
        } catch (DbException ex) {
            
        } catch (ImproperEntityException ex) {
            fail("Improper Entity ?!");
        }
        try {
            WriterHandler.add(some);
        } catch (DbException ex) {
            assertTrue(ex instanceof DuplicateKeyException);
        } catch (ImproperEntityException ex) {
            fail("Improper Entity ?!");
        }
    }


    


    /**
     * Test of addAlgorithmOntology method, of class WriterHandler.
     */
    //@Test
    public void testAddAlgorithmOntology() throws YaqpOntException, DbException, YaqpException {
        try {
            ArrayList<OTAlgorithmTypes> otlist = OTAlgorithmTypes.getAllAlgorithmTypes();
            for (OTAlgorithmTypes ot : otlist) {
                System.out.println(WriterHandler.addAlgorithmOntology(new AlgorithmOntology(ot.getResource().getLocalName())));

            }
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
            for (int i = 0; i < 1000; i++) {
                WriterHandler.add(
                        new User(
                        "ann" + i, java.util.UUID.randomUUID().toString(), "john" + i, "smith" + i,
                        "ann" + i + "@foo.goo.gr", "UNDEFINED", "Italy",
                        "Roma", "15, Efi Sarri st.", "https://opentox.ntua.gr/abc", null, new UserGroup("GUEST", 0)));
            }
        } catch (DuplicateKeyException ex) {
            System.out.println(ex);
            Logger.getLogger(WriterHandlerTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            System.out.println(ex);
            fail(ex.toString());
        }
    }

//   @Test
    public void testAddAlgorithm() throws DbException, ImproperEntityException, YaqpException {

        WriterHandler.add(YaqpAlgorithms.MLR);
        WriterHandler.add(YaqpAlgorithms.SVM);
        WriterHandler.add(YaqpAlgorithms.SVC);
    }

   //@Test
    public void testAddFeature() throws DbException, ImproperEntityException, YaqpException {
        for (int i = 5; i <= 20; i++) {
            System.out.println(WriterHandler.addFeature(new Feature("http://sth.com/feature/" + i)).getID());
        }
    }

    //@Test
    public void addQSARModel() throws DuplicateKeyException, DbException, ImproperEntityException, YaqpException {
        User u = ReaderHandler.searchUsers(new User()).get(7);
        u.setEmail("ann11@foo.goo.gr");
        Feature f = ReaderHandler.searchFeature(new Feature(1, null));
        ArrayList<Feature> lf = new ArrayList<Feature>();
        lf.add(f);
        QSARModel m = new QSARModel(java.util.UUID.randomUUID().toString(), f, f, lf, YaqpAlgorithms.SVM, u, null, "dataset1", null);
        WriterHandler.add(m);
    }

   // @Test
    public void addsvmModel() throws Exception {
        User u = ReaderHandler.searchUsers(new User()).get(1);
        Feature f = ReaderHandler.searchFeature(new Feature(-1,"http://sth.com/feature/1"));
        ArrayList<Feature> lf = new ArrayList<Feature>();
        lf.add(f);
        QSARModel m = new QSARModel(java.util.UUID.randomUUID().toString(), f, f, lf, YaqpAlgorithms.SVM, u, null, "dataset1", null);
        Map<String, AlgorithmParameter> params = new HashMap<String, AlgorithmParameter>();
        params.putAll(ConstantParameters.SVMParams());
        m.setParams(params);
        WriterHandler.add(m);
    }

    //@Test
    public void addmlrModel() throws DbException, ImproperEntityException, YaqpException {
        User u = ReaderHandler.searchUsers(new User()).get(7);        
        u.setEmail("ann10@foo.goo.gr");
        Feature f = ReaderHandler.searchFeature(new Feature(-1,"http://sth.com/feature/1"));
        ArrayList<Feature> lf = new ArrayList<Feature>();
        lf.add(f);
        QSARModel m = new QSARModel(java.util.UUID.randomUUID().toString(), f, f, lf, YaqpAlgorithms.MLR, u, null, "dataset1", null);
        ArrayList<AlgorithmParameter> tps = new ArrayList<AlgorithmParameter>();
        System.out.println(WriterHandler.add(m));
    }

   // @Test
    public void testaddTask() throws DbException, ImproperEntityException, YaqpException {
        User prot = new User();
        prot.setEmail("ann11%");
        User u = ReaderHandler.searchUsers(prot).get(1);
        for (int i = 0; i < 1000; i++) {
            Task t = new Task(java.util.UUID.randomUUID().toString(),
                    u, YaqpAlgorithms.SVC, 1000);
            WriterHandler.addTask(t);
        }
    }

    //@Test
    public void addOmega() throws DbException, ImproperEntityException, YaqpException {
        User prot = new User();
        prot.setEmail("ann12%");
        User u = ReaderHandler.searchUsers(prot).get(1);
        OmegaModel om = new OmegaModel("dset50", java.util.UUID.randomUUID().toString(), u);
        System.out.println(WriterHandler.addOmega(om));

    }
}

