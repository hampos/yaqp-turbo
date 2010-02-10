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
package org.opentox.ontology.namespaces;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import org.opentox.io.publishable.OntObject;

/**
 *
 * Datatype properties accordinf to W3C are properties that <tt>"link individuals
 * to data values."</tt>.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class OTDataTypeProperties extends YaqpOntEntity {

    public OTDataTypeProperties(Resource resource) {
        super(resource);
    }
    /**
     *
     * A value.
     */
    public static final OTDataTypeProperties value =
            new OTDataTypeProperties(_model.createDatatypeProperty(String.format(_NS_OT, "value")));
    /**
     *
     * The units of a parameter or other measure.
     */
    public static final OTDataTypeProperties units =
            new OTDataTypeProperties(_model.createDatatypeProperty(String.format(_NS_OT, "units")));
    /**
     *
     *
     */
    public static final OTDataTypeProperties has3Dstructure =
            new OTDataTypeProperties(_model.createDatatypeProperty(String.format(_NS_OT, "has3Dstructure")));
    /**
     *
     * The status of an entity. For example for a Task, the range of this variable
     * is <code>Running, Cancelled, Completed</code>.
     */
    public static final OTDataTypeProperties hasStatus =
            new OTDataTypeProperties(_model.createDatatypeProperty(String.format(_NS_OT, "hasStatus")));
    /**
     *
     * Scope of a parameter (optional/mandatory).
     */
    public static final OTDataTypeProperties paramScope =
            new OTDataTypeProperties(_model.createDatatypeProperty(String.format(_NS_OT, "paramScope")));
    /**
     *
     * Value of a parameter.
     */
    public static final OTDataTypeProperties paramValue =
            new OTDataTypeProperties(_model.createDatatypeProperty(String.format(_NS_OT, "paramValue")));
    /**
     *
     * Percentage of completion of a running task.
     */
    public static final OTDataTypeProperties percentageCompleted =
            new OTDataTypeProperties(_model.createDatatypeProperty(String.format(_NS_OT, "percentageCompleted")));
    /**
     * 
     */
    public static final OTDataTypeProperties acceptValue =
            new OTDataTypeProperties(_model.createDatatypeProperty(String.format(_NS_OT, "acceptValue")));

    @Override
    public Property createProperty(OntObject model) {
        Property p = model.getObjectProperty(getURI());
        return p == null ? model.createDatatypeProperty(getURI()) : p;
    }
}
