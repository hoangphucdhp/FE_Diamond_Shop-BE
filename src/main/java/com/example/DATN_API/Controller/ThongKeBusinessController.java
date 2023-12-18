
package com.example.DATN_API.Controller;

import java.util.ArrayList;
import java.util.Date;
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
    public ResponseEntity<ResponObject> getBillInShop(@PathVariable("idShop") int idShop,
                                                      @RequestParam("year") String year, @RequestParam("month") String month, @RequestParam("day") String day) {
        Map<Integer, Object[]> listProductBand = new HashMap<>();
        Map<Integer, Object[]> listProductStocking = new HashMap<>();
        Map<Integer, Object[]> listQuantityOrder = new HashMap<>();
        Map<Integer, Object[]> listQuantityStorage = new HashMap<>();
        Map<Integer, String> listYears = new HashMap<>();
        Map<Integer, String> listMonths = new HashMap<>();
        Map<Integer, String> listDays = new HashMap<>();
        List<Object[]> listTotal = orderDetailService.getTotalByMonth(idShop);

        List<Object[]> listStatistical = new ArrayList<>();
        Date date = new Date();
        if (year != "" && month != "" && day != "") {
            listStatistical = orderDetailService.statistical(idShop, year, month, day);
        } else {
            listStatistical = orderDetailService.statistical(idShop, String.valueOf(date.getYear() + 1900),
                    String.valueOf(date.getMonth() + 1), String.valueOf(date.getDate()));
        }
        List<Object[]> listStatisticalSave = new ArrayList<>();
        ;
        // ĐẾM SỐ LƯỢNG SẢN PHẨM CẤM BÁN VÀ SỐ LƯỢNG SẢN PHẨM HẾT HÀNG
        for (Product p : shopService.findById(idShop).getProducts()) {
            // GET QUANTITY PRODUCT IN STORAGE
            for (Storage st : p.getListStorage()) {
                listQuantityStorage.put(st.getId(),
                        new Object[] { st.getProduct().getId(), st.getQuantity(), st.getType() });
            }
            // GET QUANTITY PRODUCT IN ORDER
            for (OrderDetail order : p.getListOrderDetail()) {
                listQuantityOrder.put(order.getId(),
                        new Object[] { order.getProductOrder().getId(), order.getQuantity() });
            }

            if (p.getStatus() == 3) {
                listProductBand.put(p.getId(), new Object[] { p.getProduct_name() });
            } else if (p.getStatus() == 1) {
                int quantityInStorage = 0;
                // TỔNG SỐ LƯỢNG TRONG STORAGE
                for (Object[] value : listQuantityStorage.values()) {
                    if (value[0].equals(p.getId())) {
                        if (value[2].equals("cong")) {
                            quantityInStorage += Integer.parseInt(value[1].toString());
                        } else {
                            quantityInStorage -= Integer.parseInt(value[1].toString());
                        }
                    }
                }

                if (quantityInStorage <= 0) {
                    listProductStocking.put(p.getId(),
                            new Object[] { p.getProduct_name(), quantityInStorage });
                }
            }
        }
        // TẠO DANH SÁCH LƯU VÀO CHART
        for (Object[] value : listStatistical) {
            listYears.put(Integer.parseInt(value[1].toString()), "YEAR");
            listMonths.put(Integer.parseInt(value[2].toString()), "MONTH");
            listDays.put(Integer.parseInt(value[3].toString()), "DAY");
        }

        for (Object[] value : listStatistical) {
            int sumProduct = 0;
            int amountBill = 0;
            String label = "";
            for (int y : listYears.keySet()) {
                for (int m : listMonths.keySet()) {
                    if (value[1].equals(y) && value[2].equals(m)) {
                        sumProduct += Integer.parseInt(value[0].toString());
                        amountBill++;
                        if (Integer.parseInt(value[5].toString()) >= 13
                                && Integer.parseInt(value[5].toString()) <= 24) {
                            label = value[5] + "PM - " + value[3] + "/" + m + "/" + y;
                        } else {
                            label = value[5] + "AM - " + value[3] + "/" + m + "/" + y;
                        }
                    }
                }
            }
            listStatisticalSave.add(new Object[] { sumProduct, amountBill, label });
        }

        Map<String, Object[]> listFinal = new HashMap<>();
        for (Object[] item : listStatisticalSave) {
            String label = (String) item[2];

            if (listFinal.containsKey(label)) {
                Object[] existingData = listFinal.get(label);
                int sumQuantity = (int) existingData[1] + Integer.parseInt(item[0].toString());
                int count = (int) existingData[2] + 1;
                listFinal.put(label, new Object[] { label, sumQuantity, count });
            } else {
                int sumQuantity = Integer.parseInt(item[0].toString());
                int count = 1;
                listFinal.put(label, new Object[] { label, sumQuantity, count });
            }
        }

        List<Object[]> listStaticalDone = new ArrayList<>();
        for (Object[] value : listFinal.values()) {
            listStaticalDone.add(new Object[] { value[0], value[1], value[2] });
        }

        return new ResponseEntity<>(
                new ResponObject("success", "OK VÀO VIỆC",
                        new Object[] { listTotal, listProductBand.size(), listProductStocking.size(),
                                listStaticalDone }),
                HttpStatus.OK);
    }

    @GetMapping("/month/{idShop}")
    public ResponseEntity<ResponObject> getTotalMonth(@PathVariable("idShop") int idShop,
                                                      @RequestParam("year") String year) {
        List<Object[]> listTotal = orderDetailService.statisticalMonth(idShop,year);
        return new ResponseEntity<>(
                new ResponObject("success", "OK VÀO VIỆC", listTotal),
                HttpStatus.OK);
    }
}
