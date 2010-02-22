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

import java.net.URI;
import java.net.URISyntaxException;
import org.opentox.core.exceptions.Cause;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.io.publishable.JSONObject;
import org.opentox.io.publishable.PDFObject;
import org.opentox.io.publishable.RDFObject;
import org.opentox.io.publishable.TurtleObject;
import org.opentox.io.publishable.UriListObject;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class OmegaModel extends YaqpComponent {

    private String dataset_uri = null;
    private String code = null;
    private User user = new User();
    private String timestamp = null;
    private int id = 0;
    private int _minId = Integer.MIN_VALUE, _maxId = Integer.MAX_VALUE;


    public OmegaModel() {
        super();
    }

    public OmegaModel(String dataset_uri, String code, User user) {
        this.dataset_uri = dataset_uri;
        this.code = code;
        this.user = user;
    }

    public OmegaModel(int id, String code, User user, String dataset_uri, String timestamp ) {
        this.id=id;
        this._maxId = id;
        this._minId = id;
        this.dataset_uri = dataset_uri;
        this.code = code;
        this.user = user;
        this.timestamp = timestamp;
    }

    private OmegaModel(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDataset() {
        return dataset_uri;
    }

    public void setDataset(String dataset_uri) {
        this.dataset_uri = dataset_uri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        this._maxId = id;
        this._minId = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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
        return new QSARModel().getTag();
    }

    @Override
    public URI uri() throws YaqpException {
        String superUri = super.uri().toString();
        try {
            return new URI(superUri + "/mad" + getId());
        } catch (URISyntaxException ex) {
            throw new YaqpException(Cause.XTC743, "Improper URI", ex);
        }
    }

    @Override
    public UriListObject getUriList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public OmegaModel getSkroutz(){
        return new OmegaModel(this.getId());
    }

    @Override
    public boolean equals(Object obj){
        if(obj.getClass() == this.getClass()){
            OmegaModel model = (OmegaModel) obj;
            return (this.getId() == model.getId());
        }else{
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + this.id;
        return hash;
    }
}
