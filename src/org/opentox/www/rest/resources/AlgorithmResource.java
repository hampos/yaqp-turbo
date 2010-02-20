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
package org.opentox.www.rest.resources;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.config.Configuration;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.processors.Pipeline;
import org.opentox.io.processors.OutputProcessor;
import org.opentox.io.processors.Publisher;
import org.opentox.ontology.components.Algorithm;
import org.opentox.ontology.components.QSARModel;
import org.opentox.ontology.components.User;
import org.opentox.ontology.util.YaqpAlgorithms;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Fatal;
import org.opentox.www.rest.components.URITemplate;
import org.opentox.www.rest.components.YaqpForm;
import org.opentox.www.rest.components.YaqpResource;
import org.opentox.www.rest.services.Trainers;
import org.opentox.www.rest.services.TrainingService;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

/**
 *
 * The algorithm resource is the interface to all algorithm - related functionallities
 * such as model training, data cleanup services and other. The methods GET and POST are
 * implemented. Use GET to acquire a representation of the underlying algorithm in a supported
 * mediatype and POST to run the algorithm. The algorithm resource provides access to all
 * metadata of the algorithm (title, description, parameters etc).
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class AlgorithmResource extends YaqpResource {

    /** URI Template */
    public static final URITemplate template = new URITemplate("algorithm", "algorithm_id", null);

    /**
     * Trainer class used to perform the training
     */
    Class trainer;

    /**
     * Name of the algorithm.
     */
    String algorithmName;

    /**
     * Initialized the resource
     * @throws ResourceException
     *      In case the resource cannot be initialized.
     */
    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        initialize(
                MediaType.APPLICATION_RDF_XML,
                MediaType.APPLICATION_RDF_TURTLE,
                MediaType.APPLICATION_PDF,
                MediaType.TEXT_URI_LIST
                );

        algorithmName = Reference.decode(getRequest().getAttributes().get(template.getPrimaryKey()).toString());
        try{
            trainer = Trainers.valueOf(algorithmName).getTrainer();
        } catch (IllegalArgumentException ex){
            trainer = null;
        }
        
    }


    /**
     * The representation of a certain algorithm in some of the available mimes.
     * @param variant
     *      The requested variant for the representation of the algorithm.
     * @return
     *      Represenation of an algorithm in a supported mime type. If the requested
     *      MIME is not available, the default (application/rdf+xml) is returned instead.
     * @throws ResourceException
     *      In case the representation cannot be generated and returned to the client.
     */
    @Override
    protected Representation get(final Variant variant) throws ResourceException {        
        final Algorithm a = YaqpAlgorithms.getByName(algorithmName);
        // IF THE REQUESTED ALGORITHM WAS NOT FOUND, RETURN AN EXPLANATORY MESSAGE
        // AND SET THE STATUS CODE TO 404.
        if (a == null) {
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            String message =
                    "You have requested an algorithm which does not exist (" + algorithmName + "). You can"
                    + "get a complete list of all available algorithms at " + Configuration.baseUri + "/algorithm\n";
            return sendMessage(message);
        }
        final Publisher publisher = new Publisher(variant.getMediaType());
        final OutputProcessor representer = new OutputProcessor();
        final Pipeline pipe = new Pipeline(publisher, representer);
        try {
            getResponse().setStatus(Status.SUCCESS_OK);
            return (Representation) pipe.process(a);
        } catch (final YaqpException ex) {
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
            YaqpLogger.LOG.log(
                    new Fatal(getClass(), "error 500 occured when a client asked for the representation of the algorithm :"+algorithmName));
            String message = "We apologize for the inconvenience. This is an internal server error we are going to solve. "
                    + "The issue has been logged and the administrators of the site will fix it soon. "
                    + "Thank you for your understanding!\n";
            return sendMessage(message);
        }
    }


    /**
     * Exploits the {@link TrainingService } to train a new model, and returns the
     * trained model in a supported mime type accerding to the request of the client.
     * @param entity
     *      The parameters posted by the client as application/x-www-form-urlencoded
     * @param variant
     *      The requested variant for the respponse from the server including the
     *      Accept field in the header of the request.
     * @return
     *      Representation of the trained model.
     * @throws ResourceException
     *      In case the training cannot be performed due to client or server error
     */
    @Override
    protected Representation post(Representation entity, final Variant variant) throws ResourceException {

        // THE DEFAULT MEDIATYPE OF THE RESPONSE IS text/uri-list UNLESS THE CLIENT ASKS FOR
        // SOMETHING DIFFERENT SETTING THE 'Accept' HEADER OF THE REQUEST ACCORDINGLY, TO ONE OF THE
        // SUPPORTED MEDIATYPES.
        MediaType responseMedia = MediaType.TEXT_URI_LIST;

        // IF THE CLIENT ASKS FOR A CERTAIN MEDIATYPE, SET THE responseMedia ACCORDINGLY
        if(!getRequest().getClientInfo().getAcceptedMediaTypes().get(0).getMetadata().equals(MediaType.ALL) &&
                getVariants().contains(new Variant(responseMedia))){
            responseMedia = variant.getMediaType();
        }
        /*
         *
         * Perform the training calling a Training Service.
         */
        try {
            QSARModel trainedModel = new TrainingService(new YaqpForm(entity), new User(), trainer, responseMedia).call();
            /**
             * In the future, training services (i.e. the class TrainingService) will be submitted in
             * an executor and a task will be created right afterwards. The URI of the created task will
             * be returned to the client.
             */
            final Publisher publisher = new Publisher(responseMedia);
            final OutputProcessor representer = new OutputProcessor();
            Pipeline representationPipe = new Pipeline(publisher, representer);
            return (Representation ) representationPipe.process(trainedModel);
        } catch (YaqpException ex) {
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return new StringRepresentation(ex.toString() + "\n");
        } catch (Exception ex) {
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
            Logger.getLogger(AlgorithmResource.class.getName()).log(Level.SEVERE, null, ex);
            return new StringRepresentation(ex.toString() + "\n");
        }
    }


}
