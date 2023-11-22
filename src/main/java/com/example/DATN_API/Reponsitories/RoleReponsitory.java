package com.example.DATN_API.Reponsitories;

import com.example.DATN_API.Entity.Account;
import com.example.DATN_API.Entity.Role;
import com.example.DATN_API.Entity.RoleAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleReponsitory extends JpaRepository<Role, Integer> {

}