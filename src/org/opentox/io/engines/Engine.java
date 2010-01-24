package org.opentox.io.engines;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import org.opentox.ontology.YaqpOntModel;

/**
 *
 * @author chung
 */
public interface Engine {

    YaqpOntModel getOntModel(HttpURLConnection con, URI uri) throws IOException;

}
