package org.opentox.db.queries;

import java.util.List;
import org.opentox.db.interfaces.IStatement;

/**
 * Update-type database queries.
 * @author chung
 */
public interface IUpdateQuery extends IStatement{


    List<QueryParam> getParameters();

}
