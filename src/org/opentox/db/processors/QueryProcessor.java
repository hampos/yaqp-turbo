package org.opentox.db.processors;

import org.opentox.db.exceptions.DbException;
import org.opentox.db.interfaces.JQueryProcessor;
import org.opentox.db.queries.HyperStatement;
import org.opentox.db.queries.QueryFood;
import org.opentox.db.util.PrepStmt;
import org.opentox.db.util.PrepSwimmingPool;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Debug;

/**
 * This is a Processor that handles how parameters are set on a PrepStmt object.
 * A QueryProcessor is an implementation of the {@link AbstractDbProcessor } and it
 * is initialized/constructed with a PrepStmt object.
 *
 * @author Sopasakis Pantelis
 * @author Charalampos Chomenides
 */
public class QueryProcessor extends AbstractDbProcessor<QueryFood, HyperStatement> implements JQueryProcessor {

    private PrepStmt prepStmt;

    /**
     * Builds a new QueryProcessor for a given prepared statement encapsulated in
     * a PrepStmt object.
     * @param prepStmt a prepared statement
     */
    public QueryProcessor(PrepStmt prepStmt) {
        super();
        this.prepStmt = prepStmt;
    }

    /**
     * Executes the processor, i.e. given an instance of QueryFood (properly initialized
     * with values) produces the corresponding HyperStatement.
     * @param food the query food
     * @return The produced hyperstatement
     */
    public HyperStatement execute(QueryFood food) throws DbException {

        HyperStatement hs = null;
        try {
            hs = PrepSwimmingPool.POOL.take(prepStmt);
            String value = "";

            for (int i = 0; prepStmt.getParameters() != null && i < prepStmt.getParameters().length; i++) {

                if (!food.containsName(prepStmt.getParameters()[i].getName())) {
                    String message = "The parameter " + prepStmt.getParameters()[i].getName() + " is not set";
                    throw new DbException(message);
                }
                value = food.getValue(prepStmt.getParameters()[i].getName());
                if (prepStmt.getParameters()[i].getType().equals(String.class)) {
                    hs.setString((i + 1), value);
                } else if (prepStmt.getParameters()[i].getType().equals(Integer.class)) {
                    hs.setInt((i + 1), Integer.parseInt(value));
                } else if (prepStmt.getParameters()[i].getType().equals(Double.class)) {
                    hs.setDouble((i + 1), Double.parseDouble(value));
                }
            }
        } catch (InterruptedException ex) {
            YaqpLogger.LOG.log(new Debug(getClass(), "Exception thrown :: " + ex));
        } catch (DbException ex) {
            YaqpLogger.LOG.log(new Debug(getClass(), "Exception thrown :: " + ex));
        }
        return hs;
    }

    public PrepStmt getPrepStmt() {
        return this.prepStmt;
    }

    public void setPrepStmt(PrepStmt prepStmt) {
        this.prepStmt = prepStmt;
    }

}
