package org.opentox.db.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.queries.HyperStatement;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Fatal;

/**
 * Pool of hyper statements
 * @author chung
 */
public class PrepSwimmingPool {

    private static final int _POOL_SIZE = 10;
    private static PrepSwimmingPool instanceOfThis = null;
    public static PrepSwimmingPool POOL = getInstance();
    /**
     * Our pool
     */
    private Map<PrepStmt, ArrayBlockingQueue<HyperStatement>> BlockingQueues =
            new HashMap<PrepStmt, ArrayBlockingQueue<HyperStatement>>();

    private PrepSwimmingPool() {
        try {
            TheDbConnector db = TheDbConnector.DB;
            for (PrepStmt prepStmt : PrepStmt.values()) {
                ArrayBlockingQueue<HyperStatement> queue_i = new ArrayBlockingQueue<HyperStatement>(_POOL_SIZE);
                for (int i = 0; i < _POOL_SIZE; i++) {
                    queue_i.add(new HyperStatement(prepStmt.getSql()));
                }
                BlockingQueues.put(prepStmt, queue_i);
            }
        } catch (Exception ex) {
            YaqpLogger.LOG.log(new Fatal(getClass(), ex.toString()));
            throw new RuntimeException(ex);
        }
    }

    private static PrepSwimmingPool getInstance() {
        if (instanceOfThis == null) {
            instanceOfThis = new PrepSwimmingPool();
        }
        return instanceOfThis;
    }

    public HyperStatement take(PrepStmt prepStmt) throws InterruptedException {
        return BlockingQueues.get(prepStmt).take();
    }

    public boolean isEmpty(PrepStmt prepStmt) {
        return BlockingQueues.get(prepStmt).peek() == null ? true : false;
    }

    public void put(HyperStatement hp) throws DbException {
        PrepStmt hp_prepStmt = null;
        for (PrepStmt prepStmt : PrepStmt.values()) {
            if (prepStmt.getSql().equalsIgnoreCase(hp.toString())) {
                hp_prepStmt = prepStmt;
            }
        }
        if (hp_prepStmt == null) {
            throw new DbException("The HyperStatement you provided does not correspond to a registered prepared statement.");
        }
        try {
            hp.flush();
            BlockingQueues.get(hp_prepStmt).put(hp);
        } catch (InterruptedException ex) {
            throw new DbException(ex);
        }

    }

    
}
