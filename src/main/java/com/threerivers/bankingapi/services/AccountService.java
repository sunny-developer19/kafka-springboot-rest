package com.threerivers.bankingapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.threerivers.bankingapi.models.Account;
import com.threerivers.bankingapi.repositories.AccountRepository;

@Service
public class AccountService {
	
	@Autowired
	private AccountRepository accountRepository;
	
	public Account getAccount(String accountNum) {
		return accountRepository.findById(accountNum).get();
	}
	
	public Account save(Account a) {
		return accountRepository.save(a);
	}
	

}
