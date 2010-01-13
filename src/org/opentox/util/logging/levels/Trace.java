package org.opentox.util.logging.levels;

import org.apache.log4j.Level;
import org.opentox.util.logging.logobject.AbstractLogObject;

/**
 *
 * @author chung
 */
public class Trace extends AbstractLogObject{

    private static final Level level = Level.TRACE;

    public Trace(Class clash){
        super(clash);
        super.setLogType(level);
        super.setMessage("this is just a trace");
    }

    public Trace(Class clash, String message){
        super(clash, message);
        super.setLogType(level);
    }

}
