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


import java.net.URI;
import java.net.URISyntaxException;
import org.opentox.core.exceptions.Cause;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.io.publishable.JSONObject;
import org.opentox.io.publishable.PDFObject;
import org.opentox.io.publishable.RDFObject;
import org.opentox.io.publishable.TurtleObject;
import org.opentox.ontology.namespaces.OTClass;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Task extends YaqpComponent {

    @Override
    protected String getTag() {
        return "task";
    }

    @Override
    public URI uri() throws YaqpException {
        String superUri = super.uri().toString();
        try{
        return new URI(superUri+"/"+getName());
        } catch (URISyntaxException ex){
            throw new YaqpException(Cause.XTC743, "Improper URI", ex);
        }
    }



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

    private String name;
    private STATUS taskStatus;
    private String result, startStamp, endStamp;
    private int id, httpStatus;
    private User user;
    private Algorithm algorithm;
    private int duration_sec;

    public Task() {
        setStatus(STATUS.RUNNING);
        httpStatus = 202;
    }

    public Task(int id, String name, STATUS taskStatus, User user, Algorithm algorithm, int httpStatus,
            String result, String startStamp, String endStamp, int duration_sec) {
        this.id = id;
        this.name = name;
        this.taskStatus = taskStatus;
        this.user = user;
        this.algorithm = algorithm;
        this.httpStatus = httpStatus;
        this.result = result;
        this.startStamp = startStamp;
        this.endStamp = endStamp;
    }

    public Task(String name, User user, Algorithm algorithm, int duration_sec) {
        this.name = name;
        this.user = user;
        this.algorithm = algorithm;
        this.httpStatus = 202;
        this.duration_sec = duration_sec;
        setStatus(STATUS.RUNNING);
    }

    /**
     * The algorithm running on the background and generated this task.
     * @return An Algorithm.
     */
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

    /**
     * The result from a completed task. If the status of the task is yet
     * <code>'RUNNING'</code> the result is null, while if the task status is
     * <code>'COMPLETED'</code> the result is what should be returned to the client.
     * Normally this field is a <code>URI</code> of a created resource (e.g. a
     * new model), but in case of error this becomes an error message.
     * @return Result to be returned to the client.
     */
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

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(STATUS status) {
        this.taskStatus = status;
    }


    @Override
    public PDFObject getPDF() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RDFObject getRDF() {
        RDFObject rdf = new RDFObject();
        rdf.includeOntClass(OTClass.Task);

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

    public int getDuration_sec() {
        return duration_sec;
    }

    public void setDuration_sec(int duration_sec) {
        this.duration_sec = duration_sec;
    }



    @Override
    public String toString() {
        String task = "-- Task --\n";
        task += "ID                : " + getId() + "\n";
        task += "NAME              : " + getName() + "\n";
        task += "STATUS            : " + getStatus() + "\n";
        task += "USER              : " + getUser().getEmail() + "\n";
        task += "ALGORITHM         : " + getAlgorithm().getMeta().getName() + "\n";
        task += "HTTP STATUS       : " + getHttpStatus() + "\n";
        task += "RESULT            : " + getResult() + "\n";
        task += "START TIME        : " + getStartStamp() + "\n";
        task += "END TIME          : " + getEndStamp();
        return task;
    }
}
