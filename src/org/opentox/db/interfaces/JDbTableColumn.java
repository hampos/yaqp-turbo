package org.opentox.db.interfaces;

import org.opentox.db.util.SQLDataTypes;

/**
 *
 * @author chung
 */
public interface JDbTableColumn {

    String getColumnName();
    void setColumnName(String columnName);

    SQLDataTypes getColumnType();
    void setColumnType(SQLDataTypes columnType);

    void setPrimaryKey(boolean isPrimaryKey, boolean generatedAlwaysAsIdentity);
    boolean isPrimaryKey();
    boolean isAlwaysAsIdentity();

    void setForeignKey(String TableName, String ForeignColumn, boolean OnDeleteCascade);
    String getForeignKey();
    boolean isForeignKey();

    void setDefaultValue(String defaultValue);
    String getDefaultValue();

    void setNotNull(boolean notNull);
    boolean isNotNull();

    void setConstraint(String constraintName, String Condition);
    String getConstraint();
    
    void setUnique(boolean isUnique);
    boolean isUnique();

    boolean hasDefault();
    boolean isConstrained();


    

}
