package com.learn.euphy.model.common;

import java.io.Serializable;

public abstract class AbstractDao<T extends Serializable> implements IOperations<T> {

    protected Class<T> clazz;

    protected final void setClazz(final Class<T> clazzToSet) {
        if (clazzToSet == null) {
            throw new IllegalArgumentException("Entity class must not be null");
        }
        clazz = clazzToSet;
    }
}
