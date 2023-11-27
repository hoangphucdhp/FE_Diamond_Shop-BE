package com.example.DATN_API.Service;


import java.util.List;
import java.util.Optional;

import com.example.DATN_API.Entity.Account;
import com.example.DATN_API.Entity.AddressShop;
import com.example.DATN_API.Entity.Shop;
import com.example.DATN_API.Reponsitories.AddressShopReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DATN_API.Reponsitories.ShopReponsitory;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ShopService {
    @Autowired
    ShopReponsitory ShopReponsitory;
    @Autowired
    AddressShopReponsitory addressShopReponsitory;
    @Autowired
    IStorageSerivce iStorageSerivce;
    public List<Shop> findAll() {
        return ShopReponsitory.findAll();
    }
    public Shop findShop(Account account) {
        return ShopReponsitory.findShopByAccount(account);
    }

    public Shop findById(int id) {
        Optional<Shop> Shop = ShopReponsitory.findById(id);
        return Shop.get();
    }
	public Shop findShopByProduct(int idProduct){
		return ShopReponsitory.findShopByProduct(idProduct);
	}

    public Shop createShop(Shop Shop) {
       return ShopReponsitory.save(Shop);
    }

    public Shop updateShop(Shop shop) {
        return ShopReponsitory.save(shop);
    }

    public void deleteShop(int id) {
        ShopReponsitory.deleteById(id);
    }

    public Boolean existsById(Integer id) {
        return ShopReponsitory.existsById(id) ? true : false;
    }

    public Shop existByAccount(Integer id_account) {
		return ShopReponsitory.findByID_Account(id_account);
	}

    public Shop bussinessUpdateInf(int id, String shopName, String city, String district, String ward, String address,Optional<MultipartFile> image){
        Shop shop=findById(id);
        AddressShop addressShop= addressShopReponsitory.findByShop(shop);
        //Set shop
        shop.setShop_name(shopName);
        if(image.isPresent()){
            String name= iStorageSerivce.storeFile(image.get());
            shop.setImage(name);
        }
        //Set address
        addressShop.setCity(city);
        addressShop.setDistrict(district);
        addressShop.setWard(ward);
        addressShop.setAddress(address);
        addressShopReponsitory.save(addressShop);
        return ShopReponsitory.save(shop);
    }
}
