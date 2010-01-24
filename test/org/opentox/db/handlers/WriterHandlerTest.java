package org.opentox.db.handlers;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.db.entities.AlgorithmOntology;
import org.opentox.db.entities.User;
import org.opentox.db.entities.UserGroup;
import org.opentox.db.exceptions.DuplicateKeyException;
import org.opentox.db.util.TheDbConnector;
import static org.junit.Assert.*;

/**
 *
 * @author chung
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
        WriterHandler.addUserGroup(new UserGroup("MYGROUP9", 60));
        WriterHandler.addUserGroup(new UserGroup("MYGROUP10", 60));
        WriterHandler.addUserGroup(new UserGroup("MYGROUP11", 60));
    }

    /**
     * Test of addAlgorithmOntology method, of class WriterHandler.
     */
    @Test
    public void testAddAlgorithmOntology() {
        try {
            for (int i = 0; i < 100; i++) {
                WriterHandler.addAlgorithmOntology(new AlgorithmOntology("name" + i, "uri" + i));
            }
        } catch (DuplicateKeyException ex) {
            Logger.getLogger(WriterHandlerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    @Test
    public void testAddUser() {
        try {
            WriterHandler.addUser(
                    new User(
                    "chugn", "pasnfe8", "Pantelis", "Sopasakis",
                    "chvng@mail.ntua.gr", "", "NTUA", "Greece",
                    "Athens", "Al. Papan. 50", "https://opentox.ntua.gr/new"));
            fail();
        } catch (DuplicateKeyException ex) {
            Logger.getLogger(WriterHandlerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}