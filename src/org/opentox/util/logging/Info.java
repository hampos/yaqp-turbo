package org.opentox.util.logging;

import org.apache.log4j.Level;

/**
 *
 * @author chung
 */
public final class Info extends AbstractLogObject{

    private static final Level level = Level.INFO;

 public Info(Class clash){
        super(clash);
        super.setLogType(level);
        super.setMessage("this is a warning");
    }

    public Info(Class clash, String message){
        super(clash, message);
        super.setLogType(level);
    }
}
