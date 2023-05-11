package com.example.java_demo_test.service.impl;

import java.util.Optional;

import javax.security.auth.login.AccountException;

//import org.graalvm.compiler.core.common.type.ArithmeticOpTable.Op;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import com.example.java_demo_test.entity.Bank;
import com.example.java_demo_test.repository.BankDao;
import com.example.java_demo_test.service.ifs.BankService;
import com.example.java_demo_test.vo.BankResponse;

@Service
public class BankServiceImpl implements BankService {
	private String pattern = "\\w{3,8}";
	private String patternPwd = "[\\w~!@#$%^&*]{8,16}";
	@Autowired
	private BankDao bankDao;

	@Override
	public void addInfo(Bank bank) {
		// Check account
		checkAccount(bank);
		// check pwd
		checkPwd(bank);

		// Check amount
		if (bank.getAmount() < 0) {
			System.out.println("Amount is error");
			return;
		}

		// check pwd 1. not null 2.no space or contain space 3.length = 8~16
		// 4. contain special symbol like @!#%
		// check amount : cant be negative

		bankDao.save(bank);
	}

	private void checkAccount(Bank bank) {
		if (bank == null) {
			System.out.println("Bank is null");
			return;
		}
		String account = bank.getAccount();
		if (account == null) {
			System.out.println("Account is null");
			return;
		}
		if (!account.matches(pattern)) {
			System.out.println("Account is error");
			return;
		}
	}

	private void checkPwd(Bank bank) {
		String pwd = bank.getPwd();
		if (pwd == null) {
			System.out.println("Password is null");
			return;
		}
		if (!pwd.matches(patternPwd)) {
			System.out.println("Password format is erreor");
			return;
		}
	}

	@Override
	public Bank getAmountById(String id) {
		if (!StringUtils.hasText(id)) {
			return new Bank();
		}
		Optional<Bank> op = bankDao.findById(id);
		Bank bank = op.orElse(new Bank());
		return op.orElse(new Bank());
	}

	@Override
	public BankResponse deposit(Bank bank) {
//1.先判斷進來的bank 是不是null 還有 bank這筆資料的 account pwd  是不是 有資料的 amount是不是負的
		if (bank == null 
				|| !StringUtils.hasText(bank.getAccount()) 
				|| !StringUtils.hasText(bank.getPwd())
				|| bank.getAmount() <= 0) {
			return new BankResponse (new Bank(), "錯誤");
		}
						//        以此 account 搜尋資料庫 ex:"A01"
//		Optional<Bank> op = bankDao.findById(bank.getAccount());
//		if (!op.isPresent()) {
//			return new Bank();
//		}  // 搜尋"A01"得到的資料為op.get        // ex: "A01" 
//		Bank resBank=op.get();  //對應到此筆account的全部資料(三筆資料) 全部提出來 放到resBank
//      if(resBank.getPwd.equals(bank.getPwd())		
//---------------------------------------------------------------------------		
		// resBank這裡面放的是 >>     將指定筆(指定的account + pwd)的資料取出來
		Bank resBank = bankDao.findByAccountAndPwd(bank.getAccount(),bank.getPwd());
		//取道的要是 null就跳出方法
		if (resBank==null) {
			return new BankResponse(new Bank(), "錯誤");
		}
		//如果取出來的這筆 不是空的 是真的有取道東西的 進行原本餘額+要存的錢
		int oldAmount = resBank.getAmount();
		int depositAmount = bank.getAmount();
		int newAmount = oldAmount + depositAmount;
		resBank.setAmount(newAmount);
		return new BankResponse ( bankDao.save(resBank) , "成功");
	}

	@Override
	public BankResponse withdraw(Bank bank) {
		if (bank == null 
				|| !StringUtils.hasText(bank.getAccount()) 
				|| !StringUtils.hasText(bank.getPwd())
				|| bank.getAmount() <= 0) {
			return new BankResponse(new Bank(),"帳號或密碼錯誤");
		}
		Bank resBank = bankDao.findByAccountAndPwd(bank.getAccount(),bank.getPwd());
		if (resBank==null) {
			return new BankResponse(new Bank() , "資料不存在");
		}
		int oldAmount = resBank.getAmount();
		int withdrawAmount = bank.getAmount();
		int newAmount = oldAmount - withdrawAmount;
		if (newAmount < 0 ) {
			return new BankResponse(new Bank() , "餘額不足");
		}
		resBank.setAmount(newAmount);
		return new BankResponse(bankDao.save(resBank),"提款成功");
	}
}
