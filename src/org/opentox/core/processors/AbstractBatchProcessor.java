package org.opentox.core.processors;

import java.util.ArrayList;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.interfaces.JBatchProcessor;
import org.opentox.core.interfaces.JProcessor;
import org.opentox.core.util.MultiProcessorStatus;

/**
 *
 * @author chung
 */
public abstract class AbstractBatchProcessor<Input, Output, P extends JProcessor<Input, Output>>
        extends AbstractTurboProcessor<ArrayList<Input>, ArrayList<Output>>
        implements JBatchProcessor<Input, Output, P> {

    private P processor;
    private boolean failSensitive = false;
    private MultiProcessorStatus status = new MultiProcessorStatus();

    public AbstractBatchProcessor() {
    }

    public AbstractBatchProcessor(final P processor) {
        this();
        this.processor = processor;
    }

    public abstract ArrayList<Output> process(ArrayList<Input> data) throws YaqpException;

    
    public MultiProcessorStatus getStatus() {
        return status;
    }

    public boolean isfailSensitive() {
        return failSensitive;
    }

    public void setfailSensitive(boolean failSensitive) {
        this.failSensitive = failSensitive;
    }

    public void setProcessor(final P processor) {
        this.processor = processor;
    }

    public P getProcessor() {
        return processor;
    }

   


}
