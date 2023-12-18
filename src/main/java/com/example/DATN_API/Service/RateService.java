package com.example.DATN_API.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DATN_API.Entity.Account;
import com.example.DATN_API.Entity.Product;
import com.example.DATN_API.Entity.Rate;
import com.example.DATN_API.Reponsitories.RateRepository;

@Service
public class RateService {
    @Autowired
    RateRepository rateRepository;

    public Rate save(Rate rate) {
        return rateRepository.save(rate);
    }

    public List<Rate> findByProduct_rate(Product product) {
        return rateRepository.findByProduct_rate(product);
    }

    public Double findAverageStarByProduct(Product product) {
        return rateRepository.findAverageStarByProduct(product);
    }

    public boolean existsByAccount_rateAndProduct_rate(Account account, Product product) {
        return rateRepository.existsByAccount_rateAndProduct_rate(account, product);
    }

    public Rate findById(int ratingId) {
        return rateRepository.findById(ratingId).orElse(null);
    }

    public List<Rate> findByStar(int star,int product) {
        return rateRepository.findByStar(star,product);
    }

    public long getTotalBuy(int idproduct){
        return rateRepository.countOrderDetailsByStatusAndProductId(idproduct);
    }
}
