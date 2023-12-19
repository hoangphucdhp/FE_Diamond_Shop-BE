package com.example.DATN_API.Controller;


import com.example.DATN_API.Entity.*;
import com.example.DATN_API.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController

@RequestMapping("/api")

@CrossOrigin("*")
public class ShopController {
    @Autowired
    ShopService shopService;
    @Autowired
    AccountService accountService;
    @Autowired
    RoleAccountService roleAccService;
    @Autowired
    ProductService productService;

    @GetMapping("/findAll")
    public ResponseEntity<ResponObject> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                "SUCCESS", "find shop by product", accountService.findAll()));
    }

    @GetMapping("/shop/findAll")
    public ResponseEntity<ResponObject> getAllShop() {
        return new ResponseEntity<>(new ResponObject("success", "get all shop", shopService.findAll()), HttpStatus.OK);
    }

    @GetMapping("shop/findByProduct/{id}")
    public ResponseEntity<ResponObject> findByProduct(@PathVariable("id") int idProduct) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                "SUCCESS", "find shop by product", shopService.findShopByProduct(idProduct)));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ResponObject> findById(@PathVariable("id") int idShop) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                "SUCCESS", "find shop by id", shopService.findById(idShop)));
    }

    @GetMapping()

    public ResponseEntity<List<Shop>> getAll() {

        return new ResponseEntity<>(shopService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/shop/{id}")

    public ResponseEntity<Shop> findById(@PathVariable Integer id) {
        if (shopService.existsById(id)) {
            return new ResponseEntity<>(shopService.findById(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/shop")
    @PreAuthorize("hasRole('ROLE_Admin')")
    public ResponseEntity<ResponObject> create(@RequestParam("image") MultipartFile
                                                       image, @RequestParam("shopName") String Shopname, @RequestParam("idAccount") Integer idAccount) {
//        Shop shopnew = shopService.createShop(shop);
        return new ResponseEntity<>(new ResponObject("SUCCESS", "shop has been added.", "shopnew"),
                HttpStatus.CREATED);
    }


    @PutMapping("auth/admin/update")
    @PreAuthorize("hasRole('ROLE_Admin')")
    public ResponseEntity<ResponObject> updatestatusAdmin(@RequestParam("id") Integer id,
                                                          @RequestParam("status") Integer status, @RequestParam("isCheck") String isCheck) {
        Shop shop = shopService.findById(id);
        if (status == 2) {
            shop.getProducts().stream().forEach(item -> {
                productService.BanProduct(item.getId(), 3);
            });
        }
        if (status == 1 && isCheck.equals("model")) {
            shop.getProducts().stream().forEach(item -> {
                productService.BanProduct(item.getId(), 0);
            });
        }
        shop.setStatus(status);
        RoleAccount roleAcc = new RoleAccount();
        Role role = new Role();
        Account accountCheck = accountService.findAccountByIdShop(id);
        role.setId(3);
        roleAcc.setAccount_role(accountCheck);
        roleAcc.setRole(role);
        roleAccService.createRoleAcc(roleAcc);
        Shop shopnew = shopService.updateShop(shop);
        return new ResponseEntity<>(new ResponObject("SUCCESS", "shop has been updated.", shopnew), HttpStatus.OK);

    }


    @PutMapping("/auth/bussiness/updateInfShop/{id}")
    @PreAuthorize("hasRole('ROLE_Bussiness')")
    public ResponseEntity<ResponObject> bussinessUpdateInf(@PathVariable("id") Integer id, @RequestParam("shop_name") String shop_name,
                                                           @RequestParam("city") String city,
                                                           @RequestParam("district") String district,
                                                           @RequestParam("ward") String ward,
                                                           @RequestParam("address") String address,
                                                           @RequestParam("image") String image) {
        try {
            Shop shop = shopService.bussinessUpdateInf(id, shop_name, city, district, ward, address, image);
            return new ResponseEntity<>(new ResponObject("success", "Cập nhật thành công.", shop), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            String errorMessage = e.getMessage();
            return new ResponseEntity<>(new ResponObject("error", errorMessage, null), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/auth/adminDeleteShop/{id}")
    @PreAuthorize("hasRole('ROLE_Admin')")
    public ResponseEntity<ResponObject> adminDeleteShop(@PathVariable("id") Integer id) {
        try {
            shopService.deleteShop(id);
            return new ResponseEntity<>(new ResponObject("success", "Thành công", null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponObject("error", "Thất bại", null), HttpStatus.OK);
        }
    }
}

