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
package org.opentox.core.processors;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.interfaces.JMultiProcessorStatus.STATUS;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.*;
import org.opentox.util.logging.logobject.LogObject;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class BatchProcessorTest {

    // PROCESSOR 1
    private Processor<String, String> p1 = new Processor<String, String>() {

        public String process(String data) throws YaqpException {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                throw new YaqpException();
            }
            return data + " <-- p1";
        }
    };
    // PROCESSOR 1
    private Processor<String, Integer> p2 = new Processor<String, Integer>() {

        public Integer process(String data) throws YaqpException {
            long sleeptime = 0;
            try {
                sleeptime = Long.parseLong(data);
            } catch (NumberFormatException ex) {
                sleeptime = 500;
            }
            try {
                Thread.sleep(sleeptime);
            } catch (InterruptedException ex) {
            }
            return new Integer(1234);
        }
    };

    public BatchProcessorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.err.println("Testing :" + BatchProcessor.class.getCanonicalName());
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
    public void batchLogging() throws Exception {
        System.out.println("-- core test --");
        YaqpLogger.LOG.log(new Info(getClass(), "Initializing new Batch Processor"));
        BatchProcessor bp = new BatchProcessor(YaqpLogger.LOG);
        ArrayList<LogObject> list = new ArrayList<LogObject>();
        list.add(new Warning(BatchProcessorTest.class));
        list.add(new Warning(BatchProcessorTest.class));
        list.add(new Warning(BatchProcessorTest.class));
        list.add(new ScrewedUp(BatchProcessorTest.class));
        list.add(new ScrewedUp(BatchProcessorTest.class));
        list.add(new ScrewedUp(BatchProcessorTest.class));
        list.add(new Fatal(BatchProcessorTest.class));
        bp.process(list);
        assertTrue(bp.isEnabled());
    }

    @Test
    public void turboBatch() {
        System.out.println("-- testing using p1 --");
        BatchProcessor bp = new BatchProcessor(p1, 10, 12);
        ArrayList<String> listOfJobs = new ArrayList<String>();
        // all these will run in parallel
        for (int i = 1; i <= 10; i++) {
            listOfJobs.add(Integer.toString(i));
        }
        try {
            ArrayList<String> result = bp.process(listOfJobs);
            for (int i = 0; i < result.size(); i++) {
                assertEquals(result.get(i), (i + 1) + " <-- p1");
            }
            assertTrue(bp.getStatus().getNumProcessors(STATUS.ERROR) == 0);
            assertTrue(Math.abs(bp.getStatus().getElapsedTime(STATUS.PROCESSED) - 500) < 50);
            assertTrue(!bp.getStatus().isInProgress());
        } catch (YaqpException ex) {
            fail();
        }

    }

    @Test
    public void timeOutTest() {
        System.out.println("-- timeout test --");
        BatchProcessor<String, Integer, Processor<String, Integer>> bp =
                new BatchProcessor<String, Integer, Processor<String, Integer>>(p2, 2, 2);
        assertTrue(bp.getStatus().isInProgress());
        bp.setTimeOut(1000, TimeUnit.MILLISECONDS);
        ArrayList listOfJobs = new ArrayList();
        // all these will run in parallel
        listOfJobs.add("100");
        listOfJobs.add("20000");
        try {
            ArrayList<Integer> result = bp.process(listOfJobs);
            assertTrue(result.get(0) == 1234);
            assertEquals(result.get(1), null);
        } catch (YaqpException ex) {
            fail(ex.toString());
        }

    }

    @Test
    public void synch() {
        System.out.println("-- synchronization test --");
        p1.setSynchronized(true);
        BatchProcessor bp = new BatchProcessor(p1, 10, 12);
        ArrayList<String> listOfJobs = new ArrayList<String>();
        // all these will run in parallel
        for (int i = 1; i <= 10; i++) {
            listOfJobs.add(Integer.toString(i));
        }
        try {
            ArrayList<String> result = bp.process(listOfJobs);
            assertTrue(Math.abs(bp.getStatus().getElapsedTime(STATUS.PROCESSED) - 5000) < 50);
        } catch (YaqpException ex) {
            fail();
        }

    }
}

