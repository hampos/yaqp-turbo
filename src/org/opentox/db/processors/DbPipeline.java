/*
 * YAQP - Yet Another QSAR Project: Machine Learning algorithms designed for
 * the prediction of toxicological features of chemical compounds become
 * available on the Web. Yaqp is developed under OpenTox (http://opentox.org)
 * which is an FP7-funded EU research project.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
public class DbPipeline<QF extends QueryFood, HR extends HyperResult> extends AbstractDbProcessor<QF, HR> {

    private Pipeline pipeline;

    /**
     * Construct a new
     * @param prepStmt
     */
    public DbPipeline(PrepStmt prepStmt) {
        pipeline = new Pipeline();
        pipeline.add(new QueryProcessor(prepStmt));
        pipeline.add(new DbProcessor());

    }

    public HR execute(QF q) throws DbException {
        HR result;
        try {
            result = (HR) pipeline.process(q);
        } catch (YaqpException ex) {
            throw new DbException("XDB4",ex);
        }
        return result;
    }
}
