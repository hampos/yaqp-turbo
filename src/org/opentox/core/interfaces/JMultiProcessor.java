package org.opentox.core.interfaces;

/**
 *
 * @author chung
 */
public interface JMultiProcessor<Input, Output> extends JTurboProcessor<Input, Output>{

    boolean isfailSensitive();

    void setfailSensitive(boolean failSensitive);

    JMultiProcessorStatus getStatus();

}
