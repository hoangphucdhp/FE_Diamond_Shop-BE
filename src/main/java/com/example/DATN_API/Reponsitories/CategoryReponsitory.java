package com.example.DATN_API.Reponsitories;

import com.example.DATN_API.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryReponsitory extends JpaRepository<Category, Integer> {

}
