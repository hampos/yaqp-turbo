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
