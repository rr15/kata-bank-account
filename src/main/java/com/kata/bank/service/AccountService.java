package com.kata.bank.service;



import java.math.BigDecimal;
import java.util.List;

import com.kata.bank.model.Operation;
import com.kata.bank.model.OperationException;
import com.kata.bank.model.User;

public interface AccountService {
    void makeDeposit(User customer, BigDecimal amount) throws OperationException;

    void makeWithdrawal(User customer, BigDecimal amount) throws OperationException;

    BigDecimal checkBalance(User customer);

    List<Operation> checkHistory(User customer);
}
