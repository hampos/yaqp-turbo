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
package org.opentox.db.interfaces;

import org.opentox.db.util.SQLDataTypes;

/**
 * A Parameter for a Database Query, for example "user_name='barbie'". A Query Parameter
 * consists of two parts: The value and the datatype for this value.
 * @author Pantelis Sopasakis 
 * @author Charalampos Chomenides
 */
public interface JQueryParam {

    /**
     * Define the type of the parameter.
     * @param type type of the database parameter.
     */
    void setType(SQLDataTypes type);

    /**
     * Set the value to a parameter
     * @param value parameter value
     */
    void setValue(String value);

    /**
     *
     * @return type of the parameter.
     */
    SQLDataTypes getType();

    /**
     *
     * @return value of the parameter
     */
    String getValue();

    
    /**
     * The parameter and its value as a string.
     * @return string representation of the JQueryParam object.
     */
    @Override
    String toString();
}
