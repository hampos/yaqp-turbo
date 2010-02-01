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
package org.opentox.db.interfaces;

import org.opentox.db.util.SQLDataTypes;

/**
 *
 * Interface for table columns. A table column has lots of structural characterizations
 * such as if its a primary key for its table, a foreign key to a unique-type column is
 * some other table, if its not null etc. A column has also an SQL Datatype (INT,
 * VARCHAR etc) and optionally a default value.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface JDbTableColumn {

    /**
     * @return name of the column
     */
    String getColumnName();
    /**
     * Set the name of the column
     * @param columnName name of the column
     */
    void setColumnName(String columnName);
    /**
     * Get the type of the ccolumn as an {@link SQLDataTypes SQL type}
     * @return datatype for the column
     */
    SQLDataTypes getColumnType();
    /**
     * Set the SQL datatype of the column
     * @param columnType datatype for the column
     */
    void setColumnType(SQLDataTypes columnType);
    /**
     *
     * @param isPrimaryKey true if the column is a primary key of the table
     * @param generatedAlwaysAsIdentity true if the the column is a primary
     * key generated always as identity.
     */
    void setPrimaryKey(boolean isPrimaryKey, boolean generatedAlwaysAsIdentity);
    /**
     *
     * @return <code>true</code> if the column is a primary key for the table.
     */
    boolean isPrimaryKey();
    /**
     *
     * @return <code>true</code> if the column is a primary key being generated
     * always as identity.
     */
    boolean isAlwaysAsIdentity();
    /**
     * The column is a foreign key pointing to some column in some other table
     * in the database.
     * @param TableName Name of the table to which the foreign key point to.
     * @param ForeignColumn Column of the foreign table to which the column
     * entries point to.
     * @param OnDeleteCascade If on deletion of the foreign entry, the entry
     * of this table should be deleted as well.
     */
    void setForeignKey(String TableName, String ForeignColumn, boolean OnDeleteCascade);
    /**
     *
     * @return String representation of the foreign key
     */
    String getForeignKey();
    /**
     *
     * @return <code>true</code> if the column is a foreign key.
     */
    boolean isForeignKey();
    /**
     * Set the default value of the column. If no entry is provided, set the
     * value equal to its default.
     * @param defaultValue default value.
     */
    void setDefaultValue(String defaultValue);
    /**
     * Retrieve the default value for this column.
     * @return default value
     */
    String getDefaultValue();
    /**
     * Demand that the entries in this column be not null.
     * @param notNull <code>true</code> if the column entries should be not null
     */
    void setNotNull(boolean notNull);
    /**
     * Whether the column is of not-null type.
     * @return <code>true</code> if the column is of not-null type.
     */
    boolean isNotNull();
    /**
     * Set a constraint to the column.
     * @param constraintName Name of the constraint (Should be unique in the DB)
     * @param Condition Constraint condition (e.g. A &lt; 10)
     */
    void setConstraint(String constraintName, String Condition);
    /**
     * Get the constraint for the column or an empty string if there are
     * no active constraints.
     * @return String Representation of the constraint.
     */
    String getConstraint();
    /**
     * Demand that the entries in this column be unique.
     * @param isUnique <code>true</code> if the entries in this column should be
     * unique, <code>false</code> otherwise.
     */
    void setUnique(boolean isUnique);
    /**
     * Whether the entries in this column are unique.
     * @return <code>true</code> if unique.
     */
    boolean isUnique();
    /**
     *
     * @return <code>true</code> if the column has a default value.
     */
    boolean hasDefault();
    /**
     *
     * @return <code>true</code> if there is an active constraint for the column.
     */
    boolean isConstrained();


    

}
