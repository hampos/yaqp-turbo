package org.opentox.core.processors;

import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.interfaces.JMultiProcessorStatus.STATUS;
import org.opentox.core.interfaces.JProcessor;
import org.opentox.util.logging.levels.Debug;
import org.opentox.util.logging.levels.Trace;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.ScrewedUp;

/**
 * A set of jobs to be executed through a pipeline of processors
 * is a sequential layout if some processor is enabled. Note that if P1, P2, ..., PN
 * is a sequence of processors building a pipeline, then the output type from a processor
 * P_k should be a valid input to P_k+1 otherwise the processor P_k+1 will throw an
 * exception.
 * @param  <Input> Type for the input to the pipeline
 * @param  <Output> Type for the output of the pipeline
 * @param  <P> Type of processors contained in the pipeline
 * @author Sopasakis Pantelis
 * @author Charalampos Chomenides
 */
public class Pipeline<Input, Output, P extends JProcessor<Input, Output>>
        extends AbstractMultiProcessor<Input, Output, P>{

            

    private String PROPERTY_PIPELINE_STATUS = getStatus().getClass().getName();

    /**
     * Constructs a new Pipeline which is an ordered sequence of Processors
     * that are used to process input data sequentially (if enabled)
     */
    public Pipeline() {
        super();
        setfailSensitive(true);
    }

    

   
    
    /**
     *
     * TODO: Important! Do some tests...
     *
     * This method processes the input data to produce some output,
     * using the sequence of processors. The result from the first processor,
     * is input to the second and so on. Any non-enabled processors are overriden.
     * @param data
     * @return Pipeline Output
     * @throws YaqpException
     */
    public Output process(Input data) throws YaqpException {

        long start_time = 0;
        getStatus().setMessage("pipeline in process");
        Object o = data;
        // Process:
        for (int i = 0; i < size(); i++) {
            try {
                if (get(i).isEnabled()) {
                    start_time = System.currentTimeMillis();
                    getStatus().increment(STATUS.INITIALIZED);
                    o = get(i).process((Input)o); // --> this might throw a YaqpException
                    Output o1 = (Output) o;
                    getStatus().increment(STATUS.PROCESSED);
                    getStatus().incrementElapsedTime(STATUS.PROCESSED, System.currentTimeMillis() - start_time);
                    firePropertyChange(PROPERTY_PIPELINE_STATUS, null, getStatus());
                }
            } catch (Exception exc) {
                if (isfailSensitive()) {
                    YaqpLogger.INSTANCE.log(new Debug(Pipeline.class,
                            "Processor " + i + " is in error state!"));
                    throw new YaqpException();
                }
                getStatus().increment(STATUS.ERROR);
                getStatus().incrementElapsedTime(STATUS.ERROR, System.currentTimeMillis() - start_time);
                YaqpLogger.INSTANCE.log(new Trace(Pipeline.class,
                        "Processor " + i + " is in error state!"));
            }
        }
        getStatus().setMessage("Pipeline completed the job.");
        getStatus().completed();
        firePropertyChange(PROPERTY_PIPELINE_STATUS, null, getStatus());
        // Try to cast the result as 'Output'
        try {
            return (Output) o;
        } catch (Exception exc) {
            YaqpLogger.INSTANCE.log(new ScrewedUp( Pipeline.class,
                    YaqpException.CAUSE.pipeline_output_typecasting.toString()));
            throw new YaqpException(YaqpException.CAUSE.pipeline_output_typecasting);
        }
    }



   
   
}
