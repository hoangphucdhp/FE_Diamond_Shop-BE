package com.example.DATN_API.Controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.DATN_API.Reponsitories.LikeProductReponsitory;
import com.example.DATN_API.Service.AccountService;
import com.example.DATN_API.Service.LikeProductService;
import com.example.DATN_API.Service.ProductService;
import com.example.DATN_API.Entity.*;

@RestController
@CrossOrigin("*")
public class LikeProductController {
	@Autowired
    LikeProductService likeProductService;
	@Autowired
	ProductService productService;
	@Autowired
	AccountService accountService;

	@GetMapping("/api/likeProducts")
	public String getLikeProductsByAccountId(@RequestParam("accountId") int accountId) {
	    return likeProductService.findProductDetailsByAccountId(accountId);
	}

	@PostMapping("/api/like_Products")
	public ResponseEntity<String> likeProduct(@RequestParam("accountId") int accountId, @RequestParam("productId") int productId) {
		LikeProduct likeProduct = likeProductService.findByProductLikeIdAndAccountLikeId(productId, accountId);
		if (likeProduct != null) {
			return ResponseEntity.badRequest().body("Sản phẩm đã được like trước đó.");
		}

		// Tạo một đối tượng LikeProduct mới và gán sản phẩm và tài khoản tương ứng
		likeProduct = new LikeProduct();

		// Lấy sản phẩm từ productId
		Product product = productService.findById(productId);

		if (product == null) {
			return ResponseEntity.badRequest().body("Không tìm thấy sản phẩm với ID: " + productId);
		}

		Account account = accountService.findById(accountId);

		likeProduct.setProductLike(product);
		likeProduct.setAccountLike(account);

		likeProduct.setCreateDate(LocalDateTime.now());
		likeProductService.save(likeProduct);

		return ResponseEntity.ok("Sản phẩm đã được like.");
	}



	@DeleteMapping("/api/unlike_Products")
    public ResponseEntity<String> unlikeProduct(@RequestParam("accountId") int accountId, @RequestParam("productId") int productId) {
        LikeProduct likeProduct = likeProductService.findByProductLikeIdAndAccountLikeId(productId, accountId);
        if (likeProduct == null) {
            return ResponseEntity.badRequest().body("Sản phẩm chưa được like.");
        }

        likeProductService.delete(likeProduct);
        return ResponseEntity.ok("Sản phẩm đã được unlike.");
    }
}