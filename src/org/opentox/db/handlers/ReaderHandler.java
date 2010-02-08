/*
 *
 * YAQP - Yet Another QSAR Project:
 * Machine Learning algorithms designed for the prediction of toxicological
 * features of chemical compounds become available on the Web. Yaqp is developed
 * under OpenTox (http://opentox.org) which is an FP7-funded EU research project.
 * This project was developed at the Automatic Control Lab in the Chemical Engineering
 * School of the National Technical University of Athens. Please read README for more
 * information.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact:
 * Pantelis Sopasakis
 * chvng@mail.ntua.gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 */
package org.opentox.db.handlers;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import org.opentox.config.Configuration;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.db.exceptions.DbException;
import org.opentox.ontology.components.*;
import org.opentox.db.processors.DbPipeline;
import org.opentox.db.queries.HyperResult;
import org.opentox.db.queries.QueryFood;
import org.opentox.db.util.PrepStmt;
import org.opentox.ontology.exceptions.YaqpOntException;
import org.opentox.ontology.namespaces.OTClass;
import org.opentox.ontology.util.AlgorithmMeta;
import org.opentox.ontology.util.YaqpAlgorithms;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.*;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ReaderHandler {

    private static DbPipeline<QueryFood, HyperResult> getUsersPipeline = null,
            getUserPipeline = null,
            getAlgorithmOntologiesPipeline = null,
            getUserGroupsPipeline = null,
            getUserGroupPipeline = null,
            getAlgorithmsPipeline = null,
            getAlgOntRelationPipeline = null,
            getOntAlgRelationPipeline = null,
            getFeaturesPipeline = null,
            getFeaturePipeline = null,
            getIndepFeaturesPipeline = null,
            getQSARModelsPipeline = null,
            getMLRModelsPipeline = null,
            getTasksPipeline = null;
    private static final String baseURI =
            "http://" + Configuration.getProperties().getProperty("server.domainName")
            + ":" + Configuration.getProperties().getProperty("server.port");

    /**
     *
     * An amazing method for searching for users having certain characteristics.
     * The provided argument is a <code>user</code> which defined the search criteria.
     * This user includes information that consist search criteria while you can set
     * to <code>null</code>, user information you're not interested in. For example,
     * if the prototype is <code>new User()</code>, the search will return all users. If
     * the prototype is a user with username <code>clown</code>, the search will return
     * all users with this name. If you need all users with email containing the character
     * sequence <code>'yahoo.com'</code>, you have to use the escape character <code>'%'</code>,
     * that is you have to create a user like this:
     * <pre>
     * User u = new User();
     * u.setEmail("%yahoo.com");
     * ArrayList&lt;User&gt; search_results = ReaderHandler.getUser(u);
     * </pre>
     * Now, if you need all users with email from yahoo, use % twice, like this:
     * <pre>
     * User u = new User();
     * u.setEmail("%yahoo.%");
     * </pre>
     * If in the search prototype you specify a user group for the search, then the
     * <code>level</code> of the group is optional. If you provide a usergroup with
     * given name, the search will be performed over all users belonging to this group.
     * For example, if you want all users in the group <code>Guest</code>, here is an
     * example of how to do this:
     * <pre>
     * User u = new User();
     * u.setUserGroup(new UserGroup("Guest", -1));
     * ArrayList&lt;User&gt; search_results = ReaderHandler.getUser(u);
     * </pre>
     * So if you set the level to a non-positive value (e.g. -1) the search takes place over
     * the groups with name <code>Guest</code> and some level. If you need all users belonging
     * to a group with a certain level, set groupName to <code>null</code> and specify the
     * desired level like this:
     * <pre>
     * User u = new User();
     * u.setUserGroup(new UserGroup(null, 10));
     * </pre>
     * This will return a list of users belonging to a group with level=10.
     * @param search_prototype The prototype for the search.
     * @return List of users meeting the specifications of the search criteria or
     * an empty list if no such user was found.
     * @throws DbException In case the search cannot be performed.
     */
    public static ArrayList<User> getUser(User search_prototype) throws DbException {
        ArrayList<User> searchResult = new ArrayList<User>();
        if (getUserPipeline == null) {
            getUserPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.SEARCH_USER);
        }
        HyperResult result = null;
        String groupName = null;
        long authLevel = -1, auth_min = 0, auth_max = Integer.MAX_VALUE;
        if (search_prototype.getUserGroup() != null) {
            groupName = search_prototype.getUserGroup().getName();
            authLevel = search_prototype.getUserGroup().getLevel();
            if (authLevel > 0) {
                auth_min = authLevel;
                auth_max = authLevel;
            }
        }

        QueryFood food = new QueryFood(
                new String[][]{
                    {"USER_LEVEL_MIN", Long.toString(auth_min)},
                    {"USER_LEVEL_MAX", Long.toString(auth_max)},
                    {"EMAIL", fixNull(search_prototype.getEmail())},
                    {"USERNAME", fixNull(search_prototype.getUserName())},
                    {"FIRSTNAME", fixNull(search_prototype.getFirstName())},
                    {"LASTNAME", fixNull(search_prototype.getLastName())},
                    {"COUNTRY", fixNull(search_prototype.getCountry())},
                    {"CITY", fixNull(search_prototype.getCity())},
                    {"ADDRESS", fixNull(search_prototype.getAddress())},
                    {"ORGANIZATION", fixNull(search_prototype.getOrganization())},
                    {"WEBPAGE", fixNull(search_prototype.getWebpage())},
                    {"ROLE", fixNull(groupName)},});
        try {
            result = getUserPipeline.process(food);
        } catch (YaqpException ex) {
            String message = "Could not get User list from database";
            YaqpLogger.LOG.log(new Debug(ReaderHandler.class, message));
            throw new DbException("XR51", message, ex);
        }

        if (result.getSize() > 0) {
            for (int i = 1; i <= result.getSize(); i++) {
                Iterator<String> it = result.getColumnIterator(i);
                User user = new User(it.next(), it.next(), it.next(), it.next(), it.next(), it.next(), it.next(),
                        it.next(), it.next(), it.next(), it.next(), getUserGroup(it.next()));
                searchResult.add(user);
            }
        }
        return searchResult;
    }

    /**
     * Auxiliary method.
     * @param in some string
     * @return returns <code>in</code> (the input string) if it is not null, or
     * <code>%%</code> otherwise.
     */
    private static String fixNull(String in) {
        if (in == null) {
            return "%%";
        }
        return in;
    }

    /**
     * Get a list of URIs of all users in the database. This in fact will return an
     * instance of {@link UriList } but no other data about every user. If you need
     * a more detailed representation as a list consider using
     * {@link ReaderHandler#getUser(org.opentox.ontology.components.User) } which
     * returns all users that meet a certain criterion as an <code>ArrayList&lt;User&gt;
     * </code>.
     * @return The list of all users as a {@link UriList }
     * @throws DbException In case the list could not be retrieved from the database
     * due to connection problems or other database access issues.
     */
    public static UriList getUsers() throws DbException {
        UriList userList = new UriList();
        if (getUsersPipeline == null) {
            getUsersPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.GET_ALL_USERS);
        }
        HyperResult result = null;
        try {
            result = getUsersPipeline.process(null);
        } catch (YaqpException ex) {
            YaqpLogger.LOG.log(new Debug(ReaderHandler.class, "XR51 - Could not get User list from database\n"));
        }

        for (int i = 1; i < result.getSize() + 1; i++) {
            Iterator<String> it = result.getColumnIterator(i);
            userList.add(new Uri(baseURI + "/user/" + it.next(), OTClass.User));
        }
        return userList;
    }

    /**
     *
     * @return
     */
    public static ArrayList<QSARModel> getQSARModels(QSARModel prototype) {
        ArrayList<QSARModel> searchResult = new ArrayList<QSARModel>();
        if (getQSARModelsPipeline == null) {
            getQSARModelsPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.SEARCH_QSAR_MODELS);
        }
        QueryFood food = new QueryFood();
        food.add("PREDICTION_FEATURE", fixNull(prototype.getPredictionFeature().getURI()));
        food.add("DEPENDENT_FEATURE", fixNull(prototype.getAlgorithm().getMeta().name));
        food.add("CREATED_BY", fixNull(prototype.getUser().getEmail()));
        food.add("DATASET_URI", fixNull(prototype.getDataset()));
        HyperResult result = null;
        try {
            result = getQSARModelsPipeline.process(food);
            if (result.getSize() > 0) {
                for (int i = 1; i <= result.getSize(); i++) {
                    Iterator<String> it = result.getColumnIterator(i);
                    String code = it.next();
                    QSARModel model = new QSARModel();
                    searchResult.add(model);
                }
            }
        } catch (YaqpException ex) {
            YaqpLogger.LOG.log(new Debug(ReaderHandler.class, "Could not get QSARModels from database"));
        }



        return null;
    }

    public static UserGroup getUserGroup(String groupName) throws DbException {
        if (getUserGroupPipeline == null) {
            getUserGroupPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.GET_USER_GROUP);
        }

        HyperResult result = null;
        QueryFood food = new QueryFood(
                new String[][]{
                    {"NAME", groupName}
                });
        try {
            result = getUserGroupPipeline.process(food);
        } catch (YaqpException ex) {
            String message = "Could not get User Group " + groupName + " from database";
            YaqpLogger.LOG.log(new Debug(ReaderHandler.class, message));
            throw new DbException("XRT73", message, ex);
        }

        if (result.getSize() == 1) {
            Iterator<String> it = result.getColumnIterator(1);
            UserGroup userGroup = new UserGroup(it.next(), Integer.parseInt(it.next()));
            return userGroup;
        }
        throw new DbException("XUG710", "No such user group :" + groupName);
    }

    public static ArrayList<UserGroup> getUserGroups() throws DbException {
        if (getUserGroupsPipeline == null) {
            getUserGroupsPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.GET_USER_GROUPS);
        }

        HyperResult result = null;
        try {
            result = getUserGroupsPipeline.process(null);
        } catch (YaqpException ex) {
            String message = "Could not get User Groups from database";
            YaqpLogger.LOG.log(new Debug(ReaderHandler.class, message));
            throw new DbException("XUG973", message, ex);
        }
        ArrayList<UserGroup> userGroupList = new ArrayList<UserGroup>();
        for (int i = 1; i < result.getSize() + 1; i++) {
            Iterator<String> it = result.getColumnIterator(i);
            UserGroup userGroup = new UserGroup(it.next(), Integer.parseInt(it.next()));
            userGroupList.add(userGroup);
        }
        return userGroupList;
    }

    public static ArrayList<AlgorithmOntology> getAlgorithmOntologies() throws YaqpOntException, DbException {
        if (getAlgorithmOntologiesPipeline == null) {
            getAlgorithmOntologiesPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.GET_ALGORITHM_ONTOLOGIES);
        }

        HyperResult result = null;
        try {
            result = getAlgorithmOntologiesPipeline.process(null);
        } catch (YaqpException ex) {
            String message = "Could not get Algorithm Ontologies from database";
            YaqpLogger.LOG.log(new Debug(ReaderHandler.class, message));
            throw new DbException("XR52", message, ex);
        }
        ArrayList<AlgorithmOntology> algorithmOntologiesList = new ArrayList<AlgorithmOntology>();
        for (int i = 1; i < result.getSize() + 1; i++) {
            Iterator<String> it = result.getColumnIterator(i);

            AlgorithmOntology algont = new AlgorithmOntology(it.next());
            algorithmOntologiesList.add(algont);
        }
        return algorithmOntologiesList;
    }

    /**
     * Name-based database search for algorithms.
     * @param algorithmName
     * @return
     * @throws YaqpOntException
     */
    public static ArrayList<AlgorithmOntology> getAlgOntRelation(String algorithmName) throws YaqpOntException, DbException {
        if (getAlgOntRelationPipeline == null) {
            getAlgOntRelationPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.GET_ALGORITHM_ONTOLOGY_RELATION);
        }
        QueryFood food = new QueryFood(
                new String[][]{
                    {"ALGORITHM", algorithmName}
                });
        HyperResult result = null;
        try {
            result = getAlgOntRelationPipeline.process(food);
        } catch (YaqpException e) {
            String message = "Could not get Algorithm-Ontology Relations from database";
            YaqpLogger.LOG.log(new Debug(ReaderHandler.class, message));
            throw new DbException("XAD312", message, e);
        }
        ArrayList<AlgorithmOntology> algorithmOntologiesList = new ArrayList<AlgorithmOntology>();
        for (int i = 1; i < result.getSize() + 1; i++) {
            Iterator<String> it = result.getColumnIterator(i);
            AlgorithmOntology algont = new AlgorithmOntology(it.next());
            algorithmOntologiesList.add(algont);
        }
        return algorithmOntologiesList;
    }

    // TODO: Fix the following code and then perform tons of tests!
    public static ArrayList<Algorithm> getOntAlgRelation(AlgorithmOntology ontology) throws DbException {
        if (getOntAlgRelationPipeline == null) {
            getOntAlgRelationPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.GET_ONTOLOGY_ALGORITHM_RELATION);
        }
        QueryFood food = new QueryFood(
                new String[][]{
                    {"ONTOLOGY", ontology.getName()}
                });
        HyperResult result = null;
        try {
            result = getOntAlgRelationPipeline.process(food);
        } catch (YaqpException e) {
            String message = "Could not get Ontology-Algorithm Relations from database\n";
            YaqpLogger.LOG.log(new Debug(ReaderHandler.class, message));
            throw new DbException("XW10A", message, e);
        }
        AlgorithmMeta meta = null;
        ArrayList<Algorithm> algorithmList = new ArrayList<Algorithm>();
        for (int i = 1; i < result.getSize() + 1; i++) {
            Iterator<String> it = result.getColumnIterator(i);
            String algName = it.next();
            try {
                Class<?> c = YaqpAlgorithms.class;
                Method[] allMethods = c.getDeclaredMethods();
                for (Method m : allMethods) {
                    String mname = m.getName();
                    if (mname.contains(algName)) {
                        meta = (AlgorithmMeta) m.invoke(null, (Object[]) null);
                        Algorithm algorithm = new Algorithm(meta);
                        algorithmList.add(algorithm);
                    }
                }
            } catch (Exception e) {
                YaqpLogger.LOG.log(new Warning(ReaderHandler.class, "XX101 - Xeption : " + e.toString()));
            }
        }
        return algorithmList;
    }

    public static ArrayList<Algorithm> getAlgorithms() {
        if (getAlgorithmsPipeline == null) {
            getAlgorithmsPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.GET_ALGORITHMS);
        }
        HyperResult result = null;
        try {
            result = getAlgorithmsPipeline.process(new QueryFood());
        } catch (YaqpException e) {
            YaqpLogger.LOG.log(new Debug(ReaderHandler.class, "Could not get Algorithms from database\n"));
        }
        AlgorithmMeta meta = null;
        ArrayList<Algorithm> algorithmList = new ArrayList<Algorithm>();
        for (int i = 1; i < result.getSize() + 1; i++) {
            Iterator<String> it = result.getColumnIterator(i);
            String algName = it.next();
            try {
                Class<?> c = YaqpAlgorithms.class;
                Method[] allMethods = c.getDeclaredMethods();
                for (Method m : allMethods) {
                    String mname = m.getName();
                    if (mname.contains(algName)) {
                        meta = (AlgorithmMeta) m.invoke(null, (Object[]) null);
                        Algorithm algorithm = new Algorithm(meta);
                        algorithmList.add(algorithm);
                    }
                }
            } catch (Exception e) {
                YaqpLogger.LOG.log(new Debug(ReaderHandler.class, e.toString()));
            }
        }
        return algorithmList;
    }

    public static Algorithm getAlgorithm(String name) throws DbException {
        AlgorithmMeta meta = null;
        Algorithm algorithm = null;
        try {
            Class<?> c = YaqpAlgorithms.class;
            Method[] allMethods = c.getDeclaredMethods();
            for (Method m : allMethods) {
                String mname = m.getName();
                if (mname.contains(name)) {
                    meta = (AlgorithmMeta) m.invoke(null, (Object[]) null);
                    algorithm = new Algorithm(meta);
                    return algorithm;
                }
            }
        } catch (Exception e) {
            YaqpLogger.LOG.log(new Debug(ReaderHandler.class, e.toString()));
        }
        throw new DbException("XUS484", "No such Algorithm :" + name);
    }

    public static ArrayList<Feature> getFeatures() {
        if (getFeaturesPipeline == null) {
            getFeaturesPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.GET_FEATURES);
        }

        HyperResult result = null;
        try {
            result = getFeaturesPipeline.process(null);
        } catch (YaqpException ex) {
            YaqpLogger.LOG.log(new Debug(ReaderHandler.class, "Could not get Features from database\n"));
        }
        ArrayList<Feature> featureList = new ArrayList<Feature>();
        for (int i = 1; i < result.getSize() + 1; i++) {
            Iterator<String> it = result.getColumnIterator(i);
            Feature feature = new Feature(Integer.parseInt(it.next()), it.next());
            featureList.add(feature);
        }
        return featureList;
    }

    public static Feature getFeature(int uid) throws DbException {
        if (getFeaturePipeline == null) {
            getFeaturePipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.GET_FEATURE);
        }
        HyperResult result = null;
        QueryFood food = new QueryFood(
                new String[][]{
                    {"UID", Integer.toString(uid)}
                });
        try {
            result = getFeaturePipeline.process(food);
        } catch (YaqpException ex) {
            YaqpLogger.LOG.log(new Debug(ReaderHandler.class, "Could not get Features from database\n"));
        }
        if (result.getSize() == 1) {
            Iterator<String> it = result.getColumnIterator(1);
            Feature feature = new Feature(Integer.parseInt(it.next()), it.next());
            return feature;
        }
        throw new DbException("XUS587", "No such Feature :" + uid);
    }
