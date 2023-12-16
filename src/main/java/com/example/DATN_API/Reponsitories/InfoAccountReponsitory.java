package com.example.DATN_API.Reponsitories;

import com.example.DATN_API.Entity.InfoAccount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InfoAccountReponsitory extends JpaRepository<InfoAccount, Integer> {
	@Query(value = "SELECT * FROM info_account where email=?1", nativeQuery = true)
	Optional<InfoAccount> findByEmail(String email);
	
	@Query(value = "SELECT * FROM info_account where phone=?1", nativeQuery = true)
	InfoAccount findByPhone(String phone);	
	
	@Query(value = "SELECT * FROM info_account where id_card=?1", nativeQuery = true)
	InfoAccount findByIdCard(String id_card);	
	
	@Query(value = "SELECT * FROM info_account where id_account=?1", nativeQuery = true)
	InfoAccount findById_account(int id);
}