package org.opentox.db.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.db.entities.AlgorithmOntology;
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

    private static DbPipeline<QueryFood, HyperResult> getUsersPipeline = null,
            getAlgorithmOntologiesPipeline = null;

    public static ArrayList<User> getUsers() {
        if (getUsersPipeline == null) {
            getUsersPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.GET_USERS);
        }
        HyperResult result = null;
        try {
            result = getUsersPipeline.process(null);
        } catch (YaqpException ex) {
            YaqpLogger.LOG.log(new Debug(ReaderHandler.class, "XR51 - Could not get User list from database\n"));
        }
        ArrayList<User> userList = new ArrayList<User>();
        for (int i = 1; i < result.getSize() + 1; i++) {
            Iterator<String> it = result.getColumnIterator(i);

            User user = new User(it.next(), it.next(), it.next(), it.next(), it.next(), it.next(), it.next(),
                    it.next(), it.next(), it.next(), it.next(), it.next());
            userList.add(user);
        }
        return userList;
    }

    public static ArrayList<AlgorithmOntology> getAlgorithmOntologies() {
        if (getAlgorithmOntologiesPipeline == null) {
            getAlgorithmOntologiesPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.GET_ALGORITHM_ONTOLOGIES);
        }

        HyperResult result = null;
        try {
            result = getAlgorithmOntologiesPipeline.process(null);
        } catch (YaqpException ex) {
            YaqpLogger.LOG.log(new Debug(ReaderHandler.class, "XR52 - Could not get Algorithm Ontologies from database\n"));
        }
        ArrayList<AlgorithmOntology> algorithmOntologiesList = new ArrayList<AlgorithmOntology>();
        for (int i = 1; i < result.getSize() + 1; i++) {
            Iterator<String> it = result.getColumnIterator(i);

            AlgorithmOntology algont = new AlgorithmOntology(it.next(), it.next());
            algorithmOntologiesList.add(algont);
        }
        return algorithmOntologiesList;
    }
}