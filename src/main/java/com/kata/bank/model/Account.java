package com.kata.bank.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private Integer id;
    private User customer;
    private BigDecimal balance = BigDecimal.ZERO;
    private List<Operation> history = new ArrayList<>();

    
}
