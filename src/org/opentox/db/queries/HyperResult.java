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
package org.opentox.db.queries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Fatal;
import org.opentox.util.logging.levels.Trace;

/**
 * This is the set of results from a SELECT operation on the database.
 * 
 * @author Charalampos Chomenides
 * @author Sopasakis Pantelis
 */
public class HyperResult {

    private ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
    private Map<String, Integer> columnMap = new HashMap<String, Integer>();

    protected void addRow(ArrayList<String> entry) {
        data.add(entry);
    }

    protected void addColName(String columnName, int columnIndex) {
        columnMap.put(columnName, columnIndex);
    }

    public int getSize(){
        return data.size();
    }

    public int getColSize(){
        return data.get(1).size();
    }

    public ArrayList<String> getRow(int rowIndex) {
        return data.get(rowIndex);
    }

    public ArrayList<String> getColumn(int columnIndex) {
        ArrayList<String> column = new ArrayList<String>();
        Iterator<String> it = getColumnIterator(columnIndex);
        while (it.hasNext()) {
            column.add(it.next());
        }
        return column;
    }

    public ArrayList<String> getColumn(String columnName) {
        return getColumn(columnMap.get(columnName));
    }

    public Iterator<ArrayList<String>> getRowIterator() {
        Iterator<ArrayList<String>> rowIt = data.iterator();
        return rowIt;
    }

    public Iterator<String> getRowIterator(int columnIndex) {
        Hyperator hyp = null;
        hyp = new Hyperator(columnIndex);
        return hyp;
    }

    public Iterator<String> getRowIterator(String columnName) {
        return getRowIterator(columnMap.get(columnName));
    }

    public Iterator<String> getColumnIterator(int rowIndex) {
        if (rowIndex <= data.size() && rowIndex > 0) {
            Iterator<ArrayList<String>> rowIt = this.getRowIterator();
            ArrayList<String> row = null;
            while (rowIt.hasNext() && (rowIndex != 0)) {
                row = rowIt.next();
                rowIndex--;
            }
            return row.iterator();

        } else {
            String message = "Vertical Iterator reached an end";
            YaqpLogger.LOG.log(new Trace(getClass(), message));
            throw new ArrayIndexOutOfBoundsException(message);
        }
    }

    /**
     * Mainly for testing purposes
     * @return string representation of the HyperResult
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

        private final int columnIndex;
        private int currentRow = 0;
        private Iterator<String> colIt;

        private Hyperator(int columnIndex) {
            if (data.get(currentRow).size() < columnIndex || columnIndex < 1) {
                String message = "Array index out of bounds while reading database horizontally";
                YaqpLogger.LOG.log(new Fatal(getClass(), message));
                throw new ArrayIndexOutOfBoundsException(message);
            }
            this.columnIndex = columnIndex;
        }

        public boolean hasNext() {
            int tempCurrent = currentRow;
            tempCurrent++;
            int col = this.columnIndex;
            try {
                colIt = getColumnIterator(tempCurrent);
            } catch (RuntimeException e) {
                YaqpLogger.LOG.log(new Trace(getClass(), "([VeRtiCal] End of Hyp(It)erator...) :: " + e));
                return false;
            }
            return true;
        }

        public String next() {
            currentRow++;
            String data = null;
            int col = this.columnIndex;
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
