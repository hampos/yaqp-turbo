package org.opentox.db.exceptions;

/**
 *
 * This exception is thrown when one tries to add a duplicate key in the 
 * database.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DuplicateKeyException extends DbException {

    /**
     * Creates a new instance of <code>DuplicateKeyException</code> without detail message.
     */
    public DuplicateKeyException() {
    }


    /**
     * Constructs an instance of <code>DuplicateKeyException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public DuplicateKeyException(String msg) {
        super(msg);
    }

    public DuplicateKeyException(String msg, Throwable throwable){
        super(msg, throwable);
    }

    public DuplicateKeyException(Throwable throwable){
        super(throwable);
    }
}
