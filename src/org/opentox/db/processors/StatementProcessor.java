package org.opentox.db.processors;


import java.sql.Connection;
import java.sql.SQLException;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.interfaces.IStatement;
import org.opentox.core.exceptions.ProcessorException;
import org.opentox.core.exceptions.YaqpException;

/**
 *
 * @author chung
 */
public abstract class StatementProcessor<Query extends IStatement, Results>
        extends AbstractDbProcessor<Query, Results>{

    public void open() throws DbException {

        
    }

    @Override
    public Results process(Query target) throws YaqpException {
		Connection c = getConnection();
		if (c == null) throw new YaqpException("no connection");
		try {
			return	execute(c,target);
		} catch (Exception x) {
			throw new ProcessorException(this,x);
		}
	}

    protected abstract Results execute(Connection c,Query target) throws SQLException, YaqpException ;

}
