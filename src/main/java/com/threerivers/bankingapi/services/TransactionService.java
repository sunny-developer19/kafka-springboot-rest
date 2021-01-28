package com.threerivers.bankingapi.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.threerivers.bankingapi.models.Account;
import com.threerivers.bankingapi.models.Transaction;
import com.threerivers.bankingapi.repositories.AccountRepository;
import com.threerivers.bankingapi.repositories.TransactionRepository;

@Service
public class TransactionService {

	@Autowired
	private TransactionRepository repository;
	
	@Autowired
	private AccountService accountService;
	
	public List<Transaction> getTransactionForToday(String accountNumber) {
		
		return repository.findByAccountNumberAndTransactionTsEquals(accountNumber, new Date());
	}
	
	public List<Transaction> getTransactionLast7Days(String accountNumber) {
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		cal.add(Calendar.DAY_OF_MONTH, -7);
		Date from = cal.getTime();
		return repository.findByAccountNumberAndTransactionTsBetween(accountNumber, today, from);
	}
	
	public List<Transaction> getTransactionLastMonth(String accountNumber) {
		
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		cal.add(Calendar.MONTH, -1);
		Date from = cal.getTime();
		return repository.findByAccountNumberAndTransactionTsBetween(accountNumber, today, from);
	
	}
	
	public List<Transaction> getTransactionBetweenDates(String accountNumber, Date from, Date to) {
		return repository.findByAccountNumberAndTransactionTsBetween(accountNumber, from, to);
	}
	
	public Transaction save (Transaction t) {
		Account a = accountService.getAccount(t.getAccountNumber());
		a.processTransaction(t);
		accountService.save(a);
		
		return repository.save(t);
	}
}
