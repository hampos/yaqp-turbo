/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.opentox.db.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author hampos
 */
public class Algorithm implements Serializable{

    public static final long serialVersionUID = -18477218378326540L;

    private ArrayList<AlgorithmOntology> ontologies = null;

    private String name, uri;


    public Algorithm(){

    }

    public Algorithm(String name, String uri, ArrayList<AlgorithmOntology> ontologies){
        setName(name);
        setUri(uri);
        setOntologies(ontologies);
    }

    

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    public ArrayList<AlgorithmOntology> getOntologies(){
        return ontologies;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setOntologies(ArrayList<AlgorithmOntology> ontologies){
        this.ontologies = ontologies;
    }

    @Override
    public String toString(){
        String algorithm = "";
        algorithm += "NAME    :"+getName()+"\n";
        algorithm += "URI     :"+getUri()+"\n";
        algorithm += "ONTOLOGIES:\n";
        Iterator<AlgorithmOntology> it = ontologies.iterator();
        while(it.hasNext()){
            algorithm += it.next().toString();
            algorithm += "\n";
        }        
        return algorithm;
    }




}
