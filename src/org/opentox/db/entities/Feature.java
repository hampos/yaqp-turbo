package org.opentox.db.entities;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.vocabulary.DC;
import org.opentox.ontology.ModelFactory;
import org.opentox.ontology.TurboOntModel;
import org.opentox.ontology.namespaces.OTClass;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Feature {

    private String _uri;

    public Feature(String uri){
        this._uri = uri;
    }

    public String getURI() {
        return _uri;
    }

    public void setURI(String _name) {
        this._uri = _name;
    }


    public TurboOntModel getModel(){
        TurboOntModel model = ModelFactory.createTurboOntModel();
        model.includeOntClass(OTClass.Feature);
        model.createAnnotationProperty(DC.identifier.getURI());

        Individual feature = model.createIndividual(_uri, OTClass.Feature.getOntClass(model));
        feature.addProperty(DC.identifier, model.createTypedLiteral(_uri, XSDDatatype.XSDanyURI));
        /** The result validates as OWL-DL **/
        return model;
    }

    

}
