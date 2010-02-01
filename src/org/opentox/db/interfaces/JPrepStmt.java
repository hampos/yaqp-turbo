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

import org.opentox.db.queries.QueryParam;


/**
 *
 * Interface for the prepared statements used in YAQP. Prepared statements are
 * used for increased security as it becomes hard for someone to perform SQL
 * injections or other malicious operations and for performance reasons also
 * as these are precompiled in the database.
 * 
 * @author Sopasakis Pantelis
 * @author Charalampos Chomenides
 */
public interface JPrepStmt {

    /**
     * String representation of the SQL command corresponding to the prepared
     * statement.
     * @return SQL command
     */
    String getSql();
    /**
     * The set of parameters the client needs to provide to the prepared statement
     * before executing it.
     * @return Set of parameters
     * @see QueryParam
     */
    QueryParam[] getParameters();
   

}
