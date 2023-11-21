
package com.example.DATN_API.Reponsitories;

import com.example.DATN_API.Entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountReponsitory extends JpaRepository<Account, Integer> {
    @Query("select acc from Account acc")
    Page<Account> getAll(Pageable pageable);

    @Query("Select a FROM Account a WHERE a.username = ?1")
	Account findByUsername(String username);

}

