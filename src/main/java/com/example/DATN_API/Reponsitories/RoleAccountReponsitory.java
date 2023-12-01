package com.example.DATN_API.Reponsitories;

import com.example.DATN_API.Entity.Account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.DATN_API.Entity.RoleAccount;

@Repository
public interface RoleAccountReponsitory extends JpaRepository<RoleAccount, Integer> {

    @Query("select r from RoleAccount r where r.account_role=?1")
    RoleAccount findRoleAccountByAccount_role(Account account);

}