package org.opentox.ontology;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author chung
 */
public class ModelFactory {

    public static TurboOntModel create(){
        TurboOntModel om = new TurboOntModel();
        Map<String, String> map = new HashMap<String, String>();
        map.put("ot", "http://www.opentox.org/api/1.1");
        om.setNsPrefixes(map);
        return (TurboOntModel) om;
    }

    public static void main(String[] args){
        TurboOntModel mm = ModelFactory.create();
        mm.printConsole();
    }

}

