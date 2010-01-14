package org.opentox.core.exceptions;

/**
 *
 * @author chung
 */
public class YaqpException extends Exception {

    private int id;

    private CAUSE cause;

    private String message;

    public static enum CAUSE{


        could_not_load_properties("Could not load the standard properties hence could not use the standard " +
                        "logger - Using the console instead!"),

        pipeline_output_typecasting("The output of the pipeline cannot be cast as the type you specified - " +
                        "Consider using the type 'Object' instead."),
        time_out_exception("The operation timed out.")
                        ;



        private String message;

        private CAUSE(String explanation){
            this.message = explanation;
        };

        @Override
        public String toString(){
            return message;
        }

    }

    /**
     * Creates a new instance of <code>YaqpException</code> without detail message.
     */
    public YaqpException() {
        super();
    }
    
    public YaqpException(CAUSE cause){
      super(cause.toString());
    }

    public YaqpException(CAUSE cause, String msg){
        super(msg);
    }


    /**
     * Constructs an instance of <code>YaqpException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public YaqpException(String msg) {
        super(msg);
    }
}
