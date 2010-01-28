package org.opentox.db.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.db.entities.User;
import org.opentox.db.processors.DbPipeline;
import org.opentox.db.queries.HyperResult;
import org.opentox.db.queries.QueryFood;
import org.opentox.db.util.PrepStmt;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Debug;

/**
 *
 * @author chung
 */
public class ReaderHandler {

    public static ArrayList<User> getUsers() {
        DbPipeline<QueryFood, HyperResult> pipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.GET_USERS);
        HyperResult result = null;
        try {
            result = pipeline.process(null);
        } catch (YaqpException ex) {
            YaqpLogger.LOG.log(new Debug(ReaderHandler.class, "Could not get User list from database\n"));
        }
        ArrayList<User> userList = new ArrayList<User>();
        for (int i = 1; i < result.getSize()+1; i++) {
            Iterator<String> it = result.getColumnIterator(i);

            User user = new User(it.next(),it.next(),it.next(),it.next(),it.next(),it.next(),it.next(),
                    it.next(),it.next(),it.next(),it.next(),it.next());
            userList.add(user);
        }
        return userList;
    }

   
}
