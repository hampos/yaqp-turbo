package org.opentox.io.interfaces;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import org.opentox.ontology.YaqpOntModel;

/**
 *
 * @author hampos
 */
public interface JEngine {

    public YaqpOntModel getOntModel(HttpURLConnection con, URI uri) throws IOException;

    public String getRepresentation(YaqpOntModel m);
    
}
