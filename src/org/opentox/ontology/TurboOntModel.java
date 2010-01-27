package org.opentox.ontology;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.impl.OntModelImpl;
import java.io.InputStream;
import org.opentox.io.util.YaqpIOStream;
import org.opentox.ontology.interfaces.JOntModel;

/**
 *
 * @author chung
 */
public class TurboOntModel extends OntModelImpl implements JOntModel, OntModel{

    public TurboOntModel(){
        super(OntModelSpec.OWL_DL_MEM);
    }

    public TurboOntModel(YaqpIOStream ioStream){
        super(OntModelSpec.OWL_DL_MEM);
        read((InputStream)ioStream.getStream(), null);
    }

    public void printConsole(){
        this.write(System.out);
    }
    

}
