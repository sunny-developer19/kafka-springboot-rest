package com.threerivers.bankingapi.controllers;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.threerivers.bankingapi.models.Account;
import com.threerivers.bankingapi.services.AccountService;

@RestController
public class AccountBalanceController {

	@Autowired
	private AccountService accountService;
	
	@GetMapping(path = "/accounts/{accountNum}")
	public Account getAccount(@PathParam("accountNum")  String accountNumber) {
		return accountService.getAccount(accountNumber);
	}
	
}
