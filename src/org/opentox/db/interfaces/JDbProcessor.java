package org.opentox.db.interfaces;

import org.opentox.core.interfaces.JProcessor;
import org.opentox.db.exceptions.DbException;

/**
 *
 * @author chung
 */
public interface JDbProcessor<InputData, Result> extends JProcessor<InputData, Result>{

    public abstract Result execute(InputData q) throws DbException;

}
