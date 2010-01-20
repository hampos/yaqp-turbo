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

    public Table(String tableName){
        this.tableName = tableName.toUpperCase();
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
        this.tableName = tableName.toUpperCase();
    }

    public String getTableName() {
        return this.tableName;
    }

    public String getSQL() {
        String SQL = "CREATE TABLE " + getTableName() + "\n( ";
        ArrayList<TableColumn> colList = new ArrayList<TableColumn>();
        colList = getTableColumns();
        TableColumn temp;
        for (int i = 0; i < colList.size(); i++) {
            temp = colList.get(i);
            SQL = SQL + temp.getColumnName() + " " + temp.getColumnType().toString();

            if (temp.isUnique()) {
                SQL = SQL + " UNIQUE";
            }
            if (temp.isNotNull()) {
                SQL = SQL + " NOT NULL";
            }
            if (temp.isPrimaryKey()) {
                SQL = SQL + " PRIMARY KEY ";
            }

            if (temp.hasDefault()) {
                SQL = SQL + temp.getDefaultValue();
            }

            if (temp.isConstrained()) {
                SQL = SQL + temp.getConstraint() ;
            }

            if (temp.isForeignKey()) {
                SQL = SQL + ",\n" + temp.getForeignKey();
            }
            if (i != colList.size() -1 ) {
                SQL = SQL + ",\n";
            }
        }
        SQL = SQL + "\n)";
        return SQL;
    }

}
