package com.example.DATN_API.Reponsitories;

import com.example.DATN_API.Entity.Category;


import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

@Repository
public interface CategoryReponsitory extends JpaRepository<Category, Integer> {


    //Mdung Làm Category Admin cấm đụng vào

    @Query("select cate from Category cate")
    Page<Category> getAll(Pageable pageable);

    @Query("select cate from Category cate where CAST(cate.id as string ) like %?1%")
    Page<Category> getAllById(Pageable pageable,String id);

    @Query("select cate from Category cate where cate.type_category like %?1%")
    Page<Category> getAllByType_category(Pageable pageable,String id);

    @Query("select cate from Category cate where LOWER(cate.type_category) = LOWER(?1)")
    Category findByType_category(String type);

}
