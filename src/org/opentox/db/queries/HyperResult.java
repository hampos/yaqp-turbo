package org.opentox.db.queries;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.db.exceptions.DbException;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Fatal;

/**
 *
 * @author chung
 */
public class HyperResult {

    private ResultSet resultSet;
    private ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();

    public void addRow(ArrayList<String> entry) {
        data.add(entry);
    }

    public ArrayList<String> getRow(int rowIndex) {
        return data.get(rowIndex);
    }

    public ArrayList<String> getColumn(int columnIndex) {
        throw new UnsupportedOperationException();
    }

    public Iterator<ArrayList<String>> getRowIterator() {
        Iterator<ArrayList<String>> rowIt = data.iterator();
        return rowIt;
    }

    public Iterator<String> getRowIterator(int colNum) {
        Hyperator hyp = null;
        hyp = new Hyperator(colNum);
        return hyp;
    }

    public Iterator<String> getColumnIterator(int rowNum) {
        if (rowNum <= data.size() && rowNum > 0) {
            Iterator<ArrayList<String>> rowIt = this.getRowIterator();
            ArrayList<String> row = null;
            while (rowIt.hasNext() && (rowNum != 0)) {
                row = rowIt.next();
                rowNum--;
            }
            return row.iterator();

        } else {
            throw new ArrayIndexOutOfBoundsException("Array index out of bounds while reading database vertically");

        }
    }

    /**
     * Mainly for testing purposes
     * @return
     */
    @Override
    public String toString() {
        String result = "";
        Iterator<ArrayList<String>> it = data.iterator();
        while (it.hasNext()) {
            ArrayList<String> row = it.next();
            for (int i = 0; i < row.size(); i++) {
                result = result + row.get(i) + " - ";
            }
            result = result + "\n";
        }
        return result;
    }

    private class Hyperator implements Iterator<String> {

        private final int colNum;
        private int currentRow = 0;
        private Iterator<String> colIt;

        private Hyperator(int colNum) {
            if (data.get(currentRow).size() < colNum || colNum < 1) {
                throw new ArrayIndexOutOfBoundsException("Array index out of bounds while reading database horizontally");
            }
            this.colNum = colNum;
        }

        public boolean hasNext() {
            int tempCurrent = currentRow;
            tempCurrent++;
            int col = this.colNum;
            try{
            colIt = getColumnIterator(tempCurrent);
            }catch(RuntimeException e){
                return false;
            }
            return true;
        }

        public String next() {
            currentRow++;
            String data = null;
            int col = this.colNum;
            colIt = getColumnIterator(currentRow);
            while (colIt.hasNext() && col != 0) {
                data = colIt.next();
                col--;
            }
            return data;
        }

        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
