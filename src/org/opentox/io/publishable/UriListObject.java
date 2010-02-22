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
package org.opentox.io.publishable;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import org.opentox.io.interfaces.JPublishable;
import org.opentox.io.util.YaqpIOStream;
import org.restlet.data.MediaType;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class UriListObject implements JPublishable {

    private ArrayList<URI> uriList;
    public static final MediaType media = MediaType.TEXT_URI_LIST;

    public UriListObject() {
    }

    public UriListObject(ArrayList<URI> list) {
        this.uriList = list;
    }

    public void publish(YaqpIOStream stream) {
        OutputStream outputStream = (OutputStream) stream.getStream();
        final String NEWLINE = "\n";
        for (final URI uri : uriList) {
            String s = uri.toString()+NEWLINE;
            byte[] byte_array = new byte[s.length()];
            s.getBytes(0, s.length(), byte_array, 0);
            try {
                outputStream.write(byte_array);
                outputStream.flush();
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }




    }

    public MediaType getMediaType() {
        return media;
    }
}
