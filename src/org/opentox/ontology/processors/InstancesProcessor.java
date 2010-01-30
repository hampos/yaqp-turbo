package org.opentox.ontology.processors;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import java.net.URI;
import org.opentox.io.processors.InputProcessor;
import org.opentox.io.util.ServerList;
import org.opentox.ontology.TurboOntModel;
import org.opentox.ontology.exceptions.ImproperEntityException;
import org.opentox.ontology.exceptions.YaqpOntException;
import org.opentox.ontology.namespaces.OTClass;
import weka.core.FastVector;
import weka.core.Instances;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
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
        Resource _DataEntry = OTClass.DataEntry.getOntClass(yaqpOntModel),
                _Dataset = OTClass.Dataset.getOntClass(yaqpOntModel),
                _Feature = OTClass.Feature.getOntClass(yaqpOntModel),
                _NumericFeature = OTClass.NumericFeature.getOntClass(yaqpOntModel),
                _NominalFeature = OTClass.NominalFeature.getOntClass(yaqpOntModel);

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
                yaqpOntModel.listStatements(new SimpleSelector(null, RDF.type, _Dataset));

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
        URI uri = new URI(ServerList.ambit.getBaseURI()+"/dataset/6");
        TurboOntModel tom = p.handle(uri);
        InstancesProcessor ipr = new InstancesProcessor();
        ipr.convert(tom);
    }
}
