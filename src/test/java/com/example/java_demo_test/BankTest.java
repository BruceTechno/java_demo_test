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
		//新增資料到bank 這張表
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
//		Assert.isTrue(bank.getAmount()==16888, "查無資料");
		System.out.println("帳號 :" + bank.getAccount() + "餘額 :" + bank.getAmount());
	}
	
	@Test
	public void depositTest() {
		//先秀原本餘額 再存錢 再秀結果
//		Bank bank = new Bank ("AA999","AA123456@",2000);
		
		//創建假資料
		Bank oldBank = bankDao.save(new Bank ("AA999","AA123456@",2000));
		//存款
		Bank newBank = new Bank ("AA999","AA123456@",3000);
		BankResponse response = bankService.deposit(newBank);
		Bank resBank = response.getBank();
		//確認金額有存入
		Assert.isTrue(resBank.getAmount() == oldBank.getAmount() + newBank.getAmount(), "存款金額錯誤");
		//刪除測試資料
		bankDao.delete(resBank);
		
}
	@Test
	public void withdrawTest() {
		//先秀原本餘額 再取錢 再秀結果
		
		//創建假資料
		Bank oldBank = bankDao.save(new Bank ("AA999","AA123456@",5000));
		//存款
		Bank newBank = new Bank ("AA999","AA123456@",3000);
		//用一個 resultBank 去接收 bankService(class)裡面的withdraw方法(帶newBank就是要新增的)
		BankResponse response = bankService.withdraw(newBank);
		Bank resBank = response.getBank();
		//確認金額有存入
		Assert.isTrue(resBank.getAmount() == oldBank.getAmount() - newBank.getAmount(), "提款錯誤!!");
		Assert.isTrue(response.getMessage().equals("提款成功"), "提款失敗");
		//刪除測試資料
//	bankDao.delete(resBank);
		
}
}
