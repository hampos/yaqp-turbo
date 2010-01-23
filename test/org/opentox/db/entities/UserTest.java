/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.opentox.db.entities;

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
public class UserTest {

    public UserTest() {
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
    public void represent(){
        User user = new User(
                "jack_the_ripper",
                java.util.UUID.randomUUID().toString(),
                "Jack", "The Ripper", "jacktheripper@yahoo.com", "ADMINISTRATOR",
                "NTUA", "Greece", "Athens", "Cemetery St., 105", null);
        System.out.println(user);
    }

}