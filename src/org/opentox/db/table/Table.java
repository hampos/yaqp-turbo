package org.opentox.db.table;

import java.util.ArrayList;
import org.opentox.db.interfaces.JDbTable;

/**
 *
 * A Table in the database is characterized by its columns. This class offers a
 * flexible tool for manipulating database tables (creating and deleting them).
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Table implements JDbTable<TableColumn> {

    private ArrayList<TableColumn> listOfColumns = new ArrayList<TableColumn>();
    private String tableName = null;

    /**
     * Construct a new Table object.
     */
    public Table() {
        super();
    }

    /**
     * Construct a new Table object, given its name.
     * @param tableName
     */
    public Table(String tableName) {
        this();
        this.tableName = tableName.toUpperCase();
    }

    /**
     * Retrieve the list of columns of the table.
     * @return list of table columns.
     */
    public ArrayList<TableColumn> getTableColumns() {
        return listOfColumns;
    }

    /**
     * Declare the list of table columns of the table.
     * @param tableColumns table columns.
     */
    public void setTableColumns(ArrayList<TableColumn> tableColumns) {
        this.listOfColumns = tableColumns;
    }

    /**
     * Add a new column to the table.
     * @param column table column to be added in the table.
     */
    public void addColumn(TableColumn column) {
        this.listOfColumns.add(column);
    }

    /**
     * Remove a column from the table.
     * @param column column to be removed.
     */
    public void removeColumn(TableColumn column) {
        this.listOfColumns.remove(column);
    }

    /**
     * Set/update the name of the table.
     * @param tableName the name of the table.
     */
    public void setTableName(String tableName) {
        this.tableName = tableName.toUpperCase();
    }

    /**
     * Get the name of the table.
     * @return table name.
     */
    public String getTableName() {
        return this.tableName;
    }

    /**
     * Get the SQL command for the creation of the table.
     * @return SQL command for table creation.
     */
    public String getCreationSQL() {
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

            if (temp.isAlwaysAsIdentity()) {
                SQL = SQL + " GENERATED ALWAYS AS IDENTITY ";
            }

            if (temp.hasDefault()) {
                SQL = SQL + temp.getDefaultValue();
            }

            if (temp.isConstrained()) {
                SQL = SQL + temp.getConstraint();
            }

            if (temp.isForeignKey()) {
                SQL = SQL + ",\n" + temp.getForeignKey();
            }
            if (i != colList.size() - 1) {
                SQL = SQL + ",\n";
            }
        }
        SQL = SQL + "\n)";
        return SQL;
    }

    /**
     * Get the SQL command for the removal of the table from the database.
     * @return SQL command for dropping.
     */
    public String getDeletionSQL() {
        String SQL = "DROP TABLE " + getTableName();
        return SQL;
    }


}
