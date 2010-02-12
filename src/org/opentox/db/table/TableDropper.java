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
package org.opentox.db.table;

import java.sql.SQLException;
import java.sql.Statement;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.util.TheDbConnector;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Info;
import org.opentox.util.logging.levels.ScrewedUp;
import org.opentox.util.logging.levels.Warning;
import static org.opentox.core.exceptions.Cause.*;

/**
 *
 * Handles the deletion of tables.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public final class TableDropper extends AbstractTableProcessor {

    public TableDropper() {
        super();
    }

    /**
     * Drops a table from the database.
     * @param q
     * @return always returns <code>null</code>.
     */
    public Object execute(Table q) throws DbException {
        String deleteTable = q.getDeletionSQL();
        try {
            Statement stmt = TheDbConnector.DB.getConnection().createStatement();
            stmt.execute(deleteTable);
            stmt.close();
            YaqpLogger.LOG.log(new Info(TableDropper.class, "The table '" + q.getTableName().toUpperCase() + "' was dropped"));
        } catch (SQLException ex) {

            if (ex.getErrorCode() == -1) {
                YaqpLogger.LOG.log(new Warning(TableDropper.class, "WSQ884 - The table '" + q.getTableName().toUpperCase()
                        + "' could not be dropped because it does not exist"));
                throw new DbException(XDB16, "Trying to drop non-existing table", ex);
            } else {
                YaqpLogger.LOG.log(new ScrewedUp(TableDropper.class, "XSP726 - The table '" + q.getTableName().toUpperCase()
                        + "' could not be dropped :: " + ex));
            }

        }
        return null;
    }
}
