package com.example.DATN_API.Service;


import com.example.DATN_API.Entity.Storage;
import com.example.DATN_API.Reponsitories.StorageReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StorageService {
    @Autowired
    StorageReponsitory StorageReponsitory;

    public List<Storage> findAll() {
        return StorageReponsitory.findAll();
    }

    public Storage findById(int id) {
        Optional<Storage> Storage = StorageReponsitory.findById(id);
        return Storage.get();
    }

    public Storage createStorage(Storage Storage) {
        return StorageReponsitory.save(Storage);
    }

    public Storage updateStorage(int id, Storage Storage) {
        Storage.setId(id);
        return StorageReponsitory.save(Storage);
    }

    public boolean deleteStorage(int id) {
        try {
            StorageReponsitory.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean existsById(Integer id) {
        return StorageReponsitory.existsById(id) ? true : false;
    }
}

