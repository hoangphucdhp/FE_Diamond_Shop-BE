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

	@Query("select p from Product p where p.shop=?1")
	List<Product> findAllByShop(Shop shop);

	@Query("select p from Product p where p.status=?1 and p.shop=?2")
	List<Product> getProductbyStatus(int status, Shop shop);

	@Query("select p from Product p where p.status=?1")
	Page<Product> getPageProduct(int status, Pageable pageable);

	@Query("SELECT p FROM Product p WHERE p.product_name LIKE %?1%"
			+ " AND CAST(p.categoryItem_product.id AS STRING) LIKE %?2%" + " AND CAST(p.status AS STRING) LIKE %?3%"
			+ " AND p.shop =?4")
	List<Product> findByProductName(String keyword, String idCategoryItem, String status, Shop shop);

	@Query("SELECT p FROM Product p WHERE CAST(p.id AS STRING) LIKE %?1%"
			+ " AND CAST(p.categoryItem_product.id AS STRING) LIKE %?2%" + " AND CAST(p.status AS STRING) LIKE %?3%"
			+ " AND p.shop =?4")
	List<Product> findByKey(String keyword, String idCategoryItem, String status, Shop shop);

	@Query(value = "SELECT TOP 10 " +
			"p.id AS idProduct, " +
			"p.id_Shop AS idShop, " +
			"p.product_Name AS productName, " +
			"p.price AS price, " +
			"p.create_Date AS createDate, " +
			"(SELECT ip.url FROM Image_Product ip WHERE ip.id_Product = p.id FOR JSON AUTO) AS imageUrls " +
			"FROM Product p " +
			"LEFT JOIN Like_Product lp ON p.id = lp.id_Product " +
			"ORDER BY p.create_Date DESC", nativeQuery = true)
	List<Object[]> getTop10Products();

	@Query("SELECT p FROM Product p " +
			"WHERE p.categoryItem_product= (" +
			"   SELECT p2.categoryItem_product FROM Product p2 WHERE p2.id = :productId" +
			") AND p.id <> :productId " +
			"ORDER BY FUNCTION('NEWID')")
	List<Product> findSimilarProducts(@Param("productId") int productId);

}