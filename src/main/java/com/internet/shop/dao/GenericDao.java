package com.internet.shop.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T, K> {
    T create(T product);

    Optional<T> getById(K item);

    T update(T product);

    boolean deleteById(K item);

    List<T> getAll();
}
