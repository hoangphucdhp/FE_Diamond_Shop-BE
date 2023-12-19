package com.example.DATN_API.Reponsitories;

import com.example.DATN_API.Entity.Order;
import com.example.DATN_API.Entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailReponsitory extends JpaRepository<OrderDetail, Integer> {

    @Query("Select o FROM OrderDetail o WHERE o.orders.id = ?1")
    List<OrderDetail> findByIdOrder(int idOrder);

    @Query("Select o, odt, p FROM Order o"
            + " JOIN o.orderDetails odt JOIN o.status st"
            + " JOIN odt.productOrder p JOIN p.shop s WHERE s.id = ?1")
    List<Object[]> getTotalByMonth(int idShop);


    @Query("Select dt FROM OrderDetail dt join dt.shopOrder so WHERE so.id = ?1")
    List<OrderDetail> findByIdShop(int idShop);
    @Query("Select o FROM Order o join o.orderDetails dt join dt.shopOrder s where s.id = ?1")
    List<Order> findByIdShops(int idShop);

    @Query("SELECT  SUM(odt.quantity), YEAR(o.create_date), MONTH(o.create_date), DAY(o.create_date), o.id, HOUR(o.create_date) "
            +
            "FROM Order o " +
            "JOIN o.orderDetails odt " +
            "JOIN odt.productOrder p " +
            "JOIN p.shop s " +
            "WHERE s.id = ?1 AND CAST(YEAR(o.create_date) AS String) = ?2 AND " +
            "CAST(MONTH(o.create_date) AS String) = ?3 AND CAST(DAY(o.create_date) AS String) = ?4 " +
            "GROUP BY YEAR(o.create_date), MONTH(o.create_date), DAY(o.create_date), s.id, o.id, HOUR(o.create_date) " +
            "ORDER BY HOUR(o.create_date)")
    List<Object[]> statistical(int idShop, String year, String month, String day);

    @Query("SELECT p, SUM(odt.quantity) " +
            "FROM Order o " +
            "JOIN o.orderDetails odt " +
            "JOIN odt.productOrder p " +
            "JOIN p.shop s " +
            "GROUP BY p, s.id " +
            "ORDER BY SUM(odt.quantity) DESC " + //
            "LIMIT 10")
    List<Object[]> top10Product();

    @Query("SELECT SUM(p.price), YEAR(o.create_date), MONTH(o.create_date)" +
            "FROM Order o " +
            "JOIN o.orderDetails odt " +
            "JOIN odt.productOrder p " +
            "JOIN p.shop s " +
            "WHERE s.id = ?1 AND CAST(YEAR(o.create_date) AS String) = ?2 " +
            "GROUP BY s.id, YEAR(o.create_date), MONTH(o.create_date)")
    List<Object[]> statisticalMonth(int idShop, String year);

}
