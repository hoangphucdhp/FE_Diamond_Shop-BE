package com.example.DATN_API.Service;
import java.util.List;
import java.util.Optional;

import com.example.DATN_API.Entity.LikeProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DATN_API.Reponsitories.LikeProductReponsitory;

@Service
public class LikeProductService {
	@Autowired
	LikeProductReponsitory LikeProductReponsitory;


	public LikeProduct save(LikeProduct likeProduct) {
		return LikeProductReponsitory.save(likeProduct);
	}

	public void delete(LikeProduct likeProduct) {
		LikeProductReponsitory.delete(likeProduct);
	}


	public List<LikeProduct> findAll() {
		return LikeProductReponsitory.findAll();
	}

	public LikeProduct findById(int id) {
		Optional<LikeProduct> LikeProduct = LikeProductReponsitory.findById(id);
		return LikeProduct.get();
	}


	public void createLikeProduct(LikeProduct LikeProduct) {
		LikeProductReponsitory.save(LikeProduct);
	}

	public void updateLikeProduct(int id, LikeProduct LikeProduct) {
		LikeProduct.setId(id);
		LikeProductReponsitory.save(LikeProduct);
	}
	public void deleteLikeProduct(int id) {
		LikeProductReponsitory.deleteById(id);
	}

	public Boolean existsById(Integer id) {
		return LikeProductReponsitory.existsById(id) ? true : false;
	}
	public LikeProduct findByProductLikeIdAndAccountLikeId(int idProduct, int idAccount) {
		return LikeProductReponsitory.findByProductLikeIdAndAccountLikeId(idProduct, idAccount);
	}

	public String findProductDetailsByAccountId(int idAccount){
		return LikeProductReponsitory.findProductDetailsByAccountId(idAccount);
	}

}
