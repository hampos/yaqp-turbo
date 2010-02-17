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
package org.opentox.io.publishable;

import com.hp.hpl.jena.rdf.model.Model;
import java.io.OutputStream;
import org.opentox.io.exceptions.YaqpIOException;
import org.opentox.io.util.YaqpIOStream;
import org.restlet.data.MediaType;
import static org.opentox.core.exceptions.Cause.*;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class TurtleObject extends OntObject {

    public TurtleObject() {
        super();
    }

    public TurtleObject(Model other) {
        super(other);
    }

    public TurtleObject(OntObject other) {
        super(other);
    }

    public TurtleObject(YaqpIOStream ioStream) {
        super(ioStream);
    }

    public void publish(YaqpIOStream stream) throws YaqpIOException {
        if (stream == null) {
            throw new NullPointerException("Cannot publish an RDF document to a null stream");
        }
        try {
            this.write((OutputStream) stream.getStream(), "TURTLE");
        } catch (ClassCastException ex) {
            throw new ClassCastException("The stream you provided is not a valid stream for publishing "
                    + "an TTL document but it does not seem to be null either.");
        } catch (Exception ex) {
            throw new YaqpIOException(XTTL700, "Cannot write the TTL document to this stream", ex);
        }
    }

    @Override
    public MediaType getMediaType() {
        return MediaType.APPLICATION_RDF_TURTLE;
    }
}
