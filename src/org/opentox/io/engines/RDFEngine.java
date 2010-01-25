package org.opentox.io.engines;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.io.InputStream;
import org.opentox.io.util.YaqpIOStream;
import org.opentox.ontology.YaqpOntModel;

/**
 *
 * @author hampos
 */
public class RDFEngine extends IOEngine {

    public RDFEngine() {
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