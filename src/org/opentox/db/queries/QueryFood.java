package org.opentox.db.queries;

import java.util.HashMap;
import org.opentox.db.interfaces.JQueryFood;
import org.opentox.db.processors.DbPipeline;
import org.opentox.db.processors.QueryProcessor;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Debug;

/**
 * The set of parameters one needs to provide to a database query, in order
 * to prepare a certain statement before execution. QueryFood posseses the
 * set of name-value pairs. The class contains also methods for the addition
 * and retrieval of certain name-value pairs. The query food is provided as
 * input to the various {@link QueryProcessor Query Processors} and {@link
 * DbPipeline DB Pipelines}.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 *
 * @see org.opentox.db.handlers.ReaderHandler db readers
 * @see org.opentox.db.handlers.WriterHandler db writers
 */
public class QueryFood implements JQueryFood {

    public QueryFood(){
        super();
    }

    public QueryFood(HashMap<String, String> nameValuePairs){
        super();
        this.nameValuePairs = nameValuePairs;
    }

    public QueryFood(String[][] nameValuePairs){
        super();
        for (int i=0;i<nameValuePairs.length;i++){
            try{
                add(nameValuePairs[i][0], nameValuePairs[i][1]);
            }catch (ArrayIndexOutOfBoundsException aioob){
                YaqpLogger.LOG.log(new Debug(getClass(), "ArrayIndexOutOfBounds while constructing QueryFood : "+aioob));
            }
        }
    }

    private HashMap<String, String> nameValuePairs = new HashMap<String, String>();

    public void add(String name, String value) {
        nameValuePairs.put(name, value);
    }

    public String getValue(String name) {
        return nameValuePairs.get(name);
    }

    /**
     * Retrieve whether a parameter is contained in the QueryFood.
     * @param name naem of a parameter
     * @return true if a certain parameter is contained in the QueryFood
     */
    public boolean containsName(String name) {
        return nameValuePairs.containsKey(name);
    }

    /**
     * Retrieve whether a certain value is contained in the QueryFood.
     * @param value a value
     * @return true if the QueryFood contains the given value
     */
    public boolean containsValue(String value) {
        return nameValuePairs.containsValue(value);
    }

    /**
     * Remove all name-value pairs from the query food. After that, there's
     * nothing to eat!
     */
    public void flush() {
        nameValuePairs.clear();
    }
}
