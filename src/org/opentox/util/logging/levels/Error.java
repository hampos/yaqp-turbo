package org.opentox.util.logging.levels;

import org.apache.log4j.Level;
import org.opentox.util.logging.logobject.AbstractLogObject;

/**
 *
 * @author chung
 */
public final class Error extends AbstractLogObject {

    private static final Level level = Level.ERROR;

    public Error(Class clash) {
        super(clash);
        super.setLogType(level);
        super.setMessage("this is an error");
    }

    public Error(Class clash, String message) {
        super(clash, message);
        super.setLogType(level);
    }
}
