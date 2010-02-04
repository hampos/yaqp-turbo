/*
 * YAQP - Yet Another QSAR Project: Machine Learning algorithms designed for
 * the prediction of toxicological features of chemical compounds become
 * available on the Web. Yaqp is developed under OpenTox (http://opentox.org)
 * which is an FP7-funded EU research project.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
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
 */
package org.opentox.db.handlers;

import java.lang.Object;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.ontology.components.*;
import org.opentox.db.processors.DbPipeline;
import org.opentox.db.queries.HyperResult;
import org.opentox.db.queries.QueryFood;
import org.opentox.db.util.PrepStmt;
import org.opentox.ontology.exceptions.YaqpOntException;
import org.opentox.ontology.util.AlgorithmMeta;
import org.opentox.ontology.util.YaqpAlgorithms;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Debug;

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
            getFeaturesPipeline = null;

    public static User getUser(String userName) {
        if (getUserPipeline == null) {
            getUserPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.GET_USER);
        }
        HyperResult result = null;
        QueryFood food = new QueryFood(
                new String[][]{
                    {"USERNAME", userName}
                });
        try {
            result = getUserPipeline.process(food);
        } catch (YaqpException ex) {
            YaqpLogger.LOG.log(new Debug(ReaderHandler.class, "XR51 - Could not get User list from database\n"));
        }

        if (result.getSize() == 1) {
            Iterator<String> it = result.getColumnIterator(1);
            User user = new User(it.next(), it.next(), it.next(), it.next(), it.next(), it.next(), it.next(),
                    it.next(), it.next(), it.next(), it.next(), getUserGroup(it.next()));
            return user;
        }
        return null;
    }

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
                    it.next(), it.next(), it.next(), it.next(), getUserGroup(it.next()));
            userList.add(user);
        }
        return userList;
    }

    public static UserGroup getUserGroup(String groupName) {
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
            YaqpLogger.LOG.log(new Debug(ReaderHandler.class, "Could not get User Group " + groupName + " from database\n"));
        }

        if (result.getSize() == 1) {
            Iterator<String> it = result.getColumnIterator(1);
            UserGroup userGroup = new UserGroup(it.next(), Integer.parseInt(it.next()));
            return userGroup;
        }
        return null;
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

    public static ArrayList<AlgorithmOntology> getAlgorithmOntologies() throws YaqpOntException {
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
    public static ArrayList<AlgorithmOntology> getAlgOntRelation(String algorithmName) throws YaqpOntException {
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
            YaqpLogger.LOG.log(new Debug(ReaderHandler.class, "Could not get Algorithm-Ontology Relations from database\n"));
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
    public static ArrayList<Algorithm> getOntAlgRelation(AlgorithmOntology ontology) {
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
            YaqpLogger.LOG.log(new Debug(ReaderHandler.class, "Could not get Ontology-Algorithm Relations from database\n"));
        }
        AlgorithmMeta meta = null;
        ArrayList<Algorithm> algorithmList = new ArrayList<Algorithm>();
        for (int i = 1; i < result.getSize() + 1; i++) {
            Iterator<String> it = result.getColumnIterator(i);
            String algName = it.next();
            try {
                Class<?> c = Class.forName("org.opentox.ontology.util.YaqpAlgorithms");
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
                System.err.println(e);
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
                //Class<?> c = Class.forName("org.opentox.ontology.util.YaqpAlgorithms");
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
                System.err.println(e);
            }
        }
        return algorithmList;
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
}
