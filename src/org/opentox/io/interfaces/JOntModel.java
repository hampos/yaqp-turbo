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
package org.opentox.io.interfaces;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import org.opentox.ontology.namespaces.YaqpOntEntity;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface JOntModel extends Model, OntModel, JPublishable {

    void printConsole();

    void includeOntClass(YaqpOntEntity ont_entity);

    void includeOntClasses(YaqpOntEntity[] ont_entities);

    void createAnnotationProperties(String[] annotation_uris);

    void createDataTypeProperties(String[] datatype_uris);

    void createObjectProperties(String[] object_uris);

    void createSymmetricProperties(String[] symmetric_uris);

    void createTransitiveProperties(String[] transitive_uris);
}
