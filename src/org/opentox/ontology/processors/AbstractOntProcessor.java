package org.opentox.ontology.processors;

import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.processors.Processor;
import org.opentox.ontology.TurboOntModel;
import org.opentox.ontology.interfaces.JOntProcessor;

/**
 *
 * @author chung
 */
public abstract class AbstractOntProcessor<Output>
        extends Processor<TurboOntModel, Output>
        implements JOntProcessor<Output> {

    public Output process(TurboOntModel data) throws YaqpException {
        Output o = convert(data);
        return (Output ) o;
    }



}
