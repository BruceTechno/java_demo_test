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
//1.���P�_�i�Ӫ�bank �O���Onull �٦� bank�o����ƪ� account pwd  �O���O ����ƪ� amount�O���O�t��
		if (bank == null 
				|| !StringUtils.hasText(bank.getAccount()) 
				|| !StringUtils.hasText(bank.getPwd())
				|| bank.getAmount() <= 0) {
			return new BankResponse (new Bank(), "���~");
		}
						//        �H�� account �j�M��Ʈw ex:"A01"
//		Optional<Bank> op = bankDao.findById(bank.getAccount());
//		if (!op.isPresent()) {
//			return new Bank();
//		}  // �j�M"A01"�o�쪺��Ƭ�op.get        // ex: "A01" 
//		Bank resBank=op.get();  //�����즹��account���������(�T�����) �������X�� ���resBank
//      if(resBank.getPwd.equals(bank.getPwd())		
//---------------------------------------------------------------------------		
		// resBank�o�̭��񪺬O >>     �N���w��(���w��account + pwd)����ƨ��X��
		Bank resBank = bankDao.findByAccountAndPwd(bank.getAccount(),bank.getPwd());
		//���D���n�O null�N���X��k
		if (resBank==null) {
			return new BankResponse(new Bank(), "���~");
		}
		//�p�G���X�Ӫ��o�� ���O�Ū� �O�u�������D�F�誺 �i��쥻�l�B+�n�s����
		int oldAmount = resBank.getAmount();
		int depositAmount = bank.getAmount();
		int newAmount = oldAmount + depositAmount;
		resBank.setAmount(newAmount);
		return new BankResponse ( bankDao.save(resBank) , "���\");
	}

	@Override
	public BankResponse withdraw(Bank bank) {
		if (bank == null 
				|| !StringUtils.hasText(bank.getAccount()) 
				|| !StringUtils.hasText(bank.getPwd())
				|| bank.getAmount() <= 0) {
			return new BankResponse(new Bank(),"�b���αK�X���~");
		}
		Bank resBank = bankDao.findByAccountAndPwd(bank.getAccount(),bank.getPwd());
		if (resBank==null) {
			return new BankResponse(new Bank() , "��Ƥ��s�b");
		}
		int oldAmount = resBank.getAmount();
		int withdrawAmount = bank.getAmount();
		int newAmount = oldAmount - withdrawAmount;
		if (newAmount < 0 ) {
			return new BankResponse(new Bank() , "�l�B����");
		}
		resBank.setAmount(newAmount);
		return new BankResponse(bankDao.save(resBank),"���ڦ��\");
	}
}
