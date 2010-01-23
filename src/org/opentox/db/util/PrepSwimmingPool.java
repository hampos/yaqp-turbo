package org.opentox.db.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.queries.HyperStatement;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Fatal;

/**
 * Pool of hyper statements. This pool implements a first-in-first-out architecture
 * for the management of hyperStatements. A HyperStatement is pulled out of this pool,
 * the it is initialized with variables and executed. Afterwards, it is returned back to
 * the Pool, where it is cleared and recycle back in the queue.
 *
 * @author Sopasakis Pantelis
 * @author Charalampos Chomenides
 *
 * @see HyperStatement
 * @see PrepStmt
 */
public class PrepSwimmingPool {

    /**
     * Multiplicity for the stored HyperStatements. Specifies how many clones
     * of a HyperStatement are stored in the Pool.
     */
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

    /**
     * Get the first available Hyperstatement from the pool. In case there are no
     * available statements, wait.
     * @param prepStmt Prepared Statement (PrepStmt) for which the HyperStatement is requested.
     * @return HyperStatement corresponding to a given PrepStmt object.
     * @throws InterruptedException If the method is interrupted while waiting for a
     * Hyperstatement to become available.
     */
    public HyperStatement take(PrepStmt prepStmt) throws InterruptedException {
        return BlockingQueues.get(prepStmt).take();
    }

    /**
     * Wheter the Pool corresponding to a given PrepStmt is empty.
     * @param prepStmt a PrepStmt object.
     * @return true if the pool is empty of such HyperStatements.
     */
    public boolean isEmpty(PrepStmt prepStmt) {
        return BlockingQueues.get(prepStmt).peek() == null ? true : false;
    }

    /**
     * Once a HyperStatement is exploited, it should be returned back to the pool
     * to become available for some other client who need to perform a same operation.
     * @param hp A used HyperStatement
     * @throws DbException If the Prepared Statement committed for recycle does not
     * belong to the Pool.
     */
    public void recycle(HyperStatement hp) throws DbException {
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
