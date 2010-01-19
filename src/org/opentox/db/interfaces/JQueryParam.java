package org.opentox.db.interfaces;

/**
 * A Parameter for a Database Query, for example "user_name='barbie'". A Query Parameter
 * consists of two parts: The value and the datatype for this value.
 * @author Sopasakis Pantelis
 * @author Charalampos Chomenides
 * @param <T> Generic Datatype for the value of the Query Parameter.
 */
public interface JQueryParam<T> {

    /**
     * Define the type of the parameter.
     * @param type type of the database parameter.
     */
    void setType(Class type);

    /**
     * Set the value to a parameter
     * @param value parameter value
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
     *
     * @return The name of the parameter normally corresponding to
     * the name of a column in a table of the database.
     */
    String getParameterName();


    /**
     *
     * @param parameterName the Name of the parameter.
     */
    void setParameterName(String parameterName);

    /**
     * The parameter and its value as a string.
     * @return
     */
    @Override
    String toString();
}
