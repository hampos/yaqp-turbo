package org.opentox.ontology.exceptions;

import org.opentox.core.exceptions.YaqpException;

/**
 *
 * Ontological Type exception: improper RDF representation in input, improper
 * ontological entity or other ontological nature exceptions.
 * @author Sopasakis Pantelis
 * @author Charalampos Chomenides
 */
public class YaqpOntException extends YaqpException {

    /**
     * Creates a new instance of <code>YaqpOntException</code> without detail message.
     */
    public YaqpOntException() {
    }


    /**
     * Constructs an instance of <code>YaqpOntException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public YaqpOntException(String msg) {
        super(msg);
    }

    public YaqpOntException(Throwable throwable){
        super(throwable);
    }

    public YaqpOntException(String message, Throwable throwable){
        super(message, throwable);
    }
}
