package org.opentox.io.engines;


import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.interfaces.JEngine;
import org.opentox.io.util.YaqpIOStream;
import org.opentox.ontology.TurboOntModel;

/**
 *
 * @author chung
 */
public abstract class IOEngine implements JEngine<YaqpIOStream, TurboOntModel> {

   
    public IOEngine() {
        super();
    }

  

    public TurboOntModel ignite(YaqpIOStream fuel) throws YaqpException {
            return getYaqpOntModel(fuel);
    }

    public abstract TurboOntModel getYaqpOntModel(YaqpIOStream stream);

    public YaqpIOStream exhaust(TurboOntModel gas) throws YaqpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
