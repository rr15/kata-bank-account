package com.kata.bank.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Operation {
    private LocalDateTime date;
    private BigDecimal amount;
    private BigDecimal balance;
    private OperationType type;

}
