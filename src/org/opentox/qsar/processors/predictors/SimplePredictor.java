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

import java.util.Enumeration;
import org.opentox.config.ServerFolders;
import org.opentox.ontology.components.QSARModel;
import org.opentox.qsar.exceptions.QSARException;
import org.opentox.qsar.processors.filters.AttributeCleanup;
import org.opentox.qsar.processors.filters.AttributeCleanup.ATTRIBUTE_TYPE;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;

/**
 *
 * An implementation of {@link WekaPredictor } which uses the stored models on the server
 * to calculated the predicted values for the set of compounds provided in its input.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public final class SimplePredictor extends WekaPredictor {

    private String filePath = null;

    private SimplePredictor(){
        
    }

    public SimplePredictor(QSARModel model) throws QSARException {
        super(model);
        filePath = ServerFolders.models_weka + "/" + model.getCode();
    }

    /**
     * Perform the prediction which is based on the serialized model file on the server.
     * @param data
     *      Input data for with respect to which the predicitons are calculated
     * @return
     *      A dataset containing the compounds submitted along with their predicted values.
     * @throws QSARException
     *      In case the prediction (as a whole) is not feasible. If the prediction is not
     *      feasible for a single instance, the prediction is set to <code>?</code> (unknown/undefined/missing).
     *      If the prediction is not feasible for all instances, an exception (QSARException) is thrown.
     */
    @Override
    public Instances predict(final Instances data) throws QSARException {
        

        Instances dataClone = new Instances(data);
        /**
         * IMPORTANT!
         * String attributes have to be removed from the dataset before
         * applying the prediciton
         */
        dataClone = new AttributeCleanup(ATTRIBUTE_TYPE.string).filter(dataClone);

        /**
         * Set the class attribute of the incoming data to any arbitrary attribute
         * (Choose the last for instance).
         */
        dataClone.setClass(dataClone.attribute(model.getDependentFeature().getURI()));

        /**
         *
         * Create the Instances that will host the predictions. This object contains
         * only two attributes: the compound_uri and the target feature of the model.
         */
        Instances predictions = null;
        FastVector attributes = new FastVector();
        final Attribute compoundAttribute = new Attribute("compound_uri", (FastVector) null);
        final Attribute targetAttribute = dataClone.classAttribute();
        attributes.addElement(compoundAttribute);
        attributes.addElement(targetAttribute);

        predictions = new Instances("predictions", attributes, 0);
        predictions.setClassIndex(1);
        
        Instance predictionInstance = new Instance(2);
        try {
            final Classifier cls = (Classifier) SerializationHelper.read( filePath );

            for (int i = 0; i < data.numInstances(); i++) {
                try {
                    String currentCompound = data.instance(i).stringValue(0);
                    predictionInstance.setValue(compoundAttribute, currentCompound);

                    if (targetAttribute.type() == Attribute.NUMERIC) {
                        double clsLabel = cls.classifyInstance(dataClone.instance(i));
                        predictionInstance.setValue(targetAttribute, clsLabel);
                    }else if (targetAttribute.type() == Attribute.NOMINAL) {
                        double[] clsLable = cls.distributionForInstance(dataClone.instance(i));
                        int indexForNominalElement = maxInArray(clsLable).getPosition();
                        Enumeration nominalValues = targetAttribute.enumerateValues();
                        int counter = 0;
                        String nomValue = "";
                        while (nominalValues.hasMoreElements()) {
                            if (counter == indexForNominalElement){
                                nomValue = nominalValues.nextElement().toString();
                                break;
                            }
                            counter++;
                        }
                        predictionInstance.setValue(targetAttribute, nomValue);

                        predictionInstance.setValue(targetAttribute, cls.classifyInstance(dataClone.instance(i)));
                    }
                    
                    predictions.add(predictionInstance);
                } catch (Exception ex) {System.out.println(ex);}
            }

        } catch (Exception ex) {    }

        System.out.println(predictions);
        return data;
    }




    /**
     * Auxiliary class used here only. An ArrayElement stands for an element of a
     * java array (e.g. String[]) along with its position (an integer).
     *
     * @param <E> data type for the element
     */
    private class ArrayElement<E>{
        private final int position;
        private final E element;

        public ArrayElement(final int position, final E element) {
            this.position = position;
            this.element = element;
        }

        public E getElement() {
            return element;
        }

        public int getPosition() {
            return position;
        }
    }

    /**
     * Returns the element of an array where the maximum value occurs.
     * @param array
     *      A double array
     * @return
     *      The value and the position of the maximum.
     */
    private ArrayElement<Double> maxInArray(double[ ] array){
        if (array == null) throw new NullPointerException("You provided a null array - cannot proceed"); 
        if (array.length == 0) return new ArrayElement<Double>(-1, Double.NaN);
        double max = array[0];
        int position = 0;
        for (int i = 0 ; i< array.length; i++){
            if (array[i]>max){
                max = array[i];
                position = i;
            }
        }
        return new ArrayElement<Double>(position, max);
    }
   
    
}
