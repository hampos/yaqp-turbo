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
package org.opentox.www.rest;

import com.sun.grizzly.util.LoggingFormatter;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import org.opentox.www.rest.components.YaqpApplication;
import org.opentox.www.rest.resources.AlgorithmResource;
import org.opentox.www.rest.resources.AlgorithmsResource;
import org.opentox.www.rest.resources.ModelsResource;
import org.opentox.www.rest.resources.ModelResource;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
final public class Applecation extends YaqpApplication {

    public Applecation() throws IOException {
        super();
        FileHandler fh = new FileHandler("application.log", true);
        Formatter formatter = new LoggingFormatter();
        fh.setFormatter(formatter);
        Context.getCurrentLogger().addHandler(fh);
        Context.getCurrentLogger().setLevel(Level.SEVERE);
    }

    @Override
    public Restlet createInboundRoot() {
        Router router = new YaqpRouter(this.getContext().createChildContext());

        router.attach(AlgorithmResource.template.toString(), AlgorithmResource.class);
        router.attach(AlgorithmsResource.template.toString(), AlgorithmsResource.class);
        router.attach(ModelsResource.template.toString(), ModelsResource.class);
        router.attach(ModelResource.template.toString(), ModelResource.class);

        return router;
    }
}
