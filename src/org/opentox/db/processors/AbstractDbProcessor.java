package org.opentox.db.processors;


import org.opentox.core.exceptions.YaqpException;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.interfaces.JDbProcessor;
import org.opentox.core.processors.Processor;
import org.opentox.db.interfaces.JQuery;
import org.opentox.db.util.TheDbConnector;

/**
 * This abstract class holds and manipulates the connection to the database.
 * @author chung
 */
public abstract class AbstractDbProcessor<Query extends JQuery, QueryResult>
        extends Processor<Query, QueryResult>
        implements JDbProcessor<Query, QueryResult> {
    


    public AbstractDbProcessor() {
        super();     
    }


    public QueryResult process(Query query) throws YaqpException {        
        if (!TheDbConnector.DB.isConnected()) {
            throw new DbException("no connection");
        }
        try {
            return execute(query);
        } catch (Exception x) {
            throw new YaqpException();
        }
    }

    public abstract QueryResult execute(Query q);
}
