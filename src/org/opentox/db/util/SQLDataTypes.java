package org.opentox.db.util;

/**
 *
 * @author chung
 */
public class SQLDataTypes {

    private String columnType;

    private SQLDataTypes(){

    }

    private SQLDataTypes(String columnType){
        this.columnType = columnType;
    }

    public static SQLDataTypes Int(){
        return new SQLDataTypes("INT");
    }

    public static SQLDataTypes VarChar(int size){
        return new SQLDataTypes("VARCHAR("+size+")");
    }

    public static SQLDataTypes Float(){
        return new SQLDataTypes("FLOAT");
    }

    public static SQLDataTypes Timestamp(){
        return new SQLDataTypes("TIMESTAMP");
    }

    public static SQLDataTypes Date(){
        return new SQLDataTypes("DATE");
    }

    @Override
    public String toString(){
        return columnType;
    }

}
