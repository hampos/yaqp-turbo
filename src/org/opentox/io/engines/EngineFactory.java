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
            System.out.println("Engine: RDF");
            e = new RDFEngine();
        } else if ((mediatype.equals(MediaType.APPLICATION_RDF_TURTLE)) ){
            System.out.println("Engine: Turtle");
            e = new TurtleEngine();
        } else if (mediatype.equals(MediaType.APPLICATION_JSON)) {
            System.out.println("Engine: No");
            throw new UnsupportedOperationException();
        } else {
            throw new UnsupportedOperationException();
        }
        return e;
    }
}
