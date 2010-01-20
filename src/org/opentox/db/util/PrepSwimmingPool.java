package org.opentox.db.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.processors.BatchProcessor;
import org.opentox.db.exceptions.DbException;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Fatal;
import org.opentox.core.processors.Processor;

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
                ArrayBlockingQueue queue_i = new ArrayBlockingQueue<PreparedStatement>(_POOL_SIZE);
                for (int i = 0; i < _POOL_SIZE; i++) {
                    queue_i.add(new HyperStatement(prepStmt.getSql()));
                }
                System.out.println();
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

    public static void main(String[] args) throws SQLException, InterruptedException, DbException {
        //PrepSwimmingPool p = PrepSwimmingPool.POOL;
        System.out.println("xx");



        Processor<Integer, Object> p = new Processor<Integer, Object>() {

            public Object process(Integer data) throws YaqpException {
                HyperStatement hp = null;
                try {
                    hp = PrepSwimmingPool.POOL.take(PrepStmt.add_algorithm_ontology);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                hp.setString(1, "name_" + data);
                hp.setString(2, "http://sth.com/t/" + data);
                hp.executeUpdate();
                PrepSwimmingPool.POOL.put(hp);
                return new Object();
            }
        };


        BatchProcessor<Integer, Object, Processor<Integer, Object>> bp = new BatchProcessor<Integer, Object, Processor<Integer, Object>>(p, 50, 90);
        ArrayList<Integer> jobs = new ArrayList<Integer>(10000);
        for (int i = 0; i < 10000; i++) {
            jobs.add(new Integer(i));
        }
        try {
            bp.process(jobs);
        } catch (YaqpException ex) {
            throw new RuntimeException(ex);
        }




    }
}
