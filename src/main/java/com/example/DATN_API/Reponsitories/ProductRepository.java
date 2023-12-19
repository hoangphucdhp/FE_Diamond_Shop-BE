package com.example.DATN_API.Reponsitories;

import com.example.DATN_API.Entity.CategoryItem;
import com.example.DATN_API.Entity.Product;
import com.example.DATN_API.Entity.Shop;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("select p from Product p where p.shop=?1 ")
    Page<Product> findAllByShop(Pageable pageable, Shop shop);

    @Query("select pro from Product pro  where  CAST(pro.id AS String) like %?1% and pro.shop=?2")
    Page<Product> getAllbyIdBussiness(Pageable pageable, String name, Shop shop);

    @Query("select pro from Product pro where pro.product_name like %?1% and pro.shop=?2")
    Page<Product> getAllbyNameBussiness(Pageable pageable, String name, Shop shop);

    @Query("SELECT p FROM Product p WHERE p.product_name LIKE %?1% and p.status = ?2")
    List<Product> findByName(String keyword, int status);

    @Query("select p from Product p where p.status=?1 and p.shop=?2")
    Page<Product> getProductbyStatus(Pageable pageable, int status, Shop shop);

    @Query("select p from Product p where p.status=?1")
    Page<Product> getPageProduct(int status, Pageable pageable);

    @Query(value = "SELECT TOP 10 " +
            "p.id AS idProduct, " +
            "p.id_Shop AS idShop, " +
            "p.product_Name AS productName, " +
            "p.price AS price, " +
            "p.create_Date AS createDate, " +
            "(SELECT ip.url FROM Image_Product ip WHERE ip.id_Product = p.id FOR JSON AUTO) AS imageUrls " +
            "FROM Product p " +
            "LEFT JOIN Like_Product lp ON p.id = lp.id_Product " +
            "WHERE p.status = 1" +
            "ORDER BY p.create_Date DESC", nativeQuery = true)
    List<Object[]> getTop10Products();

    @Query("SELECT p FROM Product p " +
            "WHERE p.categoryItem_product= (" +
            "   SELECT p2.categoryItem_product FROM Product p2 WHERE p2.id = :productId" +
            ") AND p.id <> :productId " +
            "ORDER BY FUNCTION('NEWID')")
    List<Product> findSimilarProducts(@Param("productId") int productId);

    @Query("select pro from Product pro where pro.product_name like %:name% and (:status = '' or cast(pro.status as STRING )  = :status)")
    Page<Product> getAllbyName(Pageable pageable, @Param("name") String name, @Param("status") String status);

    @Query("select pro from Product pro where pro.shop.shop_name like %:name% and (:status = '' or cast(pro.status as STRING )  = :status)")
    Page<Product> getAllbyShop(Pageable pageable, @Param("name") String name, @Param("status") String status);

    @Query("select pro from Product pro where CAST(pro.id AS string) like %:name% and (:status = '' or cast(pro.status as STRING )  = :status)")
    Page<Product> getAllbyId(Pageable pageable, @Param("name") String name, @Param("status") String status);

    @Query("select pro from Product pro where (:status = '' or cast(pro.status as STRING )  = :status)")
    Page<Product> getAll(Pageable pageable, @Param("status") String status);


    //Mdung search product bussiness CẤM ĐỤNG VÀO
    @Query("select pro from Product pro where (:id = '' or cast(pro.id as STRING ) like %:id%)  and (:categoryItem IS NULL OR pro.categoryItem_product = :categoryItem) and pro.shop=:shop and (:status = '' or cast(pro.status as STRING )  = :status)")
    Page<Product> searchProductByIdAndCategory(Pageable pageable, @Param("id") String id, @Param("categoryItem") CategoryItem categoryItem, @Param("shop") Shop shop, @Param("status") String status);

    @Query("select pro from Product pro where (:product_name = '' or pro.product_name like %:product_name%)  and (:categoryItem IS NULL OR pro.categoryItem_product = :categoryItem) and pro.shop=:shop and (:status = '' or cast(pro.status as STRING )  = :status)")
    Page<Product> searchProductByNameAndCategory(@Param("product_name") String product_name, @Param("categoryItem") CategoryItem categoryItem, @Param("shop") Shop shop, @Param("status") String status, Pageable pageable);

    @Query("select pro from Product pro JOIN pro.listStorage s where (:id = '' or cast(pro.id as STRING ) like %:id%)  and (:categoryItem IS NULL OR pro.categoryItem_product = :categoryItem) and pro.shop=:shop and (:status = '' or cast(pro.status as STRING )  = :status) GROUP BY pro HAVING COALESCE(SUM(CASE WHEN s.type = 'cong' THEN s.quantity ELSE 0 END), 0) - COALESCE(SUM(CASE WHEN s.type = 'tru' THEN s.quantity ELSE 0 END), 0) = 0")
    Page<Product> searchProductByIdAndCategoryZeroQuantity(Pageable pageable, @Param("id") String id, @Param("categoryItem") CategoryItem categoryItem, @Param("shop") Shop shop, @Param("status") String status);

    @Query("select pro from Product pro JOIN pro.listStorage s where (:id = '' or pro.product_name like %:id%)  and (:categoryItem IS NULL OR pro.categoryItem_product = :categoryItem) and pro.shop=:shop and (:status = '' or cast(pro.status as STRING )  = :status) GROUP BY pro HAVING COALESCE(SUM(CASE WHEN s.type = 'cong' THEN s.quantity ELSE 0 END), 0) - COALESCE(SUM(CASE WHEN s.type = 'tru' THEN s.quantity ELSE 0 END), 0) = 0")
    Page<Product> searchProductByNameAndCategoryZeroQuantity(@Param("id") String id, @Param("categoryItem") CategoryItem categoryItem, @Param("shop") Shop shop, @Param("status") String status, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE Product s SET s.status = :status WHERE s.id = :id")
    void BanProduct(int id, int status);


    @Query("select p from Product p where p.product_name like %?1% and p.status=1")
    Page<Product> searchBarProduct(String key, Pageable pageable);

    @Query("select p from Shop p where p.shop_name like %?1% and p.status=1")
    Page<Shop> searchBarShop(String key, Pageable pageable);

    @Query(value = "SELECT pro FROM Product pro join" +
            " pro.categoryItem_product.category cate WHERE" +
            " (:category = 0  OR cate.id = :category) and" +
            " (:categoryItem IS NULL OR pro.categoryItem_product = :categoryItem) AND" +
            " (pro.price BETWEEN :minPrice AND :maxPrice) and cast(pro.status as string ) = '1' " +
            "AND (:star = 0 OR pro.id IN (SELECT ra.product_rate.id FROM Rate ra GROUP BY ra.product_rate.id HAVING FLOOR(AVG(ra.star)) = :star))")
    Page<Product> searchProductUser(
            Pageable pageable,
            @Param("category") int category,
            @Param("categoryItem") CategoryItem categoryItem,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("star") int star
    );
    @Query("SELECT COUNT(p.id) FROM Product p WHERE p.status = ?1")
    int getAmountProduct(int status);

    @Query("SELECT COALESCE(SUM(CASE WHEN s.type = 'cong' THEN s.quantity ELSE 0 END), 0) - COALESCE(SUM(CASE WHEN s.type = 'tru' THEN s.quantity ELSE 0 END), 0) FROM Product p JOIN p.listStorage s WHERE p.id = :status")
    int getTotalQuantityDifference(@Param("status") int status);
}