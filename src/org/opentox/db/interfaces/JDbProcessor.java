package org.opentox.db.interfaces;

import org.opentox.core.interfaces.JProcessor;

/**
 *
 * @author chung
 */
public interface JDbProcessor<InputData, Result> extends JProcessor<InputData, Result>{

    public abstract Result execute(InputData q);

}
