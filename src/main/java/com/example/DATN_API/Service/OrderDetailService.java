package com.example.DATN_API.Service;

import com.example.DATN_API.Entity.OrderDetail;
import com.example.DATN_API.Reponsitories.OrderDetailReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService {
    @Autowired
    OrderDetailReponsitory orderDetailReponsitory;

    public OrderDetail save(OrderDetail orderDetail) {
        return orderDetailReponsitory.save(orderDetail);

    }

    public List<OrderDetail> findByIdOrder(int idOrder) {
        return orderDetailReponsitory.findByIdOrder(idOrder);
    }

    public List<Object[]> getTotalByMonth(int idShop) {
        return orderDetailReponsitory.getTotalByMonth(idShop);
    }

    public List<Object[]> statistical(int idShop, String year, String month, String day) {
        return orderDetailReponsitory.statistical(idShop, year, month, day);
    }
    public List<Object[]> top10Product(){
        return orderDetailReponsitory.top10Product();
    }
    public List<Object[]> statisticalMonth(int idShop, String year) {
        return orderDetailReponsitory.statisticalMonth(idShop, year);
    }

}
