package org.opentox.core.interfaces;

import java.io.OutputStream;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.core.exceptions.YaqpIOException;

/**
 *
 * @author hampos
 */
public interface JEngine<Fuel, Gas> {

    Gas ignite(Fuel fuel) throws YaqpException;

    Fuel exhaust(Gas gas) throws YaqpException;
    
}
