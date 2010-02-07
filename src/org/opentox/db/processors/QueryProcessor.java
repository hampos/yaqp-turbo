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

import org.opentox.db.exceptions.DbException;
import org.opentox.db.interfaces.JQueryProcessor;
import org.opentox.db.queries.HyperStatement;
import org.opentox.db.queries.QueryFood;
import org.opentox.db.util.PrepStmt;
import org.opentox.db.util.PrepSwimmingPool;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Debug;

/**
 * This is a Processor that handles how parameters are set on a PrepStmt object.
 * A QueryProcessor is an implementation of the {@link AbstractDbProcessor } and it
 * is initialized/constructed with a PrepStmt object.
 *
 * @author Sopasakis Pantelis
 * @author Charalampos Chomenides
 */
public class QueryProcessor extends AbstractDbProcessor<QueryFood, HyperStatement> implements JQueryProcessor {

    private PrepStmt prepStmt;

    /**
     * Builds a new QueryProcessor for a given prepared statement encapsulated in
     * a PrepStmt object.
     * @param prepStmt a prepared statement
     */
    public QueryProcessor(PrepStmt prepStmt) {
        super();
        this.prepStmt = prepStmt;
    }

    /**
     * Executes the processor, i.e. given an instance of QueryFood (properly initialized
     * with values) produces the corresponding HyperStatement.
     * @param food the query food
     * @return The produced hyperstatement
     */
    public HyperStatement execute(QueryFood food) throws DbException {

        HyperStatement hs = null;
        try {
            hs = PrepSwimmingPool.POOL.take(prepStmt);
            String value = "";

            for (int i = 0; prepStmt.getParameters() != null && i < prepStmt.getParameters().length; i++) {

                if (!food.containsName(prepStmt.getParameters()[i].getName())) {
                    String message = "The parameter " + prepStmt.getParameters()[i].getName() + " is not set";
                    throw new DbException("XDR162",message);
                }
                value = food.getValue(prepStmt.getParameters()[i].getName());
                if (prepStmt.getParameters()[i].getType().equals(String.class)) {
                    hs.setString((i + 1), value);
                } else if (prepStmt.getParameters()[i].getType().equals(Integer.class)) {
                    hs.setInt((i + 1), Integer.parseInt(value));
                } else if (prepStmt.getParameters()[i].getType().equals(Double.class)) {
                    hs.setDouble((i + 1), Double.parseDouble(value));
                }
            }
        } catch (InterruptedException ex) {
            YaqpLogger.LOG.log(new Debug(getClass(), "XDR163 - Exception thrown :: " + ex));
        } catch (DbException ex) {
            YaqpLogger.LOG.log(new Debug(getClass(), "XDR164 - Exception thrown :: " + ex));
        }
        return hs;
    }

    public PrepStmt getPrepStmt() {
        return this.prepStmt;
    }

    public void setPrepStmt(PrepStmt prepStmt) {
        this.prepStmt = prepStmt;
    }

}
