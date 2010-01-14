package org.opentox.core.util;

import java.util.Observable;
import org.opentox.core.interfaces.JMultiProcessorStatus;

/**
 *
 * @author chung
 */
public final class MultiProcessorStatus
        extends Observable
        implements JMultiProcessorStatus {

    private boolean inProgress = true;
    private long[] records;
    private long[] time_elapsed;
    private String message = "";

    public MultiProcessorStatus() {
        super();
        records = new long[STATUS.getSize()];
        time_elapsed = new long[STATUS.getSize()];
        clear();
    }

    public long getNumProcessors(STATUS status) {
        return records[status.ordinal()];
    }

    public void setNumberOfProcessors(STATUS status, long numberOfProcessors) {
        records[status.ordinal()] = numberOfProcessors;
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
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("* Elapsed Time Report *\n");
        // builder.append("Initialization Time    : " + getElapsedTime(STATUS.INITIALIZED) + "ms\n");
        builder.append("Processing Time        : " + getElapsedTime(STATUS.PROCESSED) + "ms\n");
        builder.append("Error Time             : " + getElapsedTime(STATUS.ERROR) + "ms\n\n");
        builder.append("* Statistics *\n");
        builder.append("Number of Initialized Processors  : " + getNumProcessors(STATUS.INITIALIZED)+"\n");
        builder.append("Number of Succesful Processors    : " + getNumProcessors(STATUS.PROCESSED)+"\n");
        builder.append("Number of Failed Processors       : " + getNumProcessors(STATUS.ERROR)+"\n");

        return builder.toString();
    }

    public boolean isInProgress() {
        return inProgress;
    }
}
