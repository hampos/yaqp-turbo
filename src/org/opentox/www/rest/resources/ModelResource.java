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


import java.io.File;
import org.opentox.config.Configuration;
import org.opentox.config.ServerFolders;
import org.opentox.core.exceptions.Cause;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.processors.Pipeline;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.handlers.ReaderHandler;
import org.opentox.db.handlers.UpdateHandler;
import org.opentox.db.util.Page;
import org.opentox.io.processors.OutputProcessor;
import org.opentox.io.processors.Publisher;
import org.opentox.ontology.components.ComponentList;
import org.opentox.ontology.components.QSARModel;
import org.opentox.ontology.components.User;
import org.opentox.ontology.exceptions.ImproperEntityException;
import org.opentox.qsar.processors.predictors.SimplePredictor;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Fatal;
import org.opentox.www.rest.components.URITemplate;
import org.opentox.www.rest.components.YaqpForm;
import org.opentox.www.rest.components.YaqpResource;
import org.opentox.www.rest.services.PredictionService;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ModelResource extends YaqpResource {

    public static final URITemplate template = URITemplate.primarySubtemplateOf(ModelsResource.template, "modelId");
    private String model_id;
    
    private static final String modelNotFoundMessage = "The model you are looking for was not found on the server. Check out "
            + Configuration.BASE_URI + ModelsResource.template.toString() + " for a complete list of the "
            + "available models." + NEWLINE;

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
                MediaType.TEXT_URI_LIST);

        model_id = Reference.decode(getRequest().getAttributes().get(template.getPrimaryKey()).toString());
    }

    // TODO: Better exception handling and status codes.
    @Override
    @SuppressWarnings({"unchecked"})
    protected Representation get(Variant variant) throws ResourceException {        
        QSARModel prototype = new QSARModel();
        try {
            prototype.setId(Integer.parseInt(model_id));
        } catch (NumberFormatException ex) {
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return sendMessage(modelNotFoundMessage);
        }

        try {
            ComponentList listOfModels = ReaderHandler.search(prototype, new Page(), false);
            if (listOfModels.size() == 0) {
                toggleNotFound();
                return sendMessage(modelNotFoundMessage);
            }
            QSARModel mod = (QSARModel) listOfModels.getFirst();
            final Publisher publisher = new Publisher(variant.getMediaType());
            final OutputProcessor representer = new OutputProcessor();
            final Pipeline pipe = new Pipeline(publisher, representer);
            toggleSuccess();
            return (Representation) pipe.process(mod);
        } catch (YaqpException ex) {
            YaqpLogger.LOG.log(new Fatal(getClass(), "Excpetion caught : {" + ex + "}"));
            toggleServerError();
            return sendMessage(_DEAD_DB_);
        }

    }

    // TODO: Better exception handling and status codes.
    @Override
    protected Representation post(Representation entity, Variant variant) throws ResourceException {

        /*
         * First try to locate the model and see if it exists...
         */
        QSARModel prototype = new QSARModel();
        try {
            prototype.setId(Integer.parseInt(model_id));
        } catch (NumberFormatException ex) {
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return sendMessage(modelNotFoundMessage);
        }
        ComponentList listOfModels;
        try {
            listOfModels = ReaderHandler.search(prototype, new Page(), false);
            if (listOfModels.size() == 0) {
                getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
                return sendMessage(modelNotFoundMessage);
            }
        } catch (YaqpException ex) {
            YaqpLogger.LOG.log(new Fatal(getClass(), "Excpetion caught : {" + ex + "}"));
            toggleServerError();
            return sendMessage(_DEAD_DB_);
        }

        /*
         * Apply the prediction service to calculate the predictions and
         * POST the results to a dataset service.
         */
        PredictionService service;
        try {
            service = new PredictionService(new YaqpForm(entity), model_id, new User(), SimplePredictor.class, variant.getMediaType());            
            toggleSuccess();
            return service.call();
        } catch (final YaqpException ex) {            
            toggleBadRequest();
            if (ex.getCode() == Cause.XIO5052) {
                toggleRemoteError();
            }
            return sendMessage(ex.toString() + NEWLINE);
        } catch (Exception ex) {
            YaqpLogger.LOG.log(new Fatal(getClass(), "Excpetion caught : {" + ex + "}"));
            return sendMessage(_500_);
        }       
    }

    @Override
    protected Representation delete(final Variant variant) throws ResourceException {
        QSARModel modelToBeDeleted = new QSARModel();

        try{
            modelToBeDeleted.setId(Integer.parseInt(this.model_id));
        }catch (final NumberFormatException nfe){
            toggleNotFound();
            return sendMessage("The model you attempted to delete was not found on the server" + NEWLINE);
        }


        try {
            ComponentList list = ReaderHandler.search(modelToBeDeleted, new Page(), false);
            if (list.size()==0) throw new DbException();
            QSARModel model = (QSARModel) list.getFirst();
            String code = model.getCode();
            System.out.println(code);
            UpdateHandler.delete(modelToBeDeleted);
            new File(ServerFolders.models_weka+"/"+code).renameTo(new File(ServerFolders.Trash+"/"+code));
            new File(ServerFolders.models_pmml+"/"+code).renameTo(new File(ServerFolders.Trash+"/"+code+".pmml"));
        } catch (final DbException ex) {
            toggleNotFound();
            return sendMessage("The model "+Configuration.BASE_URI+"/model/"+this.model_id+" was not found on the server"+NEWLINE);
        } catch (final ImproperEntityException ex) { /* do nothing */ }

        toggleSuccess();
        return sendMessage("The model "+Configuration.BASE_URI+"/model/"+this.model_id+" was successfully deleted. " +
                "If you need to recover it, send a message to the service administrators providing the URI of the model you need to " +
                "recover. However, once deleted, there is no guarrantee that it will be possible to recover it successfully. Note that the" +
                "deleted model will be automatically deleted after 1 month!"+NEWLINE);

    }


}
