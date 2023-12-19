package com.example.DATN_API.Controller;

import com.example.DATN_API.Entity.*;
import com.example.DATN_API.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/")

public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    StatusOrderService statusOrderService;
    @Autowired
    OrderDetailService orderDetailService;
    @Autowired
    AccountService accountService;
    @Autowired
    ShopService shopService;
    @Autowired
    StorageService storageService;

    @GetMapping("auth/order/getAll")
    public ResponseEntity<ResponObject> getall(@RequestParam("keyword") Optional<String> keyword,
                                               @RequestParam("page") Optional<Integer> page,
                                               @RequestParam("type") Optional<Integer> type
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                "SUCCESS", "get all order", orderService.findAll(keyword, page, type)
        ));
    }


    @GetMapping("auth/getAllList")

    public ResponseEntity<ResponObject> getall1() {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                "SUCCESS", "get all order", orderService.findAllList()
        ));
    }

    @PostMapping("auth/order/create/account/{idAccount}")
    public ResponseEntity<ResponObject> create(@RequestBody Order order, @PathVariable("idAccount") int idAccount) {
        Account account = new Account();
        account.setId(idAccount);
        Date date = new Date();
//        // save order
        order.setCreate_date(date);
        order.setAccountOrder(account);
        Order orderSave = orderService.save(order);
////        // create status
        Status status = new Status();
        status.setId(1);

        // create status order
        StatusOrder statusOrder = new StatusOrder();
        statusOrder.setOrder(orderSave);
        statusOrder.setStatus(status);

        statusOrder.setAccount_check(orderSave.getAccountOrder());
        statusOrder.setCreate_date(date);

        statusOrderService.save(statusOrder);

////
//         create order detail
        order.getOrderDetails().stream().forEach(item -> {
            Shop shop = new Shop();
            item.setOrders(orderSave);
            Shop shopFind = shopService.findShopByProduct(item.getProductOrder().getId());
            shop.setId(shopFind.getId());
            item.setShopOrder(shop);
            orderDetailService.save(item);

            Storage storage = new Storage();
            storage.setType("tru");
            storage.setCreate_date(new Date());
            storage.setProduct(item.getProductOrder());
            storage.setQuantity(item.getQuantity());
            storageService.createStorage(storage);
        });

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponObject(
                "SUCCESS", "create order succsessfully", orderSave
        ));
    }

    @GetMapping("auth/order/find/{id}")
    public ResponseEntity<ResponObject> findById(@PathVariable("id") Integer idOrder) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                "SUCCESS", "FIND ORDER BY ID", orderService.findShopByOrder(idOrder)
        ));
    }
    @GetMapping("auth/order/id/{id}")
    public ResponseEntity<ResponObject> findByIdOrder(@PathVariable("id") Integer idOrder) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                "SUCCESS", "FIND ORDER BY ID", orderService.findOrderById(idOrder)
        ));
    }
    @GetMapping("auth/order/search")
    public ResponseEntity<ResponObject> searchOrder(
            @RequestParam("keyword") Optional<String> keyword,
            @RequestParam("type") Optional<Integer> type
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                "SUCCESS", "FIND ORDER BY ID", orderService.findShopByOrderSearch(keyword,type
        )));
    }
    @GetMapping("auth/order/find/account/{id}")
    public ResponseEntity<ResponObject> findByIdAccount(@PathVariable("id") Integer idAccount, @RequestParam("status") Optional<Integer> status) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                "SUCCESS", "FIND ORDER BY ID", orderService.findOrderByAccount(idAccount, status)
        ));
    }

//    @GetMapping("auth/find/shop/{id}")
//
//    public ResponseEntity<ResponObject> findByIdShop(@PathVariable("id") Integer idShop){
//        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
//                "SUCCESS","FIND ORDER BY ID", orderService.findOrderByShop(idShop)
//        ));
//    }

    @GetMapping("auth/findByStatus/{status}")
    public ResponseEntity<ResponObject> findByStatus(@PathVariable("status") int status) {
        List<Order> orders = orderService.findAllList();
        List<Order> ordersNew = new ArrayList<>();
        orders.stream().forEach(item -> {
            int idStatus = item.getStatus().get(item.getStatus().size() - 1).getStatus().getId();
            if (idStatus == status) {
                ordersNew.add(item);
            }
        });
        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                "SUCCESS", "FIND ORDER STATUS", ordersNew
        ));
    }

    @GetMapping("auth/order/shop/{idShop}")
    public ResponseEntity<ResponObject> findByShopAndStatus(
            @PathVariable("idShop") int idShop,
            @RequestParam("keyword") Optional<String> keyword,
            @RequestParam("type") int type,
            @RequestParam("status") Optional<Integer> status
    ) {
        List<Order> orders = orderService.findByShopAndSearch(idShop, status,keyword,type);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                "SUCCESS", "FIND ORDER STATUS", orders
        ));
    }

//    @GetMapping("auth/order/shop/{idShop}/status/{status}")
//    public ResponseEntity<ResponObject> findByShopAndStatus(@PathVariable("status") int status,@PathVariable("idShop") int idShop){
//        Page<Order> orders = orderService.findOrderByShop(idShop);
//        List<Order> ordersNew = new ArrayList<>();
//
//        orders.getContent().stream().forEach(item -> {
//            int idStatus = item.getStatus().get(item.getStatus().size() -1).getStatus().getId();
//            if(idStatus == status){
//                ordersNew.add(item);
//        }});
//        Page<Order> pageOrder = new PageImpl<>(ordersNew);
//        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
//                "SUCCESS","FIND ORDER STATUS", pageOrder
//        ));
//    }


    @PutMapping("auth/order/update/{idOrder}/account/{idAccount}")
    public ResponseEntity<ResponObject> update(@RequestParam("status") int st,
                                               @PathVariable("idAccount") int idAccount,
                                               @PathVariable("idOrder") Integer id) {
//        // find order
        Order order = orderService.findOrderById(id);
        Account account = new Account();
        account.setId(idAccount);
        Status status = new Status();
        status.setId(st);
////        // create status order
        StatusOrder statusOrder = new StatusOrder();
        statusOrder.setOrder(order);
        statusOrder.setStatus(status);
        statusOrder.setAccount_check(order.getAccountOrder());
        statusOrder.setCreate_date(new Date());

        StatusOrder stt= statusOrderService.save(statusOrder);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                "SUCCESS", "create order succsessfully", stt
        ));
    }

    @GetMapping("get/status")
    public ResponseEntity<ResponObject> getAllStatus() {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                "SUCCESS", "find all status", orderService.findAllStatus()
        ));
    }

    @RequestMapping("auth/order/shop/{idShop}/search")
    public ResponseEntity<ResponObject> findOrder(@PathVariable("idShop") int idShop,
                                                  @RequestParam("keyword") Optional<String> keyword,
                                                  @RequestParam("type") int type,
                                                  @RequestParam("status") Optional<Integer> status
    ) {
        List<Order> orders = orderService.findByShopAndSearch(idShop, status,keyword,type);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                "SUCCESS", "FIND ORDER STATUS", orders
        ));
    }

    @GetMapping("auth/getEmailByOderId/{id}")
    public ResponseEntity<ResponObject> getEmail(@PathVariable("id") int id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                "success", "Get email", accountService.getEmailByOderId(id)
        ));
    }
}
