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

import java.lang.String;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.opentox.core.exceptions.Cause;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.db.exceptions.DbException;
import org.opentox.ontology.components.*;
import org.opentox.db.processors.DbPipeline;
import org.opentox.db.queries.HyperResult;
import org.opentox.db.queries.QueryFood;
import org.opentox.db.util.Page;
import org.opentox.db.util.PrepStmt;
import org.opentox.ontology.exceptions.ImproperEntityException;
import org.opentox.ontology.util.AlgorithmMeta;
import org.opentox.ontology.util.AlgorithmParameter;
import org.opentox.ontology.util.YaqpAlgorithms;
import org.opentox.ontology.util.vocabulary.ConstantParameters;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.*;
import static org.opentox.core.exceptions.Cause.*;

/**
 *
 * Handles all read operations from the database.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ReaderHandler {


    /**
     * Global Search function. Provided a valid YaqpComponent prototype
     * the handler will search in the database and return a ComponentList that
     * contains all components that matched the prototype.
     * @param component  
     *      valid YaqpComponent to be used a prototype for the searching operation
     * @param page
     *      The requested database page
     * @param Skroutz 
     *      activate Skroutz mode (returns component primary key only) ommiting rest
     *      of the information.
     * @return 
     *      ComponentList consisting of YaqpComponent objects .
     * @throws DbException
     *      In case it is impossible to retreive the requested information from the database
     *      due to communication errors.
     * @throws ImproperEntityException
     *      In case the provided prototype is improper as a search prototype.
     * @throws NullPointerException
     *      In case the provided prototype or the the Page is <code>null</code>
     * @see ReaderHandler#searchUser(org.opentox.ontology.components.User, org.opentox.db.util.Page) search users
     */
    public static ComponentList<YaqpComponent> search(YaqpComponent component, Page page, boolean Skroutz)
            throws DbException, ImproperEntityException {
        if (component == null) {
            throw new NullPointerException("Cannot read a null component from the database");
        }
        if (page == null){
            page = new Page();
        }
        ComponentList<YaqpComponent> result = new ComponentList<YaqpComponent>();
        if (component instanceof User) {// search User
            ComponentList<User> list;
            if(Skroutz){
                list = searchUserSkroutz((User)component,page);
            }else{
                list = searchUser((User)component,page);
            }
            Iterator<User> it = list.iterator();
            while(it.hasNext()){
                result.add(it.next());
            }
        } else if (component instanceof UserGroup) {// search UserGroup
            ComponentList<UserGroup> list;
            if(Skroutz){
                list = searchUserGroupSkroutz((UserGroup)component,page);
            }else{
                list = searchUserGroup((UserGroup)component,page);
            }
            Iterator<UserGroup> it = list.iterator();
            while(it.hasNext()){
                result.add(it.next());
            }
        } else if (component instanceof Feature) {// search Feature
            ComponentList<Feature> list;
            if(Skroutz){
                list = searchFeatureSkroutz((Feature)component,page);
            }else{
                list = searchFeature((Feature)component,page);
            }
            Iterator<Feature> it = list.iterator();
            while(it.hasNext()){
                result.add(it.next());
            }
        } else if (component instanceof QSARModel) {// search QSARModel
            ComponentList<QSARModel> list;
            if(Skroutz){
                list = searchQSARModelSkroutz((QSARModel)component,page);
            }else{
                list = searchQSARModel((QSARModel)component,page);
            }
            Iterator<QSARModel> it = list.iterator();
            while(it.hasNext()){
                result.add(it.next());
            }
        } else if (component instanceof OmegaModel) {// search OmegaModel
            ComponentList<OmegaModel> list;
            if(Skroutz){
                list = searchOmegaSkroutz((OmegaModel)component,page);
            }else{
                list = searchOmega((OmegaModel)component,page);
            }
            Iterator<OmegaModel> it = list.iterator();
            while(it.hasNext()){
                result.add(it.next());
            }
        } else if (component instanceof AlgorithmOntology) {// search AlgorithmOntology
            ComponentList<AlgorithmOntology> list;
            if(Skroutz){
                list = searchAlgorithmOntologySkroutz((AlgorithmOntology)component,page);
            }else{
                list = searchAlgorithmOntology((AlgorithmOntology)component,page);
            }
            Iterator<AlgorithmOntology> it = list.iterator();
            while(it.hasNext()){
                result.add(it.next());
            }
        } else if (component instanceof Task) {// search task
            ComponentList<Task> list;
            if(Skroutz){
                list = searchTaskSkroutz((Task)component,page);
            }else{
                list = searchTask((Task)component,page);
            }
            Iterator<Task> it = list.iterator();
            while(it.hasNext()){
                result.add(it.next());
            }
        } else if (component instanceof Algorithm) {// get algorithms
            ComponentList<Algorithm> list = getAlgorithms();
            Iterator<Algorithm> it = list.iterator();
            while(it.hasNext()){
                result.add(it.next());
            }
        } else {// This component cannot be added in the database
            String message = "This component cannot be found in the "
                    + "database because it cannot be cast to any of the recognizable "
                    + "datatypes ";
            YaqpLogger.LOG.log(new Debug(WriterHandler.class, message));
            throw new ImproperEntityException(Cause.XIE0, message);
        }
        return result;
    }


    /**
     * ****************************************************************************
     * ----------------------------------------------------------------------------
     *                      SEARCH QUERIES
     * ----------------------------------------------------------------------------
     * ****************************************************************************
     */



    /**
     * Searches for User Groups in the database for a given UserGroup prototype
     * and returns the groups that match that prototype.
     * @param prototype a valid UserGroup prototype
     * @param page the required database page
     * @return ComponentList of UserGroup that contains the required groups
     * @throws DbException
     * @see ReaderHandler#searchUser(org.opentox.ontology.components.User, org.opentox.db.util.Page)
     * @see ReaderHandler#searchUserGroupSkroutz(org.opentox.ontology.components.UserGroup, org.opentox.db.util.Page)
     */
     protected static ComponentList<UserGroup>
             searchUserGroup(UserGroup prototype, Page page) throws DbException {
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
                    {"MAX_MODELS_MAX", Integer.toString(prototype.getMaxModels())},

                    {"OFFSET", page.getOffset()},
                    {"ROWS", page.getRows()}
        });       
        
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

     /**
     * Searches for User Groups in the database for a given UserGroup prototype in Skroutz Mode,
     * and returns the groups that match that prototype, filled only with group name variable.
     * @param prototype a valid UserGroup prototype
     * @param page the required database page
     * @return ComponentList of UserGroup that contains the required groups.
     * @throws DbException
      * @see ReaderHandler#searchUser(org.opentox.ontology.components.User, org.opentox.db.util.Page)
      * @see ReaderHandler#searchUserGroup(org.opentox.ontology.components.UserGroup, org.opentox.db.util.Page)
     */
    protected static ComponentList<UserGroup>
             searchUserGroupSkroutz(UserGroup prototype, Page page) throws DbException {
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
                    {"MAX_MODELS_MAX", Integer.toString(prototype.getMaxModels())},

                    {"OFFSET", page.getOffset()},
                    {"ROWS", page.getRows()}
        });
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
     * @param prototype 
     *          The prototype for the search.
     * @param page
     *          The requested page from the database.
     * @return
     *          List of users meeting the specifications of the search criteria or
     *          an empty list if no such user was found.
     * @throws DbException
     *          In case the search cannot be performed.
     * @see ReaderHandler#searchUserSkroutz(org.opentox.ontology.components.User, org.opentox.db.util.Page)
     */
     protected static ComponentList<User>
             searchUser(User prototype, Page page) throws DbException {
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
                    {"ROLE", fixNull(prototype.getUserGroup().getName())},
                    
                    {"OFFSET", page.getOffset()},
                    {"ROWS", page.getRows()}
        });
        try {
            result = pipeline.process(food);
            for (int i = 1; i <= result.getSize(); i++) {
                Iterator<String> it = result.getColumnIterator(i);
                User user = new User(it.next(), it.next(), it.next(), it.next(),
                        it.next(), it.next(), it.next(),
                        it.next(), it.next(), it.next(), it.next(),
                        searchUserGroup(new UserGroup(it.next()), new Page()).get(0)
                        );
                userList.add(user);
            }
        } catch (YaqpException ex) {
            String message = "Could not get Users for given Prototype from database";
            throw new DbException(XDH3, message, ex);
        }
        return userList;
    }

     /**
     * Searches for Users in the database for a given User prototype in Skroutz Mode,
     * and returns the users that match that prototype, filled only with their username.
     * @param prototype a valid User prototype
     * @param page the required database page
     * @return ComponentList of User that contains the required users.
     * @throws DbException
      * @see ReaderHandler#searchUser(org.opentox.ontology.components.User, org.opentox.db.util.Page)
     */
     protected static ComponentList<User>
             searchUserSkroutz(User prototype, Page page) throws DbException {
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
                    {"ROLE", fixNull(prototype.getUserGroup().getName())},

                    {"OFFSET", page.getOffset()},
                    {"ROWS", page.getRows()}
        });
        try {
            result = pipeline.process(food);
            for (int i = 1; i <= result.getSize(); i++) {
                Iterator<String> it = result.getColumnIterator(i);
                User user = new User();
                user.setUserName(it.next());
                userList.add(user);
            }
        } catch (YaqpException ex) {
            String message = "Could not get Users for given Prototype from database";
            throw new DbException(XDH3, message, ex);
        }
        return userList;
    }

     /**
     * Searches for Algorithm Ontologies in the database for a given AlgorithmOntology prototype
     * and returns the ontologies that match that prototype.
     * @param prototype a valid AlgorithmOntology prototype
     * @param page the required database page
     * @return ComponentList of AlgorithmOntology that contains the required ontologies
     * @throws DbException
     * @see ReaderHandler#searchUser(org.opentox.ontology.components.User, org.opentox.db.util.Page)
     * @see ReaderHandler#searchAlgorithmOntologySkroutz(org.opentox.ontology.components.AlgorithmOntology, org.opentox.db.util.Page)
     */
     protected static ComponentList<AlgorithmOntology>
             searchAlgorithmOntology(AlgorithmOntology prototype, Page page) throws DbException {
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

                    {"OFFSET", page.getOffset()},
                    {"ROWS", page.getRows()}
        });
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


     /**
     * Searches for Algorithm Ontologies in the database for a given AlgorithmOntology prototype in Skroutz mode,
     * and returns the ontologies that match that prototype, filled with ontology name value only.
     * @param prototype a valid AlgorithmOntology prototype
     * @param page the required database page
     * @return ComponentList of AlgorithmOntology that contains the required ontologies
     * @throws DbException
     * @see ReaderHandler#searchUser(org.opentox.ontology.components.User, org.opentox.db.util.Page)
     * @see ReaderHandler#searchAlgorithmOntology(org.opentox.ontology.components.AlgorithmOntology, org.opentox.db.util.Page)
     */
     protected static ComponentList<AlgorithmOntology>
             searchAlgorithmOntologySkroutz(AlgorithmOntology prototype, Page page) throws DbException {
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

                    {"OFFSET", page.getOffset()},
                    {"ROWS", page.getRows()}
        });
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

     /**
     * Searches for Features in the database for a given Feature prototype
     * and returns the features that match that prototype.
     * @param prototype a valid Features prototype
     * @param page the required database page
     * @return ComponentList of Feature that contains the required features
     * @throws DbException
     * @see ReaderHandler#searchUser(org.opentox.ontology.components.User, org.opentox.db.util.Page)
     * @see ReaderHandler#searchFeatureSkroutz(org.opentox.ontology.components.Feature, org.opentox.db.util.Page)
     */
     protected static ComponentList<Feature>
             searchFeature(Feature prototype, Page page) throws DbException {
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
                    {"URI", fixNull(prototype.getURI())},

                    {"OFFSET", page.getOffset()},
                    {"ROWS", page.getRows()}
        });
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
     * Searches for Features in the database for a given Feature prototype in Skroutz Mode,
     * and returns the features that match that prototype, filled with feature id value only.
     * @param prototype a valid Features prototype
     * @param page the required database page
     * @return ComponentList of Feature that contains the required features
     * @throws DbException
     * @see ReaderHandler#searchUser(org.opentox.ontology.components.User, org.opentox.db.util.Page)
     * @see ReaderHandler#searchFeature(org.opentox.ontology.components.Feature, org.opentox.db.util.Page)
     */
     protected static ComponentList<Feature>
             searchFeatureSkroutz(Feature prototype, Page page) throws DbException {
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
                    {"URI", fixNull(prototype.getURI())},

                    {"OFFSET", page.getOffset()},
                    {"ROWS", page.getRows()}
        });
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
     * Searches for QSARModels in the database for a given QSARModel prototype
     * and returns the models that match that prototype. If the prototype does not
      * contain SupportedVector parameters the search will be performed on all
      * QSARModels in the database. If the prototype contains SupportedVector parameters
      * the search will be narrowed for SVM-SVC related models only.
     * @param prototype a valid QSARModel prototype
     * @param page the required database page
     * @return ComponentList of QSARModel that contains the required models
     * @throws DbException
     * @see ReaderHandler#searchUser(org.opentox.ontology.components.User, org.opentox.db.util.Page)
     * @see ReaderHandler#searchQSARModelSkroutz(org.opentox.ontology.components.QSARModel, org.opentox.db.util.Page)
     */
     @SuppressWarnings({"unchecked"})
    protected static ComponentList<QSARModel> searchQSARModel(QSARModel prototype, Page page) throws DbException {
        if(prototype == null){
             throw new NullPointerException("QSARModel prototype provided is null");
         }

        if (prototype.getParams() == null){
            throw new NullPointerException("Are you nuts?! You provided a model with a null parameter set");
        }
        ComponentList<QSARModel> modelList = new ComponentList<QSARModel>();
        HyperResult result = null;
        DbPipeline<QueryFood,HyperResult> pipeline;

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
                    {"CREATED_BY", fixNull(prototype.getUser().getEmail())},
                    {"DATASET_URI", fixNull(prototype.getDataset())},
                    {"STATUS", fixNull(prototype.getModelStatus())}
        });

        if (prototype.hasVec()) {
            pipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.SEARCH_QSAR_MODEL);
            food.add("GAMMA_MIN", Double.toString((Double) prototype.getParams().get(ConstantParameters.gamma+"_min").paramValue));
            food.add("GAMMA_MAX", Double.toString((Double) prototype.getParams().get(ConstantParameters.gamma+"_max").paramValue));
            food.add("EPSILON_MIN", Double.toString((Double) prototype.getParams().get(ConstantParameters.epsilon+"_min").paramValue));
            food.add("EPSILON_MAX", Double.toString((Double) prototype.getParams().get(ConstantParameters.epsilon+"_max").paramValue));
            food.add("COST_MIN", Double.toString((Double) prototype.getParams().get(ConstantParameters.cost+"_min").paramValue));
            food.add("COST_MAX", Double.toString((Double) prototype.getParams().get(ConstantParameters.cost+"_max").paramValue));
            food.add("COEFF0_MIN", Double.toString((Double) prototype.getParams().get(ConstantParameters.coeff0+"_min").paramValue));
            food.add("COEFF0_MAX", Double.toString((Double) prototype.getParams().get(ConstantParameters.coeff0+"_max").paramValue));
            food.add("TOLERANCE_MIN", Double.toString((Double) prototype.getParams().get(ConstantParameters.tolerance+"_min").paramValue));
            food.add("TOLERANCE_MAX", Double.toString((Double) prototype.getParams().get(ConstantParameters.tolerance+"_max").paramValue));
            food.add("CACHESIZE_MIN", Integer.toString((Integer) prototype.getParams().get(ConstantParameters.cacheSize+"_min").paramValue));
            food.add("CACHESIZE_MAX", Integer.toString((Integer) prototype.getParams().get(ConstantParameters.cacheSize+"_max").paramValue));
            food.add("KERNEL", (String) prototype.getParams().get(ConstantParameters.kernel).paramValue);
            food.add("DEGREE_MIN", Integer.toString((Integer) prototype.getParams().get(ConstantParameters.degree+"_min").paramValue));
            food.add("DEGREE_MAX", Integer.toString((Integer) prototype.getParams().get(ConstantParameters.degree+"_max").paramValue));
        } else {
            pipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.SEARCH_QSAR_MODEL_ALL);                
        }
        food.add("OFFSET", page.getOffset());
        food.add("ROWS", page.getRows());

        try {
                result = pipeline.process(food);
                for (int i = 1; i <= result.getSize(); i++) {
                    Iterator<String> it = result.getColumnIterator(i);
                    QSARModel model = new QSARModel();
                    model.setId(Integer.parseInt(it.next()));
                    model.setCode(it.next());
                    model.setPredictionFeature(searchFeature(new Feature(Integer.parseInt(it.next())), new Page()).get(0));
                    model.setDependentFeature(searchFeature(new Feature(Integer.parseInt(it.next())), new Page()).get(0));
                    model.setAlgorithm(getAlgorithm(it.next()));
                    model.setUser(searchUser(new User(it.next()), new Page()).get(0));
                    model.setTimestamp(it.next());
                    model.setDataset(it.next());
                    model.setIndependentFeatures(getIndepFeatures(model).getComponentList());
                    model.setModelStatus(QSARModel.ModelStatus.valueOf(it.next()));
                    Map<String,AlgorithmParameter> params = null;
                    if(model.getAlgorithm().equals(YaqpAlgorithms.SVC)){
                        if( model.getId() != Integer.parseInt(it.next())){
                            throw new RuntimeException("Wrong pairing between QSAR and SVC models - this shouldn't have happened. ever.");
                        }
                        params = ConstantParameters.SVMParams();
                        params.put(ConstantParameters.gamma, params.get(ConstantParameters.gamma).updateParamValue(Double.parseDouble(it.next())));
                        it.next();
                        params.put(ConstantParameters.cost, params.get(ConstantParameters.cost).updateParamValue(Double.parseDouble(it.next())));
                        params.put(ConstantParameters.coeff0, params.get(ConstantParameters.coeff0).updateParamValue(Double.parseDouble(it.next())));
                        params.put(ConstantParameters.tolerance, params.get(ConstantParameters.tolerance).updateParamValue(Double.parseDouble(it.next())));
                        params.put(ConstantParameters.cacheSize, params.get(ConstantParameters.cacheSize).updateParamValue(Integer.parseInt(it.next())));
                        params.put(ConstantParameters.kernel, params.get(ConstantParameters.kernel).updateParamValue(it.next()));
                        params.put(ConstantParameters.degree, params.get(ConstantParameters.degree).updateParamValue(Integer.parseInt(it.next())));
                    }else if( model.getAlgorithm().equals(YaqpAlgorithms.SVM)){
                        if( model.getId() != Integer.parseInt(it.next())){
                            throw new RuntimeException("Wrong pairing between QSAR and SVC models - this shouldn't have happened. ever.");
                        }
                        params = ConstantParameters.SVMParams();
                        params.put(ConstantParameters.gamma, params.get(ConstantParameters.gamma).updateParamValue(Double.parseDouble(it.next())));
                        params.put(ConstantParameters.epsilon, params.get(ConstantParameters.epsilon).updateParamValue(Double.parseDouble(it.next())));
                        params.put(ConstantParameters.cost, params.get(ConstantParameters.cost).updateParamValue(Double.parseDouble(it.next())));
                        params.put(ConstantParameters.coeff0, params.get(ConstantParameters.coeff0).updateParamValue(Double.parseDouble(it.next())));
                        params.put(ConstantParameters.tolerance, params.get(ConstantParameters.tolerance).updateParamValue(Double.parseDouble(it.next())));
                        params.put(ConstantParameters.cacheSize, params.get(ConstantParameters.cacheSize).updateParamValue(Integer.parseInt(it.next())));
                        params.put(ConstantParameters.kernel, params.get(ConstantParameters.kernel).updateParamValue(it.next()));
                        params.put(ConstantParameters.degree, params.get(ConstantParameters.degree).updateParamValue(Integer.parseInt(it.next())));
                    }else if( model.getAlgorithm().equals(YaqpAlgorithms.MLR) || model.getAlgorithm().equals(YaqpAlgorithms.NAIVE_BAYES)){
                        params = new HashMap<String,AlgorithmParameter>();
                    }
                    model.setParams(params);
                    modelList.add(model);
                }         
        } catch (YaqpException ex) {
            System.out.println(ex);
            throw new DbException(XDH7, "Could not get QSARModels from Database", ex);
        }
        return modelList;
    }

    /**
     * Searches for QSARModels in the database for a given QSARModel prototype in Skroutz mode,
     * and returns the models that match that prototype, filled with model id value only.
      * If the prototype does not contain SupportedVector parameters the search will be performed on all
      * QSARModels in the database. If the prototype contains SupportedVector parameters
      * the search will be narrowed for SVM-SVC related models only.
     * @param prototype a valid QSARModel prototype
     * @param page the required database page
     * @return ComponentList of QSARModel that contains the required models
     * @throws DbException
     * @see ReaderHandler#searchUser(org.opentox.ontology.components.User, org.opentox.db.util.Page)
     * @see ReaderHandler#searchQSARModel(org.opentox.ontology.components.QSARModel, org.opentox.db.util.Page)
     */
    protected static ComponentList<QSARModel> searchQSARModelSkroutz(QSARModel prototype, Page page) throws DbException {
        if(prototype == null){
             throw new NullPointerException("QSARModel prototype provided is null");
         }
        if (prototype.getParams() == null){
            throw new NullPointerException("Are you nuts?! You provided a model with a null parameter set");
        }
        ComponentList<QSARModel> modelList = new ComponentList<QSARModel>();
        HyperResult result = null;
        DbPipeline<QueryFood,HyperResult> pipeline;

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
                    {"CREATED_BY", fixNull(prototype.getUser().getEmail())},
                    {"DATASET_URI", fixNull(prototype.getDataset())},
                    {"STATUS", fixNull(prototype.getModelStatus())}
        });

        if (prototype.hasVec()) {
            pipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.SEARCH_QSAR_MODEL);
            food.add("GAMMA_MIN", Double.toString((Double) prototype.getParams().get(ConstantParameters.gamma+"_min").paramValue));
            food.add("GAMMA_MAX", Double.toString((Double) prototype.getParams().get(ConstantParameters.gamma+"_max").paramValue));
            food.add("EPSILON_MIN", Double.toString((Double) prototype.getParams().get(ConstantParameters.epsilon+"_min").paramValue));
            food.add("EPSILON_MAX", Double.toString((Double) prototype.getParams().get(ConstantParameters.epsilon+"_max").paramValue));
            food.add("COST_MIN", Double.toString((Double) prototype.getParams().get(ConstantParameters.cost+"_min").paramValue));
            food.add("COST_MAX", Double.toString((Double) prototype.getParams().get(ConstantParameters.cost+"_max").paramValue));
            food.add("COEFF0_MIN", Double.toString((Double) prototype.getParams().get(ConstantParameters.coeff0+"_min").paramValue));
            food.add("COEFF0_MAX", Double.toString((Double) prototype.getParams().get(ConstantParameters.coeff0+"_max").paramValue));
            food.add("TOLERANCE_MIN", Double.toString((Double) prototype.getParams().get(ConstantParameters.tolerance+"_min").paramValue));
            food.add("TOLERANCE_MAX", Double.toString((Double) prototype.getParams().get(ConstantParameters.tolerance+"_max").paramValue));
            food.add("CACHESIZE_MIN", Integer.toString((Integer) prototype.getParams().get(ConstantParameters.cacheSize+"_min").paramValue));
            food.add("CACHESIZE_MAX", Integer.toString((Integer) prototype.getParams().get(ConstantParameters.cacheSize+"_max").paramValue));
            food.add("KERNEL", (String) prototype.getParams().get(ConstantParameters.kernel).paramValue);
            food.add("DEGREE_MIN", Integer.toString((Integer) prototype.getParams().get(ConstantParameters.degree+"_min").paramValue));
            food.add("DEGREE_MAX", Integer.toString((Integer) prototype.getParams().get(ConstantParameters.degree+"_max").paramValue));
        } else {
            pipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.SEARCH_QSAR_MODEL_ALL);
        }
        food.add("OFFSET", page.getOffset());
        food.add("ROWS", page.getRows());

        try {
                result = pipeline.process(food);
                for (int i = 1; i <= result.getSize(); i++) {
                    Iterator<String> it = result.getColumnIterator(i);
                    QSARModel model = new QSARModel();
                    model.setId(Integer.parseInt(it.next()));
                    modelList.add(model);
                }
        } catch (YaqpException ex) {
            System.out.println(ex);
            throw new DbException(XDH7, "Could not get QSARModels from Database", ex);
        }
        return modelList;
    }

    /**
     * Searches for Omega Models in the database for a given omega prototype
     * and returns the models that match that prototype.
     * @param prototype a valid OmegaModel prototype
     * @param page the required database page
     * @return ComponentList of OmegaModel that contains the required models
     * @throws DbException
     * @see ReaderHandler#searchUser(org.opentox.ontology.components.User, org.opentox.db.util.Page)
     * @see ReaderHandler#searchOmegaSkroutz(org.opentox.ontology.components.OmegaModel, org.opentox.db.util.Page)
     */
    protected static ComponentList<OmegaModel> searchOmega(OmegaModel prototype, Page page) throws DbException{
        if(prototype == null){
             throw new NullPointerException("QSARModel prototype provided is null");
         }
        ComponentList<OmegaModel> modelList = new ComponentList<OmegaModel>();
        HyperResult result = null;
        DbPipeline<QueryFood,HyperResult> pipeline = new DbPipeline<QueryFood,HyperResult>(PrepStmt.SEARCH_OMEGA);

        QueryFood food = new QueryFood(
                new String[][]{
                    {"UID_MIN", Integer.toString(prototype.getMinId())},
                    {"UID_MAX", Integer.toString(prototype.getMaxId())},
                    {"CODE", fixNull(prototype.getCode())},
                    {"CREATED_BY", fixNull(prototype.getUser().getEmail())},
                    {"DATASET_URI", fixNull(prototype.getDataset())},

                    {"OFFSET", page.getOffset()},
                    {"ROWS", page.getRows()}
        });
        try {
                result = pipeline.process(food);
                for (int i = 1; i <= result.getSize(); i++) {
                    Iterator<String> it = result.getColumnIterator(i);
                    OmegaModel model = new OmegaModel();
                    model.setId(Integer.parseInt(it.next()));
                    model.setCode(it.next());
                    model.setUser(searchUser(new User(it.next()), new Page()).get(0));
                    model.setDataset(it.next());
                    model.setTimestamp(it.next());
                    modelList.add(model);
                }
        } catch (YaqpException ex) {
            System.out.println(ex);
            throw new DbException(XDH7, "Could not get OmegaModels from Database", ex);
        }
        return modelList;
    }

    /**
     * Searches for Omega Models in the database for a given omega prototype in Skroutz Mode,
     * and returns the models that match that prototype, filled with model id value only.
     * @param prototype a valid OmegaModel prototype
     * @param page the required database page
     * @return ComponentList of OmegaModel that contains the required models
     * @throws DbException
     * @see ReaderHandler#searchUser(org.opentox.ontology.components.User, org.opentox.db.util.Page)
     * @see ReaderHandler#searchOmega(org.opentox.ontology.components.OmegaModel, org.opentox.db.util.Page)
     */
    protected static ComponentList<OmegaModel> searchOmegaSkroutz(OmegaModel prototype, Page page) throws DbException{
        if(prototype == null){
             throw new NullPointerException("QSARModel prototype provided is null");
         }
        ComponentList<OmegaModel> modelList = new ComponentList<OmegaModel>();
        HyperResult result = null;
        DbPipeline<QueryFood,HyperResult> pipeline = new DbPipeline<QueryFood,HyperResult>(PrepStmt.SEARCH_OMEGA_SKROUTZ);

        QueryFood food = new QueryFood(
                new String[][]{
                    {"UID_MIN", Integer.toString(prototype.getMinId())},
                    {"UID_MAX", Integer.toString(prototype.getMaxId())},
                    {"CODE", fixNull(prototype.getCode())},
                    {"CREATED_BY", fixNull(prototype.getUser().getEmail())},
                    {"DATASET_URI", fixNull(prototype.getDataset())},

                    {"OFFSET", page.getOffset()},
                    {"ROWS", page.getRows()}
        });
        try {
                result = pipeline.process(food);
                for (int i = 1; i <= result.getSize(); i++) {
                    Iterator<String> it = result.getColumnIterator(i);
                    OmegaModel model = new OmegaModel();
                    model.setId(Integer.parseInt(it.next()));
                    modelList.add(model);
                }
        } catch (YaqpException ex) {
            System.out.println(ex);
            throw new DbException(XDH7, "Could not get OmegaModels from Database", ex);
        }
        return modelList;
    }

    /**
     * Searches for Tasks in the database for a given task prototype
     * and returns the tasks that match that prototype.
     * @param prototype a valid OmegaModel prototype
     * @param page the required database page
     * @return ComponentList of Task that contains the required tasks
     * @throws DbException
     * @see ReaderHandler#searchUser(org.opentox.ontology.components.User, org.opentox.db.util.Page)
     * @see ReaderHandler#searchTaskSkroutz(org.opentox.ontology.components.Task, org.opentox.db.util.Page)
     */
    protected static ComponentList<Task> searchTask(Task prototype, Page page) throws DbException{
        if(prototype == null){
             throw new NullPointerException("QSARModel prototype provided is null");
         }
        ComponentList<Task> taskList = new ComponentList<Task>();
        HyperResult result = null;
        DbPipeline<QueryFood,HyperResult> pipeline = new DbPipeline<QueryFood,HyperResult>(PrepStmt.SEARCH_TASK);

        QueryFood food = new QueryFood(
                new String[][]{
                    {"NAME", fixNull(prototype.getName())},
                    {"STATUS", fixNull(prototype.getTaskStatus())},
                    {"CREATED_BY", fixNull(prototype.getUser().getEmail())},
                    {"ALGORITHM", fixNull(prototype.getAlgorithm().getMeta().getName())},
                    {"HTTPSTATUS_MIN", Integer.toString(prototype.getHttpStatusMin())},
                    {"HTTPSTATUS_MAX", Integer.toString(prototype.getHttpStatusMax())},
                    {"RESULT", fixNull(prototype.getResult())},
                    {"DURATION_MIN", Integer.toString(prototype.getDurationMin())},
                    {"DURATION_MAX", Integer.toString(prototype.getDurationMax())},

                    {"OFFSET", page.getOffset()},
                    {"ROWS", page.getRows()}
        });
        try {
                result = pipeline.process(food);
                for (int i = 1; i <= result.getSize(); i++) {
                    Iterator<String> it = result.getColumnIterator(i);
                    Task task = new Task();
                    task.setName(it.next());
                    task.setTaskStatus(Task.STATUS.valueOf(it.next()));
                    task.setUser(searchUser(new User(it.next()), new Page()).get(0));
                    task.setAlgorithm(getAlgorithm(it.next()));
                    task.setHttpStatus(Integer.parseInt(it.next()));
                    task.setResult(it.next());
                    task.setStartStamp(it.next());
                    task.setEndStamp(it.next());
                    task.setDuration(Integer.parseInt(it.next()));
                    taskList.add(task);
                }
        } catch (YaqpException ex) {
            System.out.println(ex);
            throw new DbException(XDH7, "Could not get OmegaModels from Database", ex);
        }
        return taskList;
    }

    /**
     * Searches for Tasks in the database for a given task prototype in Skroutz Mode,
     * and returns the tasks that match that prototype, filled with task name value only.
     * @param prototype a valid OmegaModel prototype
     * @param page the required database page
     * @return ComponentList of Task that contains the required tasks
     * @throws DbException
     * @see ReaderHandler#searchUser(org.opentox.ontology.components.User, org.opentox.db.util.Page)
     * @see ReaderHandler#searchTask(org.opentox.ontology.components.Task, org.opentox.db.util.Page)
     */
    protected static ComponentList<Task> searchTaskSkroutz(Task prototype, Page page) throws DbException{
        if(prototype == null){
             throw new NullPointerException("QSARModel prototype provided is null");
         }
        ComponentList<Task> taskList = new ComponentList<Task>();
        HyperResult result = null;
        DbPipeline<QueryFood,HyperResult> pipeline = new DbPipeline<QueryFood,HyperResult>(PrepStmt.SEARCH_TASK_SKROUTZ);

        QueryFood food = new QueryFood(
                new String[][]{
                    {"NAME", fixNull(prototype.getName())},
                    {"STATUS", fixNull(prototype.getTaskStatus())},
                    {"CREATED_BY", fixNull(prototype.getUser().getEmail())},
                    {"ALGORITHM", fixNull(prototype.getAlgorithm().getMeta().getName())},
                    {"HTTPSTATUS_MIN", Integer.toString(prototype.getHttpStatusMin())},
                    {"HTTPSTATUS_MAX", Integer.toString(prototype.getHttpStatusMax())},
                    {"RESULT", fixNull(prototype.getResult())},
                    {"DURATION_MIN", Integer.toString(prototype.getDurationMin())},
                    {"DURATION_MAX", Integer.toString(prototype.getDurationMax())},

                    {"OFFSET", page.getOffset()},
                    {"ROWS", page.getRows()}
        });
        try {
                result = pipeline.process(food);
                for (int i = 1; i <= result.getSize(); i++) {
                    Iterator<String> it = result.getColumnIterator(i);
                    Task task = new Task();
                    task.setName(it.next());
                    taskList.add(task);
                }
        } catch (YaqpException ex) {
            System.out.println(ex);
            throw new DbException(XDH7, "Could not get OmegaModels from Database", ex);
        }
        return taskList;
    }



    /**
     * ****************************************************************************
     * ----------------------------------------------------------------------------
     *                      GET QUERIES
     * ----------------------------------------------------------------------------
     * ****************************************************************************
     */

    /**
     * Gets all Ontology relations for a given Algorithm prototype.
     * @param prototype a valid Algorithm prototype
     * @param page the required database page
     * @return ComponentList of AlgorithmOntology that contains the required ontologies
     * @throws DbException
     * @see ReaderHandler#getOntAlgRelation(org.opentox.ontology.components.AlgorithmOntology, org.opentox.db.util.Page)
     */
    protected static ComponentList<AlgorithmOntology> getAlgOntRelation(Algorithm prototype, Page page) throws  DbException {
        if (prototype == null) {
            throw new NullPointerException("Algorithm prototype provided is null");
        }
        ComponentList<AlgorithmOntology> ontList = new ComponentList<AlgorithmOntology>();
        DbPipeline<QueryFood, HyperResult> pipeline =
                new DbPipeline<QueryFood, HyperResult>(PrepStmt.GET_ALGORITHM_ONTOLOGY_RELATION);

        QueryFood food = new QueryFood(
                new String[][]{
                    {"ALGORITHM", prototype.getMeta().getName()},
                    {"OFFSET", page.getOffset()},
                    {"ROWS", page.getRows()}
                });
        HyperResult result = null;
        try {
            result = pipeline.process(food);
            for (int i = 1; i <= result.getSize(); i++) {
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

    /**
     * Gets all Algorithms that relate to a given Ontology.
     * @param prototype a valid AlgorithmOntology prototype
     * @param page the required database page
     * @return ComponentList of Algorithm that contains the required algorithms
     * @throws DbException
     * @see ReaderHandler#getAlgOntRelation(org.opentox.ontology.components.Algorithm, org.opentox.db.util.Page)
     */
    protected static ComponentList<Algorithm> getOntAlgRelation(AlgorithmOntology prototype, Page page) throws DbException {
        if(prototype == null){
             throw new NullPointerException("AlgorithmOntology prototype provided is null");
         }
        ComponentList<Algorithm> algList = new ComponentList<Algorithm>();
        DbPipeline<QueryFood,HyperResult> pipeline =
                new DbPipeline<QueryFood, HyperResult>(PrepStmt.GET_ONTOLOGY_ALGORITHM_RELATION);

        QueryFood food = new QueryFood(
                new String[][]{
                    {"ONTOLOGY", prototype.getName()},

                    {"OFFSET", page.getOffset()},
                    {"ROWS", page.getRows()}
        });
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

    /**
     * Gets all Algorithms supported by YAQP from the database.
     * @return ComponentList of Algorithm that contains the required algorithms
     * @throws DbException
     * @see ReaderHandler#getAlgorithm(java.lang.String)
     */
    protected static ComponentList<Algorithm> getAlgorithms() throws DbException {
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

    /**
     * Gets a specific Algorithm from the database for a give algorithm name.
     * @param name The name of the Algorithm to be retrieved.
     * @return an Algorithm component
     * @throws DbException
     * @see ReaderHandler#getAlgorithms()
     */
    protected static Algorithm getAlgorithm(String name) throws DbException {
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

    /**
     * Gets all independent features for given QSARModel
     * @param model A valid QSARModel
     * @return ComponentList of Feature that contains the required features
     * @throws DbException
     * @see ReaderHandler#searchFeature(org.opentox.ontology.components.Feature, org.opentox.db.util.Page)
     */
    protected static ComponentList<Feature> getIndepFeatures(QSARModel model) throws DbException{
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


    /**
     * Auxiliary method.
     * @param in some string
     * @return returns <code>in</code> (the input string) if it is not null and not empty, or
     * <code>%%</code> otherwise.
     */
    private static String fixNull(Object in) {
        if (in == null || in.toString().equals("")) {
            return "%%";
        }
        return in.toString();
    }

}
