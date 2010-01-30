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