package com.example.DATN_API.Service;

import com.example.DATN_API.Entity.Order;
import com.example.DATN_API.Entity.OrderDetail;
import com.example.DATN_API.Entity.Status;
import com.example.DATN_API.Reponsitories.OrderDetailReponsitory;
import com.example.DATN_API.Reponsitories.OrderReponsetory;
import com.example.DATN_API.Reponsitories.StatusReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    OrderReponsetory orderReponsetory;
    @Autowired
    OrderDetailReponsitory orderDetailReponsitory;
    @Autowired
    StatusReponsitory statusReponsitory;
    public Page<Order> findAll(Optional<Integer> offset, Optional<Integer> sp, Optional<String> field) {
        String sort = field.orElse("create_date");
        int itemStart = offset.orElse(0);
        ;
        int sizePage = sp.orElse(10);


        return orderReponsetory.finAllOrder(PageRequest.of(itemStart, sizePage, Sort.Direction.DESC, sort));
    }

    public List<Order> findAllList() {
//        String sort = field.orElse("create_date");
//        int itemStart = offset.orElse(0);;
//        int sizePage = sp.orElse(10);


        return orderReponsetory.findAll();
    }

    public Order save(Order order) {
        return orderReponsetory.save(order);
    }

    public Order findOrderById(Integer id) {
        Optional<Order> o = orderReponsetory.findById(id);
        return o.get();
    }

    public List<Order> findOrderByAccount(int id, Optional<Integer> status) {
        int stt = status.orElse(0);
        List<Order> orders = orderReponsetory.findOrderByAccount(id);
        List<Order> ordersNew = new ArrayList<>();
        if (orders.size() == 0) {
            return null;
        }
        if (stt == 0) {
            return orders;
        }
        orders.stream().forEach(item -> {
            if (item.getStatus().get(item.getStatus().size() - 1).getStatus().getId() == stt) {
                ordersNew.add(item);
            }
        });
        return ordersNew;
    }

//    public Page<Order> findOrderByShop(int id){
//        Pageable pageable = PageRequest.of(0,20,Sort.by("create_date"));
//        return orderReponsetory.findOrderByShop(id,pageable);
//    }

    public List<Order> findByShop(int idShop, Optional<Integer> stt) {
        int status = stt.orElse(0);

        List<OrderDetail> orderDetails = orderDetailReponsitory.findByIdShop(idShop);
        List<Order> orders = new ArrayList<>();
        List<Order> ordersNew = new ArrayList<>();
        orderDetails.stream().forEach(item -> {
            Order orderOld = orderReponsetory.findOrderByOrderDetail(item.getId());
            orders.add(orderOld);
        });
        if (orders == null) {
            return null;
        }
        for (Order order : orders) {
            if( !ordersNew.contains(order)  && status == 0){
                ordersNew.add(order);
                continue;
            }
            if(!ordersNew.contains(order) && order.getStatus().get(order.getStatus().size() - 1).getStatus().getId() == status){
                ordersNew.add(order);
            }
        }
        ordersNew.stream().forEach(item -> {
            item.getOrderDetails().stream().forEach(dt -> {
                if (dt.getShopOrder().getId() != idShop ) {
                    item.getOrderDetails().remove(dt);
                }
            });
        });
        return ordersNew;
    }
    public List<Status> findAllStatus () {
        return statusReponsitory.findAll();
    }
}

