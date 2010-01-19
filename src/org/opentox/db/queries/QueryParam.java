package org.opentox.db.queries;

import org.opentox.db.interfaces.JQueryParam;

/**
 *
 * @author chung
 */
public class QueryParam<T> implements JQueryParam<T> {

    protected Class type;
    protected T value;
    protected String parameterName;

    public QueryParam(Class type, T value) {
        setType(type);
        setValue(value);
    }

    public void setType(Class type) {
        this.type = type;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Class getType() {
        return type;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (value == null) {
            return null;
        } else {
            return value.toString();
        }
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }
}
