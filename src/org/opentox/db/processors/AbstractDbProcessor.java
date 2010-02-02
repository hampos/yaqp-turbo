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
package org.opentox.db.processors;



import org.opentox.db.exceptions.DbException;
import org.opentox.db.interfaces.JDbProcessor;
import org.opentox.core.processors.Processor;
import org.opentox.db.util.PrepStmt;
import org.opentox.db.util.QueryType;
import org.opentox.db.util.TheDbConnector;

/**
 * This is an abstraction for the processors which manipulate the data of the
 * database. It processes input data which here are {@link PrepStmt database queries }.
 * @author Sopasakis Pantelis
 * @author Charalampos Chomenides
 * @param <Query> A Database Query of any type (Insertion, Update, Deletion etc). The Type
 * of the query is described by {@link QueryType }. A database query can sometimes be
 * an arbitrary object when 
 * @param <QueryResult> The Result from the database query. This can be of
 * any type in Update-like database queries where no result is expexted.
 */
public abstract class AbstractDbProcessor<Query, QueryResult>
        extends Processor<Query, QueryResult>
        implements JDbProcessor<Query, QueryResult> {
    


    public AbstractDbProcessor() {
        super();     
    }


    public QueryResult process(Query query) throws DbException {
        if (!TheDbConnector.DB.isConnected()) {            
            throw new DbException("XDB624 - Connection to "+TheDbConnector.DB.getDatabaseName()+" could not be established");
        }
        try {
            return execute(query);
        } catch (Exception x) {
            throw new DbException(x);
        }
    }

    
}