//    public static ArrayList<Feature> getIndepFeatures(QSARModel model){
//         if (getIndepFeaturesPipeline == null) {
//            getIndepFeaturesPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.GET_INDEP_FEATURES);
//        }
//        QueryFood food = new QueryFood(
//                new String[][]{
//                    {"MODEL_UID", Integer.toString(model.getId())}
//                });
//        HyperResult result = null;
//        try {
//            result = getIndepFeaturesPipeline.process(food);
//        } catch (YaqpException ex) {
//            YaqpLogger.LOG.log(new Debug(ReaderHandler.class, "Could not get Independent Features from database for model: "+model.getName()));
//        }
//        ArrayList<Feature> featureList = new ArrayList<Feature>();
//        for (int i = 1; i < result.getSize() + 1; i++) {
//            Iterator<String> it = result.getColumnIterator(i);
//            Feature feature = new Feature(Integer.parseInt(it.next()), it.next());
//            featureList.add(feature);
//        }
//        return featureList;
//    }
//    public static ArrayList<MLRModel> getMLRModels() throws DbException{
//        if (getMLRModelsPipeline == null) {
//            getMLRModelsPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.GET_MLR_MODELS);
//        }
//        HyperResult result = null;
//        try {
//            result = getMLRModelsPipeline.process(null);
//        } catch (YaqpException ex) {
//            YaqpLogger.LOG.log(new Debug(ReaderHandler.class, "Could not get MLRModels from database"));
//        }
//        ArrayList<MLRModel> models = new ArrayList<MLRModel>();
//        for (int i = 1; i < result.getSize() + 1; i++) {
//            Iterator<String> it = result.getColumnIterator(i);
//            MLRModel model = new MLRModel(Integer.parseInt(it.next()), it.next(), it.next(),
//                    getFeature(Integer.parseInt(it.next())), getFeature(Integer.parseInt(it.next())),
//                    getAlgorithm(it.next()), getUser(it.next()), it.next(), it.next());
//            model.setIndependentFeatures(getIndepFeatures(model));
//            models.add(model);
//        }
//        return models;
//    }
//    public static ArrayList<Task> getTasks() throws DbException {
//        if (getTasksPipeline == null) {
//            getTasksPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.GET_TASKS);
//        }
//        HyperResult result = null;
//        try {
//            result = getTasksPipeline.process(null);
//        } catch (YaqpException ex) {
//            YaqpLogger.LOG.log(new Debug(ReaderHandler.class, "Could not get Tasks from database"));
//        }
//        ArrayList<Task> tasks = new ArrayList<Task>();
//        for (int i = 1; i < result.getSize() + 1; i++) {
//            Iterator<String> it = result.getColumnIterator(i);
//            Task task = new Task(Integer.parseInt(it.next()), it.next(), it.next(),
//                    Task.STATUS.valueOf(it.next()), getUser(it.next()), getAlgorithm(it.next()), Integer.parseInt(it.next()),
//                    it.next(), it.next(), it.next());
//            tasks.add(task);
//        }
//        return tasks;
//    }
}
