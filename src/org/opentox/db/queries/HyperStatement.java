package org.opentox.db.queries;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.interfaces.JHyperStatement;
import org.opentox.db.processors.DbProcessor;
import org.opentox.db.util.QueryType;
import org.opentox.db.util.SQLDataTypes;
import org.opentox.db.util.TheDbConnector;



/**
 *
 * This is a proxy for {@link java.sql.PreparedStatement Java PreparedStatement }
 * containing the set of necessary methods to prepare a statement. HyperStatements
 * are fed into a {@link DbProcessor DataBase Processor } to produce {@link
 * HyperResult Hyper Results }.
 * @author Sopasakis Pantelis
 * @author Chomenides Charalampos
 */
public class HyperStatement implements JHyperStatement{

    private String sql;
    private PreparedStatement preparedStatement;


    public HyperStatement(final String sql) throws DbException{
        
        try {
            this.preparedStatement = TheDbConnector.DB.getConnection().prepareStatement(sql);
            this.sql = sql;
        } catch (SQLException ex) {
            throw new DbException(ex);
        }
    }
    

    public void setInt(int index, int value) throws DbException {
        try {
            preparedStatement.setInt(index, value);
        } catch (SQLException ex) {
            throw new DbException(ex);
        }
    }

    public void setString(int index, String value) throws DbException {
        try {
            preparedStatement.setString(index, value);
        } catch (SQLException ex) {
            throw new DbException(ex);
        }
    }

    public void setObject(int index, Object value) throws DbException{
        try {
            preparedStatement.setObject(index, value);
        } catch (SQLException ex) {
            throw new DbException(ex);
        }
    }

    public void setObject(int index, String value, SQLDataTypes datatype) throws DbException{
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int executeUpdate() throws DbException{
        try {
            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DbException(ex);
        }
    }

    public HyperResult executeQuery() throws DbException {
        HyperResult result = new HyperResult();
        try {
            ResultSet rs = preparedStatement.executeQuery();
            
            ArrayList<String> row = new ArrayList<String>();
           
            while (rs.next()){
                for (int col_index = 0 ; col_index < rs.getMetaData().getColumnCount(); col_index++ ){
                    row.add(rs.getString(col_index+1));
                }
                result.addRow(row);
                row = new ArrayList<String>();
            }
            rs.close();
            return result;
        } catch (SQLException ex) {
            throw new DbException(ex);
        }
    }

    @Override
    public String toString(){
        return this.sql;
    }

    public void flush() throws DbException {
        try {
            this.preparedStatement.clearParameters();
        } catch (SQLException ex) {
            throw new DbException(ex);
        }
    }

    public QueryType getType(){
        if (sql.contains("SELECT")){
            return QueryType.SELECT;
        }else{
            return QueryType.UPDATE;
        }
    }

}
