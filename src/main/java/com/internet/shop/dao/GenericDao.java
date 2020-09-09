package com.internet.shop.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T, K> {
    T create(T item);

    Optional<T> getById(K item);

    T update(T item);

    boolean deleteById(K item);

    List<T> getAll();
}
