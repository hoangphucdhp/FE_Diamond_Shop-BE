package com.example.DATN_API.Service;



import java.util.List;
import java.util.Optional;

import com.example.DATN_API.Entity.AddressShop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DATN_API.Reponsitories.AddressShopReponsitory;

@Service
public class AddressShopService {
	@Autowired
	AddressShopReponsitory AddressShopReponsitory;

	public List<AddressShop> findAll() {
		return AddressShopReponsitory.findAll();
	}

	public AddressShop findById(int id) {
		Optional<AddressShop> AddressShop = AddressShopReponsitory.findById(id);
		return AddressShop.get();
	}
	
	public void createAddressShop(AddressShop AddressShop) {
		AddressShopReponsitory.save(AddressShop);
	}
	
	public void updateAddressShop(int id,AddressShop AddressShop) {
		AddressShop.setId(id);
		AddressShopReponsitory.save(AddressShop);
	}
	
	public void deleteAddressShop(int id) {
		AddressShopReponsitory.deleteById(id);
	}

	public Boolean existsById(Integer id) {
		return AddressShopReponsitory.existsById(id) ? true : false;
	}
}
