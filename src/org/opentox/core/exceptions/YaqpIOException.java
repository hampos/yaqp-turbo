package org.opentox.core.exceptions;

/**
 *
 * @author chung
 */
public class YaqpIOException extends YaqpException {

    /**
     * Creates a new instance of <code>YaqpIOException</code> without detail message.
     */
    public YaqpIOException() {
    }


    /**
     * Constructs an instance of <code>YaqpIOException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public YaqpIOException(String msg) {
        super(msg);
    }
}
