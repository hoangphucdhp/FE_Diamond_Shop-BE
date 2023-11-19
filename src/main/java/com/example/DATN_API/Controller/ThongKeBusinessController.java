package com.example.DATN_API.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.example.DATN_API.Entity.OrderDetail;
import com.example.DATN_API.Entity.Product;
import com.example.DATN_API.Entity.ResponObject;

import com.example.DATN_API.Entity.Storage;
import com.example.DATN_API.Service.OrderDetailService;
import com.example.DATN_API.Service.OrderService;
import com.example.DATN_API.Service.ProductService;
import com.example.DATN_API.Service.ShopService;

@RestController
@RequestMapping("/api/business/thongke")
@CrossOrigin("*")

public class ThongKeBusinessController {
    @Autowired
    OrderService orderService;
    @Autowired
    OrderDetailService orderDetailService;
    @Autowired
    ShopService shopService;
    @Autowired
    ProductService productService;

    @GetMapping("{idShop}")
    public ResponseEntity<ResponObject> getBillInShop(@PathVariable("idShop") int idShop) {

        Map<Integer, Object[]> listProductBand = new HashMap<>();
        Map<Integer, Object[]> listProductStocking = new HashMap<>();
        Map<Integer, Object[]> listQuantityOrder = new HashMap<>();
        Map<Integer, Object[]> listQuantityStorage = new HashMap<>();
        List<Object[]> listTotal = orderDetailService.getTotalByMonth(idShop);

        // ĐẾM SỐ LƯỢNG SẢN PHẨM CẤM BÁN VÀ SỐ LƯỢNG SẢN PHẨM HẾT HÀNG
        for (Product p : shopService.findById(idShop).getProducts()) {
            // GET QUANTITY PRODUCT IN STORAGE
            for (Storage st : p.getListStorage()) {
                listQuantityStorage.put(st.getId(), new Object[] { st.getProduct().getId(), st.getQuantity() });
            }
            // GET QUANTITY PRODUCT IN ORDER
            for (OrderDetail order : p.getListOrderDetail()) {
                listQuantityOrder.put(order.getId(),
                        new Object[] { order.getProductOrder().getId(), order.getQuantity() });
            }

            if (p.getStatus() == 3) {
                listProductBand.put(p.getId(), new Object[] { p.getProduct_name() });
            } else if (p.getStatus() == 1) {
                int quantityInOrder = 0;
                int quantityInStorage = 0;
                // TỔNG SỐ LƯỢNG TRONG BILL
                for (Object[] value : listQuantityOrder.values()) {
                    if (value[0].equals(p.getId())) {
                        quantityInOrder += Integer.parseInt(value[1].toString());
                    }
                }
                // TỔNG SỐ LƯỢNG TRONG STORAGE
                for (Object[] value : listQuantityStorage.values()) {
                    if (value[0].equals(p.getId())) {
                        quantityInStorage += Integer.parseInt(value[1].toString());
                    }
                }

                if (quantityInStorage - quantityInOrder <= 0) {
                    listProductStocking.put(p.getId(),
                            new Object[] { p.getProduct_name(), quantityInStorage, quantityInOrder });
                }
            }
        }

        return new ResponseEntity<>(
                new ResponObject("success", "OK VÀO VIỆC",
                        new Object[] {  listTotal, listProductBand.size(), listProductStocking.size() }),
                HttpStatus.OK);
    }
}
