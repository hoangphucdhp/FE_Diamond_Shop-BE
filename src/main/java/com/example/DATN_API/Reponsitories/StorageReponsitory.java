package com.example.DATN_API.Reponsitories;

import com.example.DATN_API.Entity.Status;
import com.example.DATN_API.Entity.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageReponsitory extends JpaRepository<Storage, Integer> {
}
