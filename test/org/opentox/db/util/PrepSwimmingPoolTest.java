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
package org.opentox.db.util;

import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.queries.HyperStatement;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class PrepSwimmingPoolTest {

    public PrepSwimmingPoolTest() {
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
     * Test of take method, of class PrepSwimmingPool.
     */
    @Test
    public void testAddUserGroup() {
        try {
            TheDbConnector.init();
        } catch (DbException ex) {
            System.out.println("1-----\n" + ex);
            fail();
        }
        HyperStatement hp = null;
        try {
            System.err.println(PrepSwimmingPool.POOL.take(PrepStmt.ADD_USER_GROUP).toString());
            hp = PrepSwimmingPool.POOL.take(PrepStmt.ADD_USER_GROUP);
        } catch (InterruptedException ex) {
            fail();
            throw new RuntimeException(ex);
        }
        try {
            hp.setString(1, "GROUP_F");
            hp.setInt(2, 199);
            hp.executeUpdate();
            PrepSwimmingPool.POOL.recycle(hp);
        } catch (DbException ex) {
            System.out.println("2-----\n" + ex);
            fail();
        }

    }

    @Test
    public void testAddUser() {
        HyperStatement hp = null;
        try {
            System.err.println(PrepSwimmingPool.POOL.take(PrepStmt.ADD_USER).toString());
            hp = PrepSwimmingPool.POOL.take(PrepStmt.ADD_USER);
        } catch (InterruptedException ex) {
            fail();
            throw new RuntimeException(ex);
        }
        try {
            hp.setString(1, Long.toHexString(new Random().nextLong()));
            hp.setString(2, java.util.UUID.randomUUID().toString());
            hp.setString(3,"Pantelis");
            hp.setString(4,"Sopasakis");
            hp.setString(5,java.util.UUID.randomUUID().toString());
            hp.setString(6,"NTUA");
            hp.setString(7,"Greece/Hellas");
            hp.setString(8,"Athens/Pireaus");
            hp.setString(9,"My Address 73");
            hp.setString(10,"http://opentox.ntua.gr");
            hp.setInt(11, 1);
            hp.executeUpdate();
        } catch (DbException ex) {
            System.out.println("2-----\n" + ex);
            fail();
        }
    }
}
