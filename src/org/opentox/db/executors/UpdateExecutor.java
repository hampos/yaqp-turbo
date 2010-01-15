package org.opentox.db.executors;

import java.sql.Connection;
import java.sql.SQLException;
import org.opentox.db.queries.IUpdateQuery;
import org.opentox.core.exceptions.YaqpException;

/**
 *
 * @author chung
 */
public class UpdateExecutor<Query extends IUpdateQuery> extends StatementExecutor<Query, Integer> {

    @Override
    protected Integer execute(Connection c, Query target) throws SQLException, YaqpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    

}
