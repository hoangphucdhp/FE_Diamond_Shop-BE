package com.example.DATN_API.Reponsitories;

import com.example.DATN_API.Entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailReponsitory extends JpaRepository<OrderDetail, Integer> {
}
