package org.opentox.io.processors;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.processors.Processor;
import org.opentox.io.engines.Engine;
import org.opentox.io.engines.EngineFactory;
import org.opentox.ontology.YaqpOntModel;
import org.restlet.data.MediaType;

/**
 *
 * The main scope of this processor is to retrieve various representations from
 * remote or local locations and pass their content into a YaqpOntModel URI.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class InputProcessor extends Processor<URI, YaqpOntModel> {

    /**
     * HTTP connection used to connect to the remote resource.
     */
    private HttpURLConnection con = null;
    /**
     * Engine used to convert the remote or local representation into an
     * ontological model.
     */
    private Engine engine;

    /**
     * The list of supported mediatypes for input. The list is ordered by preference
     * priority. The MIME application/rdf+xml is always first priority MIME.
     * @return ArrayList of supported mediatypes
     */
    private static List<MediaType> supportedInputMediatypes() {
        List<MediaType> list = new ArrayList<MediaType>(2);
        list.add(MediaType.APPLICATION_RDF_XML);
        list.add(MediaType.APPLICATION_RDF_TURTLE);
        return list;
    }

    /**
     * Check if a certain MIME type is available from the given uri.
     * @param uri the URI of the resource
     * @param mime MIME type.
     * @return <code>true</code> if the specified mime type is available.
     */
    private boolean IsMimeAvailable(URI uri, MediaType mime) {
        HttpURLConnection.setFollowRedirects(false);
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.addRequestProperty("Accept", mime.toString());

            if ((connection.getResponseCode() == 200)) {
                return true;
            } else {
                return false;
            }
        } catch (IOException ex) {
            return false;
        } finally {
            connection.disconnect();
        }
    }

    /**
     * Initialized the connection to the Resource.
     * @param uri URI of the resource
     * @throws MalformedURLException In case the provided URI is malformed
     * @throws IOException In case a communication or access issue occurs, or
     * Internet connection is down.
     */
    private void initializeConnection(URI uri) throws MalformedURLException, IOException {
        HttpURLConnection.setFollowRedirects(true);
        URL dataset_url = uri.toURL();
        con = (HttpURLConnection) dataset_url.openConnection();
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);
    }

    /**
     * Get the first available media type.
     * @param uri URI of the resource.
     * @return The first available mediatype among the supported ones.
     */
    private MediaType getAvailableMediaType(URI uri) {
        MediaType mediatype = null, temp = null;


        Iterator<MediaType> it = supportedInputMediatypes().iterator();
        while (it.hasNext()) {
            temp = it.next();
            if (IsMimeAvailable(uri, temp)) {
                mediatype = temp;
                engine = EngineFactory.createInputEngine(mediatype);
                return mediatype;
            }
        }
        return mediatype;
    }

    /**
     * 
     * @param uri URI of the resource which is accepted as input to YAQP through
     * this processor.
     * @return The resource encapsulated in a {@link YaqpOntModel } object.
     * @throws YaqpException
     */
    public YaqpOntModel process(URI uri) throws YaqpException {
        System.out.println(getAvailableMediaType(uri).toString());
        YaqpOntModel yaqpOntModel = null;
        try {
            initializeConnection(uri);
            yaqpOntModel = engine.getOntModel(con, uri);
        } catch (Exception ex) {
            Logger.getLogger(InputProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return yaqpOntModel;
    }


    

}
