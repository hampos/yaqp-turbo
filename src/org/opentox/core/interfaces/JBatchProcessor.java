package org.opentox.core.interfaces;

import java.util.ArrayList;

/**
 * 
 * @author chung
 */
public interface JBatchProcessor<Input, Output> extends JTurboProcessor<ArrayList<Input>, ArrayList<Output>>
         {

    JMultiProcessorStatus getStatus();

    boolean isfailSensitive();

    void setfailSensitive(boolean failSensitive);
}
