package org.opentox.core.interfaces;

import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.processors.Pipeline;

/**
 * This is a Batch Processor, i.e. a supervisor for a pipeline, used to assure that
 * the pipeline is working efficiently and enables us to overload it to increase its
 * productivity.
 * @author Sopasakis Pantelis
 *
 * TODO: Design this class in an abstract way.
 */
public interface JBatchProcessor<Input, Result> {


    Pipeline<Input, Result, JProcessor> getPipeline();

    /**
     * Define the pipeline (list of processors) to be handled by the BatchProcessor.
     * @param pipeline
     */
    void setPipeline(Pipeline<Input, Result, JProcessor> pipeline);

    /**
     * The timeout for the whole batch. If the elapsed time surpasses the 
     * timeout, the batch processor is cancelled.
     * @return
     */
    long getTimeout();

    /**
     * Set the timeout of the Batch Processor. If the Batch execution time
     * exceeds the timeout value, then the whole process is cancelled.
     * @param timeout
     */
    void setTimeout(long timeout);

    void cancel();

    Result getResult(Input input) throws YaqpException;
}
