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
package org.opentox.ontology.interfaces;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import java.util.Set;
import org.opentox.io.publishable.OntObject;

/**
 *
 * An interface for all ontological entities in YAQP (including ontological classes
 * and properties).
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface JOntEntity {

    /**
     * Creates a new Ontological class for an Ontological Model.
     * @param model The ontological model.
     * @return The generated ontological class.
     */
    OntClass createOntClass(final OntObject model);

    /**
     * Generates a property out of a given model.
     * @param model An ontological model.
     * @return The corresponding property.
     */
    Property createProperty(final OntObject model);

    /**
     * Returns the corresponding Ontological Class (i.e. an instance of
     * {@link com.hp.hpl.jena.ontology.OntClass } )
     * @param model The ontological model
     * @return the ontological class of the model
     */
    OntClass getOntClass(final OntObject model);

    /**
     * Returns the Resource of this class
     * ( {@link org.opentox.ontology.interfaces.JOntEntity } ).
     * @return the corresponding jena resource.
     */
    Resource getResource();

    /**
     * Returns the URI of the class
     * @return class URI
     */
    String getURI();

    /**
     *
     * For a given ontological entity, get the set of all entities that "ontologically"
     * include it. For example if the ontological class <code>Cow</code> is a subclass
     * of both <code>Mammal</code> and <code>Fat</code> and in turn <code>Mammal</code>
     * is an ontological subclass of <code>Animal</code>, this method returns the set
     * <code>{Mammal, Fat, Animal}</code>. Note that this is the set of superclasses and
     * does not contain the initial entity itself.
     * @return set of super entities.
     */
    Set<Resource> getSuperEntities();
}
