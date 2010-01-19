package org.opentox.db.table;

import java.util.ArrayList;
import org.opentox.db.interfaces.JDbTable;


/**
 *
 * @author chung
 */
public class Table implements JDbTable<TableColumn>{

    private ArrayList<TableColumn> listOfColumns = new ArrayList<TableColumn>();
    private String tableName = null;

    public Table(){
        super();
    }

    public ArrayList<TableColumn> getTableColumns() {
        return listOfColumns;
    }

    public void setTableColumns(ArrayList<TableColumn> tableColumns) {
        this.listOfColumns = tableColumns;
    }

    public void addColumn(TableColumn column) {
        this.listOfColumns.add(column);
    }

    public void removeColumn(TableColumn column) {
        this.listOfColumns.remove(column);
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return this.tableName;
    }

}
