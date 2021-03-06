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

import java.sql.SQLException;
import org.opentox.core.exceptions.Cause;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.queries.HyperResult;
import org.opentox.db.queries.HyperStatement;
import org.opentox.db.util.PrepSwimmingPool;
import org.opentox.db.util.QueryType;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.*;

/**A DbProcessor handles all execution of statements.
 * A {@link HyperStatement} must be provided for it to operate,
 * and a {@link HyperResult} is given as the processor's output.
 * The processor decides if he will execute an update, or a query
 * from the provided HyperStatement, according to the {@link QueryType}
 * the HyperStatement carries.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DbProcessor extends AbstractDbProcessor<HyperStatement, HyperResult> {

    public DbProcessor() {
        super();
    }

    /**
     *
     * @param q A HyperStatement, i.e. a Query to the database. See {@link
     * HyperStatement } for more detalis.
     * @return The result of the query as an instance of {@link HyperResult }.
     * @throws DbException in case the statement cannot be executed either due to
     * malformed syntax or some connection problem to the database.
     */
    public HyperResult execute(HyperStatement q) throws DbException {
        HyperResult result = null;
        try {
            if (q.getType().equals(QueryType.UPDATE)) {
                result = q.executeUpdate();
            } else if (q.getType().equals(QueryType.SELECT)) {
                result = q.executeQuery();
            }
        } catch (SQLException e) {
            String message = "Error while executing query :" + q.toString();
            YaqpLogger.LOG.log(new Trace(getClass(), message));
            YaqpLogger.LOG.log(new Debug(getClass(), e.toString()));
            //System.out.println(e.toString());
            if (e.getErrorCode() == -1) {
                throw new DbException(Cause.XDB800, message, e);
            } else {
                throw new DbException(Cause.XDB9, message, e);
            }

        } finally {
            PrepSwimmingPool.POOL.recycle(q);
        }
        return result;
    }
}
