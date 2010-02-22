/*
 *
 * YAQP - Yet Another QSAR Project:
 * Machine Learning algorithms designed for the prediction of toxicological
 * features of chemical compounds become available on the Web. Yaqp is developed
 * under OpenTox (http://opentox.org) which is an FP7-funded EU research project.
 * This project was developed at the Automatic Control Lab in the Chemical Engineering
 * School of National Technical University of Athens. Please read README for more
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

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.OWL;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.opentox.core.exceptions.Cause.*;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.io.exceptions.YaqpIOException;
import org.opentox.io.processors.InputProcessor;
import org.opentox.io.publishable.JSONObject;
import org.opentox.io.publishable.OntObject;
import org.opentox.io.publishable.PDFObject;
import org.opentox.io.publishable.RDFObject;
import org.opentox.io.publishable.TurtleObject;
import org.opentox.io.publishable.UriListObject;
import org.opentox.io.util.YaqpIOStream;
import org.opentox.ontology.namespaces.OTClass;
import org.opentox.ontology.namespaces.OTObjectProperties;
import org.opentox.ontology.util.Meta;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Feature extends YaqpComponent {

    /**
     * URI of the feature
     */
    private String uri = null;
    /**
     * Local yaqp ID of the feature related to the database.
     */
    private int id;
    /**
     * Metadata about the feature...
     */
    private Meta meta = new Meta();
    /**
     * Min and Max value for the ID (useful when this component is used as
     * a prototype for database searching).
     */
    private int
            _minId = Integer.MIN_VALUE,
            _maxId = Integer.MAX_VALUE;

    public Feature() {
        super();
    }

    public Feature(String uri) {
        this.uri = uri;
    }

    public Feature(int id){
        this.id = id;
        this._maxId = id;
        this._minId = id;
    }

    public Feature(int id, String uri) {
        this.uri = uri;
        this.id = id;
        this._maxId = id;
        this._minId = id;
    }

    public int getMaxId() {
        return _maxId;
    }

    public void setMaxId(int _maxId) {
        this._maxId = _maxId;
    }

    public int getMinId() {
        return _minId;
    }

    public void setMinId(int _minId) {
        this._minId = _minId;
    }

    public String getURI() {
        return uri;
    }

    public int getID() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        this._maxId = id;
        this._minId = id;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }


    public void setURI(String _name) {
        this.uri = _name;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }



    @Override
    public String toString() {
        String feature = "";
        feature += "FEATURE ID        : " + getID()+"\n";
        feature += "FEATURE URI       : " + getURI()+".";
        return feature;
    }

    @Override
    public PDFObject getPDF() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RDFObject getRDF() {
        RDFObject rdf = new RDFObject();
        rdf.includeOntClass(OTClass.Feature);
        rdf.createAnnotationProperty(DC.identifier.getURI());

        Individual feaTure = rdf.createIndividual(uri, OTClass.Feature.getOntClass(rdf));


        if (this.uri!=null && !this.uri.equals("")) feaTure.addProperty(DC.identifier, rdf.createTypedLiteral(uri, XSDDatatype.XSDanyURI));
        if (!this.getMeta().title.equals("")) feaTure.addProperty(DC.title, rdf.createTypedLiteral(this.getMeta().title, XSDDatatype.XSDstring));
        if (!this.getMeta().creator.equals("")) feaTure.addProperty(DC.creator, rdf.createTypedLiteral(this.getMeta().creator, XSDDatatype.XSDstring));

        if (getMeta().sameAs!=null && getMeta().sameAs.size()>0){
            ArrayList<String> sameAs_list = getMeta().sameAs;
            for (String remoteFeature_uri : sameAs_list){
                try {
                    URI sameAsuri = new URI(remoteFeature_uri);
                    InputProcessor<OntObject> featureDownloader = new InputProcessor<OntObject>();
                    OntObject featureObject = featureDownloader.process(sameAsuri);
                    RDFObject remoteModel = new RDFObject( featureObject   );
                    StmtIterator it = remoteModel.listStatements(new SimpleSelector(remoteModel.createResource(remoteFeature_uri), OWL.sameAs, (Resource)null));

                    while (it.hasNext()){
                        feaTure.addProperty(OWL.sameAs, it.next().getObject().toString());
                    }
                    
                } catch (YaqpIOException ex) {
                    System.out.println(ex);
                } catch (URISyntaxException ex) { /* What should I do??? */  System.out.println(ex);}
            }
        }

        /** The result validates as OWL-DL **/
        return rdf;
    }

    public static void main(String... args) throws YaqpIOException{
        Feature f = new Feature();
        Meta m = new Meta();
        m.sameAs.add("http://apps.ideaconsult.net:8180/ambit2/feature/20665");
        m.title="Prediction Feature for the model http://opentox.ntua.gr:3030/model/451";
        f.setMeta(m);
        f.getRDF().publish(new YaqpIOStream(System.out));
    }


    @Override
    public JSONObject getJson() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public URI uri() throws YaqpException {
        try {
            return new URI(getURI());
        } catch (URISyntaxException ex) {
            throw new YaqpException(XTC743, "Improper URI", ex);
        }
    }


    @Override
    protected String getTag() {
        return "feature";
    }

    @Override
    public UriListObject getUriList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Feature getSkroutz(){
        return new Feature(this.getID());
    }

    @Override
    public boolean equals(Object obj){
        if(obj.getClass() == this.getClass()){
            Feature feature = (Feature) obj;
            return (this.getID() == feature.getID());
        }else{
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + this.id;
        return hash;
    }
}
