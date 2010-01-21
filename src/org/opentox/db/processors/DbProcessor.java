package org.opentox.db.processors;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.queries.HyperResult;
import org.opentox.db.queries.HyperStatement;
import org.opentox.db.util.QueryType;

/**
 *
 * @author chung
 */
public class DbProcessor extends AbstractDbProcessor<HyperStatement, HyperResult> {

    public HyperResult execute(HyperStatement q) {
        try {
            if (q.getType().equals(QueryType.UPDATE)) {
                q.executeUpdate();
            }else{

            }
        } catch (DbException ex) {
            Logger.getLogger(DbProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
