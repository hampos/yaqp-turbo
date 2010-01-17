package org.opentox.db.interfaces;

import java.sql.PreparedStatement;

/**
 *
 * @author chung
 */
public interface JQueryProcessor {

    PreparedStatement getPreparedStatement();

    void setPreparedStatement(PreparedStatement preparedStatement);

}
