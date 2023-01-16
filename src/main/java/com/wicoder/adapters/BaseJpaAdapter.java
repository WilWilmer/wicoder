package com.wicoder.adapters;

import com.wicoder.filter.FormHQl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class BaseJpaAdapter<T> {

    @PersistenceContext
    protected EntityManager manager;

    public FormHQl db() {
        return  new FormHQl<>(manager);
    }

}
