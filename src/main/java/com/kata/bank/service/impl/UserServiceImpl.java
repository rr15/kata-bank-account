package com.kata.bank.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kata.bank.dao.impl.UserDao;
import com.kata.bank.model.User;
import com.kata.bank.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDAO) {
        this.userDao = userDAO;
    }

    @Override
    public User findById(Integer id) {
        return userDao.findById(id);
    }
}
