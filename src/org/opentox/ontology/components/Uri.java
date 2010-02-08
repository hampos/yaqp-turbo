/*
 *
 * YAQP - Yet Another QSAR Project:
 * Machine Learning algorithms designed for the prediction of toxicological
 * features of chemical compounds become available on the Web. Yaqp is developed
 * under OpenTox (http://opentox.org) which is an FP7-funded EU research project.
 * This project was developed at the Automatic Control Lab in the Chemical Engineering
 * School of the National Technical University of Athens. Please read README for more
 * information.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact:
 * Pantelis Sopasakis
 * chvng@mail.ntua.gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 */


package org.opentox.ontology.components;

import com.hp.hpl.jena.vocabulary.OWL;
import org.opentox.io.publishable.JSONObject;
import org.opentox.io.publishable.PDFObject;
import org.opentox.io.publishable.RDFObject;
import org.opentox.io.util.YaqpIOStream;
import org.opentox.ontology.namespaces.YaqpOntEntity;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Uri extends YaqpComponent {
    
    private String uri;
    private YaqpOntEntity ontology;

    /**
     * Constructs a void URI. Includes an invokation to the constructor of
     * the Class <code>YaqpComponent</code>
     */
    public Uri(){
        super();
    }

    /**
     * Construct a Uri Component with a given URI and a generic ontology
     * <code>http://www.w3.org/2002/07/owl#Thing</code>
     * @param uri URI
     */
    public Uri(String uri){
        this.uri = uri;
        this.ontology = new YaqpOntEntity(OWL.Thing);
    }

    /**
     * Constructs a new Uri for a given URI String and a given ontology. The ontology
     * is provided as an instance of <code>YaqpOntEntity</code>.
     * @param uri URI
     * @param ontology Ontology of the URI
     */
    public Uri(String uri, YaqpOntEntity ontology){
        this(uri);
        this.ontology = ontology;
    }

    /**
     * The URI as a string.
     * @return URI String.
     */
    public String getUri(){
        return this.uri;
    }

    /**
     * The ontology of the URI. If not defined upon construction , the ontology
     * corresponds to owl:Thing, i.e. <code>http://www.w3.org/2002/07/owl#Thing</code>.
     * @return The ontological type of the URI as an instance of <code>YaqpOntEntity</code>
     */
    public YaqpOntEntity getOntology() {
        return ontology;
    }


    @Override
    public PDFObject getPDF() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RDFObject getRDF() {
        RDFObject rdf = new RDFObject();
        rdf.includeOntClass(ontology);
        rdf.createIndividual(uri, rdf.createOntResource(ontology.getURI()));
        return rdf;
    }

    public static void main(String args[]){
        new Uri("http://sth.com/1", new YaqpOntEntity(OWL.Nothing)).
                getTurtle().publish(new YaqpIOStream(System.out));
    }   

    @Override
    public JSONObject getJson() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}