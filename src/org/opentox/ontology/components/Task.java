/*
 *
 * YAQP - Yet Another QSAR Project:
 * Machine Learning algorithms designed for the prediction of toxicological
 * features of chemical compounds become available on the Web. Yaqp is developed
 * under OpenTox (http://opentox.org) which is an FP7-funded EU research project.
 * This project was developed at the Automatic Control Lab in the Chemical Engineering
 * School of National Technical University of Athens. Please read README for more
 * information.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
 *
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
 * Contact:
 * Pantelis Sopasakis
 * chvng@mail.ntua.gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 */
package org.opentox.ontology.components;

import org.opentox.io.publishable.JSONObject;
import org.opentox.io.publishable.OntObject;
import org.opentox.io.publishable.PDFObject;
import org.opentox.io.publishable.RDFObject;
import org.opentox.io.publishable.TurtleObject;
import org.restlet.data.Status;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Task extends YaqpComponent {

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
//
//    public static enum TYPE {
//
//        TRAINING,
//        PREDICTION,
//    };
    private String name, uri;
    private STATUS taskStatus;
    private String result, startStamp, endStamp;
    private int id, httpStatus;
    private User user;
    private Algorithm algorithm;

    public Task() {
        setStatus(STATUS.RUNNING);
        httpStatus = 202;
    }

    public Task(int id, String name, String uri, STATUS taskStatus, User user, Algorithm algorithm, int httpStatus,
            String result, String startStamp, String endStamp) {
        this.id = id;
        this.name = name;
        this.uri = uri;
        this.taskStatus = taskStatus;
        this.user = user;
        this.algorithm = algorithm;
        this.httpStatus = httpStatus;
        this.result = result;
        this.startStamp = startStamp;
        this.endStamp = endStamp;
    }

    public Task(String name, String uri, User user, Algorithm algorithm){
        this.name = name;
        this.uri = uri;
        this.user = user;
        this.algorithm = algorithm;
        this.httpStatus = 202;
        setStatus(STATUS.RUNNING);
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public String getEndStamp() {
        return endStamp;
    }

    public void setEndStamp(String endStamp) {
        this.endStamp = endStamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartStamp() {
        return startStamp;
    }

    public void setStartStamp(String startStamp) {
        this.startStamp = startStamp;
    }

    public STATUS getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(STATUS taskStatus) {
        this.taskStatus = taskStatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getResult() {
        return result;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public STATUS getStatus() {
        return taskStatus;
    }

    public String getUri() {
        return uri;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(STATUS status) {
        this.taskStatus = status;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public PDFObject getPDF() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RDFObject getRDF() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TurtleObject getTurtle() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public JSONObject getJson() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        String task = "-- Task --\n";
        task += "ID                : "+getId()+"\n";
        task += "NAME              : "+getName()+"\n";
        task += "URI               : "+getUri()+"\n";
        task += "STATUS            : "+getStatus()+"\n";
        task += "USER              : "+getUser().getEmail()+"\n";
        task += "ALGORITHM         : "+getAlgorithm().getMeta().name+"\n";
        task += "HTTP STATUS       : "+getHttpStatus()+"\n";
        task += "RESULT            : "+getResult()+"\n";
        task += "START TIME        : "+getStartStamp()+"\n";
        task += "END TIME          : "+getEndStamp();
        return task;
    }
}
