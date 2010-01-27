package org.opentox.io.engines;

import org.opentox.io.util.YaqpIOStream;
import org.opentox.ontology.TurboOntModel;


/**
 *
 * @author hampos
 */
public class TurtleEngine extends IOEngine {



    public TurtleEngine(){
        super();
    }


   @Override
    public TurboOntModel getYaqpOntModel(YaqpIOStream is) {       
        return new TurboOntModel();
    }

}
