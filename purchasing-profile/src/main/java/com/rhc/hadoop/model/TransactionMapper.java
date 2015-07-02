package com.rhc.hadoop.model;

import com.rhc.hadoop.drools.map.MapperFact;

public class TransactionMapper implements MapperFact<Transaction> {
	private Transaction transaction;
	private String key;
	
	public TransactionMapper(Transaction value) {
		transaction = value;
	}
	
	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return transaction.toString();
	}

	public Transaction getTransaction() {
		return transaction;
	}

}
