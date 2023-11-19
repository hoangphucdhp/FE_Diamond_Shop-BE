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
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    StatusOrderService statusOrderService;
    @Autowired
    OrderDetailService orderDetailService;
    @Autowired
    AccountService accountService;
    @GetMapping("/getAll")
    public ResponseEntity<ResponObject> getall(@RequestParam("offset") Optional<Integer> offSet,
                                               @RequestParam("sizePage") Optional<Integer>  sizePage,
                                               @RequestParam("sort") Optional<String> sort)
    {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                "SUCCESS","get all order",orderService.findAll(offSet,sizePage,sort)
        ));
    }

    @GetMapping("/getAllList")
    public ResponseEntity<ResponObject> getall1()
    {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                "SUCCESS","get all order",orderService.findAllList()
        ));
    }
    @PostMapping("/create/account/{idAccount}")
    public ResponseEntity<ResponObject> create(@RequestBody Order order,@PathVariable("idAccount") int idAccount){
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
            orderDetailService.save(item);
        });

        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                "SUCCESS","create order succsessfully",orderSave
        ));
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<ResponObject> findById(@PathVariable("id") Integer idOrder){
        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
           "SUCCESS","FIND ORDER BY ID", orderService.findOrderById(idOrder)
        ));
    }
    @GetMapping("/find/account/{id}")
    public ResponseEntity<ResponObject> findByIdAccount(@PathVariable("id") Integer idAccount){
        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                "SUCCESS","FIND ORDER BY ID", orderService.findOrderByAccount(idAccount)
        ));
    }
    @GetMapping("/find/shop/{id}")
    public ResponseEntity<ResponObject> findByIdShop(@PathVariable("id") Integer idShop){
        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                "SUCCESS","FIND ORDER BY ID", orderService.findOrderByShop(idShop)
        ));
    }
    @GetMapping("/findByStatus/{status}")
    public ResponseEntity<ResponObject> findByStatus(@PathVariable("status") int status){
        List<Order> orders = orderService.findAllList();
        List<Order> ordersNew = new ArrayList<>();
        orders.stream().forEach(item -> {
            int idStatus = item.getStatus().get(item.getStatus().size() -1).getStatus().getId();
            if(idStatus == status){
                ordersNew.add(item);
            }
        });
        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                "SUCCESS","FIND ORDER STATUS", ordersNew
        ));
    }
    @GetMapping("/shop/{idShop}/status/{status}")
    public ResponseEntity<ResponObject> findByShopAndStatus(@PathVariable("status") int status,@PathVariable("idShop") int idShop){
        Page<Order> orders = orderService.findOrderByShop(idShop);
        List<Order> ordersNew = new ArrayList<>();

        orders.getContent().stream().forEach(item -> {
            int idStatus = item.getStatus().get(item.getStatus().size() -1).getStatus().getId();
            if(idStatus == status){
                ordersNew.add(item);
        }});
        Page<Order> pageOrder = new PageImpl<>(ordersNew);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                "SUCCESS","FIND ORDER STATUS", pageOrder
        ));
    }

    @PutMapping("/update/{idOrder}/account/{idAccount}")
    public ResponseEntity<ResponObject> update(@RequestParam("status") int st,
                                               @PathVariable("idAccount") int idAccount,
                                               @PathVariable("idOrder") Integer id){
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

        statusOrderService.save(statusOrder);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                "SUCCESS","create order succsessfully",order
        ));
    }

}
