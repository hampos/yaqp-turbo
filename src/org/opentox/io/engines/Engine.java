package org.opentox.io.engines;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import org.opentox.io.interfaces.JEngine;
import org.opentox.ontology.YaqpOntModel;

/**
 *
 * @author chung
 */
public abstract class Engine implements JEngine {

    public abstract YaqpOntModel getOntModel(HttpURLConnection con, URI uri) throws IOException;
}
