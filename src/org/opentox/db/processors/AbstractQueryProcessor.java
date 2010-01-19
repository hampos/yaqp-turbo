package org.opentox.db.processors;

import java.util.ArrayList;
import org.opentox.db.interfaces.JQueryProcessor;
import org.opentox.core.processors.Processor;
import org.opentox.db.interfaces.JQuery;
import org.opentox.db.queries.QueryParam;
import org.opentox.db.util.PrepStmt;

/**
 * UNDER CONSTRUCTION
 * @author chung
 */
public abstract class AbstractQueryProcessor<ParameterType>
        extends Processor<ArrayList<QueryParam<ParameterType>>, JQuery<ParameterType>>
        implements JQueryProcessor<ParameterType>
         {

    private PrepStmt prep_stmt = null;

    public PrepStmt getPrepStmt() {
        return prep_stmt;
    }

    public void setPrepStmt(PrepStmt prepStmt) {
        this.prep_stmt = prepStmt;
    }

}
