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

    public OrderDetail save(OrderDetail orderDetail){
        return  orderDetailReponsitory.save(orderDetail);
    }
    public List<OrderDetail> findByIdOrder(int idOrder){
        return orderDetailReponsitory.findByIdOrder(idOrder);
    }

    public List<Object[]> getTotalByMonth(int idShop){
        return orderDetailReponsitory.getTotalByMonth(idShop);
    }
}
