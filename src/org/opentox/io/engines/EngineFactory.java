package org.opentox.io.engines;

import org.restlet.data.MediaType;

/**
 *
 * @author chung
 */
public class EngineFactory {

    public static IOEngine createEngine(final MediaType mediatype) {
        IOEngine e = null;


        if ((mediatype.equals(MediaType.APPLICATION_RDF_XML)) ) {
            e = new RDFEngine();
        } else if ((mediatype.equals(MediaType.APPLICATION_RDF_TURTLE)) ){
            e = new TurtleEngine();
        } else if (mediatype.equals(MediaType.APPLICATION_JSON)) {
            throw new UnsupportedOperationException();
        } else {
            throw new UnsupportedOperationException();
        }
        return e;
    }
}
