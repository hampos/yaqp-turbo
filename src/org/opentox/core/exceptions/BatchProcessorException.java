package org.opentox.core.exceptions;

/**
 *
 * @author chung
 */
public class BatchProcessorException extends ProcessorException {

    /**
     * Creates a new instance of <code>BatchProcessorException</code> without detail message.
     */
    public BatchProcessorException() {
    }


    /**
     * Constructs an instance of <code>BatchProcessorException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public BatchProcessorException(String msg) {
        super(msg);
    }
}
