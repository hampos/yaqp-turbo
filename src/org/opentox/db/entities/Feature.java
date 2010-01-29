package org.opentox.db.entities;

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

    

}
