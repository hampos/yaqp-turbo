package org.opentox.core.interfaces;

import java.util.List;

/**
 *
 * @author chung
 */
public interface JMultiProcessor<Input, Output, P extends JProcessor> extends JTurboProcessor<Input, Output>, List<P>{

    boolean isfailSensitive();

    void setfailSensitive(boolean failSensitive);

    JMultiProcessorStatus getStatus();

}
