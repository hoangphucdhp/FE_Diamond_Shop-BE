package com.example.DATN_API.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DATN_API.Reponsitories.RoleAccountReponsitory;
import com.example.DATN_API.Entity.RoleAccount;

@Service
public class RoleAccountService {
	@Autowired
	RoleAccountReponsitory roleaccReponsitory;
	
	public RoleAccount createRoleAcc (RoleAccount roleAcc) {
		try {
			roleaccReponsitory.save(roleAcc);
			return roleAcc;
		} catch (Exception e) {
			e.printStackTrace();
			LogError.saveToLog(e);
		}
		return null;
		
	}
}