package com.kata.bank.dao.impl;


import org.springframework.stereotype.Repository;

import com.kata.bank.dao.FakeDao;
import com.kata.bank.model.User;

@Repository
public class UserDao extends FakeDao<User> {
    @Override
    public User findById(Integer id) {
        return repository.stream().filter(item -> item.getId().equals(id)).findFirst().orElse(null);
    }
}
