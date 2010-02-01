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
package org.opentox.io.processors;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import org.opentox.core.exceptions.YaqpIOException;
import org.opentox.io.engines.EngineFactory;
import org.opentox.io.util.YaqpIOStream;
import org.opentox.io.engines.IOEngine;
import org.opentox.ontology.TurboOntModel;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.ScrewedUp;
import org.opentox.util.logging.levels.Warning;
import org.restlet.data.MediaType;

/**
 *
 * The main scope of this processor is to retrieve various representations from
 * remote or local locations and pass their content into a YaqpOntModel URI.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class InputProcessor extends AbstractIOProcessor<URI, TurboOntModel> {

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
//        for (int i = 0; i < supportedMediaTypes().size(); i++) {
//            if (IsMimeAvailable(uri, supportedMediaTypes().get(i))) {
//                return supportedMediaTypes().get(i);
//            }
//        }
        return MediaType.APPLICATION_RDF_XML;
    }

    /**
     * Initialized the connection to the Resource.
     * @param uri URI of the resource
     * @throws MalformedURLException In case the provided URI is malformed
     * @throws IOException In case a communication or access issue occurs, or
     * Internet connection is down.
     */
    private HttpURLConnection initializeConnection(URI uri) {
        try {
            HttpURLConnection con = null;
            HttpURLConnection.setFollowRedirects(true);
            URL dataset_url = uri.toURL();
            con = (HttpURLConnection) dataset_url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            media = getAvailableMime(uri);
            con.setRequestProperty("Accept", media.toString());
            return con;
        } catch (MalformedURLException ex) {
            YaqpLogger.LOG.log(new ScrewedUp(getClass(), "Ex :" + ex));
        } catch (IOException ex) {
            YaqpLogger.LOG.log(new ScrewedUp(getClass(), "Ex :" + ex));
        }
        return null;
    }

    /**
     * 
     * @param uri URI of the resource which is accepted as input to YAQP through
     * this processor.
     * @return The resource encapsulated in a {@link TurboOntModel } object.
     * @throws YaqpException
     */
    public TurboOntModel handle(URI uri) throws YaqpIOException {
        TurboOntModel yaqpOntModel = null;
        YaqpIOStream is = null;
        double start = 0, buff_time = 0, parse_time = 0;
        try {
            start = System.currentTimeMillis();
            HttpURLConnection con = initializeConnection(uri);
            InputStream remoteStream = new BufferedInputStream(con.getInputStream(), 4194304*64);
            is = new YaqpIOStream(remoteStream);
            buff_time = System.currentTimeMillis() - start;
            start = System.currentTimeMillis();
            IOEngine engine = EngineFactory.createEngine(media);            
            yaqpOntModel = engine.getYaqpOntModel(is);
            parse_time = System.currentTimeMillis() - start;
                    System.out.println("Buffer Time : "+buff_time+" +  Reading :"+parse_time+" = "+(buff_time+parse_time));

        } catch (Exception ex) {
            YaqpLogger.LOG.log(new Warning(getClass(), ex.toString()));
            throw new YaqpIOException(ex);
        }
        return yaqpOntModel;
    }
}
