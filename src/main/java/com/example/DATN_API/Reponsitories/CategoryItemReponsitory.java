package com.example.DATN_API.Reponsitories;


import com.example.DATN_API.Entity.Category;
import com.example.DATN_API.Entity.CategoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

@Repository
public interface CategoryItemReponsitory extends JpaRepository<CategoryItem, Integer> {

    @Query("select cate from CategoryItem cate where LOWER(cate.type_category_item) = LOWER(?1) and cate.category.id=?2")
    CategoryItem findByType_categoryItem(String type,int id);

}
