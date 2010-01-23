package org.opentox.core.exceptions;

/**
 *
 * @author chung
 */
public class YaqpException extends Exception {

    private int id;
    private ExceptionDetails ExceptionDetails;
    private String message;




    /**
     * Creates a new instance of <code>YaqpException</code> without detail message.
     */
    public YaqpException() {
        super();
    }

    public YaqpException(ExceptionDetails exceptionDetails) {
        super(exceptionDetails.toString());
    }

    public YaqpException(ExceptionDetails exceptionDetails, String msg) {
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
