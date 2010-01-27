package org.opentox.ontology.interfaces;

import org.opentox.ontology.TurboOntModel;

/**
 *
 * @author chung
 */
public interface JOntProcessor<Output> {

    Output convert(TurboOntModel yaqpOntModel);
}
