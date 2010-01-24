package org.opentox.db.processors;


import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.processors.Pipeline;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.queries.HyperResult;
import org.opentox.db.queries.QueryFood;
import org.opentox.db.util.PrepStmt;


/**
 * This a bundle for two importand database processors: The {@link QueryProcessor }
 * and the {@link DbProcessor }. This processor is initialized with a PrepStmt object
 * and then, invokation of the <code>process</code> method, feeds a QueryProcessor
 * with query food to produce a HyperStatement which in turn is provided as input to
 * the DbProcessor to perform a DB update or selection. The result from this procedure is
 * a HyperResult object which of course has meaning only in select-type queries. A
 * DbPipeline is a processor working as a Pipeline.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 *
 * @param <QF> QueryFood type of the input to the pipeline
 * @param <HR> HyperResult is the output type of this pipeline
 */
public class DbPipeline<QF extends QueryFood, HR extends HyperResult> extends AbstractDbProcessor<QF, HR>{

    private PrepStmt prepStmt;

    /**
     * Construct a new
     * @param prepStmt
     */
    public DbPipeline(PrepStmt prepStmt){
        this.prepStmt = prepStmt;
    }

    public HR execute(QF q) throws DbException {
       Pipeline pipeline = new Pipeline();
       pipeline.add(new QueryProcessor(prepStmt));
       pipeline.add(new DbProcessor());
        try {
            HR result = (HR) pipeline.process(q);
        } catch (YaqpException ex) {
            throw new DbException(ex);
        }
       return null;
    }




}