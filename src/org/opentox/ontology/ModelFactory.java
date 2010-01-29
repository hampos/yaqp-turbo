package org.opentox.ontology;

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

    

  
}

