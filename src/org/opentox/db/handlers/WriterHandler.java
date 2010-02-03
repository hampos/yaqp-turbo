/*
 *
 * YAQP - Yet Another QSAR Project:
 * Machine Learning algorithms designed for the prediction of toxicological
 * features of chemical compounds become available on the Web. Yaqp is developed
 * under OpenTox (http://opentox.org) which is an FP7-funded EU research project.
 * This project was developed at the Automatic Control Lab in the Chemical Engineering
 * School of National Technical University of Athens. Please read README for more
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

import java.util.ArrayList;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.processors.BatchProcessor;
import org.opentox.ontology.components.*;
import org.opentox.db.exceptions.BadEmailException;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.exceptions.DuplicateKeyException;
import org.opentox.db.processors.DbPipeline;
import org.opentox.db.queries.HyperResult;
import org.opentox.db.queries.QueryFood;
import org.opentox.db.util.EmailSupervisor;
import org.opentox.db.util.PrepStmt;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.*;

/**
 * Handles write operations in the database. Provides a collection of static methods
 * which can be used to write data in the database.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class WriterHandler {

    private static DbPipeline<QueryFood, HyperResult>
            addUserPipeline = null,
            addAlgorithmOntologyPipeline = null,
            addUserGroupPipeline = null,
            addAlgorithmPipeline = null,
            addAlgOntRelationPipeline = null,
            addFeaturePipeline = null;

    /**
     * Add a new UserGroup in the database
     * @param userGroup a user group
     * @throws NumberFormatException in case the provided user authorization level is not
     * an integer number
     */
    public static void addUserGroup(UserGroup userGroup) throws NumberFormatException {
        if (addUserGroupPipeline == null) {
            addUserGroupPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.ADD_USER_GROUP);
        }
        try {
            QueryFood food = new QueryFood(
                    new String[][]{
                        {"NAME", userGroup.getName()},
                        {"USER_LEVEL", Integer.toString(userGroup.getLevel())}
                    });

            addUserGroupPipeline.process(food);
            YaqpLogger.LOG.log(new Trace(WriterHandler.class, "Added new user group '" + userGroup.getName()
                    + "' with authorization level :" + userGroup.getLevel()));
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException("XNF981 - Authorization Level must be an integer");
        } catch (YaqpException ex) {
            YaqpLogger.LOG.log(new Debug(WriterHandler.class, "XUG512 - Could not add the following UserGroup : \n" + userGroup));
        }
    }

    /**
     * Add a new algorithm ontology in the database.
     * @param ontology algorithm ontology to be added in the database.
     * @throws DuplicateKeyException If the algorithm ontology is already registered
     * in the database.
     */
    public static void addAlgorithmOntology(AlgorithmOntology ontology) throws DuplicateKeyException {
        if (addAlgorithmOntologyPipeline == null) {
            addAlgorithmOntologyPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.ADD_ALGORITHM_ONTOLOGY);
        }
        QueryFood food = new QueryFood(
                new String[][]{
                    {"NAME", ontology.getName()},
                    {"URI", ontology.getUri()}
                });
        try {
            addAlgorithmOntologyPipeline.process(food);
            YaqpLogger.LOG.log(new Trace(WriterHandler.class, "Added new algorithm ontology '" + ontology.getName()
                    + "' with uri :" + ontology.getUri()));
        } catch (DbException ex) {
            if (ex.toString().contains("DuplicateKeyException")) {
                String message = "XA981 -Algorithm Ontology Already Registered in the Database!";
                YaqpLogger.LOG.log(new Debug(WriterHandler.class, message));
                throw new DuplicateKeyException(message, ex);
            }
            YaqpLogger.LOG.log(new Debug(WriterHandler.class, "Could not add the following Algorithm Ontology : \n" + ontology));
        }
    }


    public static void add(YaqpComponent component) throws DbException {
        if (component instanceof User){
            WriterHandler.addUser((User)component);
        } else if (component instanceof UserGroup){
            WriterHandler.addUserGroup((UserGroup) component);
        }
    }

    /**
     * Add a new user into the database.
     *
     * @param user the user to be added.
     * @throws DuplicateKeyException If the user is already registered or the mail
     * or/and username are used by some other user.
     */
    public static void addUser(User user) throws DuplicateKeyException, BadEmailException {
        if (!EmailSupervisor.checkMail(user.getEmail())) {
            throw new BadEmailException("XE449 - Bad user email");
        }
        if (addUserPipeline == null) {
            addUserPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.ADD_USER);
        }
        QueryFood food = new QueryFood(
                new String[][]{
                    {"USERNAME", user.getUserName()},
                    {"PASS", user.getUserPass()},
                    {"FIRSTNAME", user.getFirstName()},
                    {"LASTNAME", user.getLastName()},
                    {"EMAIL", user.getEmail()},
                    {"ORGANIZATION", user.getOrganization()},
                    {"COUNTRY", user.getCountry()},
                    {"CITY", user.getCity()},
                    {"ADDRESS", user.getAddress()},
                    {"WEBPAGE", user.getWebpage()},
                    {"ROLE", user.getUserGroup().getName()}
                });
        try {
            addUserPipeline.process(food);
            YaqpLogger.LOG.log(new Trace(WriterHandler.class, "User added: \n" + user));
        } catch (DbException ex) {
            if (ex.toString().contains("DuplicateKeyException")) {
                String message = "XU715 - Cannot add user because email or username are already used by some other user.";
                YaqpLogger.LOG.log(new Debug(WriterHandler.class, message));
                throw new DuplicateKeyException(message, ex);
            }
            YaqpLogger.LOG.log(new Debug(WriterHandler.class, "XU716 - Could not add the following user :\n" + user));
        }
    }

    public static void addFeature(Feature feature) throws DbException {
        if(addFeaturePipeline == null){
        addFeaturePipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.ADD_FEATURE);
        }
        QueryFood food = new QueryFood();
        food.add("URI", feature.getURI());
        addFeaturePipeline.process(food);
    }

    public static void addAlgorithm(Algorithm algorithm) throws DuplicateKeyException {
        if (addAlgorithmPipeline == null) {
            addAlgorithmPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.ADD_ALGORITHM);
        }
        if (addAlgOntRelationPipeline == null) {
            addAlgOntRelationPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.ADD_ALGORITHM_ONTOL_RELATION);
        }
        BatchProcessor bp = new BatchProcessor(addAlgOntRelationPipeline);

        QueryFood algfood = new QueryFood(
                new String[][]{
                    {"NAME", algorithm.getMeta().name},
                    {"URI", algorithm.getMeta().identifier}
                });

        ArrayList<QueryFood> relfood = new ArrayList<QueryFood>();
        AlgorithmOntology ontology = new AlgorithmOntology(algorithm.getMeta().algorithmType);

            QueryFood food = new QueryFood(
                    new String[][]{
                        {"ALGORITHM", algorithm.getMeta().name},
                        {"ONTOLOGY", ontology.getName()}
                    });
            relfood.add(food);


            /*
             * TODO: VERY IMPORTANT!!! Add all super-ontologies for algorithm.getOntology()...
             use algorithm.getMeta().algorithmType.getSuperEntities()
             */

        

        try {
            addAlgorithmPipeline.process(algfood);

            YaqpLogger.LOG.log(new Trace(WriterHandler.class, "Algorithm added: \n" + algorithm));
        } catch (DbException ex) {
            if (ex.toString().contains("DuplicateKeyException")) {
                String message = "Cannot add algorithm because it already exists.";
                YaqpLogger.LOG.log(new Debug(WriterHandler.class, message));
                throw new DuplicateKeyException(message, ex);
            }
            YaqpLogger.LOG.log(new Debug(WriterHandler.class, "Could not add the following algorithm :\n" + algorithm));
        }
        try {
            bp.process(relfood);
            YaqpLogger.LOG.log(new Trace(WriterHandler.class, "Algorithm-Ontology relations batch added"));
        } catch (Exception ex) {
            YaqpLogger.LOG.log(new Debug(WriterHandler.class, "Could not batch add Algorithm-Ontology relations :\n" + algorithm));
            YaqpLogger.LOG.log(new Debug(WriterHandler.class, ex.toString()));
        }
    }

//    private void addPredictionModel(){
//
//    }
//    public void addMlrModel(MlrModel mlrModel){
//
//    }
}
