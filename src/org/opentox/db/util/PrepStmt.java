package org.opentox.db.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.db.interfaces.JQuery;
import org.opentox.db.processors.AbstractQueryProcessor;
import org.opentox.db.table.StandardTables;
import org.opentox.db.queries.Query;
import org.opentox.db.queries.QueryParam;

/**
 *
 * @author chung
 */
public enum PrepStmt {

    // TODO: populate the list of statements
    add_user(add_user(), QueryType.insert),
    add_algorithm_ontology(addAlgOntol(), QueryType.insert),
    add_user_role(addUserRole(), QueryType.insert);

    private PrepStmt(PreparedStatement ps, QueryType query_type) {
        private_statement = ps;
    }
    private static PreparedStatement STMT_addUser = null,
            STMT_addAlgOntol = null,
            STMT_addUserRole = null;
    private PreparedStatement private_statement = null;

    public PreparedStatement getPreparedStatement() {
        return private_statement;
    }

    public AbstractQueryProcessor getProcessor() {
        final PrepStmt t = this;
        AbstractQueryProcessor my_processor = new AbstractQueryProcessor<Object>() {

            public JQuery<Object> process(ArrayList<QueryParam<Object>> data) throws YaqpException {
                for (int i = 0; i < data.size(); i++) {
                    try {
                        if (data.get(i).getType().equals(Integer.class)) {
                            private_statement.setInt(i, new Integer(data.get(i).toString()));
                        } else if (data.get(i).getType().equals(String.class)) {
                            private_statement.setString(i, data.get(i).toString());
                        } else {
                            private_statement.setObject(i, data.get(i));
                        }
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
        return my_processor;
    }

    ;

    private static PreparedStatement add_user() {
        if (STMT_addUser == null) {
            try {
                // TODO: complete the following line...
                STMT_addUser = TheDbConnector.DB.getConnection().prepareStatement("insert into ...");
            } catch (SQLException ex) {
                // TODO: think of it laser
                // TODO: log
            }
        }
        return STMT_addUser;

    }

    private static PreparedStatement addAlgOntol() {
        if (STMT_addAlgOntol == null) {
            try {
                STMT_addAlgOntol = TheDbConnector.DB.getConnection().
                        prepareStatement("INSERT INTO " + StandardTables.ALGORITHM_ONTOLOGIES.getTableName()
                        + "(NAME, URI) VALUES (?,?)");
            } catch (SQLException ex) {
                //Logger.getLogger(PrepStmt.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return STMT_addAlgOntol;
    }

    private static PreparedStatement addUserRole() {
        if (STMT_addUserRole == null) {
            try {
                STMT_addUserRole = TheDbConnector.DB.getConnection().prepareStatement("INSERT INTO "
                        + StandardTables.USER_AUTH.getTableName() + "(NAME, USER_LEVEL) VALUES (?,?)");
            } catch (SQLException ex) {
                //Logger.getLogger(PrepStmt.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return STMT_addUserRole;
    }

    public static void prepareAll() {
        STMT_addUser = add_user();
        STMT_addUserRole = addUserRole();
        STMT_addAlgOntol = addAlgOntol();
        // TODO: etc...
        // TODO: Do this more automatized
    }
}

    

    

    

