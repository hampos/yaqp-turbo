package org.opentox.core.util;

import java.util.Observable;
import org.opentox.core.interfaces.JPipelineStatus;

/**
 *
 * @author chung
 */
public final class PipelineStatus
        extends Observable
        implements JPipelineStatus {    

    private boolean inProgress = true;
    private long[] records;
    private long[] time_elapsed;
    private String message = "";

    public PipelineStatus() {
        super();
        records = new long[STATUS.getSize()];
        time_elapsed = new long[STATUS.getSize()];
        clear();
    }

    public long getNumProcessors(STATUS status) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setNumberOfProcessors(STATUS status, long numberOfProcessors) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void increment(STATUS status) {
        records[status.ordinal()]++;
        setChanged();
        notifyObservers();
    }

    public long getElapsedTime(STATUS status) {
        return time_elapsed[status.ordinal()];
    }

    public void setElapsedTime(STATUS status, long elapsedTime) {
        time_elapsed[status.ordinal()] = elapsedTime;
    }

    public void incrementElapsedTime(STATUS status, long timeIncrement) {
        time_elapsed[status.ordinal()] += timeIncrement;
        setChanged();
        notifyObservers();
    }

    public void clear() {
        for (int i = 0; i < records.length; i++) {
            records[i] = 0;
            time_elapsed[i] = 0;
        }
        inProgress = true;
        setChanged();
        notifyObservers();
    }

    public void completed() {
        inProgress = false;
        setChanged();
        notifyObservers();
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        for (STATUS status : STATUS.values()){
            builder.append(status+"\t");
        }
        // TODO: finish the implementation of this method.
        return null;
    }

    public boolean isInProgress() {
        return inProgress;
    }
}
