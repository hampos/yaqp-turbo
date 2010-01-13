package org.opentox.util.logging;

import org.apache.log4j.Level;

/**
 *
 * @author chung
 */
public final class Warning extends AbstractLogObject{

    private static final Level level = Level.WARN;

    public Warning(Class clash){
        super(clash);
        super.setLogType(level);
        super.setMessage("this is a warning");
    }

    public Warning(Class clash, String message){
        super(clash, message);
        super.setLogType(level);
    }
    

    
}
