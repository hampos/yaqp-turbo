package org.opentox.util.logging;

import org.apache.log4j.Level;

/**
 *
 * @author chung
 */
public class AbstractLogObject implements LogObject{

    private String message = "";
    private Level level = Level.WARN;
    private Class source = null;

    public AbstractLogObject(Class clash) {
        this.source = clash;
    }

    public AbstractLogObject(Class clash, String message) {
        this.source = clash;
        this.message = message;
    }

    

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public void setLogType(Level level) {
        this.level = level;
    }

    public void setSource(Class clash) {
        this.source = clash;
    }

    public Class getSource() {
        return source;
    }

    public Level getLevel() {
        return level;
    }

}
