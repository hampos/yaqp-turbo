/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.opentox.db.handlers;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.util.TheDbConnector;
import org.opentox.ontology.components.QSARModel;
import org.opentox.ontology.components.Task;
import org.opentox.ontology.components.User;
import org.opentox.ontology.components.UserGroup;
import org.opentox.ontology.exceptions.ImproperEntityException;
import org.opentox.ontology.util.YaqpAlgorithms;

/**
 *
 * @author hampos
 */
public class UpdateHandlerTest {

    public UpdateHandlerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        TheDbConnector.init();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of update method, of class UpdateHandler.
     */
    @Test
    public void testUpdate_YaqpComponent() throws Exception {
    }

    /**
     * Test of update method, of class UpdateHandler.
     */
    @Test
    public void testUpdate_ComponentList() {
    }

    /**
     * Test of updateTask method, of class UpdateHandler.
     */
    //@Test
    public void testUpdateTask() throws DbException, ImproperEntityException  {
        Task oldtask = (Task) ReaderHandler.search(new Task(), null, false).get(0);
        Task task = oldtask.getSkroutz();
        task.setAlgorithm(YaqpAlgorithms.SVM);
        task.setTaskStatus(Task.STATUS.COMPLETED);
        task.setResult("HAHAHAHAHA");
        UpdateHandler.updateTask(task);
        Task updatedTask = (Task) ReaderHandler.search(new Task(), null, false).get(0);
        System.out.println(updatedTask);
    }

    //@Test
    public void testUpdateUser() throws DbException, ImproperEntityException {
        User olduser = (User) ReaderHandler.search(new User("john@foo.goo.gr"), null, false).get(0);
        User user = olduser.getSkroutz();
        System.out.println(olduser);
        user.setCity("Atlanta");
        user.setCountry("USA");
        user.setOrganization("NASA");
        UpdateHandler.update(user);
        User updatedUser = (User)ReaderHandler.search(new User("john@foo.goo.gr"), null, false).get(0);
        System.out.println(updatedUser);
    }

    //@Test
    public void testUpdateUserGroup() throws DbException, ImproperEntityException {
        UserGroup oldgroup = (UserGroup) ReaderHandler.search(new UserGroup("ADMINISTRATOR"), null, false).get(0);
        UserGroup group = oldgroup.getSkroutz();
        System.out.println(oldgroup);
        group.setAlgorithmAuth("VVV");
        group.setModels(30);
        group.setUserAuth("VVE");
        UpdateHandler.update(group);
        UserGroup updatedgroup = (UserGroup)ReaderHandler.search(new UserGroup("ADMINISTRATOR"), null, false).get(0);
        System.out.println(updatedgroup);
        System.out.println(updatedgroup.getAlgorithmAuth());
        System.out.println(updatedgroup.getModels());
        System.out.println(updatedgroup.getUserAuth());
    }

    @Test
    public void deleteUser() throws Exception{
        UpdateHandler.delete(new User("mike@foo.goo.gr"));
        QSARModel m = new QSARModel();
        m.setId(31);
        UpdateHandler.delete(m);

    }

}