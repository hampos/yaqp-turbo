/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.db.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.db.entities.Algorithm;
import org.opentox.db.entities.AlgorithmOntology;
import org.opentox.db.entities.User;
import org.opentox.db.entities.UserGroup;

/**
 *
 * @author hampos
 */
public class ReaderHandlerTest {

    public ReaderHandlerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
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
    public void getUsersTest() {
        ArrayList<User> users = ReaderHandler.getUsers();
        Iterator<User> it = users.iterator();
        while (it.hasNext()) {
            User user = it.next();
            System.out.println(user);
        }
    }
<<<<<<< HEAD:test/org/opentox/db/handlers/ReaderHandlerTest.java
    //@Test
=======
   //@Test
>>>>>>> 7a4a5f5713a248c14662c83f60ba99cc8e8ffda1:test/org/opentox/db/handlers/ReaderHandlerTest.java
    public void getAlgorithmOntologiesTest(){
        
        ArrayList<AlgorithmOntology> algont = ReaderHandler.getAlgorithmOntologies();
        Iterator<AlgorithmOntology> it = algont.iterator();
        while(it.hasNext()){
            System.out.println(it.next());
        }
        
    }
  //  @Test
    public void getUserGroupsTest(){
        ArrayList<UserGroup> userGroups = ReaderHandler.getUserGroups();
        Iterator<UserGroup> it = userGroups.iterator();
        while(it.hasNext()){
            System.out.println(it.next());
        }
    }

    //@Test
    public void getAlgOntRelationTest(){
        Algorithm algorithm = new Algorithm("name1","uri1",null);
        ArrayList<AlgorithmOntology> ontologies = ReaderHandler.getAlgOntRelation(algorithm);
        Iterator<AlgorithmOntology> it = ontologies.iterator();
        while(it.hasNext()){
            System.out.println(it.next());
        }
    }

   // @Test
    public void getOntAlgRelationTest(){
        AlgorithmOntology ontology = new AlgorithmOntology("name1", "uri1");
        ArrayList<Algorithm> algorithms = ReaderHandler.getOntAlgRelation(ontology);
        Iterator<Algorithm> it = algorithms.iterator();
        while(it.hasNext()){
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
}


