package org.opentox.db.interfaces;

/**
 * A Parameter for a Database Query, for example "user_name='barbie'"
 * @author Sopasakis Pantelis
 * @author Charalampos Chomenides
 */
public interface JQueryParam<T> {

    /**
     * Define the type of the parameter.
     * @param type type of the database parameter.
     */
    void setType(Class type);

    /**
     * Set the value to a parameter
     * @param value
     */
    void setValue(T value);

    /**
     *
     * @return type of the parameter.
     */
    Class getType();

    /**
     *
     * @return value of the parameter
     */
    T getValue();

    /**
     * The parameter as a string
     * @return
     */
    @Override
    String toString();
}
