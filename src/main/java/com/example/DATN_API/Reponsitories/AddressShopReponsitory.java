package com.example.DATN_API.Reponsitories;

import com.example.DATN_API.Entity.AddressShop;

import com.example.DATN_API.Entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

@Repository
public interface AddressShopReponsitory extends JpaRepository<AddressShop, Integer> {

    @Query("select address from AddressShop address where address.shopAddress=?1")
    AddressShop findByShop(Shop shop);

}
