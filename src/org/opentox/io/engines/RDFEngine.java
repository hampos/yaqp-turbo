package org.opentox.io.engines;

import org.opentox.io.util.YaqpIOStream;
import org.opentox.ontology.TurboOntModel;

/**
 *
 * @author hampos
 */
public class RDFEngine extends IOEngine {

    public RDFEngine() {
        super();
    }

    @Override
    public TurboOntModel getYaqpOntModel(YaqpIOStream is) {
        return new TurboOntModel(is);
    }
}
