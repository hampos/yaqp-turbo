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
package org.opentox.ontology.namespaces;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import org.opentox.ontology.TurboOntModel;
import org.opentox.ontology.interfaces.JOntEntity;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class YaqpOntEntity implements JOntEntity {

    protected static final String _NS_OT = "http://www.opentox.org/api/1.1#%s";
    protected static final String _NS_AlgorithmTypes = "http://www.opentox.org/algorithmTypes.owl%s";
    public static final String NS_OT_core = String.format(_NS_OT, "");
    public static final String NS_AlgorithmTypes = String.format(_NS_AlgorithmTypes, "");

    
    protected static TurboOntModel _model = new TurboOntModel();
    protected Resource _resource;

    public YaqpOntEntity() {
    }

    public YaqpOntEntity(Resource resource) {
        this._resource = resource;
    }

    public OntClass createOntClass(TurboOntModel model) {
        return model.createClass(getURI());
    }

    public Resource getResource() {
        return this._resource;
    }

    public OntClass getOntClass(TurboOntModel model) {
        OntClass cl = model.getOntClass(getURI());
        if (cl==null){
            cl = createOntClass(model);
        }
        return cl;
    }

    public String getURI() {
        return _resource.getURI();
    }

    public Property createProperty(TurboOntModel model) {
        return _model.createProperty(getURI());
    }
}
