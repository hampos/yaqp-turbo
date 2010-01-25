package org.opentox.io.engines;


import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.interfaces.JEngine;
import org.opentox.io.util.YaqpIOStream;
import org.opentox.ontology.YaqpOntModel;

/**
 *
 * @author chung
 */
public abstract class IOEngine implements JEngine<YaqpIOStream, YaqpOntModel> {

   
    public IOEngine() {
        super();
    }

  

    public YaqpOntModel ignite(YaqpIOStream fuel) throws YaqpException {
            return getYaqpOntModel(fuel);
    }

    public abstract YaqpOntModel getYaqpOntModel(YaqpIOStream stream);

    public YaqpIOStream exhaust(YaqpOntModel gas) throws YaqpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
