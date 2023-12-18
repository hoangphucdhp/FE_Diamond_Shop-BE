package com.example.DATN_API.Controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.DATN_API.Entity.ResponObject;
import com.example.DATN_API.Service.AccountService;
import com.example.DATN_API.Service.OrderDetailService;
import com.example.DATN_API.Service.OrderService;
import com.example.DATN_API.Service.ProductService;
import com.example.DATN_API.Service.ShopService;

@RestController
@RequestMapping("/api/admin/thongke")
@CrossOrigin("*")

public class ThongKeAdminController {
    @Autowired
    AccountService accountService;
    @Autowired
    ProductService productService;
    @Autowired
    ShopService shopService;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderDetailService orderDetailService;

    @GetMapping("/account")
    public ResponseEntity<ResponObject> getAllAccount() {
        try {
            int accountStatusTrue = accountService.getAmountAccount(true);
            int accountStatusFalse = accountService.getAmountAccount(false);
            Object[] listAccount = new Object[] { accountStatusTrue, accountStatusFalse };
            return new ResponseEntity<>(
                    new ResponObject("success", "GET ALL ACCOUNT", listAccount), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/product")
    public ResponseEntity<ResponObject> getAllProduct() {
        try {
            int productStatus0 = productService.getAmountProduct(0);
            int productStatus1 = productService.getAmountProduct(1);
            int productStatus2 = productService.getAmountProduct(2);
            int productStatus3 = productService.getAmountProduct(3);
            Object[] listProduct = new Object[] { productStatus0, productStatus1, productStatus2, productStatus3 };
            return new ResponseEntity<>(
                    new ResponObject("success", "GET ALL PRODUCT", listProduct), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/shop")
    public ResponseEntity<ResponObject> getAllShop() {
        try {
            int shopStatus0 = shopService.getAmountShop(0);
            int shopStatus1 = shopService.getAmountShop(1);
            int shopStatus3 = shopService.getAmountShop(3);
            Object[] listShop = new Object[] { shopStatus0, shopStatus1, shopStatus3 };

            return new ResponseEntity<>(
                    new ResponObject("success", "GET ALL SHOP", listShop), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/order")
    public ResponseEntity<ResponObject> getAllOrder() {
        try {
            return new ResponseEntity<>(
                    new ResponObject("success", "GET ALL ORDER", orderService.findAllList().size()), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/top10")
    public ResponseEntity<ResponObject> top10Product(){
        try {
            return new ResponseEntity<>(new ResponObject("success","GET TOP10",orderDetailService.top10Product()),HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
