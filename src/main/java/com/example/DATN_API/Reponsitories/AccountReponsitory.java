
package com.example.DATN_API.Reponsitories;

import com.example.DATN_API.Entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountReponsitory extends JpaRepository<Account, Integer> {


    @Query("Select a FROM Account a JOIN a.infoAccount in WHERE in.email = ?1")
    Account findByEmail(String email);


    @Query("select acc from Account acc")
    Page<Account> getAll(Pageable pageable);

    @Query("select acc from Account acc where acc.us like %?1%")
    Page<Account> getAllfindbyUsername(Pageable pageable,String username);

    @Query("select acc from Account acc  where acc.infoAccount.fullname like %?1%")
    Page<Account> getAllfindbyFullname(Pageable pageable,String fullname);

    @Query( "SELECT acc FROM Account acc where acc.shop.shop_name like %?1%")
    Page<Account> getByShopName(Pageable pageable,String username);

    @Query("Select a FROM Account a where a.shop.id = ?1")
    Account findAccountByIdShop(int id);

    @Query("Select a FROM Account a where a.shop.shop_name = ?1")
    Account findAccountByShopName(String id);

    @Query("select acc from Account acc where acc.us = ?1")
    Optional<Account> findByUsername(String username);

    @Query("Select a FROM Account a join Product p on a.shop.id=p.shop.id where p.id= ?1")
    Account findAccountByidProduct(int id);

    @Query("SELECT COUNT(a) FROM Account a WHERE a.status = ?1")
    Integer getAmountAccount(boolean status);

    @Query("select a.infoAccount.email from Account a join a.statusOrders s where s.order.id=?1")
    List<String> getEmailsByOrder(int id);

}

