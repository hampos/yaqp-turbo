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

package org.opentox.io.publishable;

import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.impl.OntModelImpl;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.opentox.io.util.YaqpIOStream;
import org.opentox.io.interfaces.JOntModel;
import org.opentox.ontology.namespaces.OTClass;
import org.opentox.ontology.namespaces.YaqpOntEntity;
import org.restlet.data.MediaType;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class OntObject extends OntModelImpl implements JOntModel{

    public OntObject(){
        super(OntModelSpec.OWL_DL_MEM);
        Map<String, String> map = new HashMap<String, String>();
        map.put("ot", OTClass.NS_OT_core);
        map.put("ota", OTClass.NS_AlgorithmTypes);
        this.setNsPrefixes(map);
    }

    public OntObject(OntObject other){
        super(OntModelSpec.OWL_DL_MEM, other);
    }

    public OntObject(OntModelSpec spec){
        super(spec);
    }

    public OntObject(YaqpIOStream ioStream){
        super(OntModelSpec.OWL_DL_MEM);
        try{
        read( (InputStream)ioStream.getStream(), null);
        } catch (ClassCastException ex){
            throw new ClassCastException("The IO stream you provided cannot be used as an Input Stream " +
                    "for the construction of an OntObject.");
        }
    }

    public void printConsole(){
        this.write(System.out);
    }

    public void includeOntClass(YaqpOntEntity ont_entity){
        ont_entity.createOntClass(this);
    }

    public void includeOntClasses(YaqpOntEntity[] ont_entities){
        if (ont_entities!=null){
            for (int i = 0 ; i<ont_entities.length ; i++){
                this.includeOntClass(ont_entities[i]);
            }
        }else {
            throw new NullPointerException("The set of entities you provided is null.");
        }
    }

    public void createAnnotationProperties(String[] annotation_uris){
        if (annotation_uris!=null){
            for (int i=0;i<annotation_uris.length;i++){
                createAnnotationProperty(annotation_uris[i]);
            }
        }else{
            throw new NullPointerException("The set of annotation property URIs you " +
                    "provided is null.");
        }
    }

    public void createDataTypeProperties(String[] datatype_uris){
        if (datatype_uris!=null){
            for (int i=0;i<datatype_uris.length;i++){
                createAnnotationProperty(datatype_uris[i]);
            }
        }else{
            throw new NullPointerException("The set of datatype property URIs you " +
                    "provided is null.");
        }
    }

    public void createObjectProperties(String[] object_uris){
        if (object_uris!=null){
            for (int i=0;i<object_uris.length;i++){
                createObjectProperty(object_uris[i]);
            }
        }else{
            throw new NullPointerException("The set of object property URIs you " +
                    "provided is null.");
        }
    }

    public void createSymmetricProperties(String[] symmetric_uris){
        if (symmetric_uris!=null){
            for (int i=0;i<symmetric_uris.length;i++){
                createSymmetricProperty(symmetric_uris[i]);
            }
        }else{
            throw new NullPointerException("The set of symmetric propertiy URIs you " +
                    "provided is null.");
        }
    }

    public void createTransitiveProperties(String[] transitive_uris){
        if (transitive_uris!=null){
            for (int i=0;i<transitive_uris.length;i++){
                createTransitiveProperty(transitive_uris[i]);
            }
        }else{
            throw new NullPointerException("The set of transitive propertiy URIs you " +
                    "provided is null.");
        }
    }

    public MediaType getMediaType() {
        throw new UnsupportedOperationException(
                "Dont ask for the mediaType from the OntObject - get it from its subclasses!");
    }



    

}
