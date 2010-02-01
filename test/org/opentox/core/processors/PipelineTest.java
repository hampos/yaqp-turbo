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
package org.opentox.core.processors;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.interfaces.JMultiProcessorStatus.STATUS;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class PipelineTest {

    // PROCESSOR 1
    private Processor<String, String> p1 = new Processor<String, String>() {

        public String process(String data) throws YaqpException {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                throw new YaqpException();
            }
            return data + " <-- p1";
        }
    };
    // PROCESSOR 2
    private Processor<String, String> p2 = new Processor<String, String>() {

        public String process(String data) throws YaqpException {
            try {
                Thread.sleep(80);
            } catch (InterruptedException ex) {
                throw new YaqpException();
            }
            return data + " <-- p2";
        }
    };

    private Processor<String, String> p3 = new Processor<String, String>(){

        public String process(String data) throws YaqpException {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                throw new YaqpException();
            }
            throw new YaqpException();
        }

    };

    public PipelineTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.err.println("Testing :"+Pipeline.class.getCanonicalName());
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
     * Test of process method, of class Pipeline.
     */
    @Test
    public void testPipe() {
        System.out.println("-- first test --");
        Pipeline pipe = new Pipeline();
        pipe.setfailSensitive(false);
        pipe.add(p1);
        pipe.add(p2);
        pipe.add(p3);
        try {
            String out = (String) pipe.process("data ");
            System.out.println(out);
            System.out.println(pipe.getStatus());
            assertTrue(Math.abs(pipe.getStatus().getElapsedTime(STATUS.ERROR)-2000)<5);
            assertTrue(Math.abs(pipe.getStatus().getElapsedTime(STATUS.PROCESSED)-1080)<5);
            assertTrue(!pipe.getStatus().isInProgress());
        } catch (Throwable ex) {
            System.out.println(ex);
            //fail(/*ex.getMessage()*/);
        }
    }

    /**
     * Test of isEnabled method, of class Pipeline.
     */
    @Test
    public void oneIsEnabled() {
        System.out.println("-- one is enabled --");
        Pipeline<String, String, Processor<String, String>> pipe =
                new Pipeline<String, String, Processor<String, String>>();
        pipe.add(p1);
        pipe.add(p2);
        p2.setEnabled(false);
        try {
            String out = pipe.process("data ");
            System.out.println(out);            
        } catch (YaqpException ex) {
            fail(/*ex.getMessage()*/);
        }
    }

    @Test
    public void allDisabled(){
        System.out.println("-- all disabled --");
        Pipeline<String, String, Processor<String, String>> pipe =
                new Pipeline<String, String, Processor<String, String>>();
        pipe.add(p1);
        pipe.add(p2);
        p2.setEnabled(false);
        p1.setEnabled(false);
        try {
            String out = pipe.process("data ");
            System.out.println(out);
        } catch (YaqpException ex) {
            fail(/*ex.getMessage()*/);
        }
    }
}
