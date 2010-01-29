package org.opentox.ontology.namespaces;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import org.opentox.ontology.TurboOntModel;
import org.opentox.ontology.interfaces.JOntEntity;

/**
 *
 * @author chung
 */
public abstract class YaqpOntEntity implements JOntEntity {

    protected static final String _NS_OT = "http://www.opentox.org/api/1.1#%s";
    protected static final String _NS_AlgorithmTypes = "http://www.opentox.org/algorithmTypes.owl%s";
    public static final String NS_OT_core = String.format(_NS_OT, "");
    public static final String NS_AlgorithmTypes = String.format(_NS_AlgorithmTypes, "");

    
    protected static TurboOntModel _model = new TurboOntModel();
    protected Resource _resource;

    public YaqpOntEntity() {
    }

    public YaqpOntEntity(Resource resource) {
        this._resource = resource;
    }

    public OntClass createOntClass(TurboOntModel model) {
        return model.createClass(getURI());
    }

    public Resource getResource() {
        return this._resource;
    }

    public OntClass getOntClass(TurboOntModel model) {
        OntClass cl = model.getOntClass(getURI());
        if (cl==null){
            cl = createOntClass(model);
        }
        return cl;
    }

    public String getURI() {
        return _resource.getURI();
    }

    public Property createProperty(TurboOntModel model) {
        return _model.createProperty(getURI());
    }
}
