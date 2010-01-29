package org.opentox.db.entities;

import java.io.Serializable;

/**
 * 
 * An ontological class for the description of an algorithm.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class AlgorithmOntology implements Serializable{

    public static final long serialVersionUID = -18477218374326540L;

    public AlgorithmOntology(){

    }

    public AlgorithmOntology(String name, String uri){
        setName(name);
        setUri(uri);
    }

    private String name, uri;

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
    public String toString(){
        String algorithmOntology = "";
        algorithmOntology += "NAME    :"+getName();
        algorithmOntology += "  URI     :"+getUri();
        return algorithmOntology;
    }

    



}
