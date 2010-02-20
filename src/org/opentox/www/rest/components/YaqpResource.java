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
package org.opentox.www.rest.components;

import java.util.Collection;
import org.restlet.data.CharacterSet;
import org.restlet.data.Language;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class YaqpResource extends ServerResource {

    public void initialize(Collection<MediaType> supportedMedia) {
        super.doInit();
        for (MediaType m : supportedMedia) {
            getVariants().add(new Variant(m));
        }
    }

    public void initialize(MediaType... supportedMedia) {
        super.doInit();
        for (MediaType m : supportedMedia) {
            getVariants().add(new Variant(m));
        }

    }

    @Override
    protected Representation post(Representation entity, Variant variant) throws ResourceException {
        getResponse().setStatus(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
        String message = "POST method is not allowed on this URI. Please check the API documentation and do not repeat this request.\n";
        return new StringRepresentation(message, MediaType.TEXT_PLAIN, Language.ENGLISH, CharacterSet.UTF_8);
    }

    @Override
    protected Representation delete(Variant variant) throws ResourceException {
        getResponse().setStatus(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
        String message = "DELETE method is not allowed on this URI. Please check the API documentation and do not repeat this request.\n";
        return new StringRepresentation(message, MediaType.TEXT_PLAIN, Language.ENGLISH, CharacterSet.UTF_8);
    }

    protected Representation sendMessage(String message){
        return new StringRepresentation(message, MediaType.TEXT_PLAIN, Language.ENGLISH, CharacterSet.UTF_8);
    }


}
