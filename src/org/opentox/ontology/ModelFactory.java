package org.opentox.ontology;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;
import java.util.HashMap;
import java.util.Map;
import org.opentox.ontology.namespaces.OTClass;

/**
 *
 * @author chung
 */
public class ModelFactory {

    
    public static TurboOntModel createTurboOntModel() {
        TurboOntModel om = new TurboOntModel();
        Map<String, String> map = new HashMap<String, String>();
        map.put("ot", OTClass.NS_OT_core);
        map.put("ota", OTClass.NS_AlgorithmTypes);
        om.setNsPrefixes(map);
        return (TurboOntModel) om;
    }

    public static void main(String[] args) {
        TurboOntModel mm = ModelFactory.createTurboOntModel();
        mm.includeOntClass(OTClass.Parameter);        

        Individual in = mm.createIndividual(new ResourceImpl("http://sth.com/dataset/1234"));
        
        mm.printConsole();

    }
}

