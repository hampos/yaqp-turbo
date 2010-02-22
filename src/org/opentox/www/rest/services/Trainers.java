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


package org.opentox.www.rest.services;

import org.opentox.ontology.components.Algorithm;
import org.opentox.ontology.util.YaqpAlgorithms;
import org.opentox.qsar.processors.filters.AttributeCleanup;
import org.opentox.qsar.processors.trainers.classification.NaiveBayesTrainer;
import org.opentox.qsar.processors.trainers.classification.SVCTrainer;
import org.opentox.qsar.processors.trainers.regression.MLRTrainer;
import org.opentox.qsar.processors.trainers.regression.SVMTrainer;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public enum Trainers {
    mlr("mlr", MLRTrainer.class, YaqpAlgorithms.MLR),
    svm("svm", SVMTrainer.class, YaqpAlgorithms.SVM),
    svc("svc", SVCTrainer.class, YaqpAlgorithms.SVC),
    naiveBayes("naiveBayes", NaiveBayesTrainer.class, YaqpAlgorithms.NAIVE_BAYES),
    cleanup("cleanup", AttributeCleanup.class, YaqpAlgorithms.CLEAN_UP);

    private Class trainer;
    private Algorithm algorithmEntity;
    private String name;

    private Trainers(String name, Class trainer, Algorithm algorithmEntity) {
        this.name = name;
        this.trainer = trainer;
        this.algorithmEntity = algorithmEntity;
    }


    /**
     * An algorithm component including metadata about the algorithm and other information
     * which can be used to produce a publishable representation of the algorithm (e.g. RDF,
     * PDF etc).
     * @return
     *      The algorithm component
     */
    public Algorithm getAlgorithmEntity() {
        return algorithmEntity;
    }

    /**
     * The name of the algortithm as it will appear on the web under /algorithm
     * @return
     *      Name of algorithm
     */
    public String getName() {
        return name;
    }

    /**
     * The class responsible for the training of models invoked upon POST
     * requests from clients.
     * @return
     *      Trainer
     */
    public Class getTrainer() {
        return trainer;
    }


    /**
     * This is equivalent to {@link Trainers#getName() getName()}.
     * @return
     *      Name of the algorithm.
     */
    @Override
    public String toString(){
        return getName();
    }


}