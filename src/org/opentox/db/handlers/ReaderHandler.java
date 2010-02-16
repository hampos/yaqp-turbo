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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.config.Configuration;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.db.exceptions.DbException;
import org.opentox.ontology.components.*;
import org.opentox.db.processors.DbPipeline;
import org.opentox.db.queries.HyperResult;
import org.opentox.db.queries.QueryFood;
import org.opentox.db.util.PrepStmt;
import org.opentox.io.util.ServerList;
import org.opentox.ontology.exceptions.YaqpOntException;
import org.opentox.ontology.util.AlgorithmMeta;
import org.opentox.ontology.util.YaqpAlgorithms;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.*;
import static org.opentox.core.exceptions.Cause.*;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ReaderHandler {

    private static final String baseURI =
            "http://" + Configuration.getProperties().getProperty("server.domainName")
            + ":" + Configuration.getProperties().getProperty("server.port");



    /**
     * ****************************************************************************
     * ----------------------------------------------------------------------------
     *                      SEARCH QUERIES
     * ----------------------------------------------------------------------------
     * ****************************************************************************
     */



     public static ComponentList<UserGroup>
             searchUserGroup(UserGroup prototype, int pageSize, int pageNum) throws DbException {

         if(prototype == null){
             throw new NullPointerException("UserGroup prototype provided is null");
         }
        ComponentList<UserGroup> groupList = new ComponentList<UserGroup>();
        DbPipeline<QueryFood, HyperResult> pipeline =
                new DbPipeline<QueryFood, HyperResult>(PrepStmt.SEARCH_USER_GROUP);

        HyperResult result = null;
        QueryFood food = new QueryFood(
                new String[][]{
                    {"NAME", fixNull(prototype.getName())},
                    {"USER_LEVEL_MIN", Integer.toString(prototype.getMinLevel())},
                    {"USER_LEVEL_MAX", Integer.toString(prototype.getMaxLevel())},
                    {"MODEL_AUTH", fixNull(prototype.getModelAuth())},
                    {"USER_AUTH", fixNull(prototype.getUserAuth())},
                    {"ALGORITHM_AUTH", fixNull(prototype.getAlgorithmAuth())},
                    {"USER_GROUP_AUTH", fixNull(prototype.getUserGroupAuth())},
                    {"MAX_MODELS_MIN", Integer.toString(prototype.getMinModels())},
                    {"MAX_MODELS_MAX", Integer.toString(prototype.getMaxModels())}
        });       
        if(pageSize <= 0){
            food.add("OFFSET", Integer.toString(0));
            food.add("ROWS", Integer.toString(Integer.MAX_VALUE));
        } else if(pageNum < 0){
            food.add("OFFSET", Integer.toString(0));
            food.add("ROWS", Integer.toString(pageSize));
        } else {
            food.add("OFFSET", Integer.toString(pageNum*pageSize));
            food.add("ROWS", Integer.toString(pageSize));
        }
        try {
            result = pipeline.process(food);
            for (int i = 1; i <= result.getSize() ; i++) {
                Iterator<String> it = result.getColumnIterator(i);
                UserGroup userGroup = new UserGroup(it.next(),Integer.parseInt(it.next()),
                    it.next(),it.next(), it.next(), it.next(), Integer.parseInt(it.next()));
                groupList.add(userGroup);
            }
        } catch (YaqpException ex) {
            String message = "Could not get User Groups for given Prototype from database";
            throw new DbException(XDH1, message, ex);
        }
        return groupList;
    }

    public static ComponentList<UserGroup>
             searchUserGroupSkroutz(UserGroup prototype, int pageSize, int pageNum) throws DbException {
        if(prototype == null){
             throw new NullPointerException("UserGroup prototype provided is null");
         }
        ComponentList<UserGroup> groupList = new ComponentList<UserGroup>();
        DbPipeline<QueryFood, HyperResult> pipeline =
                new DbPipeline<QueryFood, HyperResult>(PrepStmt.SEARCH_USER_GROUP_SKROUTZ);

        HyperResult result = null;
        QueryFood food = new QueryFood(
                new String[][]{
                    {"NAME", fixNull(prototype.getName())},
                    {"USER_LEVEL_MIN", Integer.toString(prototype.getMinLevel())},
                    {"USER_LEVEL_MAX", Integer.toString(prototype.getMaxLevel())},
                    {"MODEL_AUTH", fixNull(prototype.getModelAuth())},
                    {"USER_AUTH", fixNull(prototype.getUserAuth())},
                    {"ALGORITHM_AUTH", fixNull(prototype.getAlgorithmAuth())},
                    {"USER_GROUP_AUTH", fixNull(prototype.getUserGroupAuth())},
                    {"MAX_MODELS_MIN", Integer.toString(prototype.getMinModels())},
                    {"MAX_MODELS_MAX", Integer.toString(prototype.getMaxModels())}
        });
        if(pageSize <= 0){
            food.add("OFFSET", Integer.toString(0));
            food.add("ROWS", Integer.toString(Integer.MAX_VALUE));
        } else if(pageNum < 0){
            food.add("OFFSET", Integer.toString(0));
            food.add("ROWS", Integer.toString(pageSize));
        } else {
            food.add("OFFSET", Integer.toString(pageNum*pageSize));
            food.add("ROWS", Integer.toString(pageSize));
        }
        try {
            result = pipeline.process(food);
            for (int i = 1; i < result.getSize() + 1; i++) {
            Iterator<String> it = result.getColumnIterator(i);
            UserGroup userGroup = new UserGroup(it.next(),Integer.parseInt(it.next()),
                    it.next(),it.next(), it.next(), it.next(), Integer.parseInt(it.next()));
            groupList.add(userGroup);
            }
        } catch (YaqpException ex) {
            String message = "Could not get User Group Names for given Prototype from database";
            throw new DbException(XDH2, message, ex);
        }
        return groupList;
    }


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
     * ArrayList&lt;User&gt; search_results = ReaderHandler.searchUsers(u);
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
     * ArrayList&lt;User&gt; search_results = ReaderHandler.searchUsers(u);
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
    // TODO: Change array list to Component list
     public static ComponentList<User>
             searchUser(User prototype, int pageSize, int pageNum) throws DbException {
         if(prototype == null){
             throw new NullPointerException("User prototype provided is null");
         }
        ComponentList<User> userList = new ComponentList<User>();
        DbPipeline<QueryFood, HyperResult> pipeline =
                new DbPipeline<QueryFood, HyperResult>(PrepStmt.SEARCH_USER);
        HyperResult result = null;
        QueryFood food = new QueryFood(
                new String[][]{
                    {"USER_LEVEL_MIN", Integer.toString(prototype.getUserGroup().getMinLevel())},
                    {"USER_LEVEL_MAX", Integer.toString(prototype.getUserGroup().getMaxLevel())},
                    {"MODEL_AUTH", fixNull(prototype.getUserGroup().getModelAuth())},
                    {"USER_AUTH", fixNull(prototype.getUserGroup().getUserAuth())},
                    {"ALGORITHM_AUTH", fixNull(prototype.getUserGroup().getAlgorithmAuth())},
                    {"USER_GROUP_AUTH", fixNull(prototype.getUserGroup().getUserGroupAuth())},
                    {"MAX_MODELS_MIN", Integer.toString(prototype.getUserGroup().getMinModels())},
                    {"MAX_MODELS_MAX", Integer.toString(prototype.getUserGroup().getMaxModels())},

                    {"EMAIL", fixNull(prototype.getEmail())},
                    {"USERNAME", fixNull(prototype.getUserName())},
                    {"FIRSTNAME", fixNull(prototype.getFirstName())},
                    {"LASTNAME", fixNull(prototype.getLastName())},
                    {"COUNTRY", fixNull(prototype.getCountry())},
                    {"CITY", fixNull(prototype.getCity())},
                    {"ADDRESS", fixNull(prototype.getAddress())},
                    {"ORGANIZATION", fixNull(prototype.getOrganization())},
                    {"WEBPAGE", fixNull(prototype.getWebpage())},
                    {"ROLE", fixNull(prototype.getUserGroup().getName())},});
        if(pageSize <= 0){
            food.add("OFFSET", Integer.toString(0));
            food.add("ROWS", Integer.toString(Integer.MAX_VALUE));
        } else if(pageNum < 0){
            food.add("OFFSET", Integer.toString(0));
            food.add("ROWS", Integer.toString(pageSize));
        } else {
            food.add("OFFSET", Integer.toString(pageNum*pageSize));
            food.add("ROWS", Integer.toString(pageSize));
        }
        try {
            result = pipeline.process(food);
            for (int i = 1; i <= result.getSize(); i++) {
                Iterator<String> it = result.getColumnIterator(i);
                User user = new User(it.next(), it.next(), it.next(), it.next(),
                        it.next(), it.next(), it.next(),
                        it.next(), it.next(), it.next(), it.next(),
                        searchUserGroup(new UserGroup(it.next()),0,0).get(0)
                        );
                userList.add(user);
            }
        } catch (YaqpException ex) {
            String message = "Could not get Users for given Prototype from database";
            throw new DbException(XDH3, message, ex);
        }
        return userList;
    }

     public static ComponentList<User>
             searchUserSkroutz(User prototype, int pageSize, int pageNum) throws DbException {
         if(prototype == null){
             throw new NullPointerException("User prototype provided is null");
         }
        ComponentList<User> userList = new ComponentList<User>();
        DbPipeline<QueryFood, HyperResult> pipeline =
                new DbPipeline<QueryFood, HyperResult>(PrepStmt.SEARCH_USER_SKROUTZ);
        HyperResult result = null;
        QueryFood food = new QueryFood(
                new String[][]{
                    {"USER_LEVEL_MIN", Integer.toString(prototype.getUserGroup().getMinLevel())},
                    {"USER_LEVEL_MAX", Integer.toString(prototype.getUserGroup().getMaxLevel())},
                    {"MODEL_AUTH", fixNull(prototype.getUserGroup().getModelAuth())},
                    {"USER_AUTH", fixNull(prototype.getUserGroup().getUserAuth())},
                    {"ALGORITHM_AUTH", fixNull(prototype.getUserGroup().getAlgorithmAuth())},
                    {"USER_GROUP_AUTH", fixNull(prototype.getUserGroup().getUserGroupAuth())},
                    {"MAX_MODELS_MIN", Integer.toString(prototype.getUserGroup().getMinModels())},
                    {"MAX_MODELS_MAX", Integer.toString(prototype.getUserGroup().getMaxModels())},

                    {"EMAIL", fixNull(prototype.getEmail())},
                    {"USERNAME", fixNull(prototype.getUserName())},
                    {"FIRSTNAME", fixNull(prototype.getFirstName())},
                    {"LASTNAME", fixNull(prototype.getLastName())},
                    {"COUNTRY", fixNull(prototype.getCountry())},
                    {"CITY", fixNull(prototype.getCity())},
                    {"ADDRESS", fixNull(prototype.getAddress())},
                    {"ORGANIZATION", fixNull(prototype.getOrganization())},
                    {"WEBPAGE", fixNull(prototype.getWebpage())},
                    {"ROLE", fixNull(prototype.getUserGroup().getName())}
        });
        if(pageSize <= 0){
            food.add("OFFSET", Integer.toString(0));
            food.add("ROWS", Integer.toString(Integer.MAX_VALUE));
        } else if(pageNum < 0){
            food.add("OFFSET", Integer.toString(0));
            food.add("ROWS", Integer.toString(pageSize));
        } else {
            food.add("OFFSET", Integer.toString(pageNum*pageSize));
            food.add("ROWS", Integer.toString(pageSize));
        }
        try {
            result = pipeline.process(food);
            for (int i = 1; i <= result.getSize(); i++) {
                Iterator<String> it = result.getColumnIterator(i);
                User user = new User(it.next(), it.next(), it.next(), it.next(),
                        it.next(), it.next(), it.next(),
                        it.next(), it.next(), it.next(), it.next(),
                        searchUserGroup(new UserGroup(it.next()),0,0).get(0)
                        );
                userList.add(user);
            }
        } catch (YaqpException ex) {
            String message = "Could not get Users for given Prototype from database";
            throw new DbException(XDH3, message, ex);
        }
        return userList;
    }





     public static ComponentList<AlgorithmOntology>
             searchAlgorithmOntology(AlgorithmOntology prototype, int pageSize, int pageNum) throws YaqpOntException, DbException {
         if(prototype == null){
             throw new NullPointerException("AlgorithmOntology prototype provided is null");
         }
        ComponentList<AlgorithmOntology> ontList = new ComponentList<AlgorithmOntology>();
        DbPipeline<QueryFood, HyperResult> pipeline =
                new DbPipeline<QueryFood, HyperResult>(PrepStmt.SEARCH_ALGORITHM_ONTOLOGY);
        HyperResult result = null;
        QueryFood food = new QueryFood(
                new String[][]{
                    {"NAME", fixNull(prototype.getName())},
                    {"URI", fixNull(prototype.getUri())},
        });
        if(pageSize <= 0){
            food.add("OFFSET", Integer.toString(0));
            food.add("ROWS", Integer.toString(Integer.MAX_VALUE));
        } else if(pageNum < 0){
            food.add("OFFSET", Integer.toString(0));
            food.add("ROWS", Integer.toString(pageSize));
        } else {
            food.add("OFFSET", Integer.toString(pageNum*pageSize));
            food.add("ROWS", Integer.toString(pageSize));
        }
        try {
            result = pipeline.process(food);
            for (int i = 1; i <= result.getSize() ; i++) {
                Iterator<String> it = result.getColumnIterator(i);
                AlgorithmOntology algont = new AlgorithmOntology(it.next());
                ontList.add(algont);
            }
        } catch (YaqpException ex) {
            String message = "Could not get Algorithm Ontologies from database";
            YaqpLogger.LOG.log(new Debug(ReaderHandler.class, message));
            throw new DbException(XDH3, message, ex);
        }
        return ontList;
    }


     public static ComponentList<AlgorithmOntology>
             searchAlgorithmOntologySkroutz(AlgorithmOntology prototype, int pageSize, int pageNum) throws YaqpOntException, DbException {
         if(prototype == null){
             throw new NullPointerException("AlgorithmOntology prototype provided is null");
         }
        ComponentList<AlgorithmOntology> ontList = new ComponentList<AlgorithmOntology>();
        DbPipeline<QueryFood, HyperResult> pipeline =
                new DbPipeline<QueryFood, HyperResult>(PrepStmt.SEARCH_ALGORITHM_ONTOLOGY_SKROUTZ);
        HyperResult result = null;
        QueryFood food = new QueryFood(
                new String[][]{
                    {"NAME", fixNull(prototype.getName())},
                    {"URI", fixNull(prototype.getUri())},
        });
        if(pageSize <= 0){
            food.add("OFFSET", Integer.toString(0));
            food.add("ROWS", Integer.toString(Integer.MAX_VALUE));
        } else if(pageNum < 0){
            food.add("OFFSET", Integer.toString(0));
            food.add("ROWS", Integer.toString(pageSize));
        } else {
            food.add("OFFSET", Integer.toString(pageNum*pageSize));
            food.add("ROWS", Integer.toString(pageSize));
        }
        try {
            result = pipeline.process(food);
            for (int i = 1; i <= result.getSize() ; i++) {
                Iterator<String> it = result.getColumnIterator(i);
                AlgorithmOntology algont = new AlgorithmOntology(it.next());
                ontList.add(algont);
            }
        } catch (YaqpException ex) {
            String message = "Could not get Algorithm Ontologies from database";
            YaqpLogger.LOG.log(new Debug(ReaderHandler.class, message));
            throw new DbException(XDH3, message, ex);
        }
        return ontList;
    }



     public static ComponentList<Feature>
             searchFeature(Feature prototype, int pageSize, int pageNum) throws DbException {
         if(prototype == null){
             throw new NullPointerException("Feature prototype provided is null");
         }
        ComponentList<Feature> featureList = new ComponentList<Feature>();
        DbPipeline<QueryFood,HyperResult> pipeline =
                new DbPipeline<QueryFood, HyperResult>(PrepStmt.SEARCH_FEATURE);

        HyperResult result = null;
        QueryFood food = new QueryFood(
                new String[][]{
                    {"UID_MIN", Integer.toString(prototype.getMinId())},
                    {"UID_MAX", Integer.toString(prototype.getMaxId())},
                    {"URI", fixNull(prototype.getURI())}
        });
        if(pageSize <= 0){
            food.add("OFFSET", Integer.toString(0));
            food.add("ROWS", Integer.toString(Integer.MAX_VALUE));
        } else if(pageNum < 0){
            food.add("OFFSET", Integer.toString(0));
            food.add("ROWS", Integer.toString(pageSize));
        } else {
            food.add("OFFSET", Integer.toString(pageNum*pageSize));
            food.add("ROWS", Integer.toString(pageSize));
        }
        try {
            result = pipeline.process(food);
            for (int i = 1; i <= result.getSize() ; i++) {
                Iterator<String> it = result.getColumnIterator(i);
                Feature feature = new Feature(Integer.parseInt(it.next()), it.next());
                featureList.add(feature);
            }
        } catch (YaqpException ex) {
            throw new DbException(XDH7, "Could not get Features from Database", ex);
        }
        return featureList;
    }


     public static ComponentList<Feature>
             searchFeatureSkroutz(Feature prototype, int pageSize, int pageNum) throws DbException {
         if(prototype == null){
             throw new NullPointerException("Feature prototype provided is null");
         }
        ComponentList<Feature> featureList = new ComponentList<Feature>();
        DbPipeline<QueryFood,HyperResult> pipeline =
                new DbPipeline<QueryFood, HyperResult>(PrepStmt.SEARCH_FEATURE_SKROUTZ);

        HyperResult result = null;
        QueryFood food = new QueryFood(
                new String[][]{
                    {"UID_MIN", Integer.toString(prototype.getMinId())},
                    {"UID_MAX", Integer.toString(prototype.getMaxId())},
                    {"URI", fixNull(prototype.getURI())}
        });
        if(pageSize <= 0){
            food.add("OFFSET", Integer.toString(0));
            food.add("ROWS", Integer.toString(Integer.MAX_VALUE));
        } else if(pageNum < 0){
            food.add("OFFSET", Integer.toString(0));
            food.add("ROWS", Integer.toString(pageSize));
        } else {
            food.add("OFFSET", Integer.toString(pageNum*pageSize));
            food.add("ROWS", Integer.toString(pageSize));
        }
        try {
            result = pipeline.process(food);
            for (int i = 1; i <= result.getSize() ; i++) {
                Iterator<String> it = result.getColumnIterator(i);
                Feature feature = new Feature(Integer.parseInt(it.next()), it.next());
                featureList.add(feature);
            }
        } catch (YaqpException ex) {
            throw new DbException(XDH7, "Could not get Features from Database", ex);
        }
        return featureList;
    }



        /**
     *
     * @return
     */
    public static ComponentList<QSARModel> searchQSARModels(QSARModel prototype, int pageSize, int pageNum) throws DbException {
        if(prototype == null){
             throw new NullPointerException("QSARModel prototype provided is null");
         }
        ComponentList<QSARModel> modelList = new ComponentList<QSARModel>();
        DbPipeline<QueryFood,HyperResult> pipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.SEARCH_QSAR_MODEL);

        QueryFood food = new QueryFood(
                new String[][]{
                    {"UID_MIN", Integer.toString(prototype.getMinId())},
                    {"UID_MAX", Integer.toString(prototype.getMaxId())},
                    {"CODE", fixNull(prototype.getCode())},
                    {"PRED_FEATURE_MIN", Integer.toString(prototype.getPredictionFeature().getMinId())},
                    {"PRED_FEATURE_MAX", Integer.toString(prototype.getPredictionFeature().getMaxId())},
                    {"DEP_FEATURE_MIN", Integer.toString(prototype.getDependentFeature().getMinId())},
                    {"DEP_FEATURE_MAX", Integer.toString(prototype.getDependentFeature().getMaxId())},
                    {"ALGORITHM", fixNull(prototype.getAlgorithm().getMeta().getName())},
                    {"CREATED_BY", fixNull(prototype.getUser().getUserName())},
                    {"DATASET_URI", fixNull(prototype.getDataset())},
        });
        if(pageSize <= 0){
            food.add("OFFSET", Integer.toString(0));
            food.add("ROWS", Integer.toString(Integer.MAX_VALUE));
        } else if(pageNum < 0){
            food.add("OFFSET", Integer.toString(0));
            food.add("ROWS", Integer.toString(pageSize));
        } else {
            food.add("OFFSET", Integer.toString(pageNum*pageSize));
            food.add("ROWS", Integer.toString(pageSize));
        }
        HyperResult result = null;
        try {
                result = pipeline.process(food);
                for (int i = 1; i <= result.getSize(); i++) {
                    Iterator<String> it = result.getColumnIterator(i);
                    QSARModel model = new QSARModel();
                    model.setId(Integer.parseInt(it.next()));
                    model.setCode(it.next());
                    model.setPredictionFeature(searchFeature(new Feature(Integer.parseInt(it.next())), 0, 0).get(0));
                    model.setDependentFeature(searchFeature(new Feature(Integer.parseInt(it.next())), 0, 0).get(0));
                    model.setAlgorithm(getAlgorithm(it.next()));
                    model.setUser(searchUser(new User(it.next()), 0, 0).get(0));
                    model.setDataset(it.next());
                    model.setIndependentFeatures(getIndepFeatures(model).getComponentList());
                    modelList.add(model);
                }         
        } catch (YaqpException ex) {
            throw new DbException(XDH7, "Could not get QSARModels from Database", ex);
        }
        return modelList;
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
//                    searchFeature(Integer.parseInt(it.next())), searchFeature(Integer.parseInt(it.next())),
//                    getAlgorithm(it.next()), searchUsers(it.next()), it.next(), it.next());
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
//                    Task.STATUS.valueOf(it.next()), searchUsers(it.next()), getAlgorithm(it.next()), Integer.parseInt(it.next()),
//                    it.next(), it.next(), it.next());
//            tasks.add(task);
//        }
//        return tasks;
//    }




    /**
     * ****************************************************************************
     * ----------------------------------------------------------------------------
     *                      GET QUERIES
     * ----------------------------------------------------------------------------
     * ****************************************************************************
     */




      /**
     * Name-based database search for algorithms.
     * @param algorithmName
     * @return
     * @throws YaqpOntException
     */
    public static ComponentList<AlgorithmOntology>
            getAlgOntRelation(Algorithm prototype, int pageSize, int pageNum) throws YaqpOntException, DbException {
        if(prototype == null){
             throw new NullPointerException("Algorithm prototype provided is null");
         }
        ComponentList<AlgorithmOntology> ontList = new ComponentList<AlgorithmOntology>();
        DbPipeline<QueryFood,HyperResult> pipeline =
                new DbPipeline<QueryFood, HyperResult>(PrepStmt.GET_ALGORITHM_ONTOLOGY_RELATION);

        QueryFood food = new QueryFood(
                new String[][]{
                    {"ALGORITHM", prototype.getMeta().getName()}
                });
        if(pageSize <= 0){
            food.add("OFFSET", Integer.toString(0));
            food.add("ROWS", Integer.toString(Integer.MAX_VALUE));
        } else if(pageNum < 0){
            food.add("OFFSET", Integer.toString(0));
            food.add("ROWS", Integer.toString(pageSize));
        } else {
            food.add("OFFSET", Integer.toString(pageNum*pageSize));
            food.add("ROWS", Integer.toString(pageSize));
        }
        HyperResult result = null;
        try {
            result = pipeline.process(food);
            for (int i = 1; i <= result.getSize() ; i++) {
            Iterator<String> it = result.getColumnIterator(i);
            AlgorithmOntology algont = new AlgorithmOntology(it.next());
            ontList.add(algont);
            }
        } catch (YaqpException ex) {
            String message = "Could not get Algorithm-Ontology Relations from database";
            throw new DbException(XDH4, message, ex);
        }
        return ontList;
    }

    // TODO: Fix the following code and then perform tons of tests!
    public static ComponentList<Algorithm> getOntAlgRelation(AlgorithmOntology prototype, int pageSize, int pageNum) throws DbException {
        if(prototype == null){
             throw new NullPointerException("AlgorithmOntology prototype provided is null");
         }
        ComponentList<Algorithm> algList = new ComponentList<Algorithm>();
        DbPipeline<QueryFood,HyperResult> pipeline =
                new DbPipeline<QueryFood, HyperResult>(PrepStmt.GET_ONTOLOGY_ALGORITHM_RELATION);

        QueryFood food = new QueryFood(
                new String[][]{
                    {"ONTOLOGY", prototype.getName()}
                });
        if(pageSize <= 0){
            food.add("OFFSET", Integer.toString(0));
            food.add("ROWS", Integer.toString(Integer.MAX_VALUE));
        } else if(pageNum < 0){
            food.add("OFFSET", Integer.toString(0));
            food.add("ROWS", Integer.toString(pageSize));
        } else {
            food.add("OFFSET", Integer.toString(pageNum*pageSize));
            food.add("ROWS", Integer.toString(pageSize));
        }
        HyperResult result = null;
        try {
            result = pipeline.process(food);
        } catch (YaqpException e) {
            String message = "Could not get Ontology-Algorithm Relations from database\n";
            throw new DbException(XDH5, message, e);
        }
        AlgorithmMeta meta = null;
        for (int i = 1; i <= result.getSize() ; i++) {
            Iterator<String> it = result.getColumnIterator(i);
            String algName = it.next();
            Class<?> c = YaqpAlgorithms.class;
            Method[] allMethods = c.getDeclaredMethods();
                for (Method m : allMethods) {
                    try {
                        if( m.getReturnType().equals(AlgorithmMeta.class)){
                            meta = (AlgorithmMeta) m.invoke(null, (Object[]) null); 
                            if(meta.getName().equals(algName)){
                                Algorithm algorithm = new Algorithm(meta);
                                algList.add(algorithm);
                                break;
                            }
                        }
                    } catch (IllegalAccessException ex) {
                        throw new RuntimeException(ex);
                    } catch (IllegalArgumentException ex) {
                        throw new RuntimeException(ex);
                    } catch (InvocationTargetException ex) {
                        throw new RuntimeException(ex);
                    }
                }
        }
        return algList;
    }


    public static ComponentList<Algorithm> getAlgorithms() throws DbException {
        ComponentList<Algorithm> algorithmList = new ComponentList<Algorithm>();
        DbPipeline<QueryFood,HyperResult> pipeline =
                new DbPipeline<QueryFood, HyperResult>(PrepStmt.GET_ALGORITHMS);
        HyperResult result = null;
        try {
            result = pipeline.process(new QueryFood());
        } catch (YaqpException ex) {
            String message = "Could not get Algorithms from database\n";
            throw new DbException(XDH7, message, ex);
        }
        AlgorithmMeta meta = null;
        for (int i = 1; i < result.getSize() + 1; i++) {
            Iterator<String> it = result.getColumnIterator(i);
            String algName = it.next();
            Class<?> c = YaqpAlgorithms.class;
                Method[] allMethods = c.getDeclaredMethods();
                for (Method m : allMethods) {
                    try {
                        if( m.getReturnType().equals(AlgorithmMeta.class)){
                            meta = (AlgorithmMeta) m.invoke(null, (Object[]) null);
                            if(meta.getName().equals(algName)){
                                Algorithm algorithm = new Algorithm(meta);
                                algorithmList.add(algorithm);
                                break;
                            }
                        }
                    } catch (IllegalAccessException ex) {
                        throw new RuntimeException(ex);
                    } catch (IllegalArgumentException ex) {
                        throw new RuntimeException(ex);
                    } catch (InvocationTargetException ex) {
                        throw new RuntimeException(ex);
                    }
                }
        }
        return algorithmList;
    }


    public static Algorithm getAlgorithm(String name) throws DbException {
        AlgorithmMeta meta = null;
        Algorithm algorithm = null;
            Class<?> c = YaqpAlgorithms.class;
            Method[] allMethods = c.getDeclaredMethods();
                for (Method m : allMethods) {
                    try {
                        if( m.getReturnType().equals(AlgorithmMeta.class)){
                            meta = (AlgorithmMeta) m.invoke(null, (Object[]) null);
                            if(meta.getName().equals(name)){
                                algorithm = new Algorithm(meta);
                                return algorithm;
                            }
                        }
                    } catch (IllegalAccessException ex) {
                        throw new RuntimeException(ex);
                    } catch (IllegalArgumentException ex) {
                        throw new RuntimeException(ex);
                    } catch (InvocationTargetException ex) {
                        throw new RuntimeException(ex);
                    }
                }
        throw new DbException(XDH6, "No such Algorithm :" + name);
    }

    public static ComponentList<Feature> getIndepFeatures(QSARModel model) throws DbException{
        ComponentList<Feature> featureList = new ComponentList<Feature>();
         DbPipeline<QueryFood,HyperResult> pipeline =  new DbPipeline<QueryFood, HyperResult>(PrepStmt.GET_INDEP_FEATURES);

        QueryFood food = new QueryFood(
                new String[][]{
                    {"MODEL_UID", Integer.toString(model.getId())}
                });
        HyperResult result = null;
        try {
            result = pipeline.process(food);
            for (int i = 1; i < result.getSize() + 1; i++) {
                Iterator<String> it = result.getColumnIterator(i);
                Feature feature = new Feature(Integer.parseInt(it.next()), it.next());
                featureList.add(feature);
            }
        } catch (YaqpException ex) {
            String message = "Could not get Features from database\n";
            throw new DbException(XDH8, message, ex);
        }
        return featureList;
    }

}
