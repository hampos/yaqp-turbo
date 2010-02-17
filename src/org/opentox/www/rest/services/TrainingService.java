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
package org.opentox.www.rest.services;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.concurrent.Callable;
import org.opentox.core.exceptions.Cause;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.processors.Pipeline;
import org.opentox.io.processors.InputProcessor;
import org.opentox.ontology.components.QSARModel;
import org.opentox.ontology.components.User;
import org.opentox.ontology.data.DatasetBuilder;
import org.opentox.ontology.processors.InstancesProcessor;
import org.opentox.ontology.util.vocabulary.ConstantParameters;
import org.opentox.qsar.exceptions.QSARException;
import org.opentox.qsar.interfaces.JTrainer;
import org.opentox.qsar.processors.trainers.WekaTrainer;
import org.opentox.www.rest.components.YaqpForm;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class TrainingService implements Callable<Representation> {

    private YaqpForm form;
    private User user;
    private MediaType outputMedia;
    private Class<? extends JTrainer> trainer;
    private WekaTrainer wt;

    public TrainingService() {
    }


    /**
     *
     * @param form
     *          The parameters POSTed by the client to the resource including all
     *          neccessary information for the initialization of the training pipeline.
     * @param user
     *          The {@link User user} that invoked the service
     * @param trainer
     *          A Training class (an implementation of {@link JTrainer } ).
     * @param outputMedia
     *          The mediatype for the output representation. If set to <code>null</code>
     *          the default value {@link MediaType#TEXT_URI_LIST } will be used.
     * @throws NullPointerException
     *          If the trainer, the form or the user is <code>null</code>.
     */
    public TrainingService(YaqpForm form, User user, Class<? extends JTrainer> trainer, MediaType outputMedia)  {
        if (form == null ) throw new NullPointerException("Form must be null");
        if (user == null) throw new NullPointerException("User must not be null");
        if (trainer == null) throw new NullPointerException("The trainer must not be null");
        if (outputMedia == null) outputMedia = MediaType.TEXT_PLAIN;
        this.form = form;
        this.user = user;
        this.outputMedia = outputMedia;
        this.trainer = trainer;

    }

    /**
     * Perform the training
     * @return
     *      A representation which is returned to the client along with a response status
     * @throws Exception
     *      In case something goes wrong with the training. In general, all exceptions are
     *      expected to be instances of {@link YaqpException } accompanied by an {@link
     *      Cause identification code} and an explanatory message.
     * @throws InvocationTargetException
     *      This is considered to be a programmatic error or bug of the code and should
     *      not be returned.
     */
    public Representation call() throws Exception {
        InputProcessor p1 = new InputProcessor();
        DatasetBuilder p2 = new DatasetBuilder();
        InstancesProcessor p3 = new InstancesProcessor();

        Pipeline trainingPipe = new Pipeline();
        trainingPipe.add(p1);
        trainingPipe.add(p2);
        trainingPipe.add(p3);
        
        if (WekaTrainer.class.isAssignableFrom(trainer) || JTrainer.class.isAssignableFrom(trainer)){
            final Constructor<? extends JTrainer> constructor = trainer.getConstructor(YaqpForm.class);
            try{
            wt = (WekaTrainer) constructor.newInstance(form);
            }catch (final InvocationTargetException ex){
                if (ex.getCause() instanceof YaqpException){
                    YaqpException cause = (YaqpException) ex.getCause();
                    throw cause;
                }
                throw ex;
                
            }
            trainingPipe.add(wt);
        }else {
            throw new IllegalArgumentException("The class you provided is not a valid training class");
        }

        final QSARModel model = (QSARModel) trainingPipe.process(new URI(form.getFirstValue(ConstantParameters.dataset_uri)));

        return new StringRepresentation(model.getCode() + "\n");


    }


}
