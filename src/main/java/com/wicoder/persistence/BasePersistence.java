package com.wicoder.persistence;

import com.wicoder.filter.FormHQl;

public interface BasePersistence<T> {

    <T extends Object> FormHQl db();

}
