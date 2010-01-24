package org.opentox.io.engines;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import org.opentox.ontology.YaqpOntModel;
import org.restlet.data.MediaType;

/**
 *
 * @author chung
 */
public class EngineFactory {

    public static Engine createInputEngine(final MediaType mediatype) {
        Engine e = null;
        if ((mediatype.equals(MediaType.APPLICATION_RDF_XML)) || (mediatype.equals(MediaType.APPLICATION_RDF_TURTLE))) {
            e = new Engine() {

                public YaqpOntModel getOntModel(HttpURLConnection con, URI uri) throws IOException {
                    YaqpOntModel yaqpOntModel = YaqpOntModel.createOntModel();
                    yaqpOntModel.read(con.getInputStream(), mediatype);
                    return yaqpOntModel;
                }
            };
        }
        return e;
    }
}
