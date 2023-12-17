package com.example.DATN_API.Reponsitories;

import com.example.DATN_API.Entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderReponsetory extends JpaRepository<Order, Integer> {

    @Query("select o from Order o")
    Page<Order> finAllOrder(Pageable pageable);


    @Query("select o from Order o join o.accountOrder a where a.id = ?1")
    List<Order> findOrderByAccount(int id);
    @Query("select o from Order o join o.orderDetails dt where dt.id = ?1")
    Order findOrderByOrderDetail(int id);
//    @Query("select o from Order o join o.shopOrder s where s.id = ?1")
//    Page<Order> findOrderByShop(int id,Pageable pageable);


}
