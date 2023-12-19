package com.example.DATN_API.Service;

import com.example.DATN_API.Entity.Order;
import com.example.DATN_API.Entity.OrderDetail;
import com.example.DATN_API.Entity.Shop;
import com.example.DATN_API.Entity.Status;
import com.example.DATN_API.Reponsitories.OrderDetailReponsitory;
import com.example.DATN_API.Reponsitories.OrderReponsetory;
import com.example.DATN_API.Reponsitories.StatusReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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

    public Page<Order> findAll(Optional<String> search, Optional<Integer> page, Optional<Integer> type) {
        String sort = "create_date";
        int pageNumber = page.orElse(1);
        int status = type.orElse(0);
        String keyword = search.orElse("");
        int sizePage = 6;
        System.out.println(status);
        if (status == 1) {
            return orderReponsetory.findByIdOrder(Integer.parseInt(keyword), PageRequest.of(pageNumber - 1, sizePage, Sort.Direction.DESC, sort));
        } else if (status == 2) {
            return orderReponsetory.findByNameShop("%" + keyword + "%", PageRequest.of(pageNumber - 1, sizePage, Sort.Direction.DESC, sort));
        } else {
            return orderReponsetory.finAllOrder(PageRequest.of(pageNumber - 1, sizePage, Sort.Direction.DESC, sort));
        }
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


    public List<Order> findByShop(int idShop, Optional<Integer> stt) {
        int status = stt.orElse(0);

//        List<OrderDetail> orderDetails = orderDetailReponsitory.findByIdShop(idShop);
        List<Order> orders = orderDetailReponsitory.findByIdShops(idShop);
        List<Order> ordersNew = new ArrayList<>();
//        orderDetails.stream().forEach(item -> {
//            Order orderOld = orderReponsetory.findOrderByOrderDetail(item.getId());
//            orders.add(orderOld);
//        });
//        if (orders == null) {
//            return null;
//        }
        for (Order order : orders) {
            if (!ordersNew.contains(order) && status == 0) {
                ordersNew.add(order);
                continue;
            }
            if (!ordersNew.contains(order) && order.getStatus().get(order.getStatus().size() - 1).getStatus().getId() == status) {
                ordersNew.add(order);
            }
        }
//        ordersNew.stream().forEach(item -> {
//            item.getOrderDetails().stream().forEach(dt -> {
//                if (dt.getShopOrder().getId() != idShop) {
//                    item.getOrderDetails().remove(dt);
//                }
//            });
//        });


        return ordersNew;
    }

    public List<Order> findByShopAndSearch(int idShop, Optional<Integer> stt, Optional<String> search, int type) {
        int status = stt.orElse(0);
        System.out.println("type " + type);
        String keyword = search.orElse("");
        List<Order> orders = new ArrayList<>();
        List<Order> ordersNew = new ArrayList<>();
        List<OrderDetail> orderDetails = orderDetailReponsitory.findByIdShop(idShop);
        System.out.println(orderDetails.size());
        System.out.println(keyword);

        if (type == 1 && keyword != "") {
            Order orderOld = orderReponsetory.findOrderByOrderDetailAndSearchByIdOrder(Integer.parseInt(keyword), idShop);
            if (orderOld != null) {
                orders.add(orderOld);
            }

        } else if (type == 2 || keyword == "") {
            List<Order> o = orderReponsetory.findOrderByOrderDetailAndSearchByProductName("%" + keyword + "%", idShop);
            orders = o;
        }
        for (Order order : orders) {
            if (!ordersNew.contains(order) && status == 0) {
                ordersNew.add(order);
                continue;
            }
            if (!ordersNew.contains(order) && order.getStatus().get(order.getStatus().size() - 1).getStatus().getId() == status) {
                ordersNew.add(order);
            }
        }
        return ordersNew;
    }

    public List<Status> findAllStatus() {
        return statusReponsitory.findAll();
    }

    public List<Shop> findShopByOrder(int idOrder) {
        List<Shop> shops = new ArrayList<>();
        List<Shop> newShops = new ArrayList<>();
        Order orders = orderReponsetory.findById(idOrder).get();
        orders.getOrderDetails().stream().forEach(item -> {
//            Shop shop = new Shop();
            shops.add(item.getShopOrder());
        });
        if (shops == null) {
            return null;
        }
        shops.stream().forEach(item -> {
            Shop shopNew = new Shop();
            List<OrderDetail> orderDetails = new ArrayList<>();

            for (OrderDetail orderDetail :
                    item.getListOrder()) {
                if (item.getId() == orderDetail.getShopOrder().getId()) {
                    orderDetails.add(orderDetail);
                    break;
                }
            }
            item.setListOrder(orderDetails);
        });

        return shops;
    }

    public List<Order> findShopByOrderSearch(Optional<String> search, Optional<Integer> type) {
        String keyword = search.orElse("");
        int status = type.orElse(1);

        List<Order> orders = new ArrayList<>();
        if (status == 1) {
            Order order = orderReponsetory.findById(Integer.parseInt(keyword)).get();
            orders.add(order);
        } else if (status == 2) {
            return orderReponsetory.findOrderByNameShop("%" + keyword + "%");
        } else {
            return null;
        }
        return orders;
    }
}

