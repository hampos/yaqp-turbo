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

import java.util.ArrayList;
import java.util.Iterator;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.ontology.components.*;
import org.opentox.db.processors.DbPipeline;
import org.opentox.db.queries.HyperResult;
import org.opentox.db.queries.QueryFood;
import org.opentox.db.util.PrepStmt;
import org.opentox.ontology.exceptions.YaqpOntException;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Debug;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ReaderHandler {

    private static DbPipeline<QueryFood, HyperResult> getUsersPipeline = null,
            getAlgorithmOntologiesPipeline = null,
            getUserGroupsPipeline = null,
            getAlgorithmsPipeline = null,
            getAlgOntRelationPipeline = null,
            getOntAlgRelationPipeline = null,
            getFeaturesPipeline = null;

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
                    it.next(), it.next(), it.next(), it.next(), new UserGroup(it.next(),0));
            userList.add(user);
        }
        return userList;
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

//    public static ArrayList<Algorithm> getOntAlgRelation(AlgorithmOntology ontology) {
//        if (getOntAlgRelationPipeline == null) {
//            getOntAlgRelationPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.GET_ONTOLOGY_ALGORITHM_RELATION);
//        }
//        QueryFood food = new QueryFood(
//                new String[][]{
//                    {"ONTOLOGY", ontology.getName()}
//                });
//        HyperResult result = null;
//        try {
//            result = getOntAlgRelationPipeline.process(food);
//        } catch (YaqpException e) {
//            YaqpLogger.LOG.log(new Debug(ReaderHandler.class, "Could not get Ontology-Algorithm Relations from database\n"));
//        }
//        ArrayList<Algorithm> algorithmList = new ArrayList<Algorithm>();
//        for (int i = 1; i < result.getSize() + 1; i++) {
//            Iterator<String> it = result.getColumnIterator(i);
//            Algorithm algorithm = new Algorithm(it.next(), it.next(), null);
//            ArrayList<AlgorithmOntology> ontologies = getAlgOntRelation(algorithm);
//            algorithm.setOntologies(ontologies);
//            algorithmList.add(algorithm);
//        }
//        return algorithmList;
//    }
//
//    public static ArrayList<Algorithm> getAlgorithms() {
//        if (getAlgorithmsPipeline == null) {
//            getAlgorithmsPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.GET_ALGORITHMS);
//        }
//        HyperResult result = null;
//        try {
//            result = getAlgorithmsPipeline.process(new QueryFood());
//        } catch (YaqpException e) {
//            YaqpLogger.LOG.log(new Debug(ReaderHandler.class, "Could not get Algorithms from database\n"));
//        }
//        ArrayList<Algorithm> algorithms = new ArrayList<Algorithm>();
//        for (int i = 1; i < result.getSize() + 1; i++) {
//            Iterator<String> it = result.getColumnIterator(i);
//            Algorithm algorithm = new Algorithm(it.next(), it.next(), null);
//            ArrayList<AlgorithmOntology> ontologies = getAlgOntRelation(algorithm);
//            algorithm.setOntologies(ontologies);
//            algorithms.add(algorithm);
//        }
//        return algorithms;
//    }

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
