package com.example.DATN_API.Service;

import com.example.DATN_API.Entity.AddressAccount;
import com.example.DATN_API.Reponsitories.AddressAccountReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressAccountService {
    @Autowired
    AddressAccountReponsitory addressAccountReponsitory;
    public AddressAccount getAddressDefault(int id){
        return addressAccountReponsitory.getAddressDefault(id);
    }
}
