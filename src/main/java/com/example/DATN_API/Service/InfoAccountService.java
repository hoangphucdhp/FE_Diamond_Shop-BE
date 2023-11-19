package com.example.DATN_API.Service;

import com.example.DATN_API.Entity.Account;
import com.example.DATN_API.Entity.InfoAccount;
import com.example.DATN_API.Reponsitories.InfoAccountReponsitory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InfoAccountService {
	@Autowired
	InfoAccountReponsitory infoAccountReponsitory;
	
	public List<InfoAccount> findAll() {
		return infoAccountReponsitory.findAll();
	}

	public InfoAccount findByEmail(String email) {
		try {
			return infoAccountReponsitory.findByEmail(email);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public InfoAccount findByPhone(String phone) {
		try {
			return infoAccountReponsitory.findByPhone(phone);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public InfoAccount findByIdCard(String id_card) {
		try {
			return infoAccountReponsitory.findByIdCard(id_card);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public InfoAccount findById_account(int id_account) {
		try {
			return infoAccountReponsitory.findById_account(id_account);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public InfoAccount createProfile(InfoAccount infoAccount) {
		try {
			InfoAccount infoAccountSave = infoAccountReponsitory.save(infoAccount);
			return infoAccountSave;
		} catch (Exception e) {
			e.printStackTrace();
			LogError.saveToLog(e);
		}
		return null;
	}
}