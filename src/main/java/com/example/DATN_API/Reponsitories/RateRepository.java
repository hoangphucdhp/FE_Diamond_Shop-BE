package com.example.DATN_API.Reponsitories;

import com.example.DATN_API.Entity.Account;
import com.example.DATN_API.Entity.Product;
import com.example.DATN_API.Entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RateRepository extends JpaRepository<Rate, Integer> {
    @Query("SELECT r FROM Rate r WHERE r.product_rate = :product")
    List<Rate> findByProduct_rate(@Param("product") Product product);

    @Query("SELECT AVG(r.star) FROM Rate r WHERE r.product_rate = :product")
    Double findAverageStarByProduct(@Param("product") Product product);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Rate r " +
            "WHERE r.account_rate = :account AND r.product_rate = :product")
    boolean existsByAccount_rateAndProduct_rate(@Param("account") Account account, @Param("product") Product product);
}