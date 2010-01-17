package org.opentox.db.processors;

import java.sql.Connection;
import java.sql.SQLException;
import org.opentox.db.queries.ISearchQuery;
import org.opentox.core.exceptions.YaqpException;

/**
 *
 * @author chung
 */
public class SearchEngine<Query extends ISearchQuery> extends StatementProcessor<Query, Query> {

    

    @Override
    protected Query execute(Connection c, Query target) throws SQLException, YaqpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }



}
