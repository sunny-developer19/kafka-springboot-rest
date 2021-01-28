package com.threerivers.bankingapi.controllers;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.threerivers.bankingapi.models.Transaction;
import com.threerivers.bankingapi.services.TransactionService;

@RestController
public class TransactionsController {

	@Autowired
	private TransactionService service;

	@GetMapping(path = "/accounts/{accountNum}/transactions/today")
	public List<Transaction> getTransactionsByAccountAndDateToday(@PathParam("accountNum") String accountNumber) {
		return service.getTransactionForToday(accountNumber);
	}

	@GetMapping(path = "/accounts/{accountNum}/transactions/7days")
	public List<Transaction> getTransactionsByAccountAndLast7Days(@PathParam("accountNum") String accountNumber) {
		return service.getTransactionLast7Days(accountNumber);
	}

	@GetMapping(path = "/accounts/{accountNum}/transactions/lastMonth")
	public List<Transaction> getTransactionsByAccountAndLastMonth(@PathParam("accountNum") String accountNumber) {
		return service.getTransactionLastMonth(accountNumber);
	}

	@GetMapping(path = "/accounts/{accountNum}/transactions")
	public List<Transaction> getTransactionsByAccountAndBetweenDates(@PathParam("accountNum") String accountNumber,
			@RequestParam("from") @DateTimeFormat(pattern="yyyy-MM-dd") Date from, 
			@RequestParam("to") @DateTimeFormat(pattern="yyyy-MM-dd") Date to) {
		return service.getTransactionBetweenDates(accountNumber, from, to);
	}

	/*
	 * for transaction type
	 */

	@GetMapping(path = "/accounts/{accountNum}/transactions/{type}/today")
	public List<Transaction> getTransByAccAndTypeAndDateToday(@PathParam("accountNum") String accountNumber, @PathParam("type") String type) {
		return service.getTransactionForToday(accountNumber)
				.stream()
				.filter(t -> t.getType().equals(type))
				.collect(Collectors.toList());
	}

	@GetMapping(path = "/accounts/{accountNum}/transactions/{type}/7days")
	public List<Transaction> getTransByAccAndTypeAndLast7Days(@PathParam("accountNum") String accountNumber, @PathParam("type") String type) {
		return service.getTransactionLast7Days(accountNumber)
				.stream()
				.filter(t -> t.getType().equals(type))
				.collect(Collectors.toList());
	}

	@GetMapping(path = "/accounts/{accountNum}/transactions/{type}/lastMonth")
	public List<Transaction> getTransByAccAndTypeAndLastMonth(@PathParam("accountNum") String accountNumber, @PathParam("type") String type) {
		return service.getTransactionLastMonth(accountNumber)
				.stream()
				.filter(t -> t.getType().equals(type))
				.collect(Collectors.toList());

	}

	@GetMapping(path = "/accounts/{accountNum}/transactions/{type}")
	public List<Transaction> getTransByAccAndTypeAndBetweenDates(@PathParam("accountNum") String accountNumber,
																 @PathParam("type") String type,
																 @RequestParam("from") @DateTimeFormat(pattern="yyyy-MM-dd") Date from, 
																 @RequestParam("to") @DateTimeFormat(pattern="yyyy-MM-dd") Date to) {
		return service.getTransactionBetweenDates(accountNumber, from, to)
				.stream()
				.filter(t -> t.getType().equals(type))
				.collect(Collectors.toList());

	}
}
