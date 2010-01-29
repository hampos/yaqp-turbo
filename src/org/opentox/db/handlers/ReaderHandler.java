package org.opentox.db.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.db.entities.Algorithm;
import org.opentox.db.entities.AlgorithmOntology;
import org.opentox.db.entities.User;
import org.opentox.db.entities.UserGroup;
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

    private static DbPipeline<QueryFood, HyperResult>
            getUsersPipeline = null,
            getAlgorithmOntologiesPipeline = null,
            getUserGroupsPipeline = null,
            getAlgorithmsPipeline = null,
            getAlgOntRelationPipeline = null,
            getOntAlgRelationPipeline = null;


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

     public static ArrayList<UserGroup> getUserGroups() {
        if (getUserGroupsPipeline == null) {
            getUserGroupsPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.GET_USER_GROUPS);
        }

        HyperResult result = null;
        try {
            result = getUserGroupsPipeline.process(null);
        } catch (YaqpException ex) {
            YaqpLogger.LOG.log(new Debug(ReaderHandler.class, "Could not get User Groups from database\n"));
        }
        ArrayList<UserGroup> userGroupList = new ArrayList<UserGroup>();
        for (int i = 1; i < result.getSize() + 1; i++) {
            Iterator<String> it = result.getColumnIterator(i);
            UserGroup userGroup = new UserGroup(it.next(), Integer.parseInt(it.next()));
            userGroupList.add(userGroup);
        }
        return userGroupList;
    }

    public static ArrayList<AlgorithmOntology> getAlgOntRelation(Algorithm algorithm){
        if (getAlgOntRelationPipeline == null){
            getAlgOntRelationPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.GET_ALGORITHM_ONTOLOGY_RELATION);
        }
        QueryFood food = new QueryFood(
                new String[][]{
                    {"ALGORITHM_NAME", algorithm.getName()}
                });
        HyperResult result = null;
        try {
            result = getAlgOntRelationPipeline.process(food);
        } catch (YaqpException e) {
            YaqpLogger.LOG.log(new Debug(ReaderHandler.class, "Could not get Algorithm-Ontology Relations from database\n"));
        }
        ArrayList<AlgorithmOntology> algorithmOntologiesList = new ArrayList<AlgorithmOntology>();
        for (int i = 1; i < result.getSize() + 1; i++) {
            Iterator<String> it = result.getColumnIterator(i);
            AlgorithmOntology algont = new AlgorithmOntology(it.next(), it.next());
            algorithmOntologiesList.add(algont);
        }
        return algorithmOntologiesList;
    }

    public static ArrayList<Algorithm> getOntAlgRelation(AlgorithmOntology ontology){
        if (getOntAlgRelationPipeline == null){
            getOntAlgRelationPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.GET_ONTOLOGY_ALGORITHM_RELATION);
        }
        QueryFood food = new QueryFood(
                new String[][]{
                    {"ONTOLOGY_NAME", ontology.getName()}
                });
        HyperResult result = null;
        try {
            result = getOntAlgRelationPipeline.process(food);
        } catch (YaqpException e) {
            YaqpLogger.LOG.log(new Debug(ReaderHandler.class, "Could not get Ontology-Algorithm Relations from database\n"));
        }
        ArrayList<Algorithm> algorithmList = new ArrayList<Algorithm>();
        for (int i = 1; i < result.getSize() + 1; i++) {
            Iterator<String> it = result.getColumnIterator(i);
            Algorithm algorithm = new Algorithm(it.next(), it.next(),null);
            ArrayList<AlgorithmOntology> ontologies = getAlgOntRelation(algorithm);
            algorithm.setOntologies(ontologies);
            algorithmList.add(algorithm);
        }
        return algorithmList;
    }

    public static ArrayList<Algorithm> getAlgorithms() {
        if (getAlgorithmsPipeline == null) {
            getAlgorithmsPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.GET_ALGORITHMS);
        }
        HyperResult result = null;
        try {
            result = getAlgorithmsPipeline.process(null);
        } catch (YaqpException e) {
            YaqpLogger.LOG.log(new Debug(ReaderHandler.class, "Could not get Algorithms from database\n"));
        }
        ArrayList<Algorithm> algorithms = new ArrayList<Algorithm>();
        for (int i = 1; i < result.getSize() + 1; i++) {
            Iterator<String> it = result.getColumnIterator(i);
            Algorithm algorithm = new Algorithm(it.next(), it.next(), null);
            ArrayList<AlgorithmOntology> ontologies = getAlgOntRelation(algorithm);
            algorithm.setOntologies(ontologies);
            algorithms.add(algorithm);
        }
        return algorithms;
    }

}