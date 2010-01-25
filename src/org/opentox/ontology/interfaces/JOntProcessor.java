package org.opentox.ontology.interfaces;

import org.opentox.ontology.YaqpOntModel;

/**
 *
 * @author chung
 */
public interface JOntProcessor<Output> {

    Output convert(YaqpOntModel yaqpOntModel);
}
