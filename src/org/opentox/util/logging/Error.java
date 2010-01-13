package org.opentox.util.logging;

import org.apache.log4j.Level;

/**
 *
 * @author chung
 */
public final class Error extends AbstractLogObject {

    private static final Level level = Level.ERROR;

    public Error(Class clash) {
        super(clash);
        super.setLogType(level);
        super.setMessage("this is a warning");
    }

    public Error(Class clash, String message) {
        super(clash, message);
        super.setLogType(level);
    }
}
