package org.opentox.db.interfaces;

/**
 *
 * @author chung
 */
public interface JPrepStmt {

    String getSql();
    String[] getParameterSequence();

}
