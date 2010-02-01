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
package org.opentox.core.interfaces;

import org.opentox.core.processors.ParallelProcessor;


/**
 * This interface is used to manage the processes which are being executed in
 * a multi-processing device such as a {@link ParallelProcessor ParallelProcessor }
 * or a {@link JMultiProcessor Multi-Processor } in general. It is used as a tool to
 * supervise the execution in a pipelin keeping track of the status of the execution.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
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
