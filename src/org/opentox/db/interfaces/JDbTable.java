package org.opentox.db.interfaces;

import java.util.ArrayList;

/**
 *
 * @author Sopasakis Pantelis
 * @author Charalampos Chomenides
 */
public interface JDbTable<TableColumn extends JDbTableColumn> {

    ArrayList<TableColumn> getTableColumns();
    void setTableColumns(ArrayList<TableColumn> tableColumns);
    void addColumn(TableColumn column);
    void removeColumn(TableColumn column);
    void setTableName(String tableName);
    String getTableName();
    String getSQL();

}
