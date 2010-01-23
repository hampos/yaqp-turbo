package org.opentox.core.exceptions;

/**
 *
 * @author chung
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
