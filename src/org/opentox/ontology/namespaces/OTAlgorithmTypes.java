/*
 *
 * YAQP - Yet Another QSAR Project:
 * Machine Learning algorithms designed for the prediction of toxicological
 * features of chemical compounds become available on the Web. Yaqp is developed
 * under OpenTox (http://opentox.org) which is an FP7-funded EU research project.
 * This project was developed at the Automatic Control Lab in the Chemical Engineering
 * School of National Technical University of Athens. Please read README for more
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
package org.opentox.ontology.namespaces;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.rdf.model.Resource;
import org.opentox.ontology.TurboOntModel;

/**
 *
 * TODO: Domain of Applicability (doa) algorithms
 * A collection of algorithm types used in OpenTox.
 * @author Pantelis Soapsakis
 * @author Charalampos Chomenides
 */
public class OTAlgorithmTypes extends YaqpOntEntity {

    public OTAlgorithmTypes() {
        super();
    }

    /**
     *
     * Construct a new Algorithm Type given a resource.
     * @param resource
     */
    public OTAlgorithmTypes(Resource resource) {
        super(resource);
    }

    public OTAlgorithmTypes(String resourceName){
        super(_model.createResource(String.format(_NS_AlgorithmTypes, resourceName)));
    }
    /**
     *
     * Classification Algorithms
     */
    public static final OTAlgorithmTypes Classification =
            new OTAlgorithmTypes("Classification") {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    // TODO: Move the following line(s) to the 'super' class....
                    OntClass cl = model.createClass(getURI());
                    cl.setSuperClass(Learning.getResource());
                    model.includeOntClass(Learning);
                    return cl;
                }
            };
    /**
     *
     * Eager Classification Algorithms with many targets.
     */
    public static final OTAlgorithmTypes ClassificationEagerMultipleTargets =
            new OTAlgorithmTypes("ClassificationEagerMultipleTargets") {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass(getURI());
                    cl.addSuperClass(Classification.getResource());
                    cl.addSuperClass(EagerLearning.getResource());
                    cl.addSuperClass(MultipleTargets.getResource());
                    model.includeOntClasses(new YaqpOntEntity[]{Classification, EagerLearning, MultipleTargets});
                    return cl;
                }
            };
    /**
     *
     * Eager Regression Algorithms with many targets.
     */
    public static final OTAlgorithmTypes RegressionEagerMultipleTargets =
            new OTAlgorithmTypes("RegressionEagerMultipleTargets") {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass(getURI());
                    cl.addSuperClass(Regression.getResource());
                    cl.addSuperClass(EagerLearning.getResource());
                    cl.addSuperClass(MultipleTargets.getResource());
                    model.includeOntClasses(new YaqpOntEntity[]{Regression, EagerLearning, MultipleTargets});
                    return cl;
                }
            };
    /**
     *
     * Eager Regression Algorithms with one single targer.
     */
    public static final OTAlgorithmTypes RegressionEagerSingleTarget =
            new OTAlgorithmTypes("RegressionEagerSingleTarget") {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass(getURI());
                    cl.addSuperClass(Regression.getResource());
                    cl.addSuperClass(EagerLearning.getResource());
                    cl.addSuperClass(SingleTarget.getResource());
                    model.includeOntClasses(new YaqpOntEntity[]{Regression, EagerLearning, SingleTarget});
                    return cl;
                }
            };
    /**
     *
     * Eager Classification Algorithms with one single targer.
     */
    public static final OTAlgorithmTypes ClassificationEagerSingleTarget =
            new OTAlgorithmTypes("ClassificationEagerSingleTarget") {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass(getURI());
                    cl.addSuperClass(Classification.getResource());
                    cl.addSuperClass(EagerLearning.getResource());
                    cl.addSuperClass(SingleTarget.getResource());
                    model.includeOntClasses(new YaqpOntEntity[]{Classification, EagerLearning, SingleTarget});
                    return cl;
                }
            };
    /**
     *
     * Eager Learning Algorithms.
     */
    public static final OTAlgorithmTypes EagerLearning =
            new OTAlgorithmTypes("EagerLearning") {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass(getURI());
                    cl.setSuperClass(Learning.getResource());
                    cl.setDisjointWith(LazyLearning.getResource());
                    model.includeOntClass(Learning);
                    model.includeOntClass(LazyLearning);
                    return cl;
                }
            };
    /**
     * Lazy Learning Algorithms.
     */
    public static final OTAlgorithmTypes LazyLearning =
            new OTAlgorithmTypes("LazyLearning") {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass(getURI());
                    cl.setSuperClass(Learning.getResource());
                    model.includeOntClass(Learning);
                    return cl;
                }
            };
    /**
     *
     * Regression Machine Learning Algorithms.
     */
    public static final OTAlgorithmTypes Regression =
            new OTAlgorithmTypes("Regression") {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass(getURI());
                    cl.setSuperClass(Learning.getResource());
                    cl.setDisjointWith(Classification.getResource());
                    model.includeOntClass(Learning);
                    model.includeOntClass(Classification);
                    return cl;
                }
            };
    /**
     *
     * Any kind of supervised algorithms.
     */
    public static final OTAlgorithmTypes Supervised =
            new OTAlgorithmTypes("Supervised") {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass(getURI());
                    cl.setSuperClass(AlgorithmType.getResource());
                    model.includeOntClass(AlgorithmType);
                    return cl;
                }
            };
    /**
     *
     * Learning algorithms of any type.
     */
    public static final OTAlgorithmTypes Learning =
            new OTAlgorithmTypes("Learning") {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass(getURI());
                    cl.setSuperClass(MSDMTox.getResource());
                    model.includeOntClass(MSDMTox);
                    return cl;
                }
            };
    /**
     *
     * Learning algorithms for a single target.
     */
    public static final OTAlgorithmTypes SingleTarget =
            new OTAlgorithmTypes("SingleTarget") {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass(getURI());
                    cl.setSuperClass(MSDMTox.getResource());
                    model.includeOntClass(MSDMTox);
                    return cl;
                }
            };
    /**
     *
     * Learning algorithms for multiple targets.
     */
    public static final OTAlgorithmTypes MultipleTargets =
            new OTAlgorithmTypes("MultipleTargets") {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass(getURI());
                    cl.setSuperClass(MSDMTox.getResource());
                    cl.setDisjointWith(SingleTarget.getResource());
                    model.includeOntClass(MSDMTox);
                    model.includeOntClass(SingleTarget);
                    return cl;
                }
            };
    /**
     *
     * MSDMTOx Algorithms
     */
    public static final OTAlgorithmTypes MSDMTox =
            new OTAlgorithmTypes("MSDMTox") {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass(getURI());
                    cl.setSuperClass(AlgorithmType.getResource());
                    model.includeOntClass(AlgorithmType);
                    return cl;
                }
            };
    /**
     *
     * Preprocessing algorithms.
     */
    public static final OTAlgorithmTypes Preprocessing =
            new OTAlgorithmTypes("Preprocessing") {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass(getURI());
                    cl.setSuperClass(AlgorithmType.getResource());
                    model.includeOntClass(AlgorithmType);
                    return cl;
                }
            };
    /**
     *
     * Datacleanup algorithms (Preparation of a dataset for training).
     */
    public static final OTAlgorithmTypes DataCleanup =
            new OTAlgorithmTypes("DataCleanup") {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass(getURI());
                    cl.setSuperClass(Preprocessing.getResource());
                    model.includeOntClass(Preprocessing);
                    return cl;
                }
            };
    /**
     *
     * Algorithms for the calculation of molecular descriptors.
     */
    public static final OTAlgorithmTypes DescriptorCalculation =
            new OTAlgorithmTypes("DescriptorCalculation") {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass(getURI());
                    cl.setSuperClass(AlgorithmType.getResource());
                    model.includeOntClass(AlgorithmType);
                    return cl;
                }
            };
    /**
     *
     * Normalization Algorithms.
     */
    public static final OTAlgorithmTypes Normalization =
            new OTAlgorithmTypes("Normalization") {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass(getURI());
                    cl.setSuperClass(Preprocessing.getResource());
                    model.includeOntClass(Preprocessing);
                    return cl;
                }
            };
    /**
     *
     * Unsupervised Filtering routines.
     */
    public static final OTAlgorithmTypes Unsupervised =
            new OTAlgorithmTypes("Unsupervised") {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass(getURI());
                    cl.setSuperClass(AlgorithmType.getResource());
                    model.includeOntClass(AlgorithmType);
                    return cl;
                }
            };
    /**
     *
     * Feature selection.
     */
    public static final OTAlgorithmTypes FeatureSelection =
            new OTAlgorithmTypes("FeatureSelection") {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass(getURI());
                    cl.setSuperClass(Preprocessing.getResource());
                    model.includeOntClass(Preprocessing);
                    return cl;
                }
            };
    /**
     *
     * Supervised feature selection.
     */
    public static final OTAlgorithmTypes FeatureSelectionSupervised =
            new OTAlgorithmTypes("FeatureSelectionSupervised") {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass(getURI());
                    cl.addSuperClass(FeatureSelection.getResource());
                    cl.addSuperClass(Supervised.getResource());
                    model.includeOntClass(FeatureSelection);
                    model.includeOntClass(Supervised);
                    return cl;
                }
            };
    /**
     *
     * Unsupervised feature selection.
     */
    public static final OTAlgorithmTypes FeatureSelectionUnsupervised =
            new OTAlgorithmTypes("FeatureSelectionUnsupervised") {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass(getURI());
                    cl.addSuperClass(FeatureSelection.getResource());
                    cl.addSuperClass(Unsupervised.getResource());
                    model.includeOntClasses(new YaqpOntEntity[]{FeatureSelection, Unsupervised});

                    return cl;
                }
            };
    /**
     *
     * Algorithm of any type.
     */
    public static final OTAlgorithmTypes AlgorithmType =
            new OTAlgorithmTypes("AlgorithmType") {

                @Override
                public OntClass createOntClass(TurboOntModel model) {
                    OntClass cl = model.createClass(getURI());
                    cl.setVersionInfo("1.1");
                    return cl;
                }
            };



    
}
