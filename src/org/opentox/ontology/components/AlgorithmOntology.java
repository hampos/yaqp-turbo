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

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.core.exceptions.Cause;
import org.opentox.io.publishable.JSONObject;
import org.opentox.io.publishable.PDFObject;
import org.opentox.io.publishable.RDFObject;
import org.opentox.io.publishable.TurtleObject;
import org.opentox.io.publishable.UriListObject;
import org.opentox.ontology.exceptions.YaqpOntException;
import org.opentox.ontology.namespaces.OTAlgorithmTypes;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Trace;

/**
 *
 * An algorithm ontology is itself an ontological entity used in Yaqp.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class AlgorithmOntology extends YaqpComponent {

    //public static final long serialVersionUID = -18477218374326540L;
    private String name = null,
                   uri = null;
    private OTAlgorithmTypes type;

    public AlgorithmOntology() {
        super();
    }

    public AlgorithmOntology(OTAlgorithmTypes type) {
        this.type = type;
        setName(type.getResource().getLocalName());
        setUri(type.getURI());
    }

    /**
     * Construct a new Instance of <code>AlgorithmOntology</code> given its name.
     * Note that it is not possible to construct such an instance for any arbitrary
     * name you want. The name has to correspond to an existing algorithm ontology
     * registered in {@link OTAlgorithmTypes }.
     * @param name Name of some existing ontology (case sensitive).
     * @throws YaqpOntException In case the ontology does not exist (code: XAT982)
     */
    public AlgorithmOntology(String name) throws YaqpOntException {
        super();
        setName(name);
        OTAlgorithmTypes o = new OTAlgorithmTypes();
        try {
            Field tp = ((Class) o.getClass()).getDeclaredField(name);
            tp.setAccessible(true);
            type = (OTAlgorithmTypes) tp.get(o);
            setUri(type.getURI());
        } catch (Exception ex) {
            String message = "No such algorithm ontology : " + name;
            YaqpLogger.LOG.log(new Trace(getClass(), message));
            throw new YaqpOntException(Cause.XONT517,message, ex);
        }
    }


    public OTAlgorithmTypes getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        String algorithmOntology = "";
        algorithmOntology += "Ont : ( " + getName() + " , " + getUri() + " )";
        return algorithmOntology;
    }


    public static void main(String[] args) throws YaqpOntException {
        AlgorithmOntology ao = new AlgorithmOntology("Classification");
        System.out.println(ao.getName());
        System.out.println(ao.getUri());
    }

    @Override
    public PDFObject getPDF() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RDFObject getRDF() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TurtleObject getTurtle() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public JSONObject getJson() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected String getTag() {
        return "ontology";
    }

    @Override
    public UriListObject getUriList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AlgorithmOntology getSkroutz(){
        try {
             return new AlgorithmOntology(this.getName());
        } catch (YaqpOntException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    @Override
    public boolean equals(Object obj){
        if(obj.getClass() == this.getClass()){
            AlgorithmOntology ont = (AlgorithmOntology) obj;
            boolean result = (getName()==null && ont.getName() == null);
            return result || (this.getName().equals(ont.getName()));
        }else{
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }
}
