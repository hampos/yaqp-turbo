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
import org.opentox.ontology.components.YaqpComponent;
import org.opentox.ontology.exceptions.ImproperEntityException;
import org.opentox.ontology.util.YaqpAlgorithms;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Fatal;
import org.opentox.www.rest.components.URITemplate;
import org.opentox.www.rest.components.YaqpRepresentation;
import org.opentox.www.rest.components.YaqpResource;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

/**
 * The following queries are provided on this uri:
 * <ul>
 * <li>?page=3&pagesize=10;</li>
 * <li>?algorithm=name</li>
 * <li>?dataset_uri=uri</li>
 * </ul>
 * The following queries are the next candidates (in decreasing priority order):
 * <ul>
 * <li>?algorithm_uri={uri}</li>
 * <li>?prediction_feature={uri}</li>
 * <li>?dependent_feature={uri}</li>
 * <li>?independent_features[]={uri}&independent_features[]={uri}</li>
 * </ul>
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ModelsResource extends YaqpResource {

    public static final URITemplate template = new URITemplate("model", null, null);

    private static final String PAGESIZE = "pagesize";
    private static final String PAGENUM = "page";
    private static final String ALGORITHM = "algorithm";
    private static final String DATASET_URI = "dataset_uri";

    private Page page;

    /*
     * The algorithm that was used to train the model (URL Query).
     */
    private String algorithm = null;
    /*
     * The training dataset (URL Query).
     */
    private String dataset_uri = null;

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        initialize(
                MediaType.TEXT_URI_LIST,
                MediaType.APPLICATION_RDF_XML,
                MediaType.APPLICATION_RDF_TURTLE
                
                );
        final Form queryForm = getReference().getQueryAsForm();
        int pageIndex = 0 ;
        int pagesize = 0;
        try{
            pageIndex = Integer.parseInt(queryForm.getFirstValue(PAGENUM)) - 1;            
        } catch (final NumberFormatException ex){/* do nothing */}
        try{
            pagesize = Integer.parseInt(queryForm.getFirstValue(PAGESIZE));
        } catch (final NumberFormatException ex){/* do nothing */}

        algorithm = queryForm.getFirstValue(ALGORITHM);
        dataset_uri = queryForm.getFirstValue(DATASET_URI);
        page = new Page(pagesize, pageIndex);
    }

    @Override
    @SuppressWarnings({"unchecked"}) 
    protected Representation get(final Variant variant) throws ResourceException {
        final MediaType requestMediaType = variant.getMediaType();
        boolean retrieveAllInformation = true;
        try {
            if (requestMediaType.isCompatible(MediaType.TEXT_ALL)
                    && !requestMediaType.equals(MediaType.TEXT_RDF_N3) && !requestMediaType.equals(MediaType.TEXT_RDF_NTRIPLES)) {
                retrieveAllInformation = false;
            }
            QSARModel prototype = new QSARModel();
            if (algorithm!=null){
                prototype.setAlgorithm(YaqpAlgorithms.getByName(algorithm));
            }
            if (dataset_uri!=null){
                prototype.setDataset(dataset_uri);
            }
            ComponentList<YaqpComponent> list = ReaderHandler.search(prototype, page, !retrieveAllInformation);
            final Publisher publisher = new Publisher(requestMediaType);
            final OutputProcessor representer = new OutputProcessor();
            final Pipeline pipe = new Pipeline(publisher, representer);
            YaqpRepresentation rep = (YaqpRepresentation) pipe.process(list);
            return rep;
        } catch (final YaqpException ex) {
            YaqpLogger.LOG.log(new Fatal(getClass(), "Fatal error while retrieving models from the DB : "+ex));
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
            return sendMessage("Internal Server Error");
        }        
    }

    
    @Override
    protected Representation delete(final Variant variant) throws ResourceException {
        try {
            ComponentList<YaqpComponent> list = ReaderHandler.search(new QSARModel(), new Page(), false);
            if (list.size()==0) {
                return sendMessage("No models were found on the server" + NEWLINE);
            }

            String code = null;
            /* DELETE ALL MODELS */
            for (YaqpComponent component : list){
                QSARModel model = (QSARModel) component;
                code = model.getCode();
                UpdateHandler.delete( (QSARModel) model);
                new File(ServerFolders.models_weka+"/"+code).renameTo(new File(ServerFolders.Trash+"/"+code));
                new File(ServerFolders.models_pmml+"/"+code).renameTo(new File(ServerFolders.Trash+"/"+code+".pmml"));
            }
                       
        } catch (final DbException ex) {
            toggleNotFound();
        } catch (final ImproperEntityException ex) { /* do nothing */ }

        toggleSuccess();
        return sendMessage("All models were successfully moved to trash!"+NEWLINE);

    }
}
