package com.kata.bank.dao;

import java.util.HashSet;
import java.util.Set;

public abstract class FakeDao<T> {
    protected Set<T> repository = new HashSet<>();

    public abstract T findById(Integer id);

    public boolean persist(T object) {
        return repository.add(object);
    }

    public boolean remove(T object) {
        return repository.remove(object);
    }
}
