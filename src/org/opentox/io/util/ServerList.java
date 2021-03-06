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
package org.opentox.io.util;

/**
 * This enumeration is a list of services with which YAQP may exchange information
 * and/or exploit them to perform some tasks.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public enum ServerList {

    /**
     * AMBIT Server.
     */
    ambit("http://ambit.uni-plovdiv.bg:8080/ambit2", true, true, true),
    /**
     * TUM OpenTox Server.
     */
    tum("http://opentox.informatik.tu-muenchen.de:8080/OpenTox", false, true, false),
    /**
     * In-Silico Toxicology Server.
     */
    insilico("http://webservices.in-silico.ch", true, true, true);

    private final String uri;
    private final boolean supportsDataset, supportsFeatures, supportsCompounds;

    private ServerList(final String uri, final boolean sd, final boolean sf, final boolean sc){
        this.uri = uri;
        this.supportsCompounds = sc;
        this.supportsDataset = sd;
        this.supportsFeatures = sf;
    }

    /**
     *
     * @return The base URI of the remote server.
     */
    public String getBaseURI(){
        return uri;
    }

    /**
     *
     * @return <code>true</code> if the server supports dataset services.
     */
    public boolean suppDataset(){
        return this.supportsDataset;
    }

    /**
     * Whether the server supports feature services.
     * @return <code>true</code> if the server supports feature services.
     */
    public boolean suppFeatures(){
        return this.supportsFeatures;
    }

    /**
     * Whether the server supports compound services.
     * @return <code>true</code> if the server supports compound services.
     */
    public boolean suppCompounds() {
        return this.supportsCompounds;
    }

    @Override
    public String toString(){
        return getBaseURI();
    }


}
