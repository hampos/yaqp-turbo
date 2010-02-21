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

import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.processors.Pipeline;
import org.opentox.db.handlers.ReaderHandler;
import org.opentox.db.util.Page;
import org.opentox.io.processors.OutputProcessor;
import org.opentox.io.processors.Publisher;
import org.opentox.ontology.components.Algorithm;
import org.opentox.ontology.components.ComponentList;
import org.opentox.ontology.components.YaqpComponent;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Fatal;
import org.opentox.www.rest.components.URITemplate;
import org.opentox.www.rest.components.YaqpRepresentation;
import org.opentox.www.rest.components.YaqpResource;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

/**
 * Representation of the set of all algorithms.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class AlgorithmsResource extends YaqpResource {

    public static final URITemplate template = new URITemplate("algorithm", null, null);

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        initialize(                
                MediaType.APPLICATION_RDF_XML,
                MediaType.APPLICATION_RDF_TURTLE,
                MediaType.TEXT_URI_LIST
                );
    }

    /**
     * Representation of the set of available algorithms.
     * @param variant
     *      The requested variant for the algorithm list.
     * @return
     *      Representation of list of algorithms
     * @throws ResourceException
     *      In case the representation could not be generated
     */
    @Override
    protected Representation get(final Variant variant) throws ResourceException {
        try {
            final MediaType requestMediaType = variant.getMediaType();
            final Publisher publisher = new Publisher(requestMediaType);
            final OutputProcessor representer = new OutputProcessor();
            final Pipeline pipe = new Pipeline(publisher, representer);
            ComponentList<YaqpComponent> algorithmList = ReaderHandler.search(new Algorithm(), new Page(), false);
            YaqpRepresentation representation = (YaqpRepresentation) pipe.process(algorithmList);
            return representation;
        } catch (YaqpException ex) {
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
            YaqpLogger.LOG.log(new Fatal(getClass(), "Unexpected condition : "+ex.toString()));
            String message =
                    "Unexpected condition while generating the representation " +
                    "of the list of all algorithms on the server. We apologize for the " +
                    "inconvenience. This issue has been logged and the server administrators will try to " +
                    "fix it as soon as possible.\n";
            return sendMessage(message);

        }
    }

    

}