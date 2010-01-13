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
        could_not_load_properties{
            @Override
            public String toString(){
                return "Could not load the standard properties hence could not use the standard " +
                        "logger - Using the console instead!";
            }

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
