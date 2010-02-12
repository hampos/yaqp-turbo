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
package org.opentox.io.processors;

import java.net.URI;
import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.io.exceptions.YaqpIOException;
import org.opentox.io.publishable.OntObject;
import org.opentox.io.util.ServerList;
import org.opentox.ontology.exceptions.YaqpOntException;
import static org.junit.Assert.*;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class InputProcessorTest {

    public InputProcessorTest() {
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
    public void testExistingUri() {
        try {
            InputProcessor<OntObject> p = new InputProcessor<OntObject>();
            URI uri = new URI("http://localhost/6");
            p.handle(uri);
        } catch (Exception e) {
            System.out.println(e);
            fail(e.toString());
        }
    }

    @Test
    public void testNonExistingUri() throws URISyntaxException {
        try {
            InputProcessor<OntObject> p = new InputProcessor<OntObject>();
            URI uri = new URI("http://localhost/asdfwqrety.html");
            p.handle(uri);
            fail("SHOULD HAVE FAILED");
        } catch (YaqpException e) {
            assertTrue(e instanceof YaqpIOException);
        }
    }

    @Test
    public void testWTFURI() throws URISyntaxException{
        try {
            InputProcessor<OntObject> p = new InputProcessor<OntObject>();
            URI uri = new URI("http://localhost/wtf.txt");
            p.handle(uri);
            fail("SHOULD HAVE FAILED");
        } catch (YaqpException e) {
            assertTrue(e instanceof YaqpOntException);
        }
    }


    @Test
    public void testNullInput() {
        try{
            new InputProcessor<OntObject>().process(null);
        }catch (Throwable ex){
            assertTrue(ex instanceof NullPointerException);
        }        
    }

    @Test
    public void anotherNullCrashTest(){
        try{
            new InputProcessor<OntObject>().handle(null);
        }catch (Throwable ex){
            assertTrue(ex instanceof NullPointerException);
        }
    }
}
