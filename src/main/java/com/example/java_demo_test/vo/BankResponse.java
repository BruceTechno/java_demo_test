package com.example.java_demo_test.vo;

import com.example.java_demo_test.entity.Bank;

public class BankResponse {
	
	private Bank bank ;
	
	private String message ;


	public BankResponse(Bank bank, String message) {
		super();
		this.bank = bank;
		this.message = message;
	}
	
	public BankResponse() {
		super();
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
