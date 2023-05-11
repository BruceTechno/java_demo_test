package com.example.java_demo_test.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.java_demo_test.entity.Bank;

@Repository
public interface BankDao extends JpaRepository<Bank, String>{
	public Bank findByAccountAndPwd(String account , String pwd);
//	public List<PersonInfo> doQueryByAge(int age);
}
