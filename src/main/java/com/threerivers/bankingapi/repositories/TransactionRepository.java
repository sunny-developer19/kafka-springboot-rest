package com.threerivers.bankingapi.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.threerivers.bankingapi.models.Transaction;


public interface TransactionRepository extends CrudRepository<Transaction, String> {
	public List<Transaction> findByAccountNumberAndTransactionTsBetween(String accountNumber, Date from, Date to);

	public List<Transaction> findByAccountNumberAndTransactionTsEquals(String accountNumber, Date date);

}
