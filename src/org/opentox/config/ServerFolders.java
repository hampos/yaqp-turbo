/*
 *
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
package org.opentox.config;

/**
 *
 * In this class the main folders used by YAQP are declared. There are subfolders
 * of the <code>HOME</code> folder which is declared in the <code>properties file</code>
 * (This is <code>server.properties</code> for Linux users and <code>macos.server.properties</code>
 * for Max OS X users.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 * @see Configuration Server Configuration
 */
public class ServerFolders {

    /**
     * Home folder for all files stored by YAQP. Copied here from the configuration
     * file <code>server.properties</code> or <code>macos.properties.file</code>.
     */
    public static final String home =
            Configuration.getProperties().getProperty("server.home", System.getProperty("user.home") + "/ot");
    /**
     * Directory where all models are stored.
     */
    protected static final String models =
            home + "/models";
    /**
     * Directory where all RDF representations of models produced by YAQP will be
     * stored. Most likely, these representations will not be stored in a file
     * but will be instead produced on the fly and upon request.
     */
    public static final String models_rdf =
            models + "/rdf";
    /**
     * Folder where PMML representations of MLR models are stored.
     */
    public static final String models_pmml =
            models + "/pmml";
    /**
     * Folder where models produced by Weka will be stored. These are serialized
     * files, not human-readable.
     */
    public static final String models_weka =
            models + "/weka";
    /**
     * Folder where DoA models are stored. These files contain the Omega Matrix
     * used by the Leverages algorithms and the parameter theta in a serialized
     * form.
     */
    public static final String models_omega =
            models + "/omega";
    /**
     * Collection of resources such as images, javascripts and other auxiliary
     * files etc.
     */
    public static final String resources =
            home + "/resources";

    /**
     * Folder for storing images
     */
    public static final String images =
            resources + "/images";

}
