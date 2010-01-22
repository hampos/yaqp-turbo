package org.opentox.db.processors;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.queries.HyperStatement;
import org.opentox.db.queries.QueryFood;
import org.opentox.db.util.PrepStmt;
import org.opentox.db.util.PrepSwimmingPool;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.ScrewedUp;

/**
 * This is a Processor that ...
 * @author chung
 */
public class QueryProcessor extends AbstractDbProcessor<QueryFood, HyperStatement> {

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
    public HyperStatement execute(QueryFood food) {

        HyperStatement hs = null;
        try {
            hs = PrepSwimmingPool.POOL.take(prepStmt);
            String value = "";


            System.out.println(prepStmt.getParameters()==null);

            for (int i = 0; i < prepStmt.getParameters().length; i++) {

                value = food.getValue(prepStmt.getParameters()[i].getName());

                if (prepStmt.getParameters()[i].getType().equals(String.class)){
                    System.out.println("Class"+prepStmt.getParameters()[i].getType()+" value = "+value+" ");
                    hs.setString((i+1), value);
                }else if (prepStmt.getParameters()[i].getType().equals(Integer.class)){
                    hs.setInt((i+1), Integer.parseInt(value));
                }
            }


        } catch (InterruptedException ex) {
            YaqpLogger.LOG.log(new ScrewedUp(getClass(), "Exception thrown :: "+ ex));
        } catch (DbException ex){
            YaqpLogger.LOG.log(new ScrewedUp(getClass(), "Exception thrown :: "+ ex));
        } finally{
            try {
                PrepSwimmingPool.POOL.put(hs);
            } catch (DbException ex) {
                YaqpLogger.LOG.log(new ScrewedUp(getClass(), "Could not put the statement back into the POOL"));
            }
        }

        return hs;

    }
}
