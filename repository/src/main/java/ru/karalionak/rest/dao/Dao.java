package ru.karalionak.rest.dao;

import java.util.Optional;

public interface Dao<T> {
    void create(T entity);

    void delete(long id);

    void update(T entity);

    Optional<T> findById(long id);
}
