package com.example.DATN_API.Reponsitories;

import com.example.DATN_API.Entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderReponsetory extends JpaRepository<Order, Integer> {

    @Query("select o from Order o ")
    Page<Order> finAllOrder(Pageable pageable);

    @Query("select o from Order o where o.id = ?1 ")
    Page<Order> findByIdOrder(int idOrder,Pageable pageable);

    @Query("select o from Order o join o.accountOrder a where a.id = ?1")
    List<Order> findOrderByAccount(int id);
    @Query("select o from Order o join o.orderDetails dt where dt.id = ?1")
    Order findOrderByOrderDetail(int id);
    @Query("select o from Order o join o.orderDetails dt join dt.shopOrder s where o.id = ?1 and s.id = ?2")
    Order findOrderByOrderDetailAndSearchByIdOrder( int idOrder, int idShop);

    @Query("select o from Order o join o.orderDetails dt join dt.shopOrder s where s.shop_name like ?1")
    List<Order> findOrderByNameShop(String shop_name );
    @Query("select o from Order o join o.orderDetails dt join dt.shopOrder s where s.shop_name like ?1")
    Page<Order> findByNameShop(String shop_name , Pageable pageable);
    @Query("select o from Order o join o.orderDetails dt join dt.productOrder p join p.shop s where  p.product_name like ?1 and s.id = ?2 ")
    List<Order> findOrderByOrderDetailAndSearchByProductName(String keyword, int id);

    @Query("select o from Order o join o.orderDetails dt join dt.productOrder p where  p.product_name like ?1")
    Page<Order> findByOrderDetailAndSearchByProductName(String keyword, Pageable pageable);
//    @Query("select o from Order o join o.shopOrder s where s.id = ?1")
//    Page<Order> findOrderByShop(int id,Pageable pageable);

}
