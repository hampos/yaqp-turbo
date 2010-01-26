/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.io.processors;

import org.opentox.core.exceptions.YaqpIOException;
import org.opentox.core.processors.Processor;
import org.opentox.io.interfaces.JIOProcessor;

/**
 *
 * @author hampos
 */
public abstract class AbstractIOProcessor<Input, Output>
        extends Processor<Input, Output>
        implements JIOProcessor<Input, Output> {

    public AbstractIOProcessor() {
        super();
    }

    public Output process(Input data) throws YaqpIOException {
        try {
            return handle(data);
        } catch (Exception x) {
            throw new YaqpIOException(x);
        }
    }

}
