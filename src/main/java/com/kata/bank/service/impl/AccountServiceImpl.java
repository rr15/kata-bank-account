package com.kata.bank.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kata.bank.dao.impl.AccountDao;
import com.kata.bank.model.Account;
import com.kata.bank.model.Operation;
import com.kata.bank.model.OperationException;
import com.kata.bank.model.OperationType;
import com.kata.bank.model.User;
import com.kata.bank.service.AccountService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountDao accountDAO;

    @Autowired
    public AccountServiceImpl(AccountDao accountDAO) {
        this.accountDAO = accountDAO;
    }

    @Override
    public void makeDeposit(User customer, BigDecimal amount) throws OperationException {
        makeOperation(customer, amount, OperationType.DEPOSIT);
    }

    @Override
    public void makeWithdrawal(User customer, BigDecimal amount) throws OperationException {
        makeOperation(customer, amount, OperationType.WITHDRAWAL);
    }

    @Override
    public BigDecimal checkBalance(User customer) {
        Account account = accountDAO.findByCustomer(customer);
        if (account != null) {
            return account.getBalance();
        }
        return null;
    }

    @Override
    public List<Operation> checkHistory(User customer) {
        Account account = accountDAO.findByCustomer(customer);
        if (account != null) {
            return account.getHistory();
        }
        return Collections.emptyList();
    }

    private void makeOperation(User customer, BigDecimal amount, OperationType operationType) throws OperationException {
        Account account = accountDAO.findByCustomer(customer);
        if (account != null) {
            BigDecimal balance = updateBalance(account, amount, operationType);
            saveOperation(account, amount, balance, operationType);
        }
    }

    private BigDecimal updateBalance(Account account, BigDecimal amount, OperationType operationType) throws OperationException {
        if (BigDecimal.ZERO.compareTo(amount) > 0) {
            throw new OperationException("Amount should be positive");
        }
        if (OperationType.DEPOSIT.equals(operationType)) {
            account.setBalance(account.getBalance().add(amount));
        } else if (OperationType.WITHDRAWAL.equals(operationType)) {
            account.setBalance(account.getBalance().subtract(amount));
        }
        return account.getBalance();
    }

    private void saveOperation(Account account, BigDecimal amount, BigDecimal balance, OperationType operationType) {
        Operation operation = Operation.builder().amount(amount).balance(balance).type(operationType).date(LocalDateTime.now()).build();
        account.getHistory().add(operation);
    }
}
