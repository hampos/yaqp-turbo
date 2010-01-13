/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.opentox.core.exceptions;

/**
 *
 * @author chung
 */
public class YaqpException extends Exception {

    /**
     * Creates a new instance of <code>YaqpException</code> without detail message.
     */
    public YaqpException() {
    }


    /**
     * Constructs an instance of <code>YaqpException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public YaqpException(String msg) {
        super(msg);
    }
}
