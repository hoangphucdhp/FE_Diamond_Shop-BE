package com.example.DATN_API.Reponsitories;

import com.example.DATN_API.Entity.CategoryItem;
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
	Page<Product> findAllByShop(Pageable pageable,Shop shop);

	@Query("select p from Product p where p.status=?1 and p.shop=?2")
	Page<Product> getProductbyStatus(Pageable pageable,int status, Shop shop);

	@Query("select p from Product p where p.status=?1")
	Page<Product> getPageProduct(int status, Pageable pageable);

//	@Query("SELECT p FROM Product p WHERE p.product_name LIKE %?1%"
//			+ " AND CAST(p.categoryItem_product.id AS STRING) LIKE %?2%" + " AND CAST(p.status AS STRING) LIKE %?3%"
//			+ " AND p.shop =?4")
//	Page<Product> findByProductName(Pageable pageable,String keyword, String idCategoryItem, String status, Shop shop);
//
//	@Query("SELECT p FROM Product p WHERE CAST(p.id AS STRING) LIKE %?1%"
//			+ " AND CAST(p.categoryItem_product.id AS STRING) LIKE %?2%" + " AND CAST(p.status AS STRING) LIKE %?3%"
//			+ " AND p.shop =?4")
//	Page<Product> findByKey(Pageable pageable,String keyword, String idCategoryItem, String status, Shop shop);

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

	@Query("select pro from Product pro where pro.product_name like %?1%")
	Page<Product> getAllbyName(Pageable pageable, String name);

	@Query("select pro from Product pro  where pro.shop.shop_name like %?1%")
	Page<Product> getAllbyShop(Pageable pageable,String name);
	@Query("select pro from Product pro  where CAST(pro.id AS String) like %?1%")
	Page<Product> getAllbyId(Pageable pageable,String name);

	@Query("select pro from Product pro")
	Page<Product> getAll(Pageable pageable);


	//Mdung search product bussiness CẤM ĐỤNG VÀO
	@Query("select pro from Product pro where CAST(pro.id AS String) like %?1% and pro.categoryItem_product=?2 and pro.shop=?3")
	Page<Product> searchProductByIdAndCategory(Pageable pageable, String id, CategoryItem categoryItem,Shop shop);

	@Query("select pro from Product pro where pro.product_name like %?1% and pro.categoryItem_product=?2 and pro.shop=?3")
	Page<Product> searchProductByNameAndCategory(String name, CategoryItem categoryItem,Shop shop,Pageable pageable);

	@Query("select pro from Product pro where pro.categoryItem_product.id=?1 and pro.shop=?2")
	Page<Product> searchProductByCategory(Pageable pageable, int categoryItem, Shop shop);

	@Query("select pro from Product pro  where CAST(pro.id AS String) like %?1% and pro.shop=?2")
	Page<Product> getAllbyIdBussiness(Pageable pageable,String name,Shop shop);

	@Query("select pro from Product pro where pro.product_name like %?1% and pro.shop=?2")
	Page<Product> getAllbyNameBussiness(Pageable pageable, String name, Shop shop);

}