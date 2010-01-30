package org.opentox.db.handlers;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.db.entities.Algorithm;
import org.opentox.db.entities.AlgorithmOntology;
import org.opentox.db.entities.User;
import org.opentox.db.entities.UserGroup;
import org.opentox.db.exceptions.BadEmailException;
import org.opentox.db.exceptions.DuplicateKeyException;
import org.opentox.db.util.TheDbConnector;
import static org.junit.Assert.*;

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
    public void testAddUserGroup() {
        WriterHandler.addUserGroup(new UserGroup("MYGROUP5", 60));
        WriterHandler.addUserGroup(new UserGroup("MYGROUP9", 70));
        WriterHandler.addUserGroup(new UserGroup("MYGROUP10", 80));
        WriterHandler.addUserGroup(new UserGroup("ADMIN", 10));
    }

    /**
     * Test of addAlgorithmOntology method, of class WriterHandler.
     */
    @Test
    public void testAddAlgorithmOntology() {
        try {
            for (int i = 1; i <= 100; i++) {
                WriterHandler.addAlgorithmOntology(new AlgorithmOntology("name" + i, "uri" + i));
            }
        } catch (DuplicateKeyException ex) {
            Logger.getLogger(WriterHandlerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

  //  @Test
    public void testAddUser() throws BadEmailException {
        try {
            for (int i=0;i<1000;i++)
            WriterHandler.addUser(
                    new User(
                    "vser_"+i, "patss"+i, "firstname"+i, "lastname"+i,
                    "makis"+i+"@mailntua.gr", "NTUA", "Greece",
                    "Athens", "Al. Papan. 50", "https://opentox.ntua.gr/new", null, "ADMIN"));
        } catch (DuplicateKeyException ex) {
            System.out.println(ex);
            Logger.getLogger(WriterHandlerTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex){
            System.out.println(ex);
            fail(ex.toString());
        }
    }


    @Test
    public void testAddAlgorithm() {
        try {
            ArrayList<AlgorithmOntology> ontlist = ReaderHandler.getAlgorithmOntologies();
            for (int i = 1; i <= 100; i++) {
                WriterHandler.addAlgorithm(new Algorithm("name" + i, "uri" + i, ontlist));
            }
        } catch (DuplicateKeyException ex) {
            Logger.getLogger(WriterHandlerTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}