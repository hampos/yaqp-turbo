package org.opentox.util.logging.levels;

import org.apache.log4j.Level;
import org.opentox.util.logging.logobject.AbstractLogObject;

/**
 *
 * @author chung
 */
public final class Fatal extends AbstractLogObject{

    private static final Level level = Level.FATAL;

    public Fatal(Class clash){
        super(clash);
        super.setLogType(level);
        super.setMessage("this is a fatal error");
    }

    public Fatal(Class clash, String message){
        super(clash, message);
        super.setLogType(level);
    }
}
