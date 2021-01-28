package com.threerivers.bankingapi.models;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Transaction {
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private int id;
	private String accountNumber;
	private Date transactionTs;
	private String type;
	private double amount;
	
	public Transaction() {
		// TODO Auto-generated constructor stub
		
	}

	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * @return the transactionTs
	 */
	public Date getTransactionTs() {
		return transactionTs;
	}

	/**
	 * @param transactionTs the transactionTs to set
	 */
	public void setTransactionTs(Date transactionTs) {
		this.transactionTs = transactionTs;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	

}
