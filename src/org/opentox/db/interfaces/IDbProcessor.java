package org.opentox.db.interfaces;

import java.sql.Connection;
import org.opentox.db.exceptions.DbException;
import org.opentox.core.interfaces.JProcessor;

/**
 *
 * @author chung
 */
public interface IDbProcessor<InputData, Result> extends JProcessor<InputData, Result>, IDbAccess{

    void setConnection(Connection connection) throws DbException;
    Connection getConnection();

}
