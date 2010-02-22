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

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import org.opentox.config.Configuration;
import org.opentox.core.exceptions.Cause;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.io.exceptions.YaqpIOException;
import org.opentox.io.interfaces.JPublishable;
import org.opentox.io.publishable.JSONObject;
import org.opentox.io.publishable.PDFObject;
import org.opentox.io.publishable.RDFObject;
import org.opentox.io.publishable.TurtleObject;
import org.opentox.io.publishable.UriListObject;
import org.opentox.ontology.exceptions.ImproperEntityException;
import org.restlet.data.MediaType;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class YaqpComponent implements Serializable {

    public YaqpComponent() {
    }

    public JPublishable getPublishable(MediaType mediatype) throws ImproperEntityException{

        if (mediatype.equals(PDFObject.media)){
            return getPDF();
        } else if (mediatype.equals(RDFObject.media)){
            return getRDF();
        } else if (mediatype.equals(TurtleObject.media)){
            return getTurtle();
        } else if (mediatype.equals(UriListObject.media)){
            return getUriList();
        } else {
            throw new ImproperEntityException(Cause.XIE790,
                    "This media type is not supported : {"+mediatype+"}");
        }
        
    }

    /**
     * Returns a publishable version of the component in pdf format.
     * @return PDFObject for the component.
     */
    public abstract PDFObject getPDF();
    /**
     * A publishable version of the component in RDF format (application/rdf+xml).
     * @return RDFObject for the component
     */
    public abstract RDFObject getRDF();
    /**
     * A publishable version of the component in JSON format (as an instance of
     * <code>JSONObject</code>)
     * @return JSONObject for the component.
     */
    public abstract JSONObject getJson();

    public abstract UriListObject getUriList();

    /**
     * A publishable version of the component in TURTLE (TTL) format as an
     * instance of {@link TurtleObject }. The implementation is based on the
     * implementation of the abstract method {@link YaqpComponent#getRDF() getRDF()} from
     * the subclasses of {@link YaqpComponent } like {@link Algorithm }.
     * @return TurtleObject for the component.
     */
    public TurtleObject getTurtle() {
        TurtleObject o = new TurtleObject(getRDF());
        return (TurtleObject) o;
    }


    public URI uri() throws YaqpException{
        try {
            return new URI(Configuration.BASE_URI + "/" + getTag());
        } catch (URISyntaxException ex) {
            throw new YaqpException(Cause.XTC743, "Invalid URI", ex);
        }
    }

    protected abstract String getTag();
    public abstract YaqpComponent getSkroutz();
    
}
