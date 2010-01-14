package org.opentox.util.logging.processors;

import org.opentox.util.logging.logobject.LogObject;
import org.opentox.core.interfaces.JProcessor;

/**
 *
 * @author chung
 */
public interface ILoggingProcessor<L extends LogObject> extends JProcessor<L, L>{

    void log(L log);    


}
