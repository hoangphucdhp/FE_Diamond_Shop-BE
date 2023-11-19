package com.example.DATN_API.Service;

import com.example.DATN_API.Entity.Order;
import com.example.DATN_API.Reponsitories.OrderReponsetory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    OrderReponsetory orderReponsetory;
    public Page<Order> findAll(Optional<Integer> offset, Optional<Integer> sp, Optional<String> field){
        String sort = field.orElse("create_date");
        int itemStart = offset.orElse(0);;
        int sizePage = sp.orElse(10);


        return orderReponsetory.finAllOrder(PageRequest.of(itemStart,sizePage,Sort.Direction.DESC,sort));
    }
    public List<Order> findAllList(){
//        String sort = field.orElse("create_date");
//        int itemStart = offset.orElse(0);;
//        int sizePage = sp.orElse(10);


        return orderReponsetory.findAll();
    }

    public Order save(Order order){
        return orderReponsetory.save(order);
    }

    public Order findOrderById(Integer id){
        Optional<Order> o = orderReponsetory.findById(id);
        return o.get();
    }
    public Page<Order> findOrderByAccount(int id){
        Pageable pageable = PageRequest.of(0,20,Sort.by("create_date"));
        return orderReponsetory.findOrderByAccount(id,pageable);
    }

    public Page<Order> findOrderByShop(int id){
        Pageable pageable = PageRequest.of(0,20,Sort.by("create_date"));
        return orderReponsetory.findOrderByShop(id,pageable);
    }
}
