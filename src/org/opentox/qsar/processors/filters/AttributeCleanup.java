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
package org.opentox.qsar.processors.filters;

import org.opentox.core.exceptions.Cause;
import org.opentox.qsar.exceptions.QSARException;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.RemoveType;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class AttributeCleanup extends InstancesFilter {

    /**
     * An enumeration for the main datatypes recognized by weka which
     * are also useful for YAQP. These are <code>string, nominal</code> and
     * <code>numeric</code>.
     */
    public enum ATTRIBUTE_TYPE {

        string,
        nominal,
        numeric;
    }
    private ATTRIBUTE_TYPE[] toBeRemoved;

    /**
     * Constructs a new clean-up filter which removes string attributes
     * (default behaviour).
     */
    public AttributeCleanup() {
        super();
        this.toBeRemoved = new ATTRIBUTE_TYPE[]{ATTRIBUTE_TYPE.string};
    }

    public AttributeCleanup(ATTRIBUTE_TYPE[] toBeRemoved) {
        this.toBeRemoved = toBeRemoved;
    }
    public AttributeCleanup(ATTRIBUTE_TYPE toBeRemoved) {
        this.toBeRemoved = new ATTRIBUTE_TYPE[]{toBeRemoved};
    }

    public ATTRIBUTE_TYPE[] whatIsRemoved() {
        return this.toBeRemoved;
    }

    @Override
    public Instances filter(Instances data) throws QSARException {
        if (toBeRemoved == null || (toBeRemoved != null && toBeRemoved.length == 0)) {
            this.toBeRemoved = new ATTRIBUTE_TYPE[]{ATTRIBUTE_TYPE.string};
        }

        Instances filteredData = data;
        for (int i = 0; i < toBeRemoved.length; i++) {
            filteredData = remove(filteredData, toBeRemoved[i]);
        }
        return filteredData;
    }



    private Instances remove(Instances input, ATTRIBUTE_TYPE type) throws QSARException {
        RemoveType remover = new RemoveType();
        try {
            remover.setInputFormat(input);
        } catch (Exception ex) {
            String message = "Invalid input format for attribute-type removing filter";
            throw new QSARException(Cause.XQF11, message, ex);
        }
        String typeRemoved = type.toString();
        Instances output = input;
        String[] options = new String[]{"-T", typeRemoved};
        try {
            remover.setOptions(options);
        } catch (Exception ex) {
            String message = "Invalid filter options for cleanup";
            throw new QSARException(Cause.XQF111, message, ex);
        }
        try {
            output = RemoveType.useFilter(input, remover);
        } catch (Exception ex) {
            String message = "The filter is unable to remove the specified type :" + typeRemoved;
            throw new QSARException(Cause.XQF212, message, ex);
        }
        return output;
    }
    
}
