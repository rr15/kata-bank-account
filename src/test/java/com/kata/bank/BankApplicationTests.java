package com.kata.bank;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kata.bank.dao.impl.AccountDao;
import com.kata.bank.dao.impl.UserDao;
import com.kata.bank.model.Account;
import com.kata.bank.model.Operation;
import com.kata.bank.model.OperationException;
import com.kata.bank.model.OperationType;
import com.kata.bank.model.User;
import com.kata.bank.service.AccountService;
import com.kata.bank.service.UserService;

@SpringBootTest
class BankApplicationTests {
    private static final int USER_ID = 123;
    private static final int ACCOUNT_ID = 456;
    private static final BigDecimal AMOUNT_NEGATIVE = BigDecimal.TEN.negate();
    private static final BigDecimal AMOUNT_ONE = BigDecimal.valueOf(100.01);
    private static final BigDecimal AMOUNT_TWO = BigDecimal.valueOf(15.99);

    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserDao userDAO;
    @Autowired
    private AccountDao accountDAO;

    @BeforeEach
    public void setUp() {
        User customer = new User();
        customer.setId(USER_ID);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        userDAO.persist(customer);

        Account account = new Account();
        account.setId(ACCOUNT_ID);
        account.setCustomer(customer);
        account.setBalance(BigDecimal.ZERO);
        accountDAO.persist(account);
    }

    @AfterEach
    public void tearDown() {
        User customer = userDAO.findById(USER_ID);
        userDAO.remove(customer);
        Account account = accountDAO.findById(ACCOUNT_ID);
        accountDAO.remove(account);
    }

    @Test
    public void testNoCustomer() throws OperationException {
        accountService.makeDeposit(null, BigDecimal.ZERO);
        accountService.makeWithdrawal(null, BigDecimal.ZERO);
        accountService.checkBalance(null);
        accountService.checkHistory(null);
    }

    @Test
    void testNegativeAmount() {
        User customer = userService.findById(USER_ID);
        assertThatThrownBy(() -> accountService.makeDeposit(customer, AMOUNT_NEGATIVE))
                .isInstanceOf(OperationException.class).hasMessageContaining("should be positive");
    }

    @Test
    void testEmptyAccount() {
        User customer = userService.findById(USER_ID);
        BigDecimal initialBalance = accountService.checkBalance(customer);
        assertThat(initialBalance).isEqualByComparingTo(BigDecimal.ZERO);
        List<Operation> history = accountService.checkHistory(customer);
        assertThat(history).isEmpty();
    }

    @Test
    void testMakeDeposit() throws OperationException {
        User customer = userService.findById(USER_ID);
        BigDecimal initialBalance = accountService.checkBalance(customer);
        accountService.makeDeposit(customer, AMOUNT_ONE);
        BigDecimal newBalance = accountService.checkBalance(customer);
        assertThat(newBalance).isEqualByComparingTo(initialBalance.add(AMOUNT_ONE));
    }

    @Test
    void testMakeWithdrawal() throws OperationException {
        User customer = userService.findById(USER_ID);
        BigDecimal initialBalance = accountService.checkBalance(customer);
        accountService.makeWithdrawal(customer, AMOUNT_ONE);
        BigDecimal newBalance = accountService.checkBalance(customer);
        assertThat(newBalance).isEqualByComparingTo(initialBalance.subtract(AMOUNT_ONE));
    }

    @Test
    void testOperationHistory() throws OperationException {
        User customer = userService.findById(USER_ID);
        accountService.makeDeposit(customer, AMOUNT_ONE);
        accountService.makeWithdrawal(customer, AMOUNT_TWO);
        List<Operation> history = accountService.checkHistory(customer);
        assertThat(history).hasSize(2);

        Operation operation = history.get(0);
        assertThat(operation.getDate()).isBefore(LocalDateTime.now());
        assertThat(operation.getAmount()).isEqualByComparingTo(AMOUNT_ONE);
        assertThat(operation.getType()).isEqualTo(OperationType.DEPOSIT);
        assertThat(operation.getBalance()).isEqualByComparingTo(AMOUNT_ONE);

        operation = history.get(1);
        assertThat(operation.getDate()).isBefore(LocalDateTime.now());
        assertThat(operation.getAmount()).isEqualByComparingTo(AMOUNT_TWO);
        assertThat(operation.getType()).isEqualTo(OperationType.WITHDRAWAL);
        assertThat(operation.getBalance()).isEqualByComparingTo(AMOUNT_ONE.subtract(AMOUNT_TWO));
    }
}
