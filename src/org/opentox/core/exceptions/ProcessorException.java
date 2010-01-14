package org.opentox.core.exceptions;

import org.opentox.core.interfaces.JProcessor;

/**
 *
 * @author Sopasakis Pantelis
 */
public class ProcessorException extends YaqpException {


    /**
     * Creates a new instance of <code>ProcessorException</code> without detail message.
     */
    public ProcessorException() {
    }

    public ProcessorException(JProcessor processor, Throwable throwable){
        
    }


    /**
     * Constructs an instance of <code>ProcessorException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ProcessorException(String msg) {
        super(msg);
    }
}
