package org.opentox.core.interfaces;

import org.opentox.core.processors.ParallelProcessor;


/**
 * This interface is used to manage the processes which are being executed in
 * a multi-processing device such as a {@link ParallelProcessor ParallelProcessor }
 * or a {@link JMultiProcessor Multi-Processor } in general. It is used as a tool to
 * supervise the execution in a pipelin keeping track of the status of the execution.
 * @author chung
 */
public interface JMultiProcessorStatus {

    /**
     * Available states for a processor including
     * {@link STATUS#INITIALIZED }, {@link STATUS#ERROR } and
     * {@link STATUS#PROCESSED }.
     *
     */
    public static enum STATUS {


        INITIALIZED {

            @Override
            public String toString() {
                return "initialized";
            }
        },
        PROCESSED {

            @Override
            public String toString() {
                return "processed";
            }
        },
        ERROR {

            @Override
            public String toString() {
                return "error";
            }
        };

        private static int size;

        /**
         * The size of the enumeration
         * @return size of STATUS
         */
        public static int getSize(){
            size = STATUS.values().length;
            return size;
        };
    };

    /**
     * Returns the number of processors of a given status
     * @param status A given status for the processors.
     * @return The number of processors having a certain state.
     */
    long getNumProcessors(STATUS status);

    /**
     * Updates the number of processors having a certain statuc
     * @param status Status for the processor
     * @param numberOfProcessors Number of processors having a certain status.
     */
    void setNumberOfProcessors(STATUS status, long numberOfProcessors);

    /**
     * Increments the number of processors having a certain status.
     * @param status Status of processors to be incremented.
     */
    void increment(STATUS status);

    /**
     * Get the elapsed for the processors of a certain type.
     * @param status Processor Status.
     * @return Elapsed Time for certain status.
     */
    long getElapsedTime(STATUS status);

    /**
     * Updates the elapsed time for a certain status.
     * @param status
     * @param elapsedTime
     */
    void setElapsedTime(STATUS status, long elapsedTime);

    /**
     * Increments the elapsed time for a certain processor status according to
     * a given increment value.
     * @param status Processor's status.
     * @param timeIncrement increment value.
     */
    void incrementElapsedTime(STATUS status, long timeIncrement );

    /**
     * Clears the JMultiProcessorStatus object.
     */
    void clear();

    /**
     * Invoke this method, once the Batch is completed.
     */
    void completed();

    /**
     * Returns a characteristic message from the JMultiProcessorStatus
     * objects.
     * @return message.
     */
    String getMessage();

    /**
     * Updates the message holded by JMultiProcessorStatus.
     * @param message
     */
    void setMessage(String message);

    /**
     *
     * @return true if still in progress
     */
    boolean isInProgress();
}
