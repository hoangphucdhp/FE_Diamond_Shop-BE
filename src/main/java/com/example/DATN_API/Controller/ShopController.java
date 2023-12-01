package com.example.DATN_API.Controller;

import com.example.DATN_API.Entity.Order;
import com.example.DATN_API.Entity.Shop;
import com.example.DATN_API.Entity.ResponObject;
import com.example.DATN_API.Service.OrderService;
import com.example.DATN_API.Service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    OrderService orderService;

    @GetMapping("/shop/findAll")
    public ResponseEntity<ResponObject> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                "SUCCESS", "find shop by product", shopService.findAll()
        ));
    }

    @GetMapping("/shop/findByProduct/{id}")
    public ResponseEntity<ResponObject> findByProduct(@PathVariable("id") int idProduct) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                "SUCCESS", "find shop by product", shopService.findShopByProduct(idProduct)
        ));
    }


    @GetMapping("/shop/get/{id}")
    public ResponseEntity<ResponObject> findById(@PathVariable("id") int idShop) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                "SUCCESS", "find shop by product", shopService.findById(idShop)
        ));
    }

    @GetMapping("/shop/{idShop}/status/{status}")
    public ResponseEntity<ResponObject> findByShopAndStatus(@PathVariable("status") int status, @PathVariable("idShop") int idShop) {
        Page<Order> orders = orderService.findOrderByShop(idShop);
        List<Order> ordersNew = new ArrayList<>();
        orders.getContent().stream().forEach(item -> {
            int idStatus = item.getStatus().get(item.getStatus().size() - 1).getStatus().getId();
            if (idStatus == status) {
                ordersNew.add(item);
            }
        });
        Page<Order> pageOrder = new PageImpl<>(ordersNew);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                "SUCCESS", "FIND ORDER STATUS", pageOrder
        ));
    }

    @GetMapping("/shop")
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
    public ResponseEntity<ResponObject> create(@RequestParam("image") MultipartFile
                                                       image, @RequestParam("shopName") String Shopname, @RequestParam("idAccount") Integer idAccount) {
//        Shop shopnew = shopService.createShop(shop);
        return new ResponseEntity<>(new ResponObject("SUCCESS", "shop has been added.", "shopnew"),
                HttpStatus.CREATED);
    }

    @PutMapping("/shop/admin/update/{id}/{status}")
    public ResponseEntity<ResponObject> updatestatusAdmin(@PathVariable("id") Integer id, @PathVariable("status") Integer status) {
        Shop shop = shopService.findById(id);
        shop.setStatus(status);
        Shop shopnew = shopService.updateShop(shop);
        return new ResponseEntity<>(new ResponObject("SUCCESS", "shop has been updated.", shopnew), HttpStatus.OK);
    }

    //    @DeleteMapping("{id}")
//    public ResponseEntity<ResponObject> delete(@PathVariable("id") Integer id) {
//        if (!shopService.existsById(id))
//            return new ResponseEntity<>(new ResponObject("NOT_FOUND", "shop_id: " + id + " does not exists.", id),
//                    HttpStatus.NOT_FOUND);
//        Shop shop = shopService.findById(id);
//        shop.setStatus(2);
//        shopService.updateshop(id, shop);
//        return new ResponseEntity<>(new ResponObject("SUCCESS", "shop has been deleted.", id), HttpStatus.OK);
//    }
    @GetMapping("/shop/find/product/status/{status}")
    public ResponseEntity<ResponObject> findById(@PathVariable Optional<Integer> status) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponObject(
                        "SUCCESS", "find shop by product status"
                        ,shopService.findShopByStatusProduct(status)));
    }

    @GetMapping("/shop/find/product/status/{status}/search")
    public ResponseEntity<ResponObject> search(@PathVariable Optional<Integer> status, @RequestParam("type") Optional<Integer> type,
                                               @RequestParam("keyword") Optional<String> keyword
                                               ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponObject(
                        "SUCCESS", "find shop by product status"
                        ,shopService.findShopByStatusProductSearch(status,type,keyword)));
    }
}

