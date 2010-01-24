package org.opentox.db.handlers;

import java.util.concurrent.TimeUnit;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.db.entities.AlgorithmOntology;
import org.opentox.db.entities.User;
import org.opentox.db.entities.UserGroup;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.exceptions.DuplicateKeyException;
import org.opentox.db.processors.DbPipeline;
import org.opentox.db.queries.HyperResult;
import org.opentox.db.queries.QueryFood;
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

    /**
     * Add a new UserGroup in the database
     * @param userGroup a user group
     */
    public static void addUserGroup(UserGroup userGroup) {
        DbPipeline<QueryFood, HyperResult> pipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.ADD_USER_GROUP);

        QueryFood food = new QueryFood(
                new String[][]{
                    {"NAME", userGroup.getName()},
                    {"USER_LEVEL", Integer.toString(userGroup.getLevel())}
                });
        try {
            pipeline.process(food);
            YaqpLogger.LOG.log(new Trace(WriterHandler.class, "Added new user group '" + userGroup.getName()
                    + "' with authorization level :" + userGroup.getLevel()));
        } catch (YaqpException ex) {
            YaqpLogger.LOG.log(new Debug(WriterHandler.class, "Could not add the following UserGroup : \n" + userGroup));
        }
    }

    /**
     * Add a new algorithm ontology in the database.
     * @param ontology algorithm ontology to be added in the database.
     * @throws DuplicateKeyException If the algorithm ontology is already registered
     * in the database.
     */
    public static void addAlgorithmOntology(AlgorithmOntology ontology) throws DuplicateKeyException {
        DbPipeline<QueryFood, HyperResult> pipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.ADD_ALGORITHM_ONTOLOGY);

        QueryFood food = new QueryFood(
                new String[][]{
                    {"NAME", ontology.getName()},
                    {"URI", ontology.getUri()}
                });
        try {
            pipeline.process(food);
            YaqpLogger.LOG.log(new Trace(WriterHandler.class, "Added new algorithm ontology '" + ontology.getName()
                    + "' with uri :" + ontology.getUri()));
        } catch (DbException ex) {
            if (ex.toString().contains("DuplicateKeyException")) {
                String message = "Algorithm Ontology Already Registered in the Database!";
                YaqpLogger.LOG.log(new Debug(WriterHandler.class, message));
                throw new DuplicateKeyException(message, ex);
            }
            YaqpLogger.LOG.log(new Debug(WriterHandler.class, "Could not add the following Algorithm Ontology : \n" + ontology));
        }
    }

    /**
     * Add a new user into the database.
     *
     * @param user the user to be added.
     * @throws DuplicateKeyException If the user is already registered or the mail
     * or/and username are used by some other user.
     */
    public static void addUser(User user) throws DuplicateKeyException {
        DbPipeline<QueryFood, HyperResult> pipeline = new DbPipeline<QueryFood, HyperResult>(PrepStmt.ADD_USER);
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
                    {"ROLE", "1"}// TODO: Handle user roles......
                });
        try {
            pipeline.process(food);
            YaqpLogger.LOG.log(new Trace(WriterHandler.class, "User added: \n"+user )  );
        } catch (DbException ex) {
            if (ex.toString().contains("DuplicateKeyException")) {
                String message = "Cannot add user because email or username are already used by some other user.";
                YaqpLogger.LOG.log(new Debug(WriterHandler.class, message));
                throw new DuplicateKeyException(message, ex);
            }
            YaqpLogger.LOG.log(new Debug(WriterHandler.class, "Could not add the following user :\n" + user));
        }
    }
}
