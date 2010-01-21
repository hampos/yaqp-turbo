package org.opentox.db.queries;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author chung
 */
public class HyperResult {

    private ResultSet resultSet;
    
    private ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();

    public void addRow(ArrayList<String> entry){
        data.add(entry);
    }

    public ArrayList<String> getRow(int rowIndex){
        return data.get(rowIndex);
    }

    public ArrayList<String> getColumn(int columnIndex){
        throw new UnsupportedOperationException();
    }

    /**
     * Mainly for testing purposes
     * @return
     */
    @Override
    public String toString(){
        String result = "";
        Iterator<ArrayList<String>> it = data.iterator();
        while (it.hasNext()){
            ArrayList<String> row = it.next();
            for (int i=0;i<row.size();i++){
                result = result + row.get(i) + " - ";
            }
            result = result + "\n";
        }
        return result;
    }



}
