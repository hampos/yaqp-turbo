package org.opentox.db.table;

import org.opentox.db.interfaces.JDbTableColumn;
import org.opentox.db.util.SQLDataTypes;

/**
 *
 * @author chung
 */
public class TableColumn implements JDbTableColumn {

    private String columnName = "";
    private SQLDataTypes columnType = null;
    private boolean isPrimaryKey = false;
    private boolean generatedAlwaysAsIdentity = false;
    private boolean isForeignKey = false;
    private String referencesTable = null;
    private String referencesColumn = null;
    private boolean isOnDeleteCascade = false;
    private String defaultValue = null;
    private boolean isNotNull = false;
    private boolean isUnique = false;
    private String constraintName = null;
    private String constraintCondition = null;
    private boolean isConstrained = false;
    private boolean hasDefault = false;

    public TableColumn(){
        super();
    }

    public TableColumn(String columnName){
        setColumnName(columnName);
    }

    public String getColumnName() {
        return this.columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName.toUpperCase();
    }

    public void setColumnType(SQLDataTypes columnType) {
        this.columnType = columnType;
    }

    public SQLDataTypes getColumnType() {
        return columnType;
    }

    public void setPrimaryKey(boolean isPrimaryKey, boolean generatedAlwaysAsIdentity) {
        this.isPrimaryKey = isPrimaryKey;
        this.generatedAlwaysAsIdentity = generatedAlwaysAsIdentity;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setForeignKey(String TableName, String ForeignColumn, boolean OnDeleteCascade) {
        this.isForeignKey = true;
        this.referencesTable = TableName.toUpperCase();
        this.referencesColumn = ForeignColumn.toUpperCase();
        this.isOnDeleteCascade = OnDeleteCascade;

    }

    public String getForeignKey() {
        String foreignKey = "";
        if (isForeignKey) {
            foreignKey = "FOREIGN KEY (" + columnName + ") REFERENCES " + referencesTable + "(" + referencesColumn + ")";
            if (isOnDeleteCascade) {
                foreignKey = foreignKey + " ON DELETE CASCADE ";
            }
        }
        return foreignKey;
    }

    public void setDefaultValue(String defaultValue) {
        this.hasDefault = true;
        this.defaultValue = defaultValue.toUpperCase();
    }

    public String getDefaultValue() {
        if (hasDefault){
            return " DEFAULT "+this.defaultValue;
        }else {
            return "";
        }
        
    }

    public void setNotNull(boolean notNull) {
        this.isNotNull = notNull;
    }

    public boolean isNotNull() {
        return this.isNotNull;
    }

    public void setConstraint(String constraintName, String Condition) {
        this.isConstrained = true;
        this.constraintName = constraintName.toUpperCase();
        this.constraintCondition = Condition.toUpperCase();
    }

    public String getConstraint() {
        String constraint = "";
        if (this.isConstrained) {
            constraint = " CONSTRAINT " + constraintName + " CHECK (" + constraintCondition + ") ";
        }
        return constraint;
    }

    public void setUnique(boolean isUnique) {
        this.isUnique = isUnique;
    }

    public boolean isUnique() {
        return isUnique;
    }

    public boolean isForeignKey() {
        return isForeignKey;
    }

    public boolean isAlwaysAsIdentity() {
        return generatedAlwaysAsIdentity;
    }

    public boolean isConstrained(){
        return isConstrained;
    }

    public boolean hasDefault() {
        return hasDefault;
    }
}
