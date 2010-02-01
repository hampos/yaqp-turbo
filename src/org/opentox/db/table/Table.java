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
