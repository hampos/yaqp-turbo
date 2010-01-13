package org.opentox.db.exceptions;

import org.opentox.core.exceptions.YaqpException;

/**
 *
 * @author chung
 */
public class DbException extends YaqpException {

    /**
     * Creates a new instance of <code>DbException</code> without detail message.
     */
    public DbException() {
    }


    /**
     * Constructs an instance of <code>DbException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public DbException(String msg) {
        super(msg);
    }
}
