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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import org.opentox.config.Configuration;
import org.opentox.core.exceptions.Cause;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.processors.Pipeline;
import org.opentox.io.processors.InputProcessor;
import org.opentox.io.processors.OutputProcessor;
import org.opentox.io.processors.Publisher;
import org.opentox.ontology.components.Algorithm;
import org.opentox.ontology.components.QSARModel;
import org.opentox.ontology.components.User;
import org.opentox.ontology.data.DatasetBuilder;
import org.opentox.ontology.processors.InstancesProcessor;
import org.opentox.ontology.util.YaqpAlgorithms;
import org.opentox.ontology.util.vocabulary.ConstantParameters;
import org.opentox.qsar.processors.filters.AttributeCleanup;
import org.opentox.qsar.processors.filters.AttributeCleanup.ATTRIBUTE_TYPE;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Fatal;
import org.opentox.www.rest.components.URITemplate;
import org.opentox.www.rest.components.YaqpForm;
import org.opentox.www.rest.components.YaqpResource;
import org.opentox.www.rest.services.Trainers;
import org.opentox.www.rest.services.TrainingService;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import weka.core.Attribute;
import weka.core.Instances;

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
        setAutoCommitting(false);
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
    @SuppressWarnings({"unchecked"})
    protected Representation get(final Variant variant) throws ResourceException {        
        final Algorithm a = YaqpAlgorithms.getByName(algorithmName);
        // IF THE REQUESTED ALGORITHM WAS NOT FOUND, RETURN AN EXPLANATORY MESSAGE
        // AND SET THE STATUS CODE TO 404.
        if (a == null) {
            toggleNotFound();
            String message =
                    "You have requested an algorithm which does not exist (" + algorithmName + "). You can "
                    + "get a complete list of all available algorithms at " + Configuration.BASE_URI + "/algorithm" + NEWLINE;
            return sendMessage(message);
        }
        final Publisher publisher = new Publisher(variant.getMediaType());
        final OutputProcessor representer = new OutputProcessor();
        final Pipeline pipe = new Pipeline(publisher, representer);
        try {
            toggleSuccess();
            return (Representation) pipe.process(a);
        } catch (final YaqpException ex) {
            toggleServerError();
            YaqpLogger.LOG.log(
                    new Fatal(getClass(), "error 500 occured when a client asked for the representation of the algorithm :"+algorithmName));
            return sendMessage(_500_);
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
    @SuppressWarnings({"unchecked"})
    protected Representation post(Representation entity, final Variant variant) throws ResourceException {
        // CHECK IF THE ALGORITHM EXISTS...
        final Algorithm a = YaqpAlgorithms.getByName(algorithmName);
        if (a == null) {
            toggleNotFound();
            String message =
                    "You have requested an algorithm which does not exist (" + algorithmName + "). You can"
                    + "get a complete list of all available algorithms at " + Configuration.BASE_URI + "/algorithm"+NEWLINE;
            return sendMessage(message);
        }

        // @hampos: This is not quite generic though solves the problem for now.
        //          We should discuss about how to
        if (this.algorithmName.equals(YaqpAlgorithms.CLEAN_UP.getMeta().getName())){
            return filterData(entity, variant);
        }else{
            return trainModel(entity, variant);
        }        
       
    }



    @SuppressWarnings({"unchecked"})
    private Representation trainModel(final Representation entity, final Variant variant){
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
            QSARModel trainedModel = null;
            try {
                // trainer is any implementation of WekaTrainer.
                trainedModel = new TrainingService(new YaqpForm(entity), new User(), trainer, responseMedia).call();
            } catch (final YaqpException ex) {
                toggleBadRequest();
                return sendMessage(ex.toString() + NEWLINE);
            } catch (final Exception ex) {
                toggleServerError();
                return sendMessage(_500_);
            }
            /**
             * In the future, training services (i.e. the class TrainingService) will be submitted in
             * an executor and a task will be created right afterwards. The URI of the created task will
             * be returned to the client.
             */
            final Publisher publisher = new Publisher(responseMedia);
            final OutputProcessor representer = new OutputProcessor();
            Pipeline representationPipe = new Pipeline(publisher, representer);
            return (Representation ) representationPipe.process(trainedModel);
        } catch (final YaqpException ex) {
            toggleBadRequest();
            if (ex.getCode() == Cause.XQF412){
                final String tooSparseDataset = "The dataset you provided is too sparse, thus cannot be used to build a QSAR model.";
                return sendMessage(tooSparseDataset + NEWLINE);
            }
            return sendMessage(ex.toString() + NEWLINE);
        } catch (final Exception ex) {
            toggleServerError();
            YaqpLogger.LOG.log(new Fatal(getClass(), ex.toString()));
            return sendMessage(_500_);
        }
    }


    @SuppressWarnings({"unchecked"})
    private Representation filterData(final Representation entity, final Variant variant){
        InputProcessor p1 = new InputProcessor();
        DatasetBuilder p2 = new DatasetBuilder();
        InstancesProcessor p3 = new InstancesProcessor();
        AttributeCleanup p4 = new AttributeCleanup(ATTRIBUTE_TYPE.string);
        Pipeline pipe = new Pipeline(p1,p2,p3,p4);
        
        YaqpForm form = new YaqpForm(entity);
        URI uri;
        try {
            uri = new URI(form.getFirstValue(ConstantParameters.dataset_uri));
        } catch (URISyntaxException ex) {
            toggleBadRequest();
            return sendMessage("Inacceptable URI ("+form.getFirstValue(ConstantParameters.dataset_uri)+")"+NEWLINE);
        }
        Instances filteredData = null;
        try {
            filteredData = (Instances) pipe.process(uri);
        } catch (YaqpException ex) {
            toggleBadRequest();
            return sendMessage(ex.toString());
        }

        Enumeration attributes = filteredData.enumerateAttributes();
        String list = "";
        Attribute att;
        while (attributes.hasMoreElements()){
            att = (Attribute) attributes.nextElement();
            list += "feature_uris[]="+att.name();
            if (attributes.hasMoreElements()) list += "&";
        }
        return new StringRepresentation(uri+"?"+list, MediaType.TEXT_URI_LIST);
    }

    




}
