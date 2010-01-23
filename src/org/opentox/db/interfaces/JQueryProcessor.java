package org.opentox.db.interfaces;


import org.opentox.db.util.PrepStmt;

/**
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface JQueryProcessor {

    PrepStmt getPrepStmt();

    void setPrepStmt(PrepStmt prepStmt);


}
