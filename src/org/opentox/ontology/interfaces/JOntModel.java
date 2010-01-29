package org.opentox.ontology.interfaces;

import com.hp.hpl.jena.ontology.OntModel;
import org.opentox.ontology.namespaces.OTClass;
import org.opentox.ontology.namespaces.YaqpOntEntity;

/**
 *
 * @author chung
 */
public interface JOntModel extends OntModel {

    void printConsole();

    void includeOntClass(YaqpOntEntity ont_entity);

    void includeOntClasses(YaqpOntEntity[] ont_entities);

    void createAnnotationProperties(String[] annotation_uris);

    void createDataTypeProperties(String[] datatype_uris);

    void createObjectProperties(String[] object_uris);

    void createSymmetricProperties(String[] symmetric_uris);

    void createTransitiveProperties(String[] transitive_uris);
}
