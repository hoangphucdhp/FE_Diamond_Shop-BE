package com.example.DATN_API.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.DATN_API.Entity.Account;
import com.example.DATN_API.Entity.AddressShop;
import com.example.DATN_API.Entity.Product;
import com.example.DATN_API.Entity.Shop;
import com.example.DATN_API.Reponsitories.AddressShopReponsitory;
import com.example.DATN_API.Reponsitories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    ProductRepository productRepository;


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


    public Shop findByShopName(String id) {
        return ShopReponsitory.findByShopName(id);
    }

    public Shop findShopByProduct(int idProduct) {
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

    public Shop bussinessUpdateInf(int id, String shopName, String city, String district, String ward, String address, String image) {
        Shop shop = findById(id);
        AddressShop addressShop = addressShopReponsitory.findByShop(shop);
        //Set shop
        if (!shop.getShop_name().equals(shopName)) {
            Shop shopcheck = findByShopName(shopName);
            if (shopcheck == null) {
                shop.setShop_name(shopName);
            } else {
                String errorMessage = "Tên cửa hàng đã tồn tại.";
                throw new IllegalArgumentException(errorMessage);
            }
        }
        shop.setImage(image);
        addressShop.setCity(city);
        addressShop.setDistrict(district);
        addressShop.setWard(ward);
        addressShop.setAddress(address);
        addressShopReponsitory.save(addressShop);
        return ShopReponsitory.save(shop);
    }

    public Page<Shop> findShopByStatusProduct(Optional<Integer> status) {
        int stt = status.orElse(1);
        Pageable pageable = PageRequest.of(0, 20, Sort.by("create_date"));
        return ShopReponsitory.findShopByStatusProduct(stt, pageable);
    }

    public List<Shop> findShopByStatusProductSearch(Optional<Integer> status, Optional<Integer> typeSearch, Optional<String> search) {
        int stt = status.orElse(1);
        int type = typeSearch.orElse(1);
        String keyword = search.orElse("");
        System.out.println("id " + keyword);
        System.out.println("type " + type);
        System.out.println("stt " + stt);

        Pageable pageable = PageRequest.of(0, 20, Sort.by("create_date"));
        if (type == 1) {
            return ShopReponsitory.findShopSearchByIdProduct(stt, keyword);
        } else if (type == 2) {
            return findShopByNameProduct(search, status);
        } else {
            return ShopReponsitory.findByName(stt, "%" + keyword + "%");
        }
    }

    public List<Shop> findShopByName(Optional<String> search, Optional<Integer> stt) {
        Page<Shop> shopPage = null;
        String keyword = search.orElse("");
        int status = stt.orElse(1);
        List<Shop> shops = new ArrayList<>();

        List<Product> products = productRepository.findByName(keyword, status);
        List<Integer> listIdShop = new ArrayList<>();
        products.stream().forEach(item -> {
            if (!listIdShop.contains(item.getShop().getId())) {
                listIdShop.add(item.getShop().getId());
            }
        });
        if (listIdShop.size() == 0) {
            return null;
        }
        for (int i = 0; i < listIdShop.size(); i++) {
            List<Product> productList = findIdShop(products, listIdShop.get(i));
            Shop shop = new Shop();
            shop.setProducts(productList);
            shop.setShop_name(productList.get(0).getShop().getShop_name());
            shops.add(shop);
        }
        return shops;
    }

    public List<Shop> findShopByNameProduct(Optional<String> search, Optional<Integer> stt) {
        Page<Shop> shopPage = null;
        String keyword = search.orElse("");
        int status = stt.orElse(1);
        List<Shop> shops = new ArrayList<>();

        List<Product> products = productRepository.findByName(keyword, status);
        List<Integer> listIdShop = new ArrayList<>();
        products.stream().forEach(item -> {
            if (!listIdShop.contains(item.getShop().getId())) {
                listIdShop.add(item.getShop().getId());
            }
        });
        if (listIdShop.size() == 0) {
            return null;
        }
        for (int i = 0; i < listIdShop.size(); i++) {
            List<Product> productList = findIdShop(products, listIdShop.get(i));
            Shop shop = new Shop();
            shop.setProducts(productList);
            shop.setShop_name(productList.get(0).getShop().getShop_name());
            shops.add(shop);
        }
        return shops;
    }

    public static List<Product> findIdShop(List<Product> products, int shopId) {
        return products.stream()
                .filter(product -> product.getShop().getId() == shopId)
                .collect(Collectors.toList());
    }

    public void BanShop(int id,int status) {
        ShopReponsitory.BanShop(id,status);
    }

    public int getAmountShop(int status){
        return ShopReponsitory.getAmountShop(status);
    }
}
