package org.opentox.db.interfaces;

/**
 *
 * @author chung
 */
public interface JQueryFood {

    void add(String name, String value);
    String getValue(String name);
    boolean containsName(String name);
    boolean containsValue(String value);
    void flush();

}
