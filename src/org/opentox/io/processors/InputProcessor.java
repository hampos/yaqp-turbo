package org.opentox.io.processors;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.exceptions.YaqpIOException;
import org.opentox.io.engines.EngineFactory;
import org.opentox.io.util.YaqpIOStream;
import org.opentox.io.engines.IOEngine;
import org.opentox.io.engines.RDFEngine;
import org.opentox.ontology.YaqpOntModel;
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
public class InputProcessor extends AbstractIOProcessor<URI, YaqpOntModel> {


    /**
     * Engine used to convert the remote or local representation into an
     * ontological model.
     */
    private MediaType media;

    public InputProcessor() {
        super();
    }

    private static ArrayList<MediaType> supportedMediaTypes(){
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

            if ((connexion.getResponseCode() >= 200) && (connexion.getResponseCode() < 300)) {
                return true;
            } else {
                return false;
            }
        } catch (IOException ex) {
            return false;
        }finally{
            connexion.disconnect();
        }
    }

    private MediaType getAvailableMime(URI uri){
        for (int i=0;i<supportedMediaTypes().size();i++){
            if (IsMimeAvailable(uri, supportedMediaTypes().get(i))){
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
    private HttpURLConnection initializeConnection(URI uri)  {
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
            YaqpLogger.LOG.log(new ScrewedUp(getClass(), "Ex :"+ex));
        } catch (IOException ex) {
            YaqpLogger.LOG.log(new ScrewedUp(getClass(), "Ex :"+ex));
        }
        return null;
    }

    /**
     * 
     * @param uri URI of the resource which is accepted as input to YAQP through
     * this processor.
     * @return The resource encapsulated in a {@link YaqpOntModel } object.
     * @throws YaqpException
     */
    public YaqpOntModel handle(URI uri) throws YaqpIOException {
        YaqpOntModel yaqpOntModel = null;
        YaqpIOStream is = null;
        try {
            HttpURLConnection con = initializeConnection(uri);
            is = new YaqpIOStream(con.getInputStream());
            IOEngine engine = EngineFactory.createEngine(media);
            yaqpOntModel = engine.getYaqpOntModel(is);
        } catch (Exception ex) {
            YaqpLogger.LOG.log(new Warning(getClass(), ex.toString()));
            throw new YaqpIOException(ex);
        } 
        return yaqpOntModel;
    }
}
