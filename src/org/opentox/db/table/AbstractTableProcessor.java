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
package org.opentox.db.table;

import org.opentox.db.exceptions.DbException;
import org.opentox.db.processors.AbstractDbProcessor;


/**
 * A Processor for the manipulation of the tables in the database. Subclasses of
 * {@link AbstractTableProcessor this processor } are responsible for the creation
 * and deletion of new tables in the database.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 *
 * @see TableCreator
 * @see TableDropper
 */
public abstract class AbstractTableProcessor extends AbstractDbProcessor<Table, Object>{

    /**
     * Create a new Abstract Table Creator. This constructor is intended to be
     * overriden by subclasses that implement AbstractTableCreator.
     */
    public AbstractTableProcessor(){
        super();
    }
   

    /**
     * An implementation of {@link AbstractDbProcessor#execute(java.lang.Object) execute()}
     * in {@link AbstractDbProcessor }.
     * @param q This parameter has in fact no meaning at all. You can either set it to
     * 'null' or 'new Object()' or whatever you like; it is not used in the body of the method
     * whatsoever.
     * @return null by default
     */
    public abstract Object execute(Table q) throws DbException;


}
