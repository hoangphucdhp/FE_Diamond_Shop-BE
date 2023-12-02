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
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("select p from Product p where p.shop=?1")
    Page<Product> findAllByShop(Pageable pageable, Shop shop);

    @Query("select p from Product p where p.shop=?1 and p.status=1")
    Page<Product> findAllByShopStatus(Pageable pageable, Shop shop);

    @Query("SELECT p FROM Product p WHERE p.product_name LIKE %?1% and p.status = ?2")
    List<Product> findByName(String keyword, int status);

    @Query("SELECT p FROM Product p JOIN p.listStorage s WHERE p.shop = ?1 GROUP BY p HAVING SUM(s.quantity) = 0")
    Page<Product> findAllByShopAndTotalQuantityZero(Pageable pageable, Shop shop);

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
    @Query("select pro from Product pro where CAST(pro.id AS String) like %?1% and pro.categoryItem_product=?2 and pro.shop=?3")
    Page<Product> searchProductByIdAndCategory(Pageable pageable, String id, CategoryItem categoryItem, Shop shop);

    @Query("select pro from Product pro where pro.product_name like %?1% and pro.categoryItem_product=?2 and pro.shop=?3")
    Page<Product> searchProductByNameAndCategory(String name, CategoryItem categoryItem, Shop shop, Pageable pageable);

    @Query("select pro from Product pro where pro.categoryItem_product.id=?1 and pro.shop=?2")
    Page<Product> searchProductByCategory(Pageable pageable, int categoryItem, Shop shop);

    @Query("select pro from Product pro  where CAST(pro.id AS String) like %?1% and pro.shop=?2")
    Page<Product> getAllbyIdBussiness(Pageable pageable, String name, Shop shop);

    @Query("select pro from Product pro where pro.product_name like %?1% and pro.shop=?2")
    Page<Product> getAllbyNameBussiness(Pageable pageable, String name, Shop shop);

    //GetAll check Status
    @Query("select pro from Product pro where CAST(pro.id AS String) like %?1% and pro.categoryItem_product=?2 and pro.shop=?3 and CAST(pro.status AS String) = '1'")
    Page<Product> searchProductByIdAndCategoryStatus(Pageable pageable, String id, CategoryItem categoryItem, Shop shop);

    @Query("select pro from Product pro where pro.product_name like %?1% and pro.categoryItem_product=?2 and pro.shop=?3and CAST(pro.status AS String) = '1'")
    Page<Product> searchProductByNameAndCategoryStatus(String name, CategoryItem categoryItem, Shop shop, Pageable pageable);

    @Query("select pro from Product pro where pro.categoryItem_product.id=?1 and pro.shop=?2 and CAST(pro.status AS String) = '1'")
    Page<Product> searchProductByCategoryStatus(Pageable pageable, int categoryItem, Shop shop);

    @Query("select pro from Product pro  where pro.id = ?1 and pro.shop.id=?2 and CAST(pro.status AS String) = '1'")
    Page<Product> getAllbyIdBussinessStatus(Pageable pageable, String name, Optional<Integer> shop);

    @Query("select pro from Product pro where pro.product_name like %?1% and pro.shop=?2 and CAST(pro.status AS String) = '1'")
    Page<Product> getAllbyNameBussinessStatus(Pageable pageable, String name, Shop shop);
    //end

    //getAllbussiness check quantity
    @Query("SELECT pro FROM Product pro JOIN pro.listStorage s WHERE CAST(pro.id AS String) LIKE %?1% AND pro.categoryItem_product = ?2 AND pro.shop = ?3 GROUP BY pro HAVING SUM(s.quantity) = 0")
    Page<Product> searchProductByIdAndCategoryAndZeroQuantity(String id, CategoryItem categoryItem, Shop shop, Pageable pageable);

    @Query("SELECT pro FROM Product pro JOIN pro.listStorage s WHERE pro.product_name LIKE %?1% AND pro.categoryItem_product = ?2 AND pro.shop = ?3 GROUP BY pro HAVING SUM(s.quantity) = 0")
    Page<Product> searchProductByNameAndCategoryAndZeroQuantity(String name, CategoryItem categoryItem, Shop shop, Pageable pageable);

    @Query("SELECT pro FROM Product pro JOIN pro.listStorage s WHERE pro.categoryItem_product.id = ?1 AND pro.shop = ?2 GROUP BY pro HAVING SUM(s.quantity) = 0")
    Page<Product> searchProductByCategoryAndZeroQuantity(int categoryItem, Shop shop, Pageable pageable);

    @Query("SELECT pro FROM Product pro JOIN pro.listStorage s WHERE CAST(pro.id AS String) LIKE %?1% AND pro.shop = ?2 GROUP BY pro HAVING SUM(s.quantity) = 0")
    Page<Product> getAllByIdBussinessAndZeroQuantity(String id, Shop shop, Pageable pageable);

    @Query("SELECT pro FROM Product pro JOIN pro.listStorage s WHERE pro.product_name LIKE %?1% AND pro.shop = ?2 GROUP BY pro HAVING SUM(s.quantity) = 0")
    Page<Product> getAllByNameAndZeroQuantity(Pageable pageable, String name, Shop shop);


}