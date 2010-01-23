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
public class UserGroupTest {

    public UserGroupTest() {
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
     * Test of toString method, of class UserGroup.
     */
    @Test
    public void testToString() {
        System.out.println("-- testing toString() --");
        UserGroup group = new UserGroup("ADMINISTRATORS", 15);
        System.out.println(group);
    }

}