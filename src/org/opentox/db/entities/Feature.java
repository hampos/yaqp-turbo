/*
 * YAQP - Yet Another QSAR Project: Machine Learning algorithms designed for
 * the prediction of toxicological features of chemical compounds become
 * available on the Web. Yaqp is developed under OpenTox (http://opentox.org)
 * which is an FP7-funded EU research project.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
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
 */
package org.opentox.db.entities;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.vocabulary.DC;
import java.io.Serializable;
import org.opentox.ontology.ModelFactory;
import org.opentox.ontology.TurboOntModel;
import org.opentox.ontology.namespaces.OTClass;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Feature implements Serializable {

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


    public TurboOntModel getModel(){
        TurboOntModel model = ModelFactory.createTurboOntModel();
        model.includeOntClass(OTClass.Feature);
        model.createAnnotationProperty(DC.identifier.getURI());

        Individual feature = model.createIndividual(_uri, OTClass.Feature.getOntClass(model));
        feature.addProperty(DC.identifier, model.createTypedLiteral(_uri, XSDDatatype.XSDanyURI));
        /** The result validates as OWL-DL **/
        return model;
    }

    

}
