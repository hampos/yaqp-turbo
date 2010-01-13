package org.opentox.db.queries;

/**
 *
 * @author chung
 */
public interface IQueryParam<T> {

    void setType(Class type);

    void setValue(T value);

    Class getType();

    T getValue();

    @Override
    String toString();
}
