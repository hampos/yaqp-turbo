package org.opentox.ontology.interfaces;

import org.opentox.ontology.namespaces.OTClass;

/**
 *
 * @author chung
 */
public interface JOntModel {

    void printConsole();
    void includeOntClass(OTClass ont_class);
    void includeOntClasses(OTClass[] ont_classes);

}
