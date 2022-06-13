package com.kata.bank.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.kata.bank.model.Operation;
import com.kata.bank.model.OperationException;
import com.kata.bank.model.User;
import com.kata.bank.service.AccountService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bank/account")
public class BankAccountController {

private final AccountService accountService;
	
@PostMapping(path="/withdrawal", consumes = "application/json")
public ResponseEntity<String> makeWithdrawal(@RequestBody User user, @RequestParam double amount ){
	try {
		accountService.makeWithdrawal(user, new BigDecimal(amount));
		return ResponseEntity.ok("Operation is sucessfull");
	} catch (OperationException e) {
		throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
	}
}

@PostMapping(path="/deposit",consumes = "application/json")
public ResponseEntity<String> makeDeposit(@RequestBody User user, @RequestParam double amount ){
	try {
		accountService.makeDeposit(user, new BigDecimal(amount));
		new ResponseEntity<String>(HttpStatus.OK);
		return ResponseEntity.ok("Operation is sucessfull");
	} catch (OperationException e) {
		throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
	}
}

@PostMapping(path="/history",produces = "application/json", consumes = "application/json")
public  ResponseEntity<List<Operation>> getHistory(@RequestBody User user){
	return ResponseEntity.ok(accountService.checkHistory(user));
	
}
}
