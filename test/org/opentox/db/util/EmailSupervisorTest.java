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

import java.net.URLEncoder;
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
public class EmailSupervisorTest {

    public EmailSupervisorTest() {
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
     * Test of checkMail method, of class EmailSupervisor.
     */
    @Test
    public void testCheckMail() throws Exception {
        System.out.print("Empty Strings test            : ");
        assertFalse(EmailSupervisor.checkMail(""));
        System.out.print("PASSED\n");

        System.out.print("Testing @b.gr                 : ");
        assertFalse(EmailSupervisor.checkMail("@b.gr"));
        System.out.print("PASSED\n");

        System.out.print("Testing no-dot mails          : ");
        assertFalse(EmailSupervisor.checkMail("@a@bgr"));
        System.out.print("PASSED\n");

        System.out.print("Testing a.b@c                 : ");
        assertFalse(EmailSupervisor.checkMail("a.b@c"));
        System.out.print("PASSED\n");

        System.out.print("Testing a@b.                  : ");
        assertFalse(EmailSupervisor.checkMail("a@b."));
        System.out.print("PASSED\n");

        System.out.print("Testing a$terix@yahoo.fr      : ");
        assertFalse(EmailSupervisor.checkMail("a$terix@yahoo.fr"));
        System.out.print("PASSED\n");

        System.out.print("Testing ob#lix@hotmail.fr     : ");
        assertFalse(EmailSupervisor.checkMail("ob#lix@hotmail.fr"));
        System.out.print("PASSED\n");

        System.out.print("Testing ob{lix@hotmail.fr     : ");
        assertFalse(EmailSupervisor.checkMail("ob{lix@hotmail.fr"));
        System.out.print("PASSED\n");

        System.out.print("Testing <obelix@hotmail.fr>   : ");
        assertFalse(EmailSupervisor.checkMail("<obelix@hotmail.fr>"));
        System.out.print("PASSED\n");

        System.out.print("Testing panoramix\"@yahoo.fr   : ");
        assertFalse(EmailSupervisor.checkMail("panoramix\"@yahoo.fr"));
        System.out.print("PASSED\n");

        System.out.print("Testing correct mail          : ");
        assertTrue(EmailSupervisor.checkMail("a@b.c"));
        System.out.print("PASSED\n");

    }

}