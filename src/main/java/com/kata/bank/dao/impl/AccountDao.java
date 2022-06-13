package com.kata.bank.dao.impl;


import org.springframework.stereotype.Repository;

import com.kata.bank.dao.FakeDao;
import com.kata.bank.model.Account;
import com.kata.bank.model.User;
@Repository
public class AccountDao extends FakeDao<Account> {
    @Override
    public Account findById(Integer id) {
        return repository.stream().filter(item -> item.getId().equals(id)).findFirst().orElse(null);
    }

    public Account findByCustomer(User customer) {
        return repository.stream().filter(item -> item.getCustomer().equals(customer)).findFirst().orElse(null);
    }
}
