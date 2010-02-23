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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Callable;
import org.opentox.core.exceptions.Cause;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.processors.Pipeline;
import org.opentox.db.handlers.ReaderHandler;
import org.opentox.db.util.Page;
import org.opentox.io.processors.InputProcessor;
import org.opentox.io.processors.Poster;
import org.opentox.io.util.ServerList;
import org.opentox.ontology.components.QSARModel;
import org.opentox.ontology.components.User;
import org.opentox.ontology.data.Dataset;
import org.opentox.ontology.data.DatasetBuilder;
import org.opentox.ontology.processors.InstancesProcessor;
import org.opentox.ontology.util.vocabulary.ConstantParameters;
import org.opentox.qsar.exceptions.QSARException;
import org.opentox.qsar.processors.predictors.SimplePredictor;
import org.opentox.qsar.processors.predictors.WekaPredictor;
import org.opentox.www.rest.components.YaqpForm;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class PredictionService implements Callable<Representation> {

    /**
     *
     * The ID of the model.
     */
    private String id;
    private User user;
    private Class<? extends WekaPredictor> predictor;
    private WekaPredictor pd;
    private MediaType media;
    private YaqpForm form;

    public PredictionService(
            final YaqpForm form,
            final String id,
            final User user,
            final Class<? extends WekaPredictor> predictor,
            MediaType media ) throws QSARException {
        if (id == null) {
            throw new NullPointerException("ID must be not null");
        }
        if (user == null) {
            throw new NullPointerException("User must not be null");
        }
        if (predictor == null) {
            throw new NullPointerException("The trainer must not be null");
        }
        if (form.getFirstValue(ConstantParameters.dataset_uri) == null) {
            throw new QSARException(
                    Cause.XQPred634, "The parameter " + ConstantParameters.dataset_uri + " was not specified");
        }
        if (media == null) {
            media = MediaType.TEXT_PLAIN;
        }
        this.id = id;
        this.user = user;
        this.predictor = predictor;
        this.media = media;
        this.form = form;
    }

    @SuppressWarnings({"unchecked"})
    public Representation call() throws QSARException, YaqpException {
        InputProcessor p1 = new InputProcessor();            // URI      -->  OntObject
        DatasetBuilder p2 = new DatasetBuilder();           // OntObject -->  Dataset
        InstancesProcessor p3 = new InstancesProcessor();   // Dataset   -->  Instances

        Pipeline trainingPipe = new Pipeline(p1, p2, p3);

        QSARModel prototype = new QSARModel();
        prototype.setId(Integer.parseInt(id));
        QSARModel model;

        final String dset = form.getFirstValue(ConstantParameters.dataset_uri);
        try {
            model = (QSARModel) (ReaderHandler.search(prototype, new Page(), false).getFirst());
            trainingPipe.add(new SimplePredictor(model));
            trainingPipe.add(new DatasetBuilder());

            Dataset ds = (Dataset) trainingPipe.process(new URI(dset));
            Poster poster = new Poster(ServerList.ambit + "/dataset");
            Response response = poster.handle(ds.getRDF());
            String reference = response.getLocationRef().toString();
            response.release();
            return new StringRepresentation(reference + "\n", MediaType.TEXT_URI_LIST);
        } catch (URISyntaxException ex) {
            throw new QSARException(Cause.XQPred635, "The dataset uri you provided is not a valid uri :{" + dset, ex);
        } catch (YaqpException ex) {
            throw ex;
        }

    }
}

