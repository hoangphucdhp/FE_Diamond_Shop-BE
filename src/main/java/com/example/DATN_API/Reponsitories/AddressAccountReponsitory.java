package com.example.DATN_API.Reponsitories;

import com.example.DATN_API.Entity.AddressAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AddressAccountReponsitory extends JpaRepository<AddressAccount, Integer> {
    @Query("select ad from AddressAccount ad join ad.Addressaccount a where ad.status = true and a.id = ?1")
    AddressAccount getAddressDefault(int idAccount);
}
