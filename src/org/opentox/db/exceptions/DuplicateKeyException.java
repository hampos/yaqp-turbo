/*
 * YAQP - Yet Another QSAR Project: Machine Learning algorithms designed for
 * the prediction of toxicological features of chemical compounds become
 * available on the Web. Yaqp is developed under OpenTox (http://opentox.org)
 * which is an FP7-funded EU research project.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
