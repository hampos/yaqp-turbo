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

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import org.opentox.ontology.TurboOntModel;
import org.opentox.ontology.exceptions.ImproperEntityException;
import org.opentox.ontology.exceptions.YaqpOntException;
import org.opentox.ontology.namespaces.OTClass;
import weka.core.FastVector;
import weka.core.Instances;

/**
 *
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Dataset extends YaqpOntComponent{

    
    public Dataset(TurboOntModel model) {
        super(model);
    }

    
    // <editor-fold defaultstate="collapsed" desc="get the Instances from the model">
    public Instances getInstances() throws YaqpOntException{

        /*
         *
         * Some initial definitions:
         */
        Resource _DataEntry = OTClass.DataEntry.getOntClass(yaqpOntModel),
                _Dataset = OTClass.Dataset.getOntClass(yaqpOntModel),
                _Feature = OTClass.Feature.getOntClass(yaqpOntModel),
                _NumericFeature = OTClass.NumericFeature.getOntClass(yaqpOntModel),
                _NominalFeature = OTClass.NominalFeature.getOntClass(yaqpOntModel);

        FastVector attributes = null;

        Instances data = null;

        StmtIterator dataSetIterator = null,
                featureIterator = null,
                valuesIterator = null,
                dataEntryIterator = null;

        String relationName = null;

        /*
         *
         * Iterate over all nodes in the dataset having type 'ot:Dataset'.
         */
        dataSetIterator =
                yaqpOntModel.listStatements(new SimpleSelector(null, RDF.type, _Dataset));

        if (dataSetIterator.hasNext()) {
            relationName = dataSetIterator.next().getSubject().getURI();
            if (dataSetIterator.hasNext()) {
                throw new YaqpOntException("XN311 - More than one datasets found");
            }
        } else {
            // this is not a dataset model
            throw new ImproperEntityException("XN312 - Not a dataset");
        }
        dataSetIterator.close();

        System.out.println(relationName);

        /**
         *
         *
         */
        return data;
    }
    // </editor-fold>

}
