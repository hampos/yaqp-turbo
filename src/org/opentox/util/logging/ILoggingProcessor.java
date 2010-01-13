package org.opentox.util.logging;

import org.opentox.core.interfaces.JProcessor;

/**
 *
 * @author chung
 */
public interface ILoggingProcessor<L extends LogObject> extends JProcessor<L, Object>{

    void log(L log);    


}
