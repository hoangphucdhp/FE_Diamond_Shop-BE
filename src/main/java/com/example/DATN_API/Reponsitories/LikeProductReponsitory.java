package com.example.DATN_API.Reponsitories;

import com.example.DATN_API.Entity.LikeProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeProductReponsitory extends JpaRepository<LikeProduct, Integer> {
}
