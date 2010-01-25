package org.opentox.ontology;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFReader;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.shared.Lock;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.opentox.ontology.namespaces.OTNS;
import org.restlet.data.MediaType;

/**
 * This is a proxy class for {@link com.hp.hpl.jena.ontology.OntModel OntModel}.
 * <code>OntModel</code> is an essential interface in Jena and instances of
 * <code>OntModel </code> are generated by the {@link com.hp.hpl.jena.rdf.model.ModelFactory}.
 * In <code>YaqpOntoModel</code>, instances of it are obtained through its static
 * constructors like {@link YaqpOntModel#createOntModel()  }
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class YaqpOntModel {


    private OntModel jenaModel;

    private URI uri;

    /**
     * This is a private constructor that generates new instances of YaqpOntModel.
     *
     * @param specifications Ontological Specifications.
     */
    public YaqpOntModel(final OntModel jenaModel) {
        this.jenaModel = jenaModel;
    }

    
    /**
     * This is intended to be used by RED-related engines to write this object
     * to some OutputStream in some RDF-related mediatype.
     * 
     * @param out
     * @param mediatype
     */
    public void write(OutputStream out, MediaType mediatype){
        jenaModel.write(out, getJenaFormat(mediatype));
    }

    private String getJenaFormat(MediaType mediaType) {
    
        if (mediaType.equals(MediaType.APPLICATION_RDF_TURTLE)) {
            return "TURTLE";
        } else if (mediaType.equals(MediaType.TEXT_RDF_N3)) {
            return "N3";
        } else if (mediaType.equals(MediaType.TEXT_RDF_NTRIPLES)) {
            return "N-TRIPLE";
        } else {
            return "RDF/XML";
        }
    }


    public void printConsole(String LANG) {
        jenaModel.write(System.out, LANG);
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(final URI uri) {
        this.uri = uri;
    }

    public OTNS.OTClass getType(){
        System.out.println(uri.toString());
        StmtIterator typeIterator = 
                jenaModel.listStatements(new SimpleSelector(jenaModel.createOntResource(uri.toString()), RDF.type, (Resource)null));
        System.out.println("b");
        if (typeIterator.hasNext()){
                System.out.println("c");
            System.out.println(typeIterator.next().getObject().as(Resource.class).getURI());
        }
        
        return null;
    }

   

    
}