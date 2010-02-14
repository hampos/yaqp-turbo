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
package org.opentox.core.processors;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.opentox.core.exceptions.ProcessorException;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.interfaces.JProcessor;
import static org.opentox.core.util.MultiProcessorStatus.STATUS;
import static org.opentox.core.exceptions.Cause.*;

/**
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
 * enabled by default. There are three ways to build your custom BatchProcessor: You
 * can implement the interface {@link BatchProcessor }, extends the abstract class
 * {@link AbstractBatchProcessor } or do both. The corresponding prototypes are:
 * <ul>
 * <li>class MyBatchProcessor&lt;I,O,P extends JProcessor&lt;I,O&gt;&gt;
 * implements JBatchProcessor&lt;I,O,P&lt;I,O&gt;&gt;</li>
 * <li>class MyBatchProcessor&lt;I,O,P extends JProcessor&lt;I,O&gt;&gt;
 * extends AbstractProcessor&lt;I,O,P&lt;I,O&gt;&gt;</li>
 * </ul>
 * </p>
 *
 * @author Sopasakis Pantelis
 * @author Charalampos Chomenides
 * @param <Input> Input to the processor
 * @param <Output> Output from the processor
 * @param <P> Type of internal processor
 */
@SuppressWarnings({"unchecked"})
public class BatchProcessor<Input, Output, P extends JProcessor<Input, Output>>
           extends AbstractBatchProcessor<Input, Output, P> {

    private String PROPERTY = getStatus().getClass().getName();
    private int corePoolSize = 4, maxPoolSize = 4;
    private int timeout = 1;
    private TimeUnit timeUnit = TimeUnit.HOURS;
    private ThreadPoolExecutor parallel_executor;

    private BatchProcessor() {
        super();
    }

    public BatchProcessor(final P processor) {
        this();
        setProcessor(processor);
    }

    public BatchProcessor(final P processor, final int corePoolSize, final int maxPoolSize) {
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        setProcessor(processor);
    }

    public void setTimeOut(final int timeout, final TimeUnit timeout_unit) {
        this.timeout = timeout;
        this.timeUnit = timeout_unit;
    }

    /**
     * @param data A list of data to be processed by the BatchProcessor.
     * @return Batch Processor Output
     * @throws YaqpException In case something goes wrong while processing
     * input data.
     */
    public ArrayList<Output> process(ArrayList<Input> data) throws YaqpException {

        if (data == null) {
            throw new NullPointerException("Null input to batch processor");
        }


        /**
         * If the internal processor is synchronized, run all processes in
         * a single query.
         */
        if (getProcessor().isSynchronized()) {
            corePoolSize = 1;
            maxPoolSize = 1;
        }

        // A PROCESSOR WHICH ALWAYS RETURNS 'null'.

        Processor n = new Processor() {

            public Object process(Object data) throws YaqpException {
                return null;
            }
        };

        P nullProcessor = (P) n;

        final ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(data.size() + 2);
        parallel_executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 1, TimeUnit.MINUTES, queue);
        getStatus().setMessage("processing in progress");
        long start_time = 0;

        Future[] future_array = new Future[data.size()];
        ArrayList<Output> result = new ArrayList<Output>();
        start_time = System.currentTimeMillis();

        for (int i = 0; i < data.size(); i++) {
            getStatus().increment(STATUS.INITIALIZED);
            if (getProcessor().isEnabled()) {
                future_array[i] = parallel_executor.submit(getCallable(getProcessor(), data.get(i)));
            } else {
                future_array[i] = parallel_executor.submit(getCallable(nullProcessor, null));
            }
        }
        parallel_executor.shutdown();

        /**
         * Await for the processors to terminate, but no more than a fixed timeout.
         */
        try {
            parallel_executor.awaitTermination(timeout, timeUnit);
            handleTimeOut();
        } catch (InterruptedException ex) {
            getStatus().setMessage("interrupted - not running");
            getStatus().completed();
            firePropertyChange(PROPERTY, null, getStatus());
            throw new ProcessorException(XBP1, "A processor was brutally interrupted while performing an operation", ex);
        }

        for (int i = 0; i < data.size(); i++) {
            long error_start_time = 0;

            try {
                // Successful processing...
                error_start_time = System.currentTimeMillis();
                if (future_array[i].isDone()) {
                    try {
                        result.add((Output) future_array[i].get());
                    } catch (ClassCastException ex) {
                        if (isfailSensitive()) {
                            throw new ClassCastException("Class Cast is imposible for output of batch processor");
                        }
                    }
                    getStatus().increment(STATUS.PROCESSED);
                } else {
                    result.add((Output) null);
                    getStatus().increment(STATUS.ERROR);
                }

                firePropertyChange(PROPERTY, null, getStatus());
            } catch (InterruptedException ex) {
                getStatus().setMessage("completed unexpectedly");
                getStatus().completed();
                firePropertyChange(PROPERTY, null, getStatus());
                throw new ProcessorException(XBP2, "Unknown cause of exception (Interruption)");
            } catch (ExecutionException ex) {
                /**
                 * If a processor error happens and the Parallel Processor is fail
                 * sensitive it should throw an exception and terminate.
                 */
                if (isfailSensitive()) {
                    parallel_executor.shutdownNow(); // force shut down.
                    getStatus().setMessage("completed unsuccessfully");
                    getStatus().completed();
                    firePropertyChange(PROPERTY, null, getStatus());
                    throw new ProcessorException(XBP3, "The batch Processor is fail-sensitive", ex);
                }

                result.add(null); // If the processor fails, the result should be null.
                getStatus().increment(STATUS.ERROR);
                getStatus().incrementElapsedTime(STATUS.ERROR, System.currentTimeMillis() - error_start_time);
                firePropertyChange(PROPERTY, null, getStatus());

            }
        }
        getStatus().incrementElapsedTime(STATUS.PROCESSED, System.currentTimeMillis() - start_time);
        getStatus().setMessage("completed successfully");
        getStatus().completed();
        firePropertyChange(PROPERTY, null, getStatus());

        return result;

    }

    protected Callable getCallable(final P processor, final Input input) {
        Callable callable = new Callable() {

            public Output call() throws Exception {
                Object o;
                o = processor.process(input);
                return (Output) o;
            }
        };
        return callable;
    }

    /**
     * Specifies how to cope with timeouts.
     * @throws YaqpException
     */
    protected void handleTimeOut() throws YaqpException {
        if (!parallel_executor.isTerminated()) {
            if (isfailSensitive()) {
                parallel_executor.shutdownNow();
                getStatus().setMessage("completed unsuccessfully - timeout");
                getStatus().completed();
                firePropertyChange(PROPERTY, null, getStatus());
                throw new ProcessorException(XBP7, "Timeout");
            }
        }

    }
}
