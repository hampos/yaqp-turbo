package org.opentox.io.engines;

import org.restlet.data.MediaType;

/**
 *
 * @author chung
 */
public class EngineFactory {

    public static Engine createEngine(final MediaType mediatype) {
        Engine e = null;
        if ((mediatype.equals(MediaType.APPLICATION_RDF_XML)) || (mediatype.equals(MediaType.APPLICATION_RDF_TURTLE))) {
            e = new RDFEngine(mediatype);
        } else if (mediatype.equals(MediaType.APPLICATION_JSON)) {
            throw new UnsupportedOperationException();
        } else {
            throw new UnsupportedOperationException();
        }
        return e;
    }
}
