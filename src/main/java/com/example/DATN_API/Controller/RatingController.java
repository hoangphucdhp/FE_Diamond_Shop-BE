package com.example.DATN_API.Controller;

import com.example.DATN_API.Entity.Account;
import com.example.DATN_API.Entity.Product;
import com.example.DATN_API.Entity.Rate;

import com.example.DATN_API.Entity.ResponObject;
import com.example.DATN_API.Reponsitories.AccountReponsitory;
import com.example.DATN_API.Reponsitories.ProductRepository;
import com.example.DATN_API.Reponsitories.RateRepository;

import com.example.DATN_API.Service.AccountService;
import com.example.DATN_API.Service.ProductService;
import com.example.DATN_API.Service.RateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/ratings")
@CrossOrigin(origins = "*")
public class RatingController {

    @Autowired
    RateService rateService;

    @Autowired
    AccountService accountService;

    @Autowired
    ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<?> getRating(@PathVariable int productId) {
        try {
            Product product = productService.findById(productId);
            List<Rate> ratings = rateService.findByProduct_rate(product);

            // Kiểm tra xem có đánh giá nào không
            if (ratings != null && !ratings.isEmpty()) {
                return new ResponseEntity<>(ratings, HttpStatus.OK);
            } else {
                // Trả về danh sách rỗng nếu không có đánh giá
                return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Lỗi xảy ra khi xử lý yêu cầu", HttpStatus.OK);
        }
    }

    @PostMapping("/add/{accountid}")
    public ResponseEntity<String> addRating(@PathVariable("accountid") int accountId, @RequestBody Map<String, Object> ratingData) {
        try {
            int productId = (int) ratingData.get("productId");
            int start = (int) ratingData.get("start");
            String description = (String) ratingData.get("description");

            Product product = productService.findById(productId);
            Account account = accountService.findById(accountId);

            // Kiểm tra xem tài khoản đã đánh giá sản phẩm chưa
            if (rateService.existsByAccount_rateAndProduct_rate(account, product)) {
                return new ResponseEntity<>("Bạn đã đánh giá sản phẩm này.", HttpStatus.BAD_REQUEST);
            }

            Rate rate = new Rate();
            rate.setProduct_rate(product);
            rate.setAccount_rate(account);
            rate.setStar(start);
            rate.setDescription(description);
            rate.setCreateDate(LocalDateTime.now());
            rate.setImage((String) ratingData.get("image"));
            rateService.save(rate);
            return new ResponseEntity<>("Đã đánh giá sản phẩm thành công", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CREATED);
        }
    }

    @GetMapping("/avg/{productId}")
    public Optional<Double> getAverageStarByProduct(@PathVariable int productId) {
        Product product = productService.findById(productId);

        Double averageStar = rateService.findAverageStarByProduct(product);

        return averageStar != null ? Optional.of(Math.floor(averageStar * 10) / 10) : Optional.empty();
    }




    @PutMapping("/update/{ratingId}")
    public ResponseEntity<String> updateRating(
            @PathVariable int ratingId,
            @RequestBody Map<String, Object> ratingData
    ) {
        try {
            // Get rating by ratingId
            Rate existingRating = rateService.findById(ratingId);
            if (existingRating == null) {
                return new ResponseEntity<>("Rating not found", HttpStatus.NOT_FOUND);
            }

            // Update rating fields
            Object newStarObj = ratingData.get("star");
            Object newDescriptionObj = ratingData.get("description");
            System.out.println("New Star: " + newStarObj + ", New Description: " + newDescriptionObj);
            // Check if the retrieved values are not null before converting to int or String
            if (newStarObj != null && newDescriptionObj != null) {
                int newStar = ((Number) newStarObj).intValue();
                String newDescription = newDescriptionObj.toString();

                existingRating.setStar(newStar);
                existingRating.setDescription(newDescription);
                existingRating.setImage((String) ratingData.get("image"));
                // Save updated rating
                rateService.save(existingRating);

                System.out.println("Rating updated successfully. Rating ID: " + ratingId);
                return new ResponseEntity<>("Rating updated successfully", HttpStatus.OK);
            } else {
                System.out.println("Invalid request data. Rating ID: " + ratingId);
                return new ResponseEntity<>("Invalid request data", HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error updating rating. Rating ID: " + ratingId);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getTotalBuy/{product}")
    public Long getTotalBuy(@PathVariable("product") int idproduct) {
        return rateService.getTotalBuy(idproduct);
    }

    @GetMapping("/findByStar/{product}/{star}")
    public ResponseEntity<ResponObject> findByStar(@PathVariable("product") Integer idproduct,@PathVariable("star") Integer star) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                "SUCCESS", "FIND ALL PRODUCT", rateService.findByStar(star,idproduct)));
    }
    @GetMapping("/getAll/{product}")
    public ResponseEntity<ResponObject> findAll(@PathVariable("product") Integer idproduct,@PathVariable("star") Integer star) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                "SUCCESS", "FIND ALL PRODUCT", rateService.findByStar(star,idproduct)));
    }
}