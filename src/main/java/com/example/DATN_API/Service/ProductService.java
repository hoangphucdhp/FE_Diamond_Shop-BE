package com.example.DATN_API.Service;

import java.util.List;
import java.util.Optional;

import com.example.DATN_API.Entity.Account;
import com.example.DATN_API.Entity.Product;
import com.example.DATN_API.Entity.Shop;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.DATN_API.Reponsitories.ProductReponsitory;

@Service
public class ProductService {
	@Autowired
	ProductReponsitory productReponsitory;

	public List<Product> findAll(Shop shop) {
		return productReponsitory.findAllByShop(shop);
	}

	public List<Product> findAll() {
		return productReponsitory.findAll();

	}

	public List<Product> findProductbyStatus(int status, Shop shop) {
		return productReponsitory.getProductbyStatus(status, shop);
	}

	public Page<Product> getPageProduct(Optional<Integer> stt, Optional<Integer> offset, Optional<Integer> sp,
			Optional<String> field) {
		String sort = field.orElse("create_date");
		int itemStart = offset.orElse(0);
		;
		int sizePage = sp.orElse(20);
		int status = sp.orElse(1);

		return productReponsitory.getPageProduct(status,
				PageRequest.of(itemStart, sizePage, Sort.Direction.DESC, sort));
	}

   public Product findById(Integer id) {
        return productReponsitory.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sản phẩm không được tìm thấy"));
    }

	public Product createProduct(Product product) {
		try {
			Product productsave = productReponsitory.save(product);
			return productsave;
		} catch (Exception e) {
			e.printStackTrace();
			LogError.saveToLog(e);
		}
		return null;
	}

	public Product updateProduct(int id, Product product) {
		Product productold = findById(id);
		product.setId(id);
		product.setShop(productold.getShop());
		product.setCreate_date(productold.getCreate_date());
		product.setStart_promotion(productold.getStart_promotion());
		product.setEnd_promotion(productold.getEnd_promotion());
		try {
			Product productsave = productReponsitory.save(product);
			return productsave;
		} catch (Exception e) {
			e.printStackTrace();
			LogError.saveToLog(e);
		}
		return null;
	}

	public void deleteProduct(int id) {
		productReponsitory.deleteById(id);
	}

	public Boolean existsById(Integer id) {
		return productReponsitory.existsById(id) ? true : false;
	}

	public List<Product> findByKey(String keyword, String idCategoryItem, String status, Shop shop) {
		return productReponsitory.findByKey(keyword, idCategoryItem, status, shop);
	}

	public List<Product> findByProductName(String keyword, String idCategoryItem, String status, Shop shop) {
		return productReponsitory.findByProductName(keyword, idCategoryItem, status, shop);
	}

	public List<Object[]> getTop10Products() {
		return productReponsitory.getTop10Products();
	}
	public List<Product> findSimilarProducts(int id) {
		return productReponsitory.findSimilarProducts(id);
	}

}
