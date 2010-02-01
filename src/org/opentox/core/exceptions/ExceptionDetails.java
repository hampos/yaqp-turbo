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
package org.opentox.core.exceptions;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public enum ExceptionDetails {

    /**
         * Could not load the standard properties hence could not use the standard
        + "logger - Using the console instead!
         */
        could_not_load_properties("Could not load the standard properties hence could not use the standard "
        + "logger - Using the console instead!"),
        /**
         * The output of the pipeline cannot be cast as the type you specified
         */
        pipeline_output_typecasting("The output of the pipeline cannot be cast as the type you specified - "
        + "Consider using the type 'Object' instead."),
        /**
         * The operation timed out.
         */
        time_out_exception("The operation timed out."),
        /**
         * No explanation for this error - this should not happen!
         */
        unknown_cause("No explanation for this error - this should not happen!"),
        /**
         * A processor was brutally interrupted while performing an operation
         */
        processor_interruption("A processor was brutally interrupted while performing an operation"),
        /**
         * Input to a parallel processor cannot be null!
         */
        null_input_to_parallel_processor("Input to a parallel processor cannot be null!"),
        /**
         * There were no processors found in this bundle of processors
         */
        no_processors_found("No processors found");

         /**
         * Explanatory message for the cause of the exception
         */
        private String message;

        /**
         * private constructor for the enumeration
         * @param explanation explanatory message
         */
        private ExceptionDetails(String explanation) {
            this.message = explanation;
        }

        @Override
        public String toString() {
            return message;
        }

}
