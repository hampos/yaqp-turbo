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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.io.util.ServerList;
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

    /**
     * Test of process method, of class InputProcessor.
     */
    @Test
    public void testProcess() {
        try {
            InputProcessor p = new InputProcessor();

            //URI uri = new URI("http://opentox.ntua.gr/big.rdf");
            URI uri = new URI(ServerList.ambit.getBaseURI() + "/dataset/6");

            //p.handle(uri).printConsole();

            //double start = 0, duration = 0, sum = 0;
            final int N = 10;
            for (int i = 0; i < N; i++) {
              //  start = System.currentTimeMillis();
                p.handle(uri);
                //duration = System.currentTimeMillis() - start;
              //  System.out.println(duration);
                //sum += duration;
            }
            //System.out.println("Average :"+(double)sum/(double)N);
            

        } catch (Exception e) {
            System.out.println(e);
            fail(e.toString());
        }
    }
}
