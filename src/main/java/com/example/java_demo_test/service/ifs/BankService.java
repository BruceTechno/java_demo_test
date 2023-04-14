package com.example.java_demo_test.service.ifs;

import com.example.java_demo_test.entity.Bank;
import com.example.java_demo_test.vo.BankResponse;

public interface BankService {
	public void addInfo(Bank bank);
	
	public Bank getAmountById(String id);
	
	public BankResponse deposit(Bank bank);
	
	public BankResponse withdraw(Bank bank);
}
