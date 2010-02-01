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
package org.opentox.ontology.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.config.Configuration;
import org.opentox.ontology.namespaces.OTAlgorithmTypes;
import org.opentox.ontology.util.vocabulary.Audience;
import org.opentox.ontology.util.vocabulary.ConstantParameters;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Warning;
import org.restlet.data.MediaType;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class YaqpAlgorithms {

    private AlgorithmMeta meta;
    private String name, uri;

    private YaqpAlgorithms() {
    }

    protected YaqpAlgorithms(String name) {
        uri = "http://" + Configuration.getProperties().getProperty("server.domainName") + ":"
                + Configuration.getProperties().getProperty("server.port") + "/algorithm/" + getName();
    }

    public AlgorithmMeta getMeta() {
        return meta;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }
    public static final YaqpAlgorithms MLR = new YaqpAlgorithms("mlr") {

        @Override
        public AlgorithmMeta getMeta() {

            ArrayList<AlgorithmParameter> Parameters = new ArrayList<AlgorithmParameter>();
            Parameters.add(ConstantParameters.TARGET);

            AlgorithmMeta meta = new AlgorithmMeta(getUri());
            meta.title = "Multiple Linear Regression Training Algorithm";
            meta.description = "Training algorithm for multiple linear regression models. Applies "
                    + "on datasets which contain exclusively numeric data entries. The algorithm is an "
                    + "implementation of LinearRegression of Weka.";
            meta.format.add(MediaType.APPLICATION_RDF_XML);
            meta.identifier = getUri();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                formatter.parse("2010-01-01");
            } catch (ParseException ex) {
                YaqpLogger.LOG.log(new Warning(getClass(), "(MLR) Wrong date : "+ex));
            }
            meta.type = "http://purl.org/dc/dcmitype/Service";
            meta.audience.add(Audience.Biologists);
            meta.audience.add(Audience.Toxicologists);
            meta.audience.add(Audience.QSARExperts);
            meta.provenance = "Updated vesrion from yaqp version 1.3.6 to yaqp-turbo version 1.0";
            meta.algorithmType = OTAlgorithmTypes.RegressionEagerSingleTarget;

            return meta;
        }
    };
    public static final YaqpAlgorithms SVM = new YaqpAlgorithms("svm") {

        @Override
        public AlgorithmMeta getMeta() {
            AlgorithmMeta meta = new AlgorithmMeta(getUri());
            return null;
        }
    };
}
