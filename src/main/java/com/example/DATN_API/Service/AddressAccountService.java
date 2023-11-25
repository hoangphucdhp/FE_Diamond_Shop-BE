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

    public AddressAccount save(AddressAccount addressAccount) {
        try {
            return addressAccountReponsitory.save(addressAccount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void delete(int idAddress){
        try {
            addressAccountReponsitory.deleteById(idAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
