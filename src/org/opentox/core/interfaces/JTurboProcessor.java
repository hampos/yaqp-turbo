package org.opentox.core.interfaces;

import java.beans.PropertyChangeListener;

/**
 * An interface for processor with change support. Contains methods to add and
 * remove property listeners for specific properties.
 * @author chung
 */
public interface JTurboProcessor<Input, Output> extends JProcessor<Input, Output> {

    void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener);

    void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener);

    void addPropertyChangeListener(final PropertyChangeListener listener);

    void removePropertyChangeListener(final PropertyChangeListener listener);
}
