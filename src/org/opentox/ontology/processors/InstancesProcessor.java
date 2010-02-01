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

package org.opentox.ontology.processors;

import java.net.URI;
import org.opentox.io.processors.InputProcessor;
import org.opentox.ontology.TurboOntModel;
import org.opentox.ontology.components.Dataset;
import org.opentox.ontology.exceptions.YaqpOntException;
import weka.core.Instances;

/**
 *
 * This processor converts a <code>Dataset</code> object for a dataset into the equivalent
 * <code>Instances</code> object which can be exploited by <code>weka</code>.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class InstancesProcessor 
        extends AbstractOntProcessor<Dataset, Instances>
{

    /**
     *
     * Converts an ontological model of a dataset (provided as an instance
     * of <code>Dataset</code> into a <code>weka.core.Instances</code>
     * object which can be used by weka algorithms and other weka routines.
     * @param dataset The dataset to be converted to Instances
     * @return The dataset as Instances (weka object)
     */
    public Instances convert(Dataset dataset) throws YaqpOntException{
        return dataset.getInstances();
    }






    public static void main(String[] args) throws Exception {
        InputProcessor p = new InputProcessor();
        //URI uri = new URI(ServerList.ambit.getBaseURI()+"/dataset/6");
        URI uri = new URI("http://localhost/6");
        TurboOntModel tom = p.handle(uri);
        InstancesProcessor ipr = new InstancesProcessor();
        Dataset dataset = new Dataset(tom);
        ipr.convert(dataset);
    }
}
