/*
 * YAQP - Yet Another QSAR Project: Machine Learning algorithms designed for
 * the prediction of toxicological features of chemical compounds become
 * available on the Web. Yaqp is developed under OpenTox (http://opentox.org)
 * which is an FP7-funded EU research project.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
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
 */
package org.opentox.io.engines;

import org.opentox.core.exceptions.YaqpException;
import org.opentox.io.util.YaqpIOStream;
import org.opentox.io.publishable.OntObject;
import org.opentox.io.publishable.RDFObject;
import org.opentox.ontology.exceptions.YaqpOntException;
import static org.opentox.core.exceptions.Cause.XONT1;

/**
 *
 * @author Charalampos Chomenides
 */
public class RDFEngine<O extends OntObject> extends IOEngine {

    public RDFEngine() {
        super();
    }

    @Override
    protected O getYaqpOntModel(YaqpIOStream is) throws YaqpException {
        try {
            return (O) new RDFObject(is);
        } catch (ClassCastException ex) {
            throw new ClassCastException("Typecasting error while trying to cast an RDF Object to a provided datatype");
        } catch (Exception ex) {// EXCEPTION FROM JENA: COULD NOT PARSE THE CONTENT!
            throw new YaqpOntException(XONT1,"Unable to parse content properly",ex);
        }
        }
    }
