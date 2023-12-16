package com.example.DATN_API.Reponsitories;

import com.example.DATN_API.Entity.LikeProduct;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import org.springframework.stereotype.Repository;

@Repository
public interface LikeProductReponsitory extends JpaRepository<LikeProduct, Integer> {



    @Query(nativeQuery = true, value = "SELECT " +
            "lp.id_product AS id_product, " +
            "lp.id_account AS id_account, " +
            "p.id_shop AS id_shop, " +
            "p.product_name AS product_name, " +
            "p.price AS price, " +
            "(SELECT ip.url FROM image_product ip WHERE ip.id_product = p.id FOR JSON AUTO) AS image_urls " +
            "FROM like_product lp " +
            "INNER JOIN product p ON lp.id_product = p.id " +
            "WHERE lp.id_account = :accountId " +
            "FOR JSON AUTO")
    String findProductDetailsByAccountId(@Param("accountId") int accountId);

    @Query(nativeQuery = true, value = "SELECT * FROM like_product lp WHERE lp.id_product = :productId AND lp.id_account = :accountId")
    LikeProduct findByProductLikeIdAndAccountLikeId(@Param("productId") int productId,
            @Param("accountId") int accountId);

}


