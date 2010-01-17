/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.db.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class TheDbConnectorTest {

    public TheDbConnectorTest() {
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

    /**
     * Test of getLock method, of class DbConnector.
     */
    @Test
    public void InstantiationTest() {
        System.out.println("-- instantiation test --");               
            TheDbConnector db = TheDbConnector.DB;
            System.out.println(
                    db.getConnection() == null
                      ? "null connection at "+db.getDatabaseUrl()
                      : "connected successfully to "+db.getDatabaseUrl()
                    );
    }

    
}
