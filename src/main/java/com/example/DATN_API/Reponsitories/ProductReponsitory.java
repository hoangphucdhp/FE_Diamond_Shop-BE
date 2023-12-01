package com.example.DATN_API.Reponsitories;

import com.example.DATN_API.Entity.Product;
import com.example.DATN_API.Entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductReponsitory extends JpaRepository<Product, Integer> {


    @Query("select p from Product p where p.status=?1")
    Page<Product> getPageProduct(int status, Pageable pageable);

	@Query("select p from Product p where p.status=?1")
	List<Product> getProductbyStatus(int status);

	@Query("select p from Product p where p.shop=?1")
	List<Product> findAllByShop(Shop shop);
	@Query("select p from Product p where p.status=?1 and p.shop=?2")
	List<Product> getProductbyStatus(int status,Shop shop);

	@Query("SELECT p FROM Product p WHERE p.product_name LIKE %?1%"
			+ " AND CAST(p.categoryItem_product.id AS STRING) LIKE %?2%" + " AND CAST(p.status AS STRING) LIKE %?3%"+ " AND p.shop =?4")
	List<Product> findByProductName(String keyword, String idCategoryItem, String status, Shop shop);

	@Query("SELECT p FROM Product p WHERE CAST(p.id AS STRING) LIKE %?1%"
			+ " AND CAST(p.categoryItem_product.id AS STRING) LIKE %?2%" + " AND CAST(p.status AS STRING) LIKE %?3%"+ " AND p.shop =?4")
	List<Product> findByKey(String keyword, String idCategoryItem, String status,Shop shop);

	@Query("SELECT p FROM Product p WHERE p.product_name LIKE %?1% and p.status = ?2")
	List<Product> findByName(String keyword, int status);

}