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
package org.opentox.db.entities;

import java.io.Serializable;

/**
 *
 * A Task is running operation on the server triggered by some client request (over
 * HTTP). Every Task has a URI and a status
 * @author Sopasakis Pantelis
 * @author Charalampos Chomenides
 */
public class Task implements Serializable {

    /**
     * The possible statuses a task can have.
     */
    public static enum STATUS {
        /**
         * The task is still running. Waiting for completion.
         */
        RUNNING,
        /**
         * The task has completed successfully.
         */
        COMPLETED,
        /**
         * The task was cancelled.
         */
        CANCELLED
    };

    private String _name, _uri, _algorithm, _user;
    private STATUS _status;

    public Task(){
        setStatus(STATUS.RUNNING);
    }

    public String getAlgorithm() {
        return _algorithm;
    }

    public String getName() {
        return _name;
    }

    public STATUS getStatus() {
        return _status;
    }

    public String getUri() {
        return _uri;
    }

    public String getUser() {
        return _user;
    }

    public void setAlgorithm(String _algorithm) {
        this._algorithm = _algorithm;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public void setStatus(STATUS _status) {
        this._status = _status;
    }

    public void setUri(String _uri) {
        this._uri = _uri;
    }


    /**
     * Set the user that created the task.
     * @param _user the username of the user who created the task.
     */
    public void setUser(String _user) {
        this._user = _user;
    }

    @Override
    public String toString() {
        return super.toString();
    }
    



    

}
