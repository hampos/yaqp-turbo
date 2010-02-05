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
import java.util.Iterator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.ontology.components.*;
import org.opentox.db.util.TheDbConnector;
import org.opentox.ontology.exceptions.YaqpOntException;
import org.opentox.ontology.util.AlgorithmMeta;
import org.opentox.ontology.util.YaqpAlgorithms;

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

    //@Test
    public void getUsersTest() {
        ArrayList<User> users = ReaderHandler.getUsers();
        Iterator<User> it = users.iterator();
        while (it.hasNext()) {
            User user = it.next();
            System.out.println(user + "\n");
        }
    }

    //@Test
    public void getAlgorithmOntologiesTest() throws YaqpOntException {

        ArrayList<AlgorithmOntology> algont = ReaderHandler.getAlgorithmOntologies();
        Iterator<AlgorithmOntology> it = algont.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }

    }

    //@Test
    public void getUserGroupsTest() {
        ArrayList<UserGroup> userGroups = ReaderHandler.getUserGroups();
        Iterator<UserGroup> it = userGroups.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    //@Test
    public void getAlgOntRelationTest() throws YaqpOntException {
        ArrayList<AlgorithmOntology> ontologies = ReaderHandler.getAlgOntRelation("svm");
        Iterator<AlgorithmOntology> it = ontologies.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

   // @Test
    public void getOntAlgRelationTest() throws Exception {
        AlgorithmOntology ontology = new AlgorithmOntology("Classification");
        ArrayList<Algorithm> algorithms = ReaderHandler.getOntAlgRelation(ontology);
        Iterator<Algorithm> it = algorithms.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }
    //@Test
    public void getAlgorithmsTest(){
        ArrayList<Algorithm> algorithms = ReaderHandler.getAlgorithms();
        Iterator<Algorithm> it = algorithms.iterator();
        while(it.hasNext()){
            System.out.println(it.next());
        }
    }

    @Test
    public void getMLRModelsTest(){
        ArrayList<MLRModel> models = ReaderHandler.getMLRModels();
        Iterator<MLRModel> it = models.iterator();
        while(it.hasNext()){
            System.out.println(it.next());
        }
    }
}


