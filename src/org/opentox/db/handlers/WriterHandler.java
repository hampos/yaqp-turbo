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
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.opentox.core.exceptions.Cause;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.interfaces.JProcessor;
import org.opentox.core.processors.BatchProcessor;
import org.opentox.ontology.components.*;
import org.opentox.db.exceptions.BadEmailException;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.exceptions.DuplicateKeyException;
import org.opentox.db.processors.DbPipeline;
import org.opentox.db.queries.HyperResult;
import org.opentox.db.queries.QueryFood;
import org.opentox.db.table.collection.AlgOntTable;
import org.opentox.db.table.collection.FeaturesTable;
import org.opentox.db.table.collection.QSARModelsTable;
import org.opentox.db.table.collection.SupportVecTable;
import org.opentox.db.table.collection.UserAuthTable;
import org.opentox.db.table.collection.UsersTable;
import org.opentox.db.util.EmailSupervisor;
import org.opentox.db.util.PrepStmt;
import org.opentox.db.util.TheDbConnector;
import org.opentox.ontology.exceptions.ImproperEntityException;
import org.opentox.ontology.util.AlgorithmParameter;
import org.opentox.ontology.util.YaqpAlgorithms;
import org.opentox.ontology.util.vocabulary.ConstantParameters;
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
@SuppressWarnings({"unchecked"}) public class WriterHandler {

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
    public static YaqpComponent add(YaqpComponent component) throws DbException, ImproperEntityException {
        if (component == null) {
            throw new NullPointerException("Cannot add a null component into the database");
        }
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
            throw new ImproperEntityException(Cause.XIE0, message);
        }
    }

    /**
     * Add a new UserGroup in the database. The name and the user_level of the group
     * have to specified.
     * @param userGroup a user group with specified name and authorization level.
     * @throws NumberFormatException in case the provided user authorization level is not
     * an integer number
     */
    protected static UserGroup addUserGroup(UserGroup userGroup) throws DuplicateKeyException, DbException {
        DbPipeline<QueryFood, HyperResult> addUserGroupPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.ADD_USER_GROUP);
        if (userGroup == null) {
            throw new NullPointerException("Cannot add a null user group in the database.");
        }
        if (userGroup.getName() == null) {
            throw new DbException(Cause.XDB321, "It seems you forgot to specify the name "
                    + "of the userGroup");
        }
        if (userGroup.getName().length() > 40) {
            throw new DbException(Cause.XDB490, "Very Big Name for the user group");
        }
        try {
            QueryFood food = new QueryFood(
                    new String[][]{
                        {UserAuthTable.NAME.getColumnName(), userGroup.getName()},
                        {UserAuthTable.USER_LEVEL.getColumnName(), Integer.toString(userGroup.getLevel())},
                        {UserAuthTable.MODEL_AUTH.getColumnName(), userGroup.getModelAuth()},
                        {UserAuthTable.USER_AUTH.getColumnName(), userGroup.getUserAuth()},
                        {UserAuthTable.ALGORITHM_AUTH.getColumnName(), userGroup.getAlgorithmAuth()},
                        {UserAuthTable.USER_GROUP_AUTH.getColumnName(), userGroup.getUserGroupAuth()},
                        {UserAuthTable.MAX_MODELS.getColumnName(), Integer.toString(userGroup.getMaxModels())}
                    });

            addUserGroupPipeline.process(food);
            YaqpLogger.LOG.log(new Trace(WriterHandler.class, "Added new user group '" + userGroup.getName()
                    + "' with authorization level :" + userGroup.getLevel()));
        } catch (final DbException ex) {
            if (ex.getCode() == Cause.XDB800) {
                throw new DuplicateKeyException(Cause.XDH100, "UserGroup already registered", ex);
            }
            String message = "UserGroup could not be added";
            YaqpLogger.LOG.log(new Debug(WriterHandler.class, message));
            throw new DbException(Cause.XDH101, message, ex);
        }
        return userGroup;
    }

    /**
     *
     * Add a new algorithm ontology in the database.
     * @param ontology algorithm ontology to be added in the database with specified
     * name and uri.
     * @return The algorithm ontology itself as added in the database.
     * @throws DuplicateKeyException If the algorithm ontology is already registered
     * in the database
     * @throws DbException if the provided
     * algorithm ontology is <code>null</code> or its name or URI is not specified/missing.
     * @throws NullPointerException If the provided algorithm ontology is null
     */
    protected static AlgorithmOntology addAlgorithmOntology(AlgorithmOntology ontology) throws DuplicateKeyException, DbException {
        DbPipeline<QueryFood, HyperResult> addAlgorithmOntologyPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.ADD_ALGORITHM_ONTOLOGY);
        if (ontology == null) {
            throw new NullPointerException("The algorithm ontology you provided in the db writer is null");
        }
        if (ontology.getName() == null) {
            throw new DbException(Cause.XDB1002, "You have to specify the name of an algorithm ontology to add it in the database");
        }
        if (ontology.getName() == null) {
            throw new DbException(Cause.XDB1003, "You have to specify the URI of an algorithm ontology to add it in the database");
        }
        if (ontology.getName().length() > 40) {
            throw new DbException(Cause.XDB1004, "The name of the ontology you tried to add in the "
                    + "database is too long : " + ontology.getName().length());
        }
        if (ontology.getUri().length() > 200) {
            throw new DbException(Cause.XDB1004, "The URI of the ontology you try to add in the database is too long: "
                    + ontology.getUri().length());
        }
        QueryFood food = new QueryFood(
                new String[][]{
                    {AlgOntTable.NAME.getColumnName(), ontology.getName()},
                    {AlgOntTable.URI.getColumnName(), ontology.getUri()}
                });
        try {
            addAlgorithmOntologyPipeline.process(food);
            YaqpLogger.LOG.log(new Trace(WriterHandler.class, "Added new algorithm ontology '" + ontology.getName()
                    + "' with uri :" + ontology.getUri()));
        } catch (final DbException ex) {
            //TODO: verify that the ontology is indeed added!
            if (ex.getCode() == Cause.XDB800) {
                throw new DuplicateKeyException(Cause.XDB4301, "Algorithm Ontology already added in the database", ex);
            } else {
                throw new DbException(Cause.XDH102, "Error while adding the ontology with name = " + ontology.getName(), ex);
            }
        }
        return ontology;
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
     * @see ReaderHandler#searchUsers(org.opentox.ontology.components.User) search for user
     * @see EmailSupervisor
     */
    protected static User addUser(User user) throws BadEmailException, DbException {
        // CHECK IF THE MANDATORY PARAMETERS ARE SPECIFIED:
        if (user == null) {
            throw new NullPointerException("Cannot add a null user in the database");
        }
        if (user.getUserName() == null) {
            throw new DbException(Cause.XDB5870, "Username has to be specified");
        }
        if (user.getEmail() == null) {
            throw new DbException(Cause.XDB5871, "You have to specify the user's email");
        }
        if (user.getUserPass() == null) {
            throw new DbException(Cause.XDB5872, "You must specify a password");
        }
        if (user.getFirstName() == null) {
            throw new DbException(Cause.XDB5873, "You must specify your first name");
        }
        if (user.getUserGroup() == null) {
            throw new DbException(Cause.XDB5874, "Usergroup has to be specified");
        }
        if (user.getUserGroup().getName() == null) {
            throw new DbException(Cause.XDB5875, "You have to specify a user group with a group name");
        }
        if (!EmailSupervisor.checkMail(user.getEmail())) {
            throw new BadEmailException(Cause.XDH103, "Bad user email");
        }

        DbPipeline<QueryFood, HyperResult> addUserPipeline =
                new DbPipeline<QueryFood, HyperResult>(PrepStmt.ADD_USER);

        QueryFood food = new QueryFood(
                new String[][]{
                    {UsersTable.USERNAME.getColumnName(), user.getUserName()},
                    {UsersTable.PASSWORD.getColumnName(), user.getUserPass()},
                    {UsersTable.FIRSTNAME.getColumnName(), user.getFirstName()},
                    {UsersTable.LASTNAME.getColumnName(), user.getLastName()},
                    {UsersTable.EMAIL.getColumnName(), user.getEmail()},
                    {UsersTable.ORGANIZATION.getColumnName(), user.getOrganization() != null ? user.getOrganization() : "N/A"},
                    {UsersTable.COUNTRY.getColumnName(), user.getCountry() != null ? user.getCountry() : "N/A"},
                    {UsersTable.CITY.getColumnName(), user.getCity() != null ? user.getCity() : "N/A"},
                    {UsersTable.ADDRESS.getColumnName(), user.getAddress() != null ? user.getAddress() : "N/A"},
                    {UsersTable.WEBPAGE.getColumnName(), user.getWebpage() != null ? user.getWebpage() : "N/A"},
                    {UsersTable.ROLE.getColumnName(), user.getUserGroup().getName()}
                });
        try {
            addUserPipeline.process(food);
            YaqpLogger.LOG.log(new Trace(WriterHandler.class, "User added: \n" + user));
        } catch (final DbException ex) {
            if (ex.getCode() == Cause.XDB800) {
                throw new DuplicateKeyException(Cause.XDB4300, "Username and/or email already registered");
            }
        }
        return user;
    }

    /**
     * Add a new feature in the database. The URI of the feature has to be specified.
     * @param feature a feature to be added in the database.
     * @throws DbException In case the feature cannot be added in the database. This
     * is the case when the feature already exists ({@link DuplicateKeyException }), when
     * the URI of the feature is not set or if you try to add a <code>null</code>
     * feature.
     */
    protected static Feature addFeature(Feature feature) throws DbException {
        if (feature == null) {
            throw new NullPointerException("The feature you provided is null");
        }
        if (feature.getURI() == null) {
            throw new DbException(Cause.XDB3236, "Cannot register a feature with no URI provided");
        }
        try {
            new URI(feature.getURI());
        } catch (URISyntaxException ex) {
            throw new DbException(Cause.XDB3237, "Invalid URI for feature :" + feature.getURI());
        }
        DbPipeline<QueryFood, HyperResult> addFeaturePipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.ADD_FEATURE);
        QueryFood food = new QueryFood(
                new String[][]{
                    {FeaturesTable.URI.getColumnName(), feature.getURI()}
                });
        HyperResult result = null;
        try {
            result = addFeaturePipeline.process(food);
            YaqpLogger.LOG.log(new Trace(WriterHandler.class, "Feature added: \n" + feature));
            if (result.getSize() == 1) {
                Iterator<String> it = result.getColumnIterator(1);
                int r = Integer.parseInt(it.next());
                feature.setId(r);
            }
        } catch (DbException ex) {
            if (ex.getCode() == Cause.XDB800) {
                try {
                    feature.setId(ReaderHandler.searchFeature(new Feature(feature.getURI()) , 0 , 0).get(0).getID());
                } catch (DbException ex1) {// in this case feature failed to be added but is not in the DB either!
                    throw new DbException(Cause.XDB3238, "Error {"+ex1+"}", ex1);
                }
            }
        }
        return feature;
    }

    /**
     * Add an algorithm in the database. You only have to provide the Name of the algorithm
     * in the algorithm object you provide and the algorithm types of the provided entity.
     * @param algorithm the algorithm to be added in the database. The algorithm is registered
     * in the database along with a set of algorithm-ontology-relation entries which facilitate
     * search for algorithms.
     * @throws DuplicateKeyException In case the algorithm already exists.
     * @throws DbException In case the algorithm cannot be added. Make sure that the corresponding
     * algorithm ontologies are already added in the database.
     */
    protected static Algorithm addAlgorithm(Algorithm algorithm) throws DbException {
        if (algorithm == null) {
            throw new NullPointerException("Cannot add a null algorithm in the database");
        }
        if (algorithm.getMeta() == null) {
            throw new NullPointerException("Cannot add an algorithm with unknown metadata");
        }
        if (algorithm.getMeta().getName() == null) {
            throw new DbException(Cause.XDB3235, "Specify the name of the algorithm");
        }
        if (algorithm.getMeta().getAlgorithmType() == null) {
            throw new DbException(Cause.XDB3235, "Specify the type of the algorithm");
        }
        DbPipeline<QueryFood, HyperResult> addAlgorithmPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.ADD_ALGORITHM);
        DbPipeline<QueryFood, HyperResult> addAlgOntRelationPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.ADD_ALGORITHM_ONTOL_RELATION);
        BatchProcessor bp = new BatchProcessor((JProcessor)addAlgOntRelationPipeline);
        bp.setfailSensitive(false);

        QueryFood algfood = new QueryFood(
                new String[][]{
                    {"NAME", algorithm.getMeta().getName()}
                });

        // THE RELATIONS OF THE ALGORITHM:
        ArrayList<QueryFood> relfood = new ArrayList<QueryFood>();
        // THE 'SMALLEST' ONTOLOGY FOR THE ALGORITHM:
        AlgorithmOntology ontology = new AlgorithmOntology(algorithm.getMeta().getAlgorithmType());
        QueryFood food = new QueryFood(
                new String[][]{
                    {"ALGORITHM", algorithm.getMeta().getName()},
                    {"ONTOLOGY", ontology.getName()}
                });
        relfood.add(food);
        // ALL OTHER SUPER-ONTOLOGIES:
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
        // ADD THE ALGORITH IN THE DATABASE...
        try {
            addAlgorithmPipeline.process(algfood);
            YaqpLogger.LOG.log(new Trace(WriterHandler.class, "Algorithm added: \n" + algorithm));
        } catch (DbException ex) {
            if (ex.getCode() == Cause.XDB800) {
                String message = "Cannot add algorithm because it already exists.";
                throw new DuplicateKeyException(Cause.XDH105, message, ex);
            }
            YaqpLogger.LOG.log(new Trace(WriterHandler.class, "Could not add the following algorithm :\n" + algorithm));
        }
        // ADD ALL ALGORITHM ONTOLOGIES FOR THIS ALGORITHM.
        try {
            bp.process(relfood);
            YaqpLogger.LOG.log(new Trace(WriterHandler.class, "Algorithm-Ontology relations batch added"));
        } catch (YaqpException ex) {
            YaqpLogger.LOG.log(new Trace(WriterHandler.class, "Could not batch add Algorithm-Ontology relations :\n" + algorithm));
        }
        return algorithm;
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

        checkQSARModelIntegrity(model);

        DbPipeline<QueryFood, HyperResult> addQSARModelPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.ADD_QSAR_MODEL);
        DbPipeline<QueryFood, HyperResult> addIndepFeaturePipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.ADD_INDEP_FEATURE_RELATION);

        model.setDependentFeature(addFeature(model.getDependentFeature()));
        model.setPredictionFeature(addFeature(model.getPredictionFeature()));
        ArrayList<Feature> listIndependent = new ArrayList<Feature>(model.getIndependentFeatures().size());
        for (Feature indep : model.getIndependentFeatures()) {
            listIndependent.add(addFeature(indep));
        }
        model.setIndependentFeatures(listIndependent);

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
            if (ex.getCode() == Cause.XDB800) throw new DbException(Cause.XDB7701, "Most likely" +
                    " this exception was caused by violation of a foreign key like the user's email. " +
                    "Check the existence of a user with e-mail addess :"+model.getUser().getEmail(), ex);
            throw new DbException(Cause.XDH107, "Foreign Key or Unique Entry Violation",ex);
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
            throw new DbException();
        }
        return r;
    }

    private static void checkQSARModelIntegrity(QSARModel model) throws DbException {
        if (model == null) {
            throw new NullPointerException("Cannot add null QSAR model");
        }
        if (model.getCode() == null) {
            throw new DbException(Cause.XDB5001, "Cannot add a model with unspecified code");
        }
        if (model.getDependentFeature() == null) {
            throw new DbException(Cause.XDB5002, "Cannot add a model with no dependent feature");
        }
        if (model.getPredictionFeature() == null) {
            throw new DbException(Cause.XDB5003, "Cannot add a model with no prediction feature");
        }
        if (model.getDataset() == null) {
            throw new DbException(Cause.XDB5004, "You did not specify a training dataset for the QSAΡ model");
        }
        if (model.getUser() == null) {
            throw new DbException(Cause.XDB5005, "You must specify a user/creator for the QSAR model");
        }
        if (model.getIndependentFeatures() == null || model.getIndependentFeatures().size() == 0) {
            throw new DbException(Cause.XDB5006, "You did not specify and independent features");
        }
        if (model.getAlgorithm() == null) {
            throw new DbException(Cause.XDB5007, "You must specify an algorithm for the model you try to add");
        }
        if (model.getParams() == null) {
            throw new DbException(Cause.XDB5008, "Algorithm parameters cannot be null");
        }
        try{
            new URI(model.getDataset());
        }catch (URISyntaxException ex){
            throw new DbException(Cause.XDB18, "Invalid URI for dataset : "+model.getDataset(), ex);
        }

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
    protected static QSARModel addPredictiveModel(QSARModel model) throws DbException {
        checkQSARModelIntegrity(model);
        String algorithmName = model.getAlgorithm().getMeta().getName();
        if (algorithmName.equals(YaqpAlgorithms.SVM.getMeta().getName())
                || algorithmName.equals(YaqpAlgorithms.SVC.getMeta().getName())) {
            return addSuppVectModel(model);
        } else if (algorithmName.equals(YaqpAlgorithms.MLR.getMeta().getName())) {
            int new_id = addQSARModel(model);
            model.setId(new_id);
            return model;
        }
        return null;
    }

    private static QSARModel addSuppVectModel(QSARModel model) throws DbException {
        // CHECK THE INTEGRITY OF THE GIVEN MODEL...
        checkQSARModelIntegrity(model);
        // CHECK IF ALL NECESSARY PARAMETERS ARE PROVIDED. IF SOME ARE NOT,
        // USE THE DEFAULT VALUES PROVIDED IN ConstantParameters.SVMParams().
        Map<String, AlgorithmParameter> params = model.getParams();
        if (!params.containsKey(ConstantParameters.gamma)) params.put(ConstantParameters.gamma, ConstantParameters.SVMParams().get(ConstantParameters.gamma));
        if (!params.containsKey(ConstantParameters.cost)) params.put(ConstantParameters.cost, ConstantParameters.SVMParams().get(ConstantParameters.cost));
        if (!params.containsKey(ConstantParameters.coeff0)) params.put(ConstantParameters.coeff0, ConstantParameters.SVMParams().get(ConstantParameters.coeff0));
        if (!params.containsKey(ConstantParameters.degree)) params.put(ConstantParameters.degree, ConstantParameters.SVMParams().get(ConstantParameters.degree));
        if (!params.containsKey(ConstantParameters.epsilon)) params.put(ConstantParameters.epsilon, ConstantParameters.SVMParams().get(ConstantParameters.epsilon));
        if (!params.containsKey(ConstantParameters.kernel)) params.put(ConstantParameters.kernel, ConstantParameters.SVMParams().get(ConstantParameters.kernel));
        if (!params.containsKey(ConstantParameters.cacheSize)) params.put(ConstantParameters.cacheSize, ConstantParameters.SVMParams().get(ConstantParameters.cacheSize));
        if (!params.containsKey(ConstantParameters.tolerance)) params.put(ConstantParameters.tolerance, ConstantParameters.SVMParams().get(ConstantParameters.tolerance));
        model.setParams(params);
        int r = 0; /* The id of the model */
        try {
            r = WriterHandler.addQSARModel((QSARModel) model);
        } catch (DbException ex) {
            // Could not add the model to the parent table
            // Reproduce the exception!
            throw new DbException(Cause.XDH108, "Support Vector Could not be added in the database as QSAR Model", ex);
        }
        DbPipeline<QueryFood, HyperResult> addSupportVectorPipeline =
                new DbPipeline<QueryFood, HyperResult>(PrepStmt.ADD_SUPPORT_VECTOR);
        QueryFood food = new QueryFood();
        food.add(SupportVecTable.UID.getColumnName(), Integer.toString(r));
        for (Entry e : model.getParams().entrySet()) {
            String pName = e.getKey().toString();
            AlgorithmParameter algParam = (AlgorithmParameter) e.getValue();
            food.add(pName.toUpperCase(), algParam.paramValue.toString());
        }

        try {
            addSupportVectorPipeline.process(food);
        } catch (DbException ex) {
            try {
                // First delete the model from the parent table!
                Statement delete = TheDbConnector.DB.getConnection().createStatement();
                delete.executeUpdate("DELETE FROM " + QSARModelsTable.TABLE.getTableName() + " WHERE UID =" + r);
            } catch (SQLException ex1) {
                throw new DbException(Cause.XDH109, "Could not delete the QSAR model with uid : " + r, ex1);
            }
            if (ex.getCode() == Cause.XDB15) throw new DbException(Cause.XDB7702, "You did not specify all svm parameters", ex);
            if (ex.getCode() == Cause.XDB800) throw new DbException(Cause.XDB7703, "The SVM parameters you provided do not " +
                    "comply with the requirements", ex);
        }
        model.setId(r);
        return model;
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
    protected static Task addTask(Task task) throws DbException {
        // SOME PRELIMINARY CONSISTENCY CHECKS:
        if (task == null) {
            throw new NullPointerException("Cannot add a null task in the database");
        }
        if (task.getDuration_sec() < 0) {
            throw new DbException(Cause.XDB4001, "You provided a negative duration for a task");
        }
        if (task.getName() == null) {
            throw new DbException(Cause.XDB4002, "A task has to have a not-null name.");
        }
        if (task.getAlgorithm() == null) {
            throw new DbException(Cause.XDB4003, "You did not provide an algorithm for the task you attempted to register");
        }
        if (task.getUser() == null) {
            throw new DbException(Cause.XDB4004, "You need to specify a user/creator or your task");
        }
        if (task.getUser().getEmail() == null) {
            throw new DbException(Cause.XDB4005, "You did not specify the email of the user that created the task");
        }


        DbPipeline<QueryFood, HyperResult> addTaskPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.ADD_TASK);
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
            if (ex.getCode() == Cause.XDB800) {
                throw new DbException(Cause.XDH110, "Cannot add task due to some constraint violation (user email or algorithm)");
            }
            throw new DbException(Cause.XDH2, "Unexpected condition while trying to add a task", ex);
        }
        return task;
    }

    /**
     * Add a new Omega model in the database.
     * @param model
     * @return
     * @throws DbException
     */
    protected static OmegaModel addOmega(OmegaModel model) throws DbException {
        // SOME PRELIMINARY CONSISTENCY CHECKS:
        if (model == null) {
            throw new NullPointerException("Cannot add a null Omega in the database");
        }
        if (model.getCode() == null) {
            throw new DbException(Cause.XDB9090, "You did not provided the code for the Omega Model you try to register");
        }
        if (model.getUser() == null) {
            throw new DbException(Cause.XDB9091, "You did not provide any user fot the Omega Model");
        }
        if (model.getUser().getEmail() == null) {
            throw new DbException(Cause.XDB9092, "The user/created you provided for the Omega Modle has undefined email address");
        }
        if (model.getDataset() == null) {
            throw new DbException(Cause.XDB9093, "You did not provide the dataset for the Omega Model");
        }
        try {
            new URI(model.getDataset());
        } catch (URISyntaxException ex) {
            throw new DbException(Cause.XDB9094, "Invalid dataset uri :" + model.getDataset());
        }

        int r = 0;
        DbPipeline<QueryFood, HyperResult> addOmegaPipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.ADD_OMEGA_MODEL);

        User u = model.getUser();
        String userEmail = u.getEmail();

        QueryFood food = new QueryFood();
        food.add("CODE", model.getCode());
        food.add("CREATED_BY", userEmail);
        food.add("DATASET_URI", model.getDataset());
        try {
            HyperResult result = addOmegaPipeline.process(food);
            if (result.getSize() == 1) {
                Iterator<String> it = result.getColumnIterator(1);
                r = Integer.parseInt(it.next());
            }// TODO: Otherwise what?
            model.setId(r);
            return model;
        } catch (DbException ex) {
            if (ex.getCode() == Cause.XDB800) throw new DbException(Cause.XDB9095, "Most likely the reason for this" +
                    " exception is that you tried to add an Omega Model which does not correspond to a user. Check the" +
                    "existence of user with email :"+model.getUser().getEmail());
            throw ex;
        }

    }
}
