package org.opentox.io.engines;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.io.InputStream;
import org.opentox.io.util.YaqpIOStream;
import org.opentox.ontology.YaqpOntModel;
import org.restlet.data.MediaType;


/**
 *
 * @author hampos
 */
public class TurtleEngine extends IOEngine {



    public TurtleEngine(){
        super();
    }








   @Override
    public YaqpOntModel getYaqpOntModel(YaqpIOStream is) {
        OntModel model = ModelFactory.createOntologyModel();
        model.read((InputStream) is.getStream(), null);
        YaqpOntModel yaqpOntModel = new YaqpOntModel(model);
        return yaqpOntModel;
    }

}
