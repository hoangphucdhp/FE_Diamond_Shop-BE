package com.example.DATN_API.Service;

import java.io.IOException;

import java.util.List;
import java.util.Optional;

import com.example.DATN_API.Entity.ImageProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DATN_API.Reponsitories.ImageProductReponsitory;

@Service
public class ImageProductService {

    @Autowired
    ImageProductReponsitory ImageProductReponsitory;

    public List<ImageProduct> findAll() {
        return ImageProductReponsitory.findAll();
    }

    public ImageProduct findById(int id) {
        Optional<ImageProduct> ImageProduct = ImageProductReponsitory.findById(id);
        return ImageProduct.get();
    }

    public void createImageProduct(ImageProduct ImageProduct) {
        ImageProductReponsitory.save(ImageProduct);
    }

    public void updateImageProduct(int id, ImageProduct ImageProduct) {
        ImageProduct.setId(id);
        ImageProductReponsitory.save(ImageProduct);
    }

    public boolean deleteImageProduct(int id) {
        ImageProduct imageProduct = findById(id);
        try {
            ImageProductReponsitory.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }

    public Boolean existsById(Integer id) {
        return ImageProductReponsitory.existsById(id) ? true : false;
    }

}

