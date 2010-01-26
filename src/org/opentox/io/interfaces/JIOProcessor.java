
package org.opentox.io.interfaces;

import org.opentox.core.exceptions.YaqpIOException;
import org.opentox.core.interfaces.JProcessor;

/**
 *
 * @author hampos
 */
public interface JIOProcessor<Input,Output> extends JProcessor<Input,Output> {

    public abstract Output handle(Input i) throws YaqpIOException;

}
