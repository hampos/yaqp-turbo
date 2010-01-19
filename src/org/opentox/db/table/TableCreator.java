package org.opentox.db.table;

import java.sql.SQLException;
import java.sql.Statement;
import org.opentox.core.exceptions.YaqpException;
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

    private TableCreator() {
        super();
    }

    public TableCreator(String tableName, String tableStructure) {
        super(tableName, tableStructure);
    }

    /**
     * This is the constructor you are adviced to use.
     * @param TABLE
     */
    public TableCreator(StandardTables TABLE) {
        super(TABLE);
    }



    


    /**
     * Creates a new table in the database.
     * @param q this parameters stands only for reasong of consistency and
     * compliance with the {@link JProcessor Processor Pattern }. Set it to
     * <code>null</code> or <code>new Object()</code>; it is not used whatsoever.
     * @return Returns always <code>null</code> since this is a processor from
     * which we don't expect a certain output neither do we need to provide a
     * certain input.
     */
    @Override
    public Object execute(Object q) {
        TheDbConnector db = TheDbConnector.DB;
        try {
            // If the database does not contain the table, it will be created....
            if (!db.getTableNames().contains(getTableName())) {
                String createTable =
                        "CREATE TABLE  " + getTableName() + "(" + getTableStructure() + ")";
                YaqpLogger.LOG.log(new Trace(getClass(), "Table Creation SQL Command ::" + createTable));
                try {
                    Statement stmt = db.getConnection().createStatement();
                    stmt.execute(createTable);
                    stmt.close();
                    YaqpLogger.LOG.log(new Info(getClass(), "The table '" + getTableName().toUpperCase() + "' was created without content"));
                } catch (SQLException ex) {
                    if (ex.toString().contains("already exists")) {
                        YaqpLogger.LOG.log(new Trace(getClass(), ex.toString()));
                        YaqpLogger.LOG.log(new Warning(getClass(), "The table '" + getTableName().toUpperCase()
                                + "' could not be created because it already exists"));
                    } else {
                        YaqpLogger.LOG.log(new ScrewedUp(getClass(), "The table '" + getTableName().toUpperCase()
                                + "' could not be created :: " + ex));
                    }
                }
            } else {
                YaqpLogger.LOG.log(new Info(getClass(), "The table '" + getTableName() + "' exists"));
            }
        } catch (YaqpException ex) {
            YaqpLogger.LOG.log(new ScrewedUp(getClass(), "Exception :: " + ex));
            YaqpLogger.LOG.log(new Debug(getClass(),
                    !db.isConnected()
                    ? "There is no connection to the database " + db.getDatabaseName()
                    : "There is a well-established connection but the following exception was thrown :: " + ex));
        }

        return null;
    }
}
