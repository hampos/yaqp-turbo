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
package org.opentox.ontology.interfaces;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import org.opentox.ontology.TurboOntModel;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface JOntEntity {

    /**
     * Creates a new Ontological class for an Ontological Model.
     * @param model The ontological model.
     * @return The generated ontological class.
     */
    OntClass createOntClass(final TurboOntModel model);

    /**
     * Generates a property out of a given model.
     * @param model An ontological model.
     * @return The corresponding property.
     */
    Property createProperty(final TurboOntModel model);

    /**
     * Returns the corresponding Ontological Class (i.e. an instance of
     * {@link com.hp.hpl.jena.ontology.OntClass } )
     * @param model The ontological model
     * @return the ontological class of the model
     */
    OntClass getOntClass(final TurboOntModel model);

    /**
     * Returns the Resource of this class
     * ( {@link org.opentox.interfaces.IOntClass } ).
     * @return the corresponding jena resource.
     */
    Resource getResource();

    /**
     * Returns the URI of the class
     * @return class URI
     */
    String getURI();
}
