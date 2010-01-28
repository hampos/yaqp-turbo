package org.opentox.ontology;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.impl.OntModelImpl;
import java.io.InputStream;
import org.opentox.io.util.YaqpIOStream;
import org.opentox.ontology.interfaces.JOntModel;
import org.opentox.ontology.namespaces.OTClass;

/**
 *
 * @author chung
 */
public class TurboOntModel extends OntModelImpl implements JOntModel, OntModel{

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

    public void includeOntClass(OTClass ont_class){
        ont_class.createOntClass(this);
    }

    public void includeOntClasses(OTClass[] ont_classes){
        if (ont_classes!=null){
            for (int i = 0 ; i<ont_classes.length ; i++){
                this.includeOntClass(ont_classes[i]);
            }
        }
    }
    

}
