/*
 * YAQP - Yet Another QSAR Project:
 * Machine Learning algorithms designed for the prediction of toxicological
 * features of chemical compounds become available on the Web. Yaqp is developed
 * under OpenTox (http://opentox.org) which is an FP7-funded EU research project.
 * This project was developed at the Automatic Control Lab in the Chemical Engineering
 * School of the National Technical University of Athens. Please read README for more
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
package org.opentox.db.table;

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
public class StandardTablesTest {

    public StandardTablesTest() {
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
     * Test of is2Bincluded method, of class StandardTables.
     */
    @Test
    public void testIs2Bincluded() {
        System.out.println("-- is2Bincluded --");
        for (StandardTables t : StandardTables.values()) {
            assertTrue(t.is2Bincluded());
        }
    }

    /**
     * Test of getTable method, of class StandardTables.
     */
    @Test
    public void testGetTable() {
        System.out.println("-- getTable --");
        for (StandardTables t : StandardTables.values()) {
            System.out.println("**" + t.getTable().getTableName());
        }
    }

    /**
     * Test of AlgorithmOntologies method, of class StandardTables.
     */
    @Test
    public void testSQL() {
        System.out.println("-- AlgorithmOntologies --");
        for (StandardTables t : StandardTables.values()) {
            System.out.println(t.getTable().getCreationSQL());
        }
    }
}
