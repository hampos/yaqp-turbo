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
package org.opentox.io.processors;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.io.interfaces.JPublishable;
import org.opentox.io.publishable.RDFObject;
import org.opentox.io.util.ServerList;
import org.opentox.io.util.YaqpIOStream;
import org.opentox.ontology.util.YaqpAlgorithms;
import org.restlet.Client;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.data.Status;
import org.restlet.representation.OutputRepresentation;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Poster
        extends AbstractIOProcessor<JPublishable, Response>
{

    public Response handle(final JPublishable objectToPost) throws YaqpException {

        OutputRepresentation rep = new OutputRepresentation(objectToPost.getMediaType()) {

            @Override
            public void write(OutputStream outputStream) throws IOException {
                try {
                    objectToPost.publish(new YaqpIOStream(outputStream));
                } catch (YaqpException ex) {
                    Logger.getLogger(Poster.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        Client cli = new Client(Protocol.HTTP);
        int N_RETRIES =5, i=0;
        boolean success = false;


        Response response = new Response(null);


        while (!success && i < N_RETRIES){
             response = cli.post(ServerList.ambit+"/feature", rep);
             success = (response.getStatus().equals(Status.SUCCESS_OK));
             i++;
        }

        return response;
    }

    public static void main(String... args) throws YaqpException{
        RDFObject rdf = YaqpAlgorithms.MLR.getRDF();
        Poster p = new Poster();
        System.out.println(p.handle(rdf).toString());
    }


}
