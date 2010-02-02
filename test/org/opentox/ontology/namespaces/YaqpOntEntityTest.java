/*
 *
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
package org.opentox.ontology.namespaces;

import com.hp.hpl.jena.rdf.model.Resource;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
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
public class YaqpOntEntityTest {

    public YaqpOntEntityTest() {
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
    public void testSuper_OTClass() {
        System.out.println("-- Testing OTClass: getSuperEntities() --");
        OTClass cl = OTClass.NominalFeature;
        Set<Resource> my_supers = cl.getSuperEntities();
        Iterator<Resource> it = my_supers.iterator();
        assertTrue(my_supers.size() == 1);
        Set<String> resourceURIs = new HashSet<String>();
        while (it.hasNext()) {
            resourceURIs.add(it.next().getURI());
        }
        assertTrue(resourceURIs.contains(OTClass.Feature.getURI()));
        System.out.println("OK!");
    }

    @Test
    public void testSuper_OTAlgorithmTypes() {
        System.out.println("-- Testing OTAlgorithmTypes: getSuperEntities() --");
        OTAlgorithmTypes my = OTAlgorithmTypes.DataCleanup;
        Set<Resource> my_supers = my.getSuperEntities();
        Iterator<Resource> it = my_supers.iterator();
        assertTrue(my_supers.size() == 2);
        Set<String> resourceURIs = new HashSet<String>();
        while (it.hasNext()) {
            resourceURIs.add(it.next().getURI());
        }
        assertTrue(resourceURIs.contains(OTAlgorithmTypes.AlgorithmType.getURI()));
        assertTrue(resourceURIs.contains(OTAlgorithmTypes.Preprocessing.getURI()));
        System.out.println("OK!");
    }
}
