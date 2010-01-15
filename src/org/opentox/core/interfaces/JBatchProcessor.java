package org.opentox.core.interfaces;

import java.util.ArrayList;
import org.opentox.core.util.MultiProcessorStatus;

/**
 *
 * @author chung
 */
public interface JBatchProcessor<Input, Output, P extends JProcessor<Input, Output>>
        extends JTurboProcessor<ArrayList<Input>, ArrayList<Output>> {
    
    MultiProcessorStatus getStatus();

    boolean isfailSensitive();

    void setfailSensitive(boolean failSensitive);

    void setProcessor(final P processor);

    P getProcessor();

}
