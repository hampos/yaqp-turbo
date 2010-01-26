package org.opentox.io.engines;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import org.opentox.ontology.YaqpOntModel;
import org.restlet.data.MediaType;


/**
 *
 * @author hampos
 */
public class RDFEngine extends Engine {

    MediaType mediatype;
    
    public RDFEngine(MediaType mediatype){
        this.mediatype = mediatype;
    }

    public YaqpOntModel getOntModel(HttpURLConnection con, URI uri) throws IOException {
        YaqpOntModel yaqpOntModel = YaqpOntModel.createOntModel();
        yaqpOntModel.read(con.getInputStream(), mediatype);
        return yaqpOntModel;
    }

    public String getRepresentation(YaqpOntModel m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
