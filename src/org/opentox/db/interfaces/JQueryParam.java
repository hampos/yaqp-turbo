package org.opentox.db.interfaces;

import org.opentox.db.util.SQLDataTypes;

/**
 * A Parameter for a Database Query, for example "user_name='barbie'". A Query Parameter
 * consists of two parts: The value and the datatype for this value.
 * @author Pantelis Sopasakis 
 * @author Charalampos Chomenides
 */
public interface JQueryParam {

    /**
     * Define the type of the parameter.
     * @param type type of the database parameter.
     */
    void setType(SQLDataTypes type);

    /**
     * Set the value to a parameter
     * @param value parameter value
     */
    void setValue(String value);

    /**
     *
     * @return type of the parameter.
     */
    SQLDataTypes getType();

    /**
     *
     * @return value of the parameter
     */
    String getValue();

    
    /**
     * The parameter and its value as a string.
     * @return string representation of the JQueryParam object.
     */
    @Override
    String toString();
}
