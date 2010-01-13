package org.opentox.db.executors;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.opentox.db.queries.ISearchQuery;
import org.opentox.core.exceptions.YaqpException;

/**
 *
 * @author chung
 */
public class SearchExecutor<Query extends ISearchQuery> extends StatementExecutor<Query, ResultSet> {

    @Override
    protected ResultSet execute(Connection c, Query target) throws SQLException, YaqpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isEnabled() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setEnabled(boolean enabled) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
