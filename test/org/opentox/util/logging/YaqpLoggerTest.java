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
package org.opentox.util.logging;

import org.opentox.util.logging.levels.ScrewedUp;
import org.opentox.util.logging.levels.Fatal;
import org.opentox.util.logging.levels.Trace;
import org.opentox.util.logging.levels.Warning;
import org.opentox.util.logging.levels.Info;
import org.opentox.util.logging.levels.Debug;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class YaqpLoggerTest {

    public YaqpLoggerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.err.println("Testing :"+YaqpLogger.class.getCanonicalName());
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
     * Test of log method, of class YaqpLogger.
     */
    @Test
    public void testLogger() {
        try{
        YaqpLogger.LOG.log(new Trace(YaqpLoggerTest.class));
        YaqpLogger.LOG.log(new Debug(YaqpLoggerTest.class));
        YaqpLogger.LOG.log(new Info(YaqpLoggerTest.class));
        YaqpLogger.LOG.log(new Warning(YaqpLoggerTest.class));
        YaqpLogger.LOG.log(new ScrewedUp(YaqpLoggerTest.class));
        YaqpLogger.LOG.log(new Fatal(YaqpLoggerTest.class));
        }catch(Throwable ex){
            System.out.println(ex);
            fail();
        }
    }

    
}