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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.derby.client.am.DateTime;
import org.opentox.core.exceptions.Cause;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.processors.DbPipeline;
import org.opentox.db.queries.HyperResult;
import org.opentox.db.queries.QueryFood;
import org.opentox.db.table.collection.AlgOntTable;
import org.opentox.db.table.collection.QSARModelsTable;
import org.opentox.db.table.collection.UserAuthTable;
import org.opentox.db.table.collection.UsersTable;
import org.opentox.db.util.PrepStmt;
import org.opentox.ontology.components.*;
import org.opentox.ontology.exceptions.ImproperEntityException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class UpdateHandler {

    public static void update(YaqpComponent component) throws DbException, ImproperEntityException {
        if (component instanceof Task) {
            updateTask((Task) component);
        }else if(component instanceof User){
            updateUser((User)component);
        }else if(component instanceof UserGroup){
            updateUserGroup((UserGroup)component);
        }else{
            throw new UnsupportedOperationException("Update operation for component "+component.getClass().toString()+" not supported.");
        }
    }

    public static void update(ComponentList<YaqpComponent> list) throws DbException, ImproperEntityException {
        for(YaqpComponent component : list){
            update(component);
        }
    }

    public static void delete(YaqpComponent component) throws DbException, ImproperEntityException {
        if (component instanceof Task) {
            deleteTask((Task) component);
        }else if(component instanceof User){
            deleteUser((User)component);
        }else if(component instanceof UserGroup){
            deleteUserGroup((UserGroup)component);
        }else if(component instanceof QSARModel){
            deleteQSARModel((QSARModel)component);
        }else if(component instanceof OmegaModel){
            deleteOmegaModel((OmegaModel)component);
        }else if(component instanceof Feature){
            deleteFeature((Feature)component);
        }else{
            throw new UnsupportedOperationException("Delete operation for component "+component.getClass().toString()+" not supported.");
        }
    }

    public static void delete(ComponentList<YaqpComponent> list) throws DbException, ImproperEntityException {
        for(YaqpComponent component : list){
            delete(component);
        }
    }

    /**
     * ****************************************************************************
     * ----------------------------------------------------------------------------
     *                      UPDATE QUERIES
     * ----------------------------------------------------------------------------
     * ****************************************************************************
     */

    protected static void updateTask(Task prototype) throws DbException, ImproperEntityException {
        if(prototype == null){
            throw new NullPointerException("Task prototype provided is null");
        }else if(prototype.getName() == null){
            throw new NullPointerException("Task prototype provided has null name (primary key)");
        }
        prototype = (Task) fixNull(prototype);
        DbPipeline<QueryFood,HyperResult> pipeline =  new DbPipeline<QueryFood, HyperResult>(PrepStmt.UPDATE_TASK);
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        QueryFood food = new QueryFood(
                new String[][]{
                    {"NAME", prototype.getName()},
                    {"STATUS", prototype.getTaskStatus().toString()},
                    {"CREATED_BY", prototype.getUser().getEmail()},
                    {"ALGORITHM", prototype.getAlgorithm().getMeta().getName()},
                    {"HTTPSTATUS", Integer.toString(prototype.getHttpStatus())},
                    {"RESULT", prototype.getResult()},
                    {"DURATION", Integer.toString(prototype.getDuration())},
                    {"ENDSTAMP", ts.toString() }
        });
        try {
            pipeline.process(food);
        } catch (YaqpException ex) {
            String message = "Could not update Task in the database for given Prototype";
            throw new DbException(Cause.XDH1, message, ex);
        }

    }

    protected static void updateUser(User prototype) throws DbException, ImproperEntityException {
        if(prototype == null){
            throw new NullPointerException("User prototype provided is null");
        }else if(prototype.getEmail() == null){
            throw new NullPointerException("User prototype provided has null email (primary key)");
        }
        prototype = (User) fixNull(prototype);
        DbPipeline<QueryFood,HyperResult> pipeline =  new DbPipeline<QueryFood, HyperResult>(PrepStmt.UPDATE_USER);
        QueryFood food = new QueryFood(
                new String[][]{
                    {UsersTable.USERNAME.getColumnName(), prototype.getUserName()},
                    {UsersTable.PASSWORD.getColumnName(), prototype.getUserPass()},
                    {UsersTable.FIRSTNAME.getColumnName(), prototype.getFirstName()},
                    {UsersTable.LASTNAME.getColumnName(), prototype.getLastName()},
                    {UsersTable.EMAIL.getColumnName(), prototype.getEmail()},
                    {UsersTable.ORGANIZATION.getColumnName(), prototype.getOrganization()},
                    {UsersTable.COUNTRY.getColumnName(), prototype.getCountry()},
                    {UsersTable.CITY.getColumnName(), prototype.getCity()},
                    {UsersTable.ADDRESS.getColumnName(), prototype.getAddress()},
                    {UsersTable.WEBPAGE.getColumnName(), prototype.getWebpage()},
                    {UsersTable.ROLE.getColumnName(), prototype.getUserGroup().getName()}
                });
        try {
            pipeline.process(food);
        } catch (YaqpException ex) {
            String message = "Could not update User in the database for given Prototype";
            throw new DbException(Cause.XDH1, message, ex);
        }

    }

    protected static void updateUserGroup(UserGroup prototype) throws DbException, ImproperEntityException {
        if(prototype == null){
            throw new NullPointerException("UserGroup prototype provided is null");
        }else if(prototype.getName() == null){
            throw new NullPointerException("UserGroup prototype provided has null name (primary key)");
        }
        prototype = (UserGroup) fixNull(prototype);
        DbPipeline<QueryFood,HyperResult> pipeline =  new DbPipeline<QueryFood, HyperResult>(PrepStmt.UPDATE_USER_GROUP);
        QueryFood food = new QueryFood(
                new String[][]{
                        {UserAuthTable.NAME.getColumnName(), prototype.getName()},
                        {UserAuthTable.USER_LEVEL.getColumnName(), Integer.toString(prototype.getLevel())},
                        {UserAuthTable.MODEL_AUTH.getColumnName(), prototype.getModelAuth()},
                        {UserAuthTable.USER_AUTH.getColumnName(), prototype.getUserAuth()},
                        {UserAuthTable.ALGORITHM_AUTH.getColumnName(), prototype.getAlgorithmAuth()},
                        {UserAuthTable.USER_GROUP_AUTH.getColumnName(), prototype.getUserGroupAuth()},
                        {UserAuthTable.MAX_MODELS.getColumnName(), Integer.toString(prototype.getMaxModels())}
                    });
        try {
            pipeline.process(food);
        } catch (YaqpException ex) {
            String message = "Could not update UserGroup in the database for given Prototype";
            throw new DbException(Cause.XDH1, message, ex);
        }
    }


    /**
     * ****************************************************************************
     * ----------------------------------------------------------------------------
     *                      DELETE QUERIES
     * ----------------------------------------------------------------------------
     * ****************************************************************************
     */

    protected static void deleteTask(Task prototype) throws DbException, ImproperEntityException {
        if(prototype == null){
            throw new NullPointerException("Task prototype provided is null");
        }else if(prototype.getName() == null){
            throw new NullPointerException("Task prototype provided has null name (primary key)");
        }
        DbPipeline<QueryFood,HyperResult> pipeline =  new DbPipeline<QueryFood, HyperResult>(PrepStmt.DELETE_TASK);
        QueryFood food = new QueryFood(
                new String[][]{
                    {"NAME", prototype.getName()},
        });
        try {
            pipeline.process(food);
        } catch (YaqpException ex) {
            String message = "Could not delete Task in the database for given Prototype";
            throw new DbException(Cause.XDH1, message, ex);
        }

    }

    protected static void deleteUser(User prototype) throws DbException, ImproperEntityException {
        if(prototype == null){
            throw new NullPointerException("User prototype provided is null");
        }else if(prototype.getEmail() == null){
            throw new NullPointerException("User prototype provided has null email (primary key)");
        }
        DbPipeline<QueryFood,HyperResult> pipeline =  new DbPipeline<QueryFood, HyperResult>(PrepStmt.DELETE_USER);
        QueryFood food = new QueryFood(
                new String[][]{
                    {UsersTable.EMAIL.getColumnName(), prototype.getEmail()},
                });
        try {
            pipeline.process(food);
        } catch (YaqpException ex) {
            String message = "Could not delete User in the database for given Prototype";
            throw new DbException(Cause.XDH1, message, ex);
        }
    }

    protected static void deleteUserGroup(UserGroup prototype) throws DbException, ImproperEntityException {
        if(prototype == null){
            throw new NullPointerException("UserGroup prototype provided is null");
        }else if(prototype.getName() == null){
            throw new NullPointerException("UserGroup prototype provided has null name (primary key)");
        }
        DbPipeline<QueryFood,HyperResult> pipeline =  new DbPipeline<QueryFood, HyperResult>(PrepStmt.DELETE_USERGROUP);
        QueryFood food = new QueryFood(
                new String[][]{
                        {UserAuthTable.NAME.getColumnName(), prototype.getName()},
                    });
        try {
            pipeline.process(food);
        } catch (YaqpException ex) {
            String message = "Could not delete UserGroup in the database for given Prototype";
            throw new DbException(Cause.XDH1, message, ex);
        }
    }

    protected static void deleteQSARModel(QSARModel prototype) throws DbException, ImproperEntityException {
        if(prototype == null){
            throw new NullPointerException("QSARModel prototype provided is null");
        }else if(prototype.getId() == 0){
            throw new NullPointerException("QSARModel prototype provided has null id (primary key)");
        }
        DbPipeline<QueryFood,HyperResult> pipeline =  new DbPipeline<QueryFood, HyperResult>(PrepStmt.DELETE_QSARMODEL);
        QueryFood food = new QueryFood(
                new String[][]{
                        {QSARModelsTable.UID.getColumnName(), Integer.toString(prototype.getId())}
                    });
        try {
            pipeline.process(food);
        } catch (YaqpException ex) {
            String message = "Could not delete QSARModel in the database for given Prototype";
            throw new DbException(Cause.XDH1, message, ex);
        }
    }

    protected static void deleteOmegaModel(OmegaModel prototype) throws DbException, ImproperEntityException {
        if(prototype == null){
            throw new NullPointerException("OmegaModel prototype provided is null");
        }else if(prototype.getId() == 0){
            throw new NullPointerException("OmegaModel prototype provided has null id (primary key)");
        }
        DbPipeline<QueryFood,HyperResult> pipeline =  new DbPipeline<QueryFood, HyperResult>(PrepStmt.DELETE_OMEGAMODEL);
        QueryFood food = new QueryFood(
                new String[][]{
                        {QSARModelsTable.UID.getColumnName(), Integer.toString(prototype.getId())}
                    });
        try {
            pipeline.process(food);
        } catch (YaqpException ex) {
            String message = "Could not delete OmegaModel in the database for given Prototype";
            throw new DbException(Cause.XDH1, message, ex);
        }
    }

    protected static void deleteFeature(Feature prototype) throws DbException, ImproperEntityException {
        if(prototype == null){
            throw new NullPointerException("Feature prototype provided is null");
        }else if(prototype.getID() == 0){
            throw new NullPointerException("Feature prototype provided has null id (primary key)");
        }
        DbPipeline<QueryFood,HyperResult> pipeline =  new DbPipeline<QueryFood, HyperResult>(PrepStmt.DELETE_FEATURE);
        QueryFood food = new QueryFood(
                new String[][]{
                        {QSARModelsTable.UID.getColumnName(), Integer.toString(prototype.getID())}
                    });
        try {
            pipeline.process(food);
        } catch (YaqpException ex) {
            String message = "Could not delete Feature in the database for given Prototype";
            throw new DbException(Cause.XDH1, message, ex);
        }
    }


    /**
     * Auxiliary Method
     * Fixes the null or empty variables of a given prototype
     * with the correct values from the database for the prototype
     * that has the same primary key with the given.
     * @param prototype A valid YaqpComponent prototype.
     * @return a YaqpComponent with no null or empty values.
     * @throws DbException
     * @throws ImproperEntityException
     */
    @SuppressWarnings({"unchecked"})
    private static YaqpComponent fixNull(YaqpComponent prototype) throws DbException, ImproperEntityException {
        try {
            YaqpComponent old = ReaderHandler.search(prototype.getSkroutz(), null, false).get(0);
            Field[] fields;
            Class c = prototype.getClass();
            Constructor con = c.getConstructor();
            Object obj = con.newInstance();
            fields = c.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                if (fields[i].get(prototype) == null || fields[i].get(prototype).equals(fields[i].get(obj))) {
                    fields[i].set(prototype, fields[i].get(old));
                }
            }
            return prototype;
        } catch (InstantiationException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        } catch (SecurityException ex) {
            throw new RuntimeException(ex);
        }
    }
}
