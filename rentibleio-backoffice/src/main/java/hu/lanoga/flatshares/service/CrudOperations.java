package hu.lanoga.flatshares.service;

import java.util.List;

public interface CrudOperations<T> {

    int save(T entity);

    int update(T entity);

    T findOne(int id);

    List<T> findAll();

    void delete(int id);
}
