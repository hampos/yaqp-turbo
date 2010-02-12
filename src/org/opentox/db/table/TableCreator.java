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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.interfaces.JProcessor;
import org.opentox.db.util.TheDbConnector;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.*;

/**
 *
 * This processor creates a sinle table in the database, using the connection
 * provided by the {@link TheDbConnector Connector } (Singleton). The table's name
 * and Structure are specified in the constructor of this class. You are adviced
 * to use the standard {@link StandardTables list of tables } of YAQP provided as an enueration.
 *
 * @author Sopasakis Pantelis
 * @author Charalampos Chomenides
 * @see AbstractTableProcessor
 * @see TableDropper
 */
public final class TableCreator extends AbstractTableProcessor {

    public TableCreator() {
        super();
    }

    /**
     * Creates a new table in the database.
     * @param q this parameters stands only for reasong of consistency and
     * compliance with the {@link org.opentox.core.interfaces.JProcessor Processor Pattern}. Set it to
     * <code>null</code> or <code>new Object()</code>; it is not used whatsoever.
     * @return Returns always <code>null</code> since this is a processor from
     * which we don't expect a certain output neither do we need to provide a
     * certain input.
     */
    @Override
    public Object execute(Table q) {
        TheDbConnector db = TheDbConnector.DB;
        try {
            // If the database does not contain the table, it will be created....
            if (!db.getTableNames().contains(q.getTableName())) {
                String createTable = q.getCreationSQL();
                YaqpLogger.LOG.log(new Trace(getClass(), "Table Creation SQL Command ::\n" + createTable));
                try {
                    Statement stmt = db.getConnection().createStatement();
                    stmt.execute(createTable);
                    stmt.close();
                    YaqpLogger.LOG.log(new Info(getClass(), "The table '" + q.getTableName().toUpperCase()
                            + "' was created without content"));
                } catch (SQLException ex) {
                    String message = "The table '" + q.getTableName().toUpperCase()
                            + "' could not be created";
                    YaqpLogger.LOG.log(new ScrewedUp(getClass(),message + " due to SQLException " + ex));
                    throw new RuntimeException(message);
                }
            } else {
                YaqpLogger.LOG.log(new Info(getClass(), "The table '" + q.getTableName() + "' exists"));
            }
        } catch (YaqpException ex) {           
            YaqpLogger.LOG.log(new ScrewedUp(getClass(), ex.toString()));
            YaqpLogger.LOG.log(new Debug(getClass(),
                    !db.isConnected()
                    ? "XIL10A - There is no connection to the database " + db.getDatabaseName()
                    : "XIL12B - There is a well-established connection but the following exception was thrown :: " + ex));
        }

        return null;
    }
}
