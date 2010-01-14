package org.opentox.core.processors;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.interfaces.JBatchProcessor;
import org.opentox.core.interfaces.JMultiProcessorStatus;
import org.opentox.core.interfaces.JProcessor;

/**
 * <p>
 * Phase: Planning...
 * <p>
 * <p>
 * The scope of this processor is to use a single processor in a multi-threaded
 * approach. Specifically, if you need to use a specific processor to perform a set
 * or processes, that is you want your processor to do a (considerable) number of mutually
 * independent jobs simultaneously, you can use a BatchProcessor. A BatchProcessor is a
 * processor it self, exploiting some other processor which is the one that does the
 * real job. A Batch Processor guarrantees that a set of jobs will run in parallel.
 * Provide the set of inputs to the BatchProcecssor and manipulate it as a single
 * {@link JProcessor Processor}.
 * </p>
 * <p>
 * A BatchProcessor is an extension of the default Yaqp {@link Processor }, hence
 * enabled by default. Note that this class is abstract, so it has to be subclassed
 * to be used for batch processing.
 * </p>
 *
 * @author chung
 * @param <Input> Input to the processor
 * @param <Output> Output from the processor
 * @param <P> Type of internal processor
 */
public abstract class BatchProcessor<Input, Output, P extends JProcessor<Input, Output>>
        extends Processor<ArrayList<Input>, ArrayList<Output>>
        implements JBatchProcessor<Input, Output>{

    /**
     * Internal processor
     */
    private JProcessor<Input, Output> processor;

    private JMultiProcessorStatus status;

    
    private BatchProcessor(){
        super();
    }

    public BatchProcessor(final P processor) {
        this();
        this.processor = processor;
    }


    /**
     *
     * @return
     */
    public abstract Iterator<Input> iterator();

    /**
     * TODO: More Constructors have to be created.
     */

    /**
     * TODO: Initial Implementation of Process
     * TODO: Consider Not providing input data as an ArrayList but throught an iterator
     * @param data
     * @return
     * @throws YaqpException
     */
    public ArrayList<Output> process(ArrayList<Input> data) throws YaqpException {        
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public JMultiProcessorStatus getStatus() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isfailSensitive() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setfailSensitive(boolean failSensitive) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
