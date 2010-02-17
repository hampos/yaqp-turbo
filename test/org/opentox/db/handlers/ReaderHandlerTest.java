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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.util.Page;
import org.opentox.ontology.components.*;
import org.opentox.db.util.TheDbConnector;
import org.opentox.ontology.exceptions.YaqpOntException;
import org.opentox.ontology.namespaces.OTAlgorithmTypes;
import org.opentox.ontology.util.AlgorithmParameter;
import org.opentox.ontology.util.YaqpAlgorithms;
import org.opentox.ontology.util.vocabulary.ConstantParameters;
import static org.junit.Assert.*;

/**
 *
 * @author hampos
 */
public class ReaderHandlerTest {

    public ReaderHandlerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        TheDbConnector.init();
//        Jennifer.INSTANCE.start();
//        Thread.sleep(3000);
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
     * Get all users - check if it works
     * @throws DbException
     */
    @Test
    public void searchForUser() throws DbException{
        System.out.println("-- serch user - test 1 --");
        User prototype = new User();
        ComponentList<User> list = ReaderHandler.searchUser(prototype, new Page(0,0));
        for (User user : list.getComponentList()){
            System.out.println(user);
        }
    }

    /**
     * Check if the page size works.
     * @throws DbException
     */
    @Test
    public void searchUser_size() throws DbException{
        System.out.println("-- serch user - test 2 --");
        User prototype = new User();
        ComponentList<User> list = ReaderHandler.searchUser(prototype,new Page(1,0));
        assertTrue(list.getComponentList().size() == 1);
    }


    @Test
    public void unknownEmail() throws DbException{
        System.out.println("-- serch user - test 3 --");
        User prototype = new User();
        prototype.setEmail("unknown@user.mail.tnt");
        ComponentList<User> list = ReaderHandler.searchUser(prototype,new Page(1,0));
        assertTrue(list.getComponentList().size() == 0);
    }

    @Test
    public void unknownName() throws DbException{
        System.out.println("-- serch user - test 3 --");
        User prototype = new User();
        prototype.setUserName("mitsos");
        ComponentList<User> list = ReaderHandler.searchUser(prototype,new Page(1,0));
        assertTrue(list.getComponentList().size() == 0);
    }



    /**
     * Assure that null prototype throws NPE.
     * @throws DbException
     */
    @Test
    public void searchNullUser() throws DbException{
        System.out.println("-- serch user - test 4 --");
        User prototype = null;
        try{
        ComponentList<User> list = ReaderHandler.searchUser(prototype,new Page(0,0));
        } catch (Exception ex){
            assertTrue(ex instanceof NullPointerException);
        }
    }

    @Test
    public void getAlgorithmOntologiesTest() throws YaqpOntException, DbException {
        System.out.println("-- check whether all ontologies are in the DB --");
        ComponentList<AlgorithmOntology> algont = ReaderHandler.searchAlgorithmOntology(new AlgorithmOntology(), new Page(0,0));
        assertEquals(algont.getComponentList().size(), OTAlgorithmTypes.getAllAlgorithmTypes().size());

    }

    @Test
    public void getUserGroupsTest() throws DbException {
        System.out.println("-- user groups page size --");
        ComponentList<UserGroup> userGroups = ReaderHandler.searchUserGroup(new UserGroup(), new Page(1,0));
        ArrayList<UserGroup> list = userGroups.getComponentList();
        assertEquals(list.size(), 1);
    }


    @Test
    public void getAlgOntRelationTest() throws YaqpOntException, DbException {
        System.out.println("---------------- search for ontologies for given algorithm ------------");
        ComponentList<AlgorithmOntology> ontologies = ReaderHandler.getAlgOntRelation(new Algorithm(YaqpAlgorithms.mlr_metadata()),new Page(0,0));
        for(AlgorithmOntology ont : ontologies.getComponentList()){
            System.out.println(ont);
        }
    }

    @Test
    public void getOntAlgRelationTest() throws Exception {
        System.out.println("---------------- search for algorithms for given ontology ------------");
        AlgorithmOntology ontology = new AlgorithmOntology("Regression");
        ComponentList<Algorithm> algorithms = ReaderHandler.getOntAlgRelation(ontology,new Page(0,0));
        for(Algorithm alg : algorithms.getComponentList()){
            System.out.println(alg);
        }
    }

    @Test
    public void getAlgorithmsTest() throws DbException {
        System.out.println("---------------- get all algorithms ------------");
        ComponentList<Algorithm> algorithms = ReaderHandler.getAlgorithms();
        for(Algorithm alg : algorithms.getComponentList()){
            System.out.println(alg);
        }
    }

    @Test
    public void getFeaturesTest() throws DbException {
        System.out.println("---------------- search features ------------");
        ComponentList<Feature> features = ReaderHandler.searchFeature(new Feature(), new Page(0,0));
        for(Feature f : features.getComponentList()){
            System.out.println(f);
        }
    }

        @Test
    public void getQSARMods() throws DbException{
        System.out.println("---------------- search QSARModels ------------");
        QSARModel model = new QSARModel();
        Map<String,AlgorithmParameter> map = new HashMap<String,AlgorithmParameter>();
        
        //AlgorithmParameter<Double> p = map.get("gamma");
        AlgorithmParameter p = new AlgorithmParameter(1.5);

        map.put("gamma_min", p);

        model.setParams(map);
        ComponentList<QSARModel> models = ReaderHandler.searchQSARModels(model, new Page(0,0));
        for (QSARModel m : models.getComponentList()){
            System.out.println(m.getId());
            System.out.println(m.getDependentFeature());
        }
    }


//   @Test
//    public void getMLRModelsTest() throws DbException{
//        ArrayList<MLRModel> models = ReaderHandler.getMLRModels();
//        Iterator<MLRModel> it = models.iterator();
//        while(it.hasNext()){
//            System.out.println(it.next());
//        }
//    }

   // @Test
//    public void getTasksTest() throws DbException{
//        ArrayList<Task> tasks = ReaderHandler.getTasks();
//        Iterator<Task> it = tasks.iterator();
//        while(it.hasNext()){
//            System.out.println(it.next());
//        }
//    }
}


