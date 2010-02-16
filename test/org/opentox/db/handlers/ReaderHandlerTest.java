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

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.db.exceptions.DbException;
import org.opentox.ontology.components.*;
import org.opentox.db.util.TheDbConnector;
import org.opentox.io.util.YaqpIOStream;
import org.opentox.ontology.exceptions.YaqpOntException;
import org.opentox.ontology.util.YaqpAlgorithms;
import org.opentox.util.monitoring.Jennifer;
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


    @Test
    public void searchForUser() throws DbException{
        System.out.println("---------------- search for user ------------");

        User prototype = new User();
        ComponentList<User> list = ReaderHandler.searchUser(prototype,0,0);
        for (User user : list.getComponentList()){
            System.out.println(user);
        }
    }

    @Test
    public void getAlgorithmOntologiesTest() throws YaqpOntException, DbException {
        System.out.println("---------------- search for ontologies ------------");
        ComponentList<AlgorithmOntology> algont = ReaderHandler.searchAlgorithmOntology(new AlgorithmOntology(), 0, 0);
        for(AlgorithmOntology ont : algont.getComponentList()){
            System.out.println(ont);
        }

    }

    @Test
    public void getUserGroupsTest() throws DbException {
        System.out.println("---------------- search for user groups ------------");
        ComponentList<UserGroup> userGroups = ReaderHandler.searchUserGroup(new UserGroup(), 0, 0);
        ArrayList<UserGroup> list = userGroups.getComponentList();

        for(UserGroup group : list) {
            System.out.println(group);
        }
    }


    @Test
    public void getAlgOntRelationTest() throws YaqpOntException, DbException {
        System.out.println("---------------- search for ontologies for given algorithm ------------");
        ComponentList<AlgorithmOntology> ontologies = ReaderHandler.getAlgOntRelation(new Algorithm(YaqpAlgorithms.mlr_metadata()),0,0);
        for(AlgorithmOntology ont : ontologies.getComponentList()){
            System.out.println(ont);
        }
    }

    @Test
    public void getOntAlgRelationTest() throws Exception {
        System.out.println("---------------- search for algorithms for given ontology ------------");
        AlgorithmOntology ontology = new AlgorithmOntology("Regression");
        ComponentList<Algorithm> algorithms = ReaderHandler.getOntAlgRelation(ontology,0,0);
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
        ComponentList<Feature> features = ReaderHandler.searchFeature(new Feature(), 0, 0);
        for(Feature f : features.getComponentList()){
            System.out.println(f);
        }
    }

        @Test
    public void getQSARMods() throws DbException{
        System.out.println("---------------- search QSARModels ------------");
        ComponentList<QSARModel> models = ReaderHandler.searchQSARModels(new QSARModel(), 0, 0);
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


