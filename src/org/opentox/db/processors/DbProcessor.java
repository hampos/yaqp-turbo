package org.opentox.db.processors;

import org.opentox.db.exceptions.DbException;
import org.opentox.db.queries.HyperResult;
import org.opentox.db.queries.HyperStatement;
import org.opentox.db.util.QueryType;

/**
 *
 * @author chung
 */
public class DbProcessor extends AbstractDbProcessor<HyperStatement, HyperResult> {

    public HyperResult execute(HyperStatement q) throws DbException {

        if (q.getType().equals(QueryType.UPDATE)) {
            q.executeUpdate();
        } else {
            
        }

        return null;
    }
}
