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

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.interfaces.JMultiProcessor;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class ParallelProcessorTest {

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
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                throw new YaqpException();
            }
            return data + " <-- p2";
        }
    };
    private Processor<String, String> fails_after_long = new Processor<String, String>() {

        public String process(String data) throws YaqpException {

            try {
                Thread.sleep(10000);
                throw new YaqpException();
            } catch (InterruptedException ex) {
                throw new YaqpException();
            }
        }
    };

    public ParallelProcessorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.err.println("Testing :"+ParallelProcessor.class.getCanonicalName());
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
     * Test of isfailSensitive method, of class ParallelProcessor.
     */
    //@Test
    public void firstTest() {
        System.out.println("-- first test --");
        JMultiProcessor pp =
                new ParallelProcessor<String, String, Processor<ArrayList<String>, ArrayList<String>>>();
        ArrayList<String> list = new ArrayList<String>(2);
        pp.setfailSensitive(true);

        pp.add(p1);
        list.add("1");
        pp.add(p2);
        list.add("2");

        try {
            ArrayList result = (ArrayList) pp.process(list);
            System.out.println(result.get(1));
        } catch (Exception ex) {
            fail();
        }

        System.out.println(pp.getStatus());
    }

    //@Test
    public void largeLoad() {
        System.out.println("-- largeLoad test --");
        final int load = 20;
        ParallelProcessor pp =
                new ParallelProcessor<String, String, Processor<ArrayList<String>, ArrayList<String>>>(5, 20);
        pp.setfailSensitive(false);

        ArrayList<String> list = new ArrayList<String>(load);

        for (int i = 0; i < load; i++) {
            pp.add(p1);
            list.add("in" + i);
        }

        try {
            ArrayList result = (ArrayList) pp.process(list);
            for (int i = 0; i < load; i++) {
                System.out.println(result.get(i));
            }
            System.out.println(pp.getStatus());
        } catch (Throwable ex) {
            fail(ex.toString());
        }
    }

    //@Test
    public void timeOut() {
        System.out.println("-- timeout test --");
        final int load = 20;
        final int timeout = 1600;
        ParallelProcessor pp =
                new ParallelProcessor<String, String, Processor<ArrayList<String>, ArrayList<String>>>(5, 20);
        pp.setfailSensitive(false);
        pp.setTimeOut(timeout, TimeUnit.MILLISECONDS);

        ArrayList<String> list = new ArrayList<String>(load);

        for (int i = 0; i < load; i++) {
            pp.add(p1);
            list.add("in" + i);
        }

        try {
            ArrayList result = (ArrayList) pp.process(list);
            for (int i = 0; i < load; i++) {
                System.out.println(result.get(i));
            }
            System.out.println(pp.getStatus());
        } catch (Throwable ex) {
            fail(ex.toString());
        }
    }

    @Test
    public void synch(){
        System.out.println("-- Synchronization (NEW!) --");
        final int timeout = 1600;
        ParallelProcessor pp =
                new ParallelProcessor<String, String, Processor<ArrayList<String>, ArrayList<String>>>(2, 6);
        pp.setfailSensitive(false);
        pp.setTimeOut(timeout, TimeUnit.MILLISECONDS);
               p1.setEnabled(true);
               p2.setEnabled(true);
        p2.setSynchronized(true);
        pp.add(p1);
        pp.add(p2);
        ArrayList<String> list = new ArrayList<String>(2);
        list.add("3");
        list.add("4");
        try {
            ArrayList result = pp.process(list);
            System.out.println("result from 0: "+result.get(0));
            System.out.println("result from 1" +
                    ": "+result.get(1));
            System.out.println(pp.getStatus());
        } catch (YaqpException ex) {
            
        }
    }

    //@Test
    public void noInput() {
        System.out.println("-- No input to processors --");
        JMultiProcessor pp =
                new ParallelProcessor<String, String, Processor<ArrayList<String>, ArrayList<String>>>();
        pp.add(p1);
        pp.setfailSensitive(true);
        try {
            pp.process(null);
        } catch (YaqpException ex) {
            System.out.println(ex + "\n");
            assertTrue(ex instanceof YaqpException);
        }
    }

    //@Test
    public void noProcessorsFound() {
        System.out.println("-- No processors --");
        JMultiProcessor pp =
                new ParallelProcessor<String, String, Processor<ArrayList<String>, ArrayList<String>>>();
        pp.setfailSensitive(false);
        try {
            Object o = pp.process(pp);
        } catch (YaqpException ex) {
            System.out.println(ex + "\n");
        }
    }
}
