package org.opentox.core.interfaces;

import org.opentox.core.exceptions.YaqpException;

/**
 * A Processor is any class having the ability to process some input
 * and produce some output provided that it is enabled.
 * @author chung
 */
public interface JProcessor<InputData, Result> {

    /**
     * Processes some input data to produce some output.
     * @param data Input data.
     * @return The output of the processor
     * @throws YaqpException Exception is thrown if the processor is unable
     * to produce a result for the given data.
     */
    Result process(InputData data) throws YaqpException;

    /**
     *
     * @return true if the processor is enabled, false otherwise.
     */
    boolean isEnabled();

    /**
     * A Processor will process the input only if it is enabled. Using
     * this method you can enable/disable the processor
     * @param enabled
     */
    void setEnabled(boolean enabled);

}
