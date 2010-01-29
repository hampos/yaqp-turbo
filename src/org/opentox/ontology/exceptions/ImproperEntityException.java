package org.opentox.ontology.exceptions;

/**
 *
 * @author chung
 */
public class ImproperEntityException extends YaqpOntException {

    /**
     * Creates a new instance of <code>ImproperEntityException</code> without detail message.
     */
    public ImproperEntityException() {
    }


    /**
     * Constructs an instance of <code>ImproperEntityException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ImproperEntityException(String msg) {
        super(msg);
    }
}
