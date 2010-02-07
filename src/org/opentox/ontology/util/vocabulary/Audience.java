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
package org.opentox.ontology.util.vocabulary;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * A collection of audiences that might be interested in YAQP services.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Audience implements Serializable {

    private String name;

    private Audience(String name) {
        this.name = name;
    }

    private Audience() {
    }

    /**
     * Characteristic name for the intended audience.
     * @return the name of the audience.
     */
    public String getName() {
        return name;
    }
    /**
     *
     * The general public includes anyone who can interfere with the services
     * using some HTTP client. No special background is required.
     */
    public static final Audience GeneralPublic = new Audience("General Public");
    /**
     * Biologists and in general people with knowledge of biology.
     */
    public static final Audience Biologists = new Audience("Biologists");
    /**
     *
     * People who have knowledge of toxicology.
     */
    public static final Audience Toxicologists = new Audience("Toxicologists");
    /**
     *
     * QSAR experts and people which are expert at QSAR and machine learning.
     */
    public static final Audience QSARExperts = new Audience("QSAR Experts");
    /**
     *
     * People working at a pharmaceutical industry.
     */
    public static final Audience Industry = new Audience("Industry");
    /**
     *
     * People working at the university or other research organizations/institutes.
     */
    public static final Audience University = new Audience("University");
    /**
     *
     * Researchers.
     */
    public static final Audience Researchers = new Audience("Researchers");
    /**
     *
     * All expert users (All categories except for the General Public).
     */
    public static final ArrayList<Audience> AllExpert = getAllExceptGeneralPublic();

    private static ArrayList<Audience> getAllExceptGeneralPublic() {
        ArrayList<Audience> list = new ArrayList<Audience>();
        try {
            Audience o = new Audience();
            Field[] fields = o.getClass().getFields();
            //System.out.println(fields.length);
            for (int i = 0; i < fields.length - 1 ; i++) {
                Audience au = (Audience) fields[i].get(o);
                if (!au.getName().equals(Audience.GeneralPublic.getName()) && au != null) {
                    list.add(au);
                }                
            }
            return list;
        } catch (Exception ex) {
            return null;
        }
    }

//    public static void main(String[] a) {
//
//    }

}
