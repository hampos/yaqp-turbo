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
package org.opentox.core.processors;

import org.opentox.core.exceptions.ExceptionDetails;
import org.opentox.core.exceptions.ProcessorException;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.interfaces.JMultiProcessorStatus.STATUS;
import org.opentox.core.interfaces.JProcessor;
import org.opentox.util.logging.levels.*;
import org.opentox.util.logging.YaqpLogger;

/**
 * A set of jobs to be executed through a pipeline of processors
 * is a sequential layout if some processor is enabled. Note that if P1, P2, ..., PN
 * is a sequence of processors building a pipeline, then the output type from a processor
 * P_k should be a valid input to P_k+1 otherwise the processor P_k+1 will throw an
 * exception.
 * @param  <Input> Type for the input to the pipeline
 * @param  <Output> Type for the output of the pipeline
 * @param  <P> Type of processors contained in the pipeline
 * @author Sopasakis Pantelis
 * @author Charalampos Chomenides
 */
public class Pipeline<Input, Output, P extends JProcessor<Input, Output>>
          extends AbstractMultiProcessor<Input, Output, P> {

    private String PROPERTY_PIPELINE_STATUS = getStatus().getClass().getName();

    /**
     * Constructs a new Pipeline which is an ordered sequence of Processors
     * that are used to process input data sequentially (if enabled)
     */
    public Pipeline() {
        super();
        setfailSensitive(true);
    }

    /**
     *
     *
     * This method processes the input data to produce some output,
     * using the sequence of processors. The result from the first processor,
     * is input to the second and so on. Any non-enabled processors are overriden.
     * @param data
     * @return Pipeline Output
     * @throws YaqpException
     */
    public Output process(Input data) throws YaqpException {

        long start_time = 0;
        getStatus().setMessage("pipeline in process");
        Object o = data;
        // Process:
        for (int i = 0; i < size(); i++) {
            try {
                if (get(i).isEnabled()) {
                    start_time = System.currentTimeMillis();
                    getStatus().increment(STATUS.INITIALIZED);
                    o = get(i).process((Input) o); // --> this might throw a YaqpException
                    Output o1 = (Output) o;
                    getStatus().increment(STATUS.PROCESSED);
                    getStatus().incrementElapsedTime(STATUS.PROCESSED, System.currentTimeMillis() - start_time);
                    firePropertyChange(PROPERTY_PIPELINE_STATUS, null, getStatus());
                }
            } catch (Exception ex) {
                String message = "Processor " + i + " is in error state :: " + ex;
                if (isfailSensitive()) {
                    YaqpLogger.LOG.log(new Debug(getClass(), message));
                    throw new ProcessorException("XCI108", message, ex);
                }
                getStatus().increment(STATUS.ERROR);
                getStatus().incrementElapsedTime(STATUS.ERROR, System.currentTimeMillis() - start_time);
                YaqpLogger.LOG.log(new Trace(getClass(), message + " :: " + ex));
            }
        }
        getStatus().setMessage("Pipeline completed the job.");
        getStatus().completed();
        firePropertyChange(PROPERTY_PIPELINE_STATUS, null, getStatus());
        // Try to cast the result as 'Output'
        try {
            return (Output) o;
        } catch (Exception ex) {
            String message = "pipeline output typecasting error";
            YaqpLogger.LOG.log(new ScrewedUp(getClass(), message));
            throw new YaqpException("XBR", message, ex);
        }
    }
}
