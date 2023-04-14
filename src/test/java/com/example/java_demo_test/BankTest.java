package com.example.java_demo_test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.example.java_demo_test.entity.Bank;
import com.example.java_demo_test.repository.BankDao;
import com.example.java_demo_test.service.ifs.BankService;
import com.example.java_demo_test.vo.BankResponse;

@SpringBootTest(classes = JavaDemoTestApplication.class)
public class BankTest {

	@Autowired
	private BankDao bankDao;
	
	@Autowired
	private BankService bankService;
	
	@Test
	public void addBankInfo () {
		Bank bank = new Bank("A01","AA123",16888);
		//�s�W��ƨ�bank �o�i��
		bankDao.save(bank);
	}

	@Test
	public void addInfoTest() {
		Bank bank = new Bank ("AA999","AA123456@",2000);
		bankService.addInfo(bank);
	}
	
	@Test
	public void getAmountByIdTest() {
		Bank bank = bankService.getAmountById("A01");
//		Assert.isTrue(bank.getAmount()==16888, "�d�L���");
		System.out.println("�b�� :" + bank.getAccount() + "�l�B :" + bank.getAmount());
	}
	
	@Test
	public void depositTest() {
		//���q�쥻�l�B �A�s�� �A�q���G
//		Bank bank = new Bank ("AA999","AA123456@",2000);
		
		//�Ыذ����
		Bank oldBank = bankDao.save(new Bank ("AA999","AA123456@",2000));
		//�s��
		Bank newBank = new Bank ("AA999","AA123456@",3000);
		BankResponse response = bankService.deposit(newBank);
		Bank resBank = response.getBank();
		//�T�{���B���s�J
		Assert.isTrue(resBank.getAmount() == oldBank.getAmount() + newBank.getAmount(), "�s�ڪ��B���~");
		//�R�����ո��
		bankDao.delete(resBank);
		
}
	@Test
	public void withdrawTest() {
		//���q�쥻�l�B �A���� �A�q���G
		
		//�Ыذ����
		Bank oldBank = bankDao.save(new Bank ("AA999","AA123456@",5000));
		//�s��
		Bank newBank = new Bank ("AA999","AA123456@",3000);
		//�Τ@�� resultBank �h���� bankService(class)�̭���withdraw��k(�anewBank�N�O�n�s�W��)
		BankResponse response = bankService.withdraw(newBank);
		Bank resBank = response.getBank();
		//�T�{���B���s�J
		Assert.isTrue(resBank.getAmount() == oldBank.getAmount() - newBank.getAmount(), "���ڿ��~!!");
		Assert.isTrue(response.getMessage().equals("���ڦ��\"), "���ڥ���");
		//�R�����ո��
//	bankDao.delete(resBank);
		
}
}
