package org.opentox.db.interfaces;


import java.util.ArrayList;
import org.opentox.core.interfaces.JProcessor;
import org.opentox.db.util.PrepStmt;

/**
 *
 * @author chung
 * @param <ParameterType> Type for the parameters of the query
 */
public interface JQueryProcessor {

    PrepStmt getPrepStmt();

    void setPrepStmt(PrepStmt prepStmt);


}
