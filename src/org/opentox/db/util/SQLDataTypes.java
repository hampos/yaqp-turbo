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
package org.opentox.db.util;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
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

    public static SQLDataTypes LongVarChar(){
        return new SQLDataTypes("LONG VARCHAR");
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

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object obj) {
        SQLDataTypes other = (SQLDataTypes)obj;
        return  (toString() == null ? other.toString() == null : toString().equals(other.toString()));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.columnType != null ? this.columnType.hashCode() : 0);
        return hash;
    }



}
