package org.opentox.db.interfaces;

import java.sql.Connection;
import org.opentox.db.exceptions.DbException;
import org.opentox.core.interfaces.JProcessor;

/**
 *
 * @author chung
 */
public interface JDbProcessor<InputData, Result> extends JProcessor<InputData, Result>{

    Connection getConnection() throws DbException;

    void disconnect() throws DbException;


}
