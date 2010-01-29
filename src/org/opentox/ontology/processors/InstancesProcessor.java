package org.opentox.ontology.processors;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import java.net.URI;
import org.opentox.io.processors.InputProcessor;
import org.opentox.ontology.TurboOntModel;
import org.opentox.ontology.exceptions.ImproperEntityException;
import org.opentox.ontology.exceptions.YaqpOntException;
import org.opentox.ontology.namespaces.OTClass;
import weka.core.FastVector;
import weka.core.Instances;

/**
 *
 * @author chung
 */
public class InstancesProcessor extends AbstractOntProcessor<Instances> {

    /**
     *
     * Converts an ontological model of a dataset into a <code>weka.core.Instances</code>
     * object which can be used by weka algorithms and other weka routines.
     * @param yaqpOntModel An ontological model for a dataset.
     * @return The dataset as Instances (weka object)
     */
    public Instances convert(TurboOntModel yaqpOntModel) throws YaqpOntException{

        /*
         *
         * Some initial definitions:
         */
        Resource dataEntryResource = OTClass.DataEntry.getOntClass(yaqpOntModel),
                dataSetResource = OTClass.Dataset.getOntClass(yaqpOntModel),
                featureResource = OTClass.Feature.getOntClass(yaqpOntModel);
        FastVector attributes = null;
        Instances data = null;
        StmtIterator dataSetIterator = null,
                featureIterator = null,
                valuesIterator = null,
                dataEntryIterator = null;
        String relationName = null;

        /*
         *
         * Iterate over all nodes in the dataset having type 'ot:Dataset'.
         */
        dataSetIterator =
                yaqpOntModel.listStatements(new SimpleSelector(null, RDF.type, dataSetResource));

        if (dataSetIterator.hasNext()) {
            relationName = dataSetIterator.next().getSubject().getURI();
            if (dataSetIterator.hasNext()) {
                throw new YaqpOntException("XN311 - More than one datasets found");
            }
        } else {
            // this is not a dataset model
            throw new ImproperEntityException("XN312 - Not a dataset");
        }
        dataSetIterator.close();

        System.out.println(relationName);

        /**
         *
         *
         */

        return data;
    }

    public static void main(String[] args) throws Exception {
        InputProcessor p = new InputProcessor();
        URI uri = new URI("http://localhost/ds.rdf");
        TurboOntModel tom = p.handle(uri);
        InstancesProcessor ipr = new InstancesProcessor();
        ipr.convert(tom);
    }
}
