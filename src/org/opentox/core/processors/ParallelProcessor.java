package org.opentox.core.processors;

import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;
import org.opentox.core.exceptions.YaqpException;




/**
 * A Parallel Processor is a collection of processors that run in parallel. Use this
 * processor in cases where no synchronization is needed.
 * @param <I> Common Input Type - might be Object
 * @param <O> Common Output Type - might be Object
 * @author chung
 */
public class ParallelProcessor<I, O> extends Processor<ArrayList<I>, ArrayList<O>> {

    private ThreadPoolExecutor parallel_executor;
    
    public ParallelProcessor(){
        
    }

    public ArrayList<O> process(ArrayList<I> data) throws YaqpException {

        throw new UnsupportedOperationException("Not supported yet.");
    }




    

}
