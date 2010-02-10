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
import com.hp.hpl.jena.vocabulary.DC;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.io.publishable.JSONObject;
import org.opentox.io.publishable.PDFObject;
import org.opentox.io.publishable.RDFObject;
import org.opentox.io.publishable.TurtleObject;
import org.opentox.ontology.exceptions.ImproperEntityException;
import org.opentox.ontology.namespaces.OTClass;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Feature extends YaqpComponent {

    private String uri;
    private int id;

    public Feature() {
        super();
    }

    public Feature(String uri) {
        this.uri = uri;
    }

    public Feature(int id, String uri) {
        this.uri = uri;
        this.id = id;
    }

    public String getURI() {
        return uri;
    }

    public int getID() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
    

    public void setURI(String _name) {
        this.uri = _name;
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

        Individual feature = rdf.createIndividual(uri, OTClass.Feature.getOntClass(rdf));
        feature.addProperty(DC.identifier, rdf.createTypedLiteral(uri, XSDDatatype.XSDanyURI));
        /** The result validates as OWL-DL **/
        return rdf;
    }

    @Override
    public TurtleObject getTurtle() {
        TurtleObject rdf = new TurtleObject();
        rdf.includeOntClass(OTClass.Feature);
        rdf.createAnnotationProperty(DC.identifier.getURI());

        Individual feature = rdf.createIndividual(uri, OTClass.Feature.getOntClass(rdf));
        feature.addProperty(DC.identifier, rdf.createTypedLiteral(uri, XSDDatatype.XSDanyURI));
        /** The result validates as OWL-DL **/
        return rdf;
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
            throw new YaqpException("XLI9", "Improper URI", ex);
        }
    }


    @Override
    protected String getTag() {
        return "feature";
    }
}
