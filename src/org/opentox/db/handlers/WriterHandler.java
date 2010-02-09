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

import com.hp.hpl.jena.rdf.model.Resource;
import java.net.URI;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.processors.BatchProcessor;
import org.opentox.ontology.components.*;
import org.opentox.db.exceptions.BadEmailException;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.exceptions.DuplicateKeyException;
import org.opentox.db.processors.DbPipeline;
import org.opentox.db.queries.HyperResult;
import org.opentox.db.queries.QueryFood;
import org.opentox.db.table.StandardTables;
import org.opentox.db.util.EmailSupervisor;
import org.opentox.db.util.PrepStmt;
import org.opentox.db.util.TheDbConnector;
import org.opentox.ontology.exceptions.ImproperEntityException;
import org.opentox.ontology.util.AlgorithmParameter;
import org.opentox.ontology.util.YaqpAlgorithms;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.*;

/**
 * Handles write operations in the database. Provides a collection of static methods
 * which can be used to write data and entities like {@link User }, {@link UserGroup },
 * {@link QSARModel } etc. Each method throws a {@link DbException } in case the entity
 * could not be registered in the DB. Other more specific exceptions like {@link
 * BadEmailException
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class WriterHandler {
    

    private static DbPipeline<QueryFood, HyperResult> addUserPipeline = null,
            addAlgorithmOntologyPipeline = null,
            addUserGroupPipeline = null,
            addAlgorithmPipeline = null,
            addAlgOntRelationPipeline = null,
            addFeaturePipeline = null,
            addQSARModelPipeline = null,
            addSupportVectorPipeline = null,
            addIndepFeaturePipeline = null,
            addTaskPipeline = null,
            addOmegaPipeline = null;

    /**
     *
     * <p>Add an arbitrary entity in the database. The registration will take place
     * according to the type of the component you submit. For example
     * <code>WriterHandler.add(new UserGroup(...));</code> will add a new user group
     * while <code>WriterHandler.add(new Feature(..));</code> will add a new feature
     * in the corresponding table of the database. So, there is no need to know
     * anything about the structure of the database or even what you're adding.</p>
     * @param component A YAQP component such as a {@link User }, a {@link UserGroup Group},
     * of users, a {@link QSARModel QSAR Model}, an Algorithm {@link AlgorithmOntology Ontology} etc.
     * @throws DbException In case the component could not be added in the database either
     * because it is already registered, or because the submitted entity is unacceptable, e.g.
     * a user with email <code>asdf.qwerty</code> (which is not valid). This method is void, so
     * there is no result expected from it, e.g. if you register a QSAR Model in the database using
     * this method you won't be able to know its UID. This being the case, you should
     * use other methods like {@link WriterHandler#addQSARModel(org.opentox.ontology.components.QSARModel)
     * addQSARModel(QSARModel model)}.
     * @see WriterHandler#addAlgorithm(org.opentox.ontology.components.Algorithm) add algorithm
     * @see WriterHandler#addFeature(org.opentox.ontology.components.Feature) add feature
     * @see WriterHandler#addUser(org.opentox.ontology.components.User) add user
     * @see WriterHandler#addUserGroup(org.opentox.ontology.components.UserGroup)  add user group
     * @see WriterHandler#addTask(org.opentox.ontology.components.Task) add task
     */
    public static URI add(YaqpComponent component) throws YaqpException {
        if (component instanceof User) {// add user
            return addUser((User) component);
        } else if (component instanceof UserGroup) { // add user group
            return addUserGroup((UserGroup) component);
        } else if (component instanceof Algorithm) {// add algorithm
            return addAlgorithm((Algorithm) component);
        } else if (component instanceof AlgorithmOntology) {
            return addAlgorithmOntology((AlgorithmOntology) component);
        } else if (component instanceof Feature) {// add a feature
            return addFeature((Feature) component);
        } else if (component instanceof QSARModel) {// add QSAR model
            return addPredictiveModel((QSARModel) component);
        } else if (component instanceof Task) {
            return addTask((Task) component);
        } else if (component instanceof OmegaModel) {
            return addOmega((OmegaModel) component);
        } else {// This component cannot be added in the database
            String message = "This component cannot be added in the "
                    + "database because it cannot be cast to any of the recognizable "
                    + "datatypes ";
            YaqpLogger.LOG.log(new Debug(WriterHandler.class, message));
            throw new ImproperEntityException("XKK7A", message);
        }
    }

    /**
     * Add a new UserGroup in the database. The name and the user_level of the group
     * have to specified.
     * @param userGroup a user group with specified name and authorization level.
     * @throws NumberFormatException in case the provided user authorization level is not
     * an integer number
     */
    protected static URI addUserGroup(UserGroup userGroup) throws YaqpException {
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
        } catch (DbException ex) {
            String message = "UserGroup could not be added";
            YaqpLogger.LOG.log(new Debug(WriterHandler.class, message));
            throw new DbException("XUG512", message, ex);
        }
        return userGroup.uri();
    }

    /**
     *
     * Add a new algorithm ontology in the database.
     * @param ontology algorithm ontology to be added in the database with specified
     * name and uri.
     * @throws DbException If the algorithm ontology is already registered
     * in the database (instance of {@link DuplicateKeyException }) or if the provided
     * algorithm ontology is <code>null</code> or its name or URI is not specified/missing.
     */
    protected static URI addAlgorithmOntology(AlgorithmOntology ontology) throws YaqpException {
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
                String message = "Algorithm Ontology Already Registered in the Database!";
                YaqpLogger.LOG.log(new Trace(WriterHandler.class, message));
                YaqpLogger.LOG.log(new Debug(WriterHandler.class, ex.toString()));
                throw new DuplicateKeyException("XQW8Y", message, ex);
            }
            YaqpLogger.LOG.log(new Trace(WriterHandler.class,
                    "Could not add the following Algorithm Ontology : \n" + ontology));
            YaqpLogger.LOG.log(new Debug(WriterHandler.class, ex.toString()));
            throw new DbException("XFE12", ex);
        }
        return ontology.uri();
    }

    /**
     * Add a new user in the database with specified username, email and other personal
     * information. For security reasons the password should be provided as a digest
     * (e.g. SHA-512). The information that should be privided are the username, the password,
     * first and last name of the user and the email. All other information like <code>
     * organization</code> are optional. For more information take a look at the class
     * {@link User } and documentation therein.
     * @param user The user to be added in the database
     * @throws DuplicateKeyException If the user is already registered.
     * @throws BadEmailException If the provided email is not acceptable.
     * @throws DbException If the provided user could not be added in the database. This is
     * the case when one tries to register a user like <code>User u = new User()</code>, i.e.
     * without specified the mandatory fields.
     * @see ReaderHandler#getUser(org.opentox.ontology.components.User) search for user
     * @see EmailSupervisor
     */
    protected static URI addUser(User user) throws YaqpException {
        if (!EmailSupervisor.checkMail(user.getEmail())) {
            throw new BadEmailException("XE449", "Bad user email");
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
                String message = "Cannot add user because email or username are already used by some other user.";
                YaqpLogger.LOG.log(new Debug(WriterHandler.class, message));
                YaqpLogger.LOG.log(new Debug(WriterHandler.class, ex.toString()));
                throw new DuplicateKeyException("XU715", message, ex);
            }
            String message = "XU716 - Could not add the following user :\n" + user;
            YaqpLogger.LOG.log(new Trace(WriterHandler.class, message));
            YaqpLogger.LOG.log(new Debug(WriterHandler.class, ex.toString()));
        }
        return user.uri();
    }

    /**
     * Add a new feature in the database. The URI of the feature has to be specified.
     * @param feature a feature to be added in the database.
     * @throws DbException In case the feature cannot be added in the database. This
     * is the case when the feature already exists ({@link DuplicateKeyException }), when
     * the URI of the feature is not set or if you try to add a <code>null</code>
     * feature.
     */
    protected static URI addFeature(Feature feature) throws YaqpException {
        if (addFeaturePipeline == null) {
            addFeaturePipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.ADD_FEATURE);
        }
        QueryFood food = new QueryFood(
                new String[][]{
                    {"URI", feature.getURI()}
                });
        try {
            addFeaturePipeline.process(food);
            YaqpLogger.LOG.log(new Trace(WriterHandler.class, "Feature added: \n" + feature));
        } catch (DbException ex) {
            if (ex.toString().contains("DuplicateKeyException")) {
                String message = "Cannot add feature because it already exists: " + feature;
                YaqpLogger.LOG.log(new Trace(WriterHandler.class, message));
                YaqpLogger.LOG.log(new Debug(WriterHandler.class, ex.toString()));
                throw new DuplicateKeyException("XU719", message, ex);
            }
            YaqpLogger.LOG.log(new Debug(WriterHandler.class, "XU719 - Could not add the following feature :\n" + feature));
        }
        return feature.uri();
    }

    /**
     * Add an algorithm in the database. You only have to provide the Name of the algorithm
     * in the algorithm object you provide and the algorithm types of the provided entity.
     * @param algorithm the algorithm to be added in the database. The algorithm is registered
     * in the database along with a set of algorithm-ontology-relation entries which facilitate
     * search for algorithms.
     * @throws DuplicateKeyException In case the algorithm already exists.
     */
    protected static URI addAlgorithm(Algorithm algorithm) throws YaqpException {
        if (addAlgorithmPipeline == null) {
            addAlgorithmPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.ADD_ALGORITHM);
        }
        if (addAlgOntRelationPipeline == null) {
            addAlgOntRelationPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.ADD_ALGORITHM_ONTOL_RELATION);
        }
        BatchProcessor bp = new BatchProcessor(addAlgOntRelationPipeline);

        QueryFood algfood = new QueryFood(
                new String[][]{
                    {"NAME", algorithm.getMeta().getName()}
                });

        ArrayList<QueryFood> relfood = new ArrayList<QueryFood>();
        AlgorithmOntology ontology = new AlgorithmOntology(algorithm.getMeta().getAlgorithmType());

        QueryFood food = new QueryFood(
                new String[][]{
                    {"ALGORITHM", algorithm.getMeta().getName()},
                    {"ONTOLOGY", ontology.getName()}
                });
        relfood.add(food);
        Set<Resource> r = algorithm.getMeta().getAlgorithmType().getSuperEntities();
        Iterator<Resource> it = r.iterator();
        while (it.hasNext()) {
            food = new QueryFood(
                    new String[][]{
                        {"ALGORITHM", algorithm.getMeta().getName()},
                        {"ONTOLOGY", it.next().getLocalName()}
                    });
            relfood.add(food);
        }
        try {
            addAlgorithmPipeline.process(algfood);
            YaqpLogger.LOG.log(new Trace(WriterHandler.class, "Algorithm added: \n" + algorithm));
        } catch (DbException ex) {
            if (ex.toString().contains("DuplicateKeyException")) {
                String message = "Cannot add algorithm because it already exists.";
                YaqpLogger.LOG.log(new Trace(WriterHandler.class, message));
                YaqpLogger.LOG.log(new Debug(WriterHandler.class, ex.toString()));
                throw new DuplicateKeyException("XE4B", message, ex);
            }
            YaqpLogger.LOG.log(new Trace(WriterHandler.class, "Could not add the following algorithm :\n" + algorithm));
            YaqpLogger.LOG.log(new Debug(WriterHandler.class, ex.toString()));
        }
        try {
            bp.process(relfood);
            YaqpLogger.LOG.log(new Trace(WriterHandler.class, "Algorithm-Ontology relations batch added"));
        } catch (Exception ex) {
            YaqpLogger.LOG.log(new Trace(WriterHandler.class, "Could not batch add Algorithm-Ontology relations :\n" + algorithm));
            YaqpLogger.LOG.log(new Debug(WriterHandler.class, ex.toString()));
        }
        return algorithm.uri();
    }

    /**
     * Register a QSAR model in the database for a certain user. QSAR Models include
     * <code>MLR</code> ones and all models that do not possess any tuning parameters.
     * <code>Tunable</code> models are registered in the database using the method
     * {@link WriterHandler#addPredictiveModel(org.opentox.ontology.components.TunableQSARModel)
     * addPredictiveModel() }.
     * @param model The model to be added in the database.
     * @return the unique id (UID) of the registered QSAR model.
     * @throws DbException In case the model cannot be added. This is the case when
     * you attempt to add a QSARModel providing incomplete information, i.e. when you
     * ommit the <code>prediction_feature</code>, or the <code>Algorithm</code> or
     * the <code>dataset_uri</code> and so on. Alse a {@link DbException } is thrown
     * if you atempt to add a <code>null</code> model or you somehow violate some
     * Foreign Key Constraint.
     * @see WriterHandler#addPredictiveModel(org.opentox.ontology.components.TunableQSARModel) add svm/svc models
     */
    protected static int addQSARModel(QSARModel model) throws DbException {

        if (addQSARModelPipeline == null) {
            addQSARModelPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.ADD_QSAR_MODEL);
        }
        if (addIndepFeaturePipeline == null) {
            addIndepFeaturePipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.ADD_INDEP_FEATURE_RELATION);
        }
        QSARModel.ModelStatus modelStatus = model.getModelStatus();
        if (model.getModelStatus() == null) {
            modelStatus = QSARModel.ModelStatus.UNDER_DEVELOPMENT;
        }
        BatchProcessor bp = new BatchProcessor(addIndepFeaturePipeline);
        int r = 0;
        HyperResult result = new HyperResult();
        QueryFood food = new QueryFood(
                new String[][]{
                    {"CODE", model.getCode()},
                    {"PREDICTION_FEATURE", Integer.toString(model.getPredictionFeature().getID())},
                    {"DEPENDENT_FEATURE", Integer.toString(model.getDependentFeature().getID())},
                    {"ALGORITHM", model.getAlgorithm().getMeta().getName()},
                    {"CREATED_BY", model.getUser().getEmail()},
                    {"DATASET_URI", model.getDataset()},
                    {"STATUS", modelStatus.toString()}
                });
        try {
            result = addQSARModelPipeline.process(food);
            YaqpLogger.LOG.log(new Trace(WriterHandler.class, "QSarModel added"));
        } catch (DbException ex) {
            if (ex.toString().contains("DuplicateKeyException")) {
                String message = "XU715 - Cannot add model because URI is already used by some other model.";
                YaqpLogger.LOG.log(new Trace(WriterHandler.class, message));
                YaqpLogger.LOG.log(new Debug(WriterHandler.class, ex.toString()));
                throw new DuplicateKeyException(message, ex);
            }
            YaqpLogger.LOG.log(new Trace(WriterHandler.class, "XU716 - Could not add the following QSAR model :\n" + model));
            YaqpLogger.LOG.log(new Debug(WriterHandler.class, ex.toString()));
            throw new DbException("XU716", ex);
        }
        if (result.getSize() == 1) {
            Iterator<String> it = result.getColumnIterator(1);
            r = Integer.parseInt(it.next());
        }// TODO: Otherwise what?

        ArrayList<QueryFood> foodlist = new ArrayList<QueryFood>();
        for (Feature feature : model.getIndependentFeatures()) {
            QueryFood f = new QueryFood(
                    new String[][]{
                        {"MODEL_UID", Integer.toString(r)},
                        {"FEATURE_UID", Integer.toString(feature.getID())}
                    });
            foodlist.add(f);
        }
        try {
            bp.process(foodlist);
            YaqpLogger.LOG.log(new Trace(WriterHandler.class, "EDD444 - Independent feature "
                    + "relations batch added for model: " + model.getId()));
        } catch (Exception ex) {
            YaqpLogger.LOG.log(new Trace(WriterHandler.class, "XDD445 - Could not batch "
                    + "add Independent feature relations for model :" + model.getId()));
            YaqpLogger.LOG.log(new Debug(WriterHandler.class, ex.toString()));
        }
        return r;
    }

    /**
     * Add a model in the database along with its tuning parameters. SVM and SVC models
     * are added using this method.
     * @param model A model with some tuning parameters to be added in the models' table
     * in the database.
     * @return the ID of the model.
     * @throws DbException In case the model could not be added. This is a very usual
     * case when you don't submit all the needed parameters. For example, if you want
     * to add an SVM model and forget to provide the parameter <code>tolerance</code>,
     * this exception will be throws. Consider using {@link ConstantParameters#DEFAULTS the
     * defaults} for SVM models if you need to do so.
     */
    protected static URI addPredictiveModel(QSARModel model) throws YaqpException {
        String algorithmName = model.getAlgorithm().getMeta().getName();

        if (algorithmName.equals(YaqpAlgorithms.SVM.getMeta().getName())
                || algorithmName.equals(YaqpAlgorithms.SVC.getMeta().getName())) {
            return addSuppVectModel(model);
        }else if (algorithmName.equals(YaqpAlgorithms.MLR.getMeta().getName())){
            int new_id = addQSARModel(model);
            System.err.println(new_id);
            model.setId(new_id);
            return model.uri();
        }
        return null;
    }

    private static URI addSuppVectModel(QSARModel model) throws YaqpException {
        int r = 0;
        try {
            r = WriterHandler.addQSARModel((QSARModel) model);
        } catch (DbException ex) {
            // Could not add the model to the parent table
            // Reproduce the exception!
            throw new DbException("XKW9", "Support Vector Could not be added in the database", ex);
        }
        if (addSupportVectorPipeline == null) {
            addSupportVectorPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.ADD_SUPPORT_VECTOR);
        }
        QueryFood food = new QueryFood();
        Map<String, AlgorithmParameter> modelParams = model.getParams();
        Set<Entry<String, AlgorithmParameter>> entrySet = modelParams.entrySet();
        food.add("UID", Integer.toString(r));
        for (Entry e : entrySet){
            String pName = e.getKey().toString();
            System.out.println(pName);
            AlgorithmParameter algParam = (AlgorithmParameter) e.getValue();
            System.out.println(algParam.paramValue.toString());
            System.out.println(e.getKey().toString() + "--" + e.getValue().toString());
            food.add(pName.toUpperCase(), algParam.paramValue.toString());
        }
        
        try {
            addSupportVectorPipeline.process(food);
        } catch (DbException ex) {
            try {
                // First delete the model from the parent table!
                Statement delete = TheDbConnector.DB.getConnection().createStatement();
                delete.executeUpdate("DELETE FROM " + StandardTables.QSARModels().getTableName() + " WHERE UID =" + r);
            } catch (SQLException ex1) {
                throw new DbException("XDL4", "Could not delete the QSAR model with uid : " + r, ex1);
            }

        }
        model.setId(r);
        return model.uri();
    }

    /**
     * Add a task in the database. Tasks are added upon creation where te following
     * parameters are specified: <code>name, status</code> (Always set to <code>RUNNING</code>
     * when the task is created), <code>user</code> that created the task, the <code>algorithm
     * </code> and the <code>estimated duarion</code> in seconds.
     * Note that the <code>STATUS</code> of the Task is always set to <code>RUNNING</code>
     * irrespective of the specified status of the provided task and the corresponding
     * <code>HTTP STATUS</code> is always set to <code>202</code>. These values are updated
     * when the task is completed, cancelled or deleted.
     * @param task A task to be registered in the database, which corresponds to a
     * running task which just started running.
     * @throws DbException In case the task cannot be added in the database. This is the
     * case if the specified <code>user</code> does not correspond to some existing user of the
     * provided <code>name</code> is not valid, the provided task is <code>null</code> or some
     * other issue occurs (e.g. DB connectivity).
     * @see WriterHandler#add(org.opentox.ontology.components.YaqpComponent) add an arbitrary
     * component
     */
    protected static URI addTask(Task task) throws YaqpException {
        if (addTaskPipeline == null) {
            addTaskPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.ADD_TASK);
        }
        QueryFood food = new QueryFood(
                new String[][]{
                    {"NAME", task.getName()},
                    {"CREATED_BY", task.getUser().getEmail()},
                    {"ALGORITHM", task.getAlgorithm().getMeta().getName()},
                    {"DURATION", Integer.toString(task.getDuration_sec())}
                });
        try {
            addTaskPipeline.process(food);
            YaqpLogger.LOG.log(new Trace(WriterHandler.class, "Task added: \n" + task));
        } catch (DbException ex) {
            String message = "Could not add the following Task :\n" + task;
            YaqpLogger.LOG.log(new Debug(WriterHandler.class, message));
            throw new DbException("XU718", message, ex);
        }
        return task.uri();
    }

    /**
     * Add a new Omega model in the database.
     * @param model
     * @return
     * @throws DbException
     */
    protected static URI addOmega(OmegaModel model) throws YaqpException {
        int r = 0;
        if (addOmegaPipeline == null) {
            addOmegaPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.ADD_OMEGA_MODEL);
        }

        User u = model.getUser();
        if (u == null) {
            throw new DbException("XVW981", "You did not provide the user that created the DoA model");
        }
        String userEmail = u.getEmail();
        if (userEmail == null) {
            throw new DbException("XVW982", "Unknown user because the provided email is null");
        }
        QueryFood food = new QueryFood();
        food.add("CODE", model.getCode());
        food.add("CREATED_BY", userEmail);
        food.add("DATASET_URI", model.getDataset());
        HyperResult result = addOmegaPipeline.process(food);
        if (result.getSize() == 1) {
            Iterator<String> it = result.getColumnIterator(1);
            r = Integer.parseInt(it.next());
        }// TODO: Otherwise what?
        model.setId(r);
        return model.uri();
    }
}
