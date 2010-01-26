package org.opentox.io.processors;

import java.net.URI;
import org.opentox.core.exceptions.YaqpIOException;
import org.opentox.io.engines.Engine;
import org.opentox.io.engines.EngineFactory;
import org.opentox.ontology.YaqpOntModel;
import org.restlet.data.MediaType;

/**
 *
 * @author hampos
 */
public class OutputProcessor extends AbstractIOProcessor<YaqpOntModel,URI> {

    private Engine engine;

    public OutputProcessor(MediaType mediatype){
        this.engine = EngineFactory.createEngine(mediatype);
    }

    public URI handle(YaqpOntModel i) throws YaqpIOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
