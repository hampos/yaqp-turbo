package org.opentox.db.table;

import java.sql.SQLException;
import java.sql.Statement;
import org.opentox.db.util.TheDbConnector;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Info;
import org.opentox.util.logging.levels.ScrewedUp;
import org.opentox.util.logging.levels.Warning;

/**
 *
 * @author chung
 */
public final class TableDropper extends AbstractTableProcessor{

    public TableDropper(){
        super();
    }
       

    /**
     * Drops a table from the database.
     * @param q
     * @return
     */
    public Object execute(Table q) {
        String createTable =
                "DROP TABLE  " + q.getTableName() ;
        try {
            Statement stmt = TheDbConnector.DB.getConnection().createStatement();
            stmt.execute(createTable);
            stmt.close();
            YaqpLogger.LOG.log(new Info(TableDropper.class, "The table '" + q.getTableName().toUpperCase() +"' was dropped"));
        } catch (SQLException ex) {
            System.out.println(ex);
            if (ex.toString().contains("because it does not exist")){
                YaqpLogger.LOG.log(new Warning(TableDropper.class, "The table '" + q.getTableName().toUpperCase()
                    + "' could not be dropped because it does not exist"));
            }else{
            YaqpLogger.LOG.log(new ScrewedUp(TableDropper.class, "The table '" + q.getTableName().toUpperCase()
                    + "' could not be dropped :: " + ex));
            }

        }
        return null;
    }

}
