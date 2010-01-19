package org.opentox.db.processors;


import org.opentox.core.exceptions.YaqpException;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.interfaces.JDbProcessor;
import org.opentox.core.processors.Processor;
import org.opentox.db.interfaces.JQuery;
import org.opentox.db.util.QueryType;
import org.opentox.db.util.TheDbConnector;

/**
 * This is an abstraction for the processors which manipulate the data of the
 * database. It processes input data which here are {@link JQuery database queries }.
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


    public QueryResult process(Query query) throws YaqpException {        
        if (!TheDbConnector.DB.isConnected()) {
            throw new DbException("There in no connection to "+TheDbConnector.DB.getDatabaseName());
        }
        try {
            return execute(query);
        } catch (Exception x) {
            throw new YaqpException(x);
        }
    }

    
}
