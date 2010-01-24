package org.opentox.db.processors;

import org.opentox.db.exceptions.DbException;
import org.opentox.db.queries.HyperResult;
import org.opentox.db.queries.HyperStatement;
import org.opentox.db.util.PrepSwimmingPool;
import org.opentox.db.util.QueryType;

/**A DbProcessor handles all execution of statements.
 * A {@link HyperStatement} must be provided for it to operate,
 * and a {@link HyperResult} is given as the processor's output.
 * The processor decides if he will execute an update, or a query
 * from the provided HyperStatement, according to the {@link QueryType}
 * the HyperStatement carries.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DbProcessor extends AbstractDbProcessor<HyperStatement, HyperResult> {

    public DbProcessor() {
        super();
    }

    /**
     *
     * @param q
     * @return
     * @throws DbException
     */
    public HyperResult execute(HyperStatement q) throws DbException {
        HyperResult result = null;
        try {
            if (q.getType().equals(QueryType.UPDATE)) {
                q.executeUpdate();
            } else if (q.getType().equals(QueryType.SELECT)) {
                result = q.executeQuery();
            }
        } catch (Exception e) {
            throw new DbException(e);
        } finally {
            PrepSwimmingPool.POOL.recycle(q);

            if (q.getType().equals(QueryType.UPDATE)) {
                q.executeUpdate();
            } else {
            }
            return result;
        }
    }
}
