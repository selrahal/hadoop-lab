package com.rhc.hadoop.model;

import com.rhc.hadoop.drools.map.ReducerFact;

public class TransactionCreditCardReducer implements ReducerFact {
	private String creditCard;
	private double totalAmount = 0;
	
	public TransactionCreditCardReducer(String creditCard) {
		this.creditCard = creditCard;
	}
	
	public void setKey(String key) {
		this.creditCard = key;
	}
	
	public void add(double amount) {
		this.totalAmount += amount;
	}

	public String getKey() {
		return creditCard;
	}

	public String getValue() {
		return Double.toString(totalAmount);
	}

}
