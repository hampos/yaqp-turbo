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

import org.opentox.config.Configuration;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.processors.Pipeline;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.handlers.ReaderHandler;
import org.opentox.db.util.Page;
import org.opentox.io.processors.OutputProcessor;
import org.opentox.io.processors.Publisher;
import org.opentox.ontology.components.ComponentList;
import org.opentox.ontology.components.Feature;
import org.opentox.ontology.components.QSARModel;
import org.opentox.ontology.exceptions.ImproperEntityException;
import org.opentox.www.rest.components.URITemplate;
import org.opentox.www.rest.components.YaqpResource;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ModelMetaResource extends YaqpResource {

    public static final URITemplate template = URITemplate.secondarySubTemplateOf(ModelResource.template, "meta");
    
    private static final String _404_ = "The model URI you specified does not correspond to a model resource "
            + "on the server. You can get a list of all available models at " + Configuration.BASE_URI + "/model" + NEWLINE;

    private enum META_KEY {

        predicted,
        independent,
        dependent
    }
    private META_KEY metaKey = null;
    private int modelId = -1;

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        initialize(
                MediaType.TEXT_URI_LIST,
                MediaType.APPLICATION_RDF_XML,
                MediaType.APPLICATION_RDF_TURTLE);
        try {
            metaKey = META_KEY.valueOf(Reference.decode(getRequest().getAttributes().get(template.getMetaKey()).toString()));
        } catch (final IllegalArgumentException ex) {
            /* No matching for the provided meta key - Leave metaKey null */
        }

        try {
            modelId = Integer.parseInt(Reference.decode(getRequest().getAttributes().get(template.getPrimaryKey()).toString()));
        } catch (final NumberFormatException ex) {
            /* The provided model id is not numeric : modelId = -1 */
        }

    }

    @Override
    protected Representation get(final Variant variant) throws ResourceException {

        final MediaType requestMedia = variant.getMediaType();

        if (modelId == -1) {
            toggleNotFound();
            return sendMessage(_404_);
        }
        if (metaKey == null) {
            toggleNotFound();
            String message = "Suggestions :" + NEWLINE;
            String uri = Configuration.BASE_URI + "/model/" + modelId + "/";
            for (META_KEY mk : META_KEY.values()) {
                message += uri + mk.name() + NEWLINE;
            }
            message += "Did you mean any of the above?" + NEWLINE;
            return sendMessage(message);
        }

        QSARModel qsarModel = null;
        try {
            QSARModel prototype = new QSARModel();
            prototype.setId(modelId);
            qsarModel = (QSARModel) ReaderHandler.search(prototype, new Page(), false).getFirst();
        } catch (DbException ex) {
            toggleNotFound();
            return sendMessage(_404_);
        } catch (ImproperEntityException ex) {
        }

        ComponentList listOFComponents = new ComponentList();        

        if (metaKey == META_KEY.dependent){
            listOFComponents.add(qsarModel.getDependentFeature());
        } else if (metaKey == META_KEY.predicted){
            listOFComponents.add(qsarModel.getPredictionFeature());
        } else if (metaKey == META_KEY.independent){
            for (Feature indF : qsarModel.getIndependentFeatures()){
                listOFComponents.add(indF);
            }
        }

        final Publisher publisher = new Publisher(requestMedia);
        final OutputProcessor representer = new OutputProcessor();
        final Pipeline pipe = new Pipeline(publisher, representer);

        Representation rep = null;
        try {
            rep = (Representation) pipe.process(listOFComponents);
        } catch (final YaqpException ex) {
            toggleServerError();
            //Logger.getLogger(ModelMetaResource.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rep;
    }
}
