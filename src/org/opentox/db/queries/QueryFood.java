package org.opentox.db.queries;

import java.util.HashMap;
import org.opentox.db.interfaces.JQueryFood;

/**
 *
 * @author chung
 */
public class QueryFood implements JQueryFood {

    private HashMap<String, String> nameValuePairs = new HashMap<String, String>();

    public void add(String name, String value) {
        nameValuePairs.put(name, value);
    }

    public String getValue(String name) {
        return nameValuePairs.get(name);
    }

    public boolean containsName(String name) {
        return nameValuePairs.containsKey(name);
    }

    public boolean containsValue(String value) {
        return nameValuePairs.containsValue(value);
    }

    public void flush() {
        nameValuePairs.clear();
    }
}
