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

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.impl.OntModelImpl;
import com.hp.hpl.jena.vocabulary.OWL;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.ontology.components.User;
import org.opentox.qsar.processors.trainers.classification.NaiveBayesTrainer;
import org.opentox.qsar.processors.trainers.classification.SVCTrainer;
import org.opentox.qsar.processors.trainers.regression.MLRTrainer;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Warning;
import org.opentox.www.rest.components.YaqpForm;
import org.opentox.www.rest.components.YaqpResource;
import org.opentox.www.rest.services.TrainingService;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.OutputRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class AlgorithmResource extends YaqpResource {

    public static final String key = "/algorithm/a";

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        getVariants().add(new Variant(MediaType.TEXT_PLAIN));
        getVariants().add(new Variant(MediaType.TEXT_XML));
    }

    @Override
    protected Representation get(Variant variant) throws ResourceException {
        return new OutputRepresentation(MediaType.APPLICATION_RDF_XML) {

            @Override
            public void write(OutputStream out) throws IOException {
                OntModel mod = new OntModelImpl(OntModelSpec.OWL_DL_MEM);
                mod.createIndividual(mod.createResource("http://my.com/sth/1")).addRDFType(OWL.Nothing);
                mod.write(out);
                out.close();
            }
        };
    }

    @Override
    protected Representation post(Representation entity, Variant variant) throws ResourceException {
        YaqpLogger.LOG.log(new Warning(getClass(), "X--X"));
        try {
            return new TrainingService(new YaqpForm(entity), new User(), SVCTrainer.class, MediaType.TEXT_PLAIN).call();
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
