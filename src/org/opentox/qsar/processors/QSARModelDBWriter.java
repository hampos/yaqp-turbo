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


package org.opentox.qsar.processors;

import org.opentox.core.exceptions.Cause;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.processors.Processor;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.handlers.ReaderHandler;
import org.opentox.db.handlers.WriterHandler;
import org.opentox.db.util.Page;
import org.opentox.ontology.components.QSARModel;
import org.opentox.ontology.components.User;
import org.opentox.ontology.components.YaqpComponent;
import org.opentox.ontology.exceptions.ImproperEntityException;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Warning;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class QSARModelDBWriter extends Processor<QSARModel, QSARModel>{

    /**
     * The user that created the model calling the training service.
     */
    private User user ;

    public QSARModelDBWriter() {
        try {
            user = (User) ReaderHandler.search(new User(), new Page(1, 0), false).get(0);
        } catch (DbException ex) {
            YaqpLogger.LOG.log(new Warning(getClass(), "No users found in the database!!!"));
        } catch (ImproperEntityException ex){

        }
    }



    /**
     * Construct a new {@link QSARModelDBWriter model writer} providing the
     * user that called the service
     * @param user
     *      The user who called the training service
     */
    public QSARModelDBWriter(User user){
        this.user = user;
    }

    /**
     *
     * @param model
     *          The model produced by the trainer as a {@link YaqpComponent component}.
     * @return
     *          The component corresponding to the generated model including the
     *          user who created it as well as the ID of the model in the database.
     *          The provided model is registered in the models' table.
     * @throws YaqpException
     *          In case the model cannot be registered in the database.
     */
    public QSARModel process(QSARModel model) throws YaqpException {
        model.setUser(user);
        QSARModel writtenModel = (QSARModel) WriterHandler.add(model);
        if (writtenModel.getId()==0) throw new YaqpException(Cause.XDB10101,
                "Failed to write the generated model in the database");
        return writtenModel;
    }


}