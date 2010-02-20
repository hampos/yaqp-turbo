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


package org.opentox.qsar.processors.predictors;

import org.opentox.core.exceptions.Cause;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.processors.Processor;
import org.opentox.db.handlers.ReaderHandler;
import org.opentox.db.util.Page;
import org.opentox.ontology.components.ComponentList;
import org.opentox.ontology.components.QSARModel;
import org.opentox.ontology.components.YaqpComponent;
import org.opentox.qsar.exceptions.QSARException;
import weka.core.Instances;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class WekaPredictor extends Processor<Instances, Instances>{

    private QSARModel model;

    public WekaPredictor(QSARModel model) throws QSARException {
        if (model==null) throw new NullPointerException("Cannot predict from a null model - Provide a model first");
        this.model = model;
        if (!modelExists()) throw new QSARException(Cause.XQPred631, "This model does not exist");
    }

    private boolean modelExists(){
        QSARModel prototype = new QSARModel();
        prototype.setId(model.getId());
        try {
            ComponentList<YaqpComponent> list = ReaderHandler.search(model, new Page(), false);
            if (list.size()>0) return true;
        } catch (YaqpException ex) {
            // 
        }
        return false;
    }


    public Instances process(Instances data) throws YaqpException {
        if (model.getCode()==null) throw new QSARException(Cause.XQPred630,
                "Unknown Model - Probably the model was deleted or does not exist");
        if (model.getAlgorithm()==null || model.getAlgorithm().getMeta()==null || model.getAlgorithm().getMeta().getName()==null)
            throw new QSARException(Cause.XQPred631, "Unknown Model - Probably the model was deleted or does not exist");
        
        throw new UnsupportedOperationException("Not supported yet.");
    }

}