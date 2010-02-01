/*
 * YAQP - Yet Another QSAR Project: Machine Learning algorithms designed for
 * the prediction of toxicological features of chemical compounds become
 * available on the Web. Yaqp is developed under OpenTox (http://opentox.org)
 * which is an FP7-funded EU research project.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
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
 */
package org.opentox.io.util;

/**
 * This enumeration is a list of services with which YAQP may exchange information
 * and/or exploit them to perform some tasks.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public enum ServerList {

    ambit("http://ambit.uni-plovdiv.bg:8080/ambit2", true, true, true),
    tum("http://opentox.informatik.tu-muenchen.de:8080/OpenTox", false, true, false),
    insilico("http://webservices.in-silico.ch", true, true, true);

    private final String uri;
    private final boolean supportsDataset, supportsFeatures, supportsCompounds;

    private ServerList(final String uri, final boolean sd, final boolean sf, final boolean sc){
        this.uri = uri;
        this.supportsCompounds = sc;
        this.supportsDataset = sd;
        this.supportsFeatures = sf;
    }

    public String getBaseURI(){
        return uri;
    }

    public boolean suppDataset(){
        return this.supportsDataset;
    }

    public boolean suppFeatures(){
        return this.supportsFeatures;
    }

    public boolean suppCompounds() {
        return this.supportsCompounds;
    }


}
