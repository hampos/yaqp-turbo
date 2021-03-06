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
import java.util.ArrayList;
import java.util.List;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.io.exceptions.YaqpIOException;
import org.opentox.io.interfaces.JPublishable;
import org.opentox.io.util.YaqpIOStream;
import org.opentox.www.rest.components.YaqpRepresentation;
import org.restlet.data.Language;

/**
 *
 * This is the final processor in every pipeline which ends to the client. Recieves
 * a {@link JPublishable publishable object} and invokes its own method
 * {@link JPublishable#publish(org.opentox.io.util.YaqpIOStream) publish} to write it
 * to an outputstream. At the same time, creates a {@link YaqpRepresentation representation}
 * which is returned to the client. The media type (MIME) of this representation is
 * set accordingly.
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class OutputProcessor
        extends AbstractIOProcessor<JPublishable,YaqpRepresentation>
{
 

    public OutputProcessor(){
       
    }

    public YaqpRepresentation handle(final JPublishable i) throws YaqpIOException {
        YaqpRepresentation representation = new YaqpRepresentation(i.getMediaType()) {

            @Override
            public void write(OutputStream outputStream) throws IOException {
                try {
                    i.publish(new YaqpIOStream(outputStream));
                } catch (YaqpException ex) {
                    //
                }finally{
                    outputStream.flush();
                    outputStream.close();
                }
            }
        };
        List<Language> langs= new ArrayList<Language>();
        langs.add(Language.ENGLISH);
        langs.add(Language.ENGLISH_US);
        representation.setLanguages(langs);
        return representation;
    }

    



}
