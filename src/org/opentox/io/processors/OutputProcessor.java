package org.opentox.io.processors;

import java.net.URI;
import org.opentox.core.exceptions.YaqpIOException;
import org.opentox.io.engines.EngineFactory;
import org.opentox.io.engines.IOEngine;
import org.opentox.ontology.TurboOntModel;
import org.restlet.data.MediaType;
import org.restlet.data.Response;

/**
 *
 * This acts like a HTTP POSTer. It posts a representation of a YaqpOntModel (in
 * some mediatype specified in the constructor) to a remote service
 *
 * @author hampos
 */
public class OutputProcessor
        extends AbstractIOProcessor<TurboOntModel,Response>
{

    private IOEngine engine;
   

    public OutputProcessor(){
       
    }

    public Response handle(TurboOntModel i) throws YaqpIOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    



}
