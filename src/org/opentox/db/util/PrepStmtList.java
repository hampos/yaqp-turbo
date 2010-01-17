package org.opentox.db.util;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.interfaces.JProcessor;
import org.opentox.core.processors.Processor;
import org.opentox.db.interfaces.JQuery;
import org.opentox.db.interfaces.JQueryParam;
import org.opentox.db.queries.Query;

/**
 *
 * @author chung
 */
public class PrepStmtList {

    private static PreparedStatement STMT_addUser = null;

    public enum PREP_STMT{
        add_user(add_user(), QueryType.insert),
        // TODO: populate the list of statements
        ;
        
        private PreparedStatement private_statement = null;

        private PREP_STMT(PreparedStatement ps, QueryType query_type){
            private_statement = ps;
        }

        private PreparedStatement getPrepStmt(){
            return private_statement;
        }


        public JProcessor /* Might not be JProcessor but something more specific */
                <ArrayList<JQueryParam>, JQuery> getProcessor()
        {
            final PREP_STMT t = this;
            JProcessor<ArrayList<JQueryParam>, JQuery> my_processor = new Processor<ArrayList<JQueryParam>, JQuery>() {

                public JQuery process(ArrayList<JQueryParam> data) throws YaqpException {
                    for (int i=0;i<data.size();i++){
                        try {
                            t.getPrepStmt().setObject(i, data.get(i));
                        } catch (SQLException ex) {
                            throw new YaqpException("Illegal preparation of statement!");
                        }
                    }
                    Query result = new Query();
                    result.setPrepStmt(t);
                    result.setParameters(data);
                    return result;
                }
            };
            throw new UnsupportedOperationException();
        };

        
        
    }




    public static PreparedStatement add_user() {
        if (STMT_addUser == null) {
            try {
                // TODO: complete the following line...
                STMT_addUser = DbConnector.INSTANCE.getConnection().prepareStatement("insert into ");
            } catch (SQLException ex) {
                // TODO: think of it laser
                Logger.getLogger(PrepStmtList.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return STMT_addUser;

    }

    public static void prepareAll() {
        STMT_addUser = add_user();
        // TODO: etc...
    }
}
