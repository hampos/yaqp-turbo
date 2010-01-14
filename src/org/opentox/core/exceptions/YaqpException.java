package org.opentox.core.exceptions;

/**
 *
 * @author chung
 */
public class YaqpException extends Exception {

    private int id;
    private CAUSE cause;
    private String message;

    /**
     * Standard causes of error
     */
    public static enum CAUSE {

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
        processor_interruption("A processor was brutally interrupted while performing an operation");

        /**
         * Explanatory message for the cause of the exception
         */
        private String message;

        /**
         * private constructor for the enumeration
         * @param explanation explanatory message
         */
        private CAUSE(String explanation) {
            this.message = explanation;
        }

        ;

        @Override
        public String toString() {
            return message;
        }
    }

    /**
     * Creates a new instance of <code>YaqpException</code> without detail message.
     */
    public YaqpException() {
        super();
    }

    public YaqpException(CAUSE cause) {
        super(cause.toString());
    }

    public YaqpException(CAUSE cause, String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>YaqpException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public YaqpException(String msg) {
        super(msg);
    }
    
    public YaqpException(Throwable throwable){
        super(throwable);
    }

    public YaqpException(String message, Throwable throwable){
        super(message, throwable);
    }


}
