package org.opentox.core.processors;

import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.interfaces.JBatchProcessor;
import org.opentox.core.interfaces.JBatchStatus;
import org.opentox.core.interfaces.JProcessor;

/**
 * TODO: Implement all methods - look at JBatchProcessor
 * @author chung
 */
public class BatchProcessor<Input, Output, J extends JBatchStatus>
        extends Processor<Input,J>
        implements JBatchProcessor<Input, Output>
    {

    public J process(Input data) throws YaqpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Pipeline<Input, Output, JProcessor> getPipeline() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setPipeline(Pipeline<Input, Output, JProcessor> pipeline) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long getTimeout() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setTimeout(long timeout) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void cancel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Output getResult(Input input) throws YaqpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
