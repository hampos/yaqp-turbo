/*
 *
 *   ._.  ._.      ,__,   ._____.
 *    \ \ | |/\   /    \ /  ___ |
 *     \ \| /  \ |  ()  |  _____|
 *      \  / /\ \|  {}  | |
 *       || .--. |  [] \\ |
 *       ||_|  |_|\____/\\|      --.-.--.-...-..--..-.
 *
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
package org.opentox.io.processors;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.io.engines.EngineFactory;
import org.opentox.io.util.YaqpIOStream;
import org.opentox.io.engines.IOEngine;
import org.opentox.io.exceptions.YaqpIOException;
import org.opentox.io.publishable.OntObject;
import org.opentox.ontology.exceptions.YaqpOntException;
import org.restlet.data.MediaType;
import static org.opentox.core.exceptions.Cause.*;

/**
 *
 * The main scope of this processor is to retrieve various representations from
 * remote or local locations and pass their content into a YaqpOntModel URI.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
@SuppressWarnings({"unchecked"})
public class InputProcessor<O extends OntObject> extends AbstractIOProcessor<URI, O> {

    /**
     * Engine used to convert the remote or local representation into an
     * ontological model.
     */
    private MediaType media;

    public InputProcessor() {
        super();
    }

    private static ArrayList<MediaType> supportedMediaTypes() {
        ArrayList<MediaType> list = new ArrayList<MediaType>();
        list.add(MediaType.APPLICATION_RDF_XML);
        list.add(MediaType.APPLICATION_RDF_TURTLE);
        return list;
    }

    private boolean IsMimeAvailable(URI serviceUri, MediaType mime) {

        HttpURLConnection.setFollowRedirects(true);
        HttpURLConnection connexion = null;
        try {
            connexion = (HttpURLConnection) serviceUri.toURL().openConnection();
            connexion.setDoInput(true);
            connexion.setDoOutput(true);
            connexion.setUseCaches(false);
            connexion.addRequestProperty("Accept", mime.toString());
            if (connexion.getResponseCode() == 200) {
                return true;
            } else {
                return false;
            }
        } catch (IOException ex) {
            return false;
        } finally {
            connexion.disconnect();
        }
    }

    private MediaType getAvailableMime(URI uri) {
        for (int i = 0; i < supportedMediaTypes().size(); i++) {
            if (IsMimeAvailable(uri, supportedMediaTypes().get(i))) {
                return supportedMediaTypes().get(i);
            }
        }
        return null;
    }

    /**
     * Initialized the connection to the Resource.
     * @param uri URI of the resource
     * @throws MalformedURLException In case the provided URI is malformed
     * @throws IOException In case a communication or access issue occurs, or
     * Internet connection is down.
     */
    private HttpURLConnection initializeConnection(URI uri) throws YaqpIOException {
        HttpURLConnection con = null;
        try {
            media = getAvailableMime(uri);
            if (media == null) {
                return null;
            }
            HttpURLConnection.setFollowRedirects(true);
            URL dataset_url = uri.toURL();
            con = (HttpURLConnection) dataset_url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Accept", media.toString());
            return con;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 
     * @param uri URI of the resource which is accepted as input to YAQP through
     * this processor.
     * @return The resource encapsulated in a {@link OntObject } object.
     * @throws YaqpException
     */
    public O handle(URI uri) throws YaqpException {
        if (uri==null){
            throw new NullPointerException("The provided uri in the InputProcessor in null");
        }
        O yaqpOntModel = null;
        YaqpIOStream is = null;
        InputStream remoteStream = null;
        try {
            HttpURLConnection con = initializeConnection(uri);
            if (con == null) {
                throw new YaqpIOException(XIO54, "Communication Error with the remote at " + uri);
            }
            remoteStream = new BufferedInputStream(con.getInputStream(), 4194304);
            is = new YaqpIOStream(remoteStream);
            IOEngine engine = EngineFactory.createEngine(media);
            try {
                yaqpOntModel = (O) engine.ignite(is);
                return yaqpOntModel;
            } catch (YaqpOntException ex) {
                throw new YaqpOntException(XONT5,
                        "Unable to parse the content of the resource", ex);
            }

        } catch (IOException ex) {
            throw new YaqpIOException(XIO76, "Cannot read from input stream of " + uri.toString(), ex);
        } finally {
            try {
                if (remoteStream != null) {
                    remoteStream.close();
                }
            } catch (IOException ex) {
                throw new YaqpIOException(XIO77, "Remote Stream could not close", ex);
            }
        }


    }
}
