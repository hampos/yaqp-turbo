package org.opentox.db.exceptions;

/**
 * An exception thrown in case an email address is not valid.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class BadEmailException extends DbException {

    /**
     * Creates a new instance of <code>BadEmailException</code> without detail message.
     */
    public BadEmailException() {
    }


    /**
     * Constructs an instance of <code>BadEmailException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public BadEmailException(String msg) {
        super(msg);
    }
}
