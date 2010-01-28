package org.opentox.ontology;

import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.impl.OntModelImpl;
import java.io.InputStream;
import org.opentox.io.util.YaqpIOStream;
import org.opentox.ontology.interfaces.JOntModel;
import org.opentox.ontology.namespaces.YaqpOntEntity;

/**
 *
 * @author chung
 */
public class TurboOntModel extends OntModelImpl implements JOntModel{

    public TurboOntModel(){
        super(OntModelSpec.OWL_DL_MEM);
    }

    public TurboOntModel(OntModelSpec spec){
        super(spec);
    }

    public TurboOntModel(YaqpIOStream ioStream){
        super(OntModelSpec.OWL_DL_MEM);
        read( (InputStream)ioStream.getStream(), null);
    }

    public void printConsole(){
        this.write(System.out);
    }

    public void includeOntClass(YaqpOntEntity ont_entity){
        ont_entity.createOntClass(this);
    }

    public void includeOntClasses(YaqpOntEntity[] ont_entities){
        if (ont_entities!=null){
            for (int i = 0 ; i<ont_entities.length ; i++){
                this.includeOntClass(ont_entities[i]);
            }
        }
    }


    

}
