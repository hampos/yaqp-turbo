package org.opentox.io.engines;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
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
