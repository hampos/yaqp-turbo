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
import org.opentox.db.entities.AlgorithmOntology;
import org.opentox.db.entities.User;
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
    //@Test
    public void getAlgorithmOntologiesTest(){
        for(int i=0; i<10000; i++){
        ArrayList<AlgorithmOntology> algont = ReaderHandler.getAlgorithmOntologies();
        
        }
    }
}
