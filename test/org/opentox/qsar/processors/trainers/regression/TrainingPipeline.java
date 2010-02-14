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


package org.opentox.qsar.processors.trainers.regression;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.processors.Pipeline;
import org.opentox.core.processors.Processor;
import org.opentox.db.util.TheDbConnector;
import org.opentox.io.processors.InputProcessor;
import org.opentox.io.util.ServerList;
import org.opentox.ontology.components.QSARModel;
import org.opentox.ontology.data.DatasetBuilder;
import org.opentox.ontology.processors.InstancesProcessor;
import org.opentox.ontology.util.AlgorithmParameter;
import org.opentox.qsar.interfaces.JTrainer;
import org.opentox.qsar.processors.filters.AttributeCleanup;
import org.opentox.qsar.processors.filters.AttributeCleanup.ATTRIBUTE_TYPE;
import org.opentox.qsar.processors.filters.InstancesFilter;
import org.opentox.qsar.processors.filters.SimpleMVHFilter;
import org.opentox.qsar.processors.trainers.AbstractTrainer;
import org.opentox.qsar.processors.trainers.WekaTrainer;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class TrainingPipeline  extends Processor<URI, QSARModel> {

     AbstractTrainer trainer;

        public TrainingPipeline(AbstractTrainer trainer) {            
            this.trainer= trainer;
        }

        public QSARModel process(URI data) throws YaqpException {
            TheDbConnector.init();
            InputProcessor p1 = new InputProcessor();
            DatasetBuilder p2 = new DatasetBuilder();
            InstancesProcessor p3 = new InstancesProcessor();
            InstancesFilter p4 = new SimpleMVHFilter();
            InstancesFilter p5 = new AttributeCleanup(new ATTRIBUTE_TYPE[]{ATTRIBUTE_TYPE.string, ATTRIBUTE_TYPE.nominal});
            

            Pipeline pipe = new Pipeline();
            pipe.add(p1);
            pipe.add(p2);
            pipe.add(p3);
            pipe.add(p4);
            pipe.add(p5);
            pipe.add(trainer);

            QSARModel model = (QSARModel) pipe.process(data);
            return model;
        }

}