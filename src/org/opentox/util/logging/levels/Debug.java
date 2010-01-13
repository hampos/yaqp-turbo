package org.opentox.util.logging.levels;

import org.apache.log4j.Level;
import org.opentox.util.logging.logobject.AbstractLogObject;

/**
 *
 * @author chung
 */
public final class Debug extends AbstractLogObject {

    private static final Level level = Level.DEBUG;

    public Debug(Class clash) {
        super(clash);
        super.setLogType(level);
        super.setMessage("this is a debug message");
    }

    public Debug(Class clash, String message) {
        super(clash, message);
        super.setLogType(level);
    }
}
