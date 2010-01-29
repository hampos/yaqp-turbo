package org.opentox.ontology.interfaces;

import org.opentox.ontology.TurboOntModel;
import org.opentox.ontology.exceptions.YaqpOntException;

/**
 *
 * @author chung
 */
public interface JOntProcessor<Output> {

    Output convert(TurboOntModel yaqpOntModel) throws YaqpOntException;
}
