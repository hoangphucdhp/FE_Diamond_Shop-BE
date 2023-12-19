package com.example.DATN_API.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.DATN_API.Entity.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.DATN_API.Reponsitories.ProductRepository;


@Service
public class ProductService {
    @Autowired

    ProductRepository productRepository;
    @Autowired
    ImageProductService imageProductService;
    @Autowired
    StorageService storageService;
    @Autowired
    ShopService shopService;
    @Autowired
    CategoryService categoryService;

    public Page<Product> findAllbyShop(Optional<Integer> offset, Optional<Integer> sp,
                                       Optional<String> field, Optional<String> key, Optional<String> keyword, Optional<Integer> idshop) {
        String sort = field.orElse("create_date");
        int itemStart = offset.orElse(0);
        int sizePage = sp.orElse(20);
        int ishop = idshop.orElse(0);
        String keysearch = key.orElse("");
        String keywords = keyword.orElse("");

        if (ishop != 0) {
            Shop shop = shopService.findById(ishop);
            if (keysearch.equals("id")) {
                return productRepository.getAllbyIdBussiness(PageRequest.of(itemStart, sizePage, Sort.Direction.DESC, sort), keywords, shop);
            } else if (keysearch.equals("product_name")) {
                return productRepository.getAllbyNameBussiness(PageRequest.of(itemStart, sizePage, Sort.Direction.DESC, sort), keywords, shop);
            } else if (keysearch.isEmpty() && !keywords.isEmpty()) {
                return productRepository.getAllbyIdBussiness(PageRequest.of(itemStart, sizePage, Sort.Direction.DESC, sort), keywords, shop);
            } else {
                return productRepository.findAllByShop(PageRequest.of(itemStart, sizePage, Sort.Direction.DESC, sort), shop);
            }
        }
        return null;

    }

    public List<Product> findAll() {
        return productRepository.findAll();

    }

    public Page<Product> findProductbyStatus(Optional<Integer> offset, Optional<Integer> sp,
                                             Optional<String> field, int status, Shop shop) {
        String sort = field.orElse("create_date");
        int itemStart = offset.orElse(0);
        int sizePage = sp.orElse(20);
        return productRepository.getProductbyStatus(PageRequest.of(itemStart, sizePage, Sort.Direction.DESC, sort), status, shop);
    }

    public Page<Product> getPageProduct(Optional<Integer> stt, Optional<Integer> offset, Optional<Integer> sp,
                                        Optional<String> field) {

        String sort = field.orElse("create_date");
        int itemStart = offset.orElse(0);

        int sizePage = sp.orElse(20);
        int status = sp.orElse(1);


        return productRepository.getPageProduct(status,
                PageRequest.of(itemStart, sizePage, Sort.Direction.DESC, sort));
    }

    public Product findById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sản phẩm không được tìm thấy"));

    }

    public Product createProduct(Product product) {
        try {

            Product productsave = productRepository.save(product);

            return productsave;
        } catch (Exception e) {
            e.printStackTrace();
            LogError.saveToLog(e);
        }
        return null;
    }

    public Product updateProduct(int id, Product product) {
        Product productold = findById(id);
        product.setId(id);
        product.setShop(productold.getShop());
        product.setCreate_date(productold.getCreate_date());
        try {
            Product productsave = productRepository.save(product);
            return productsave;
        } catch (Exception e) {
            e.printStackTrace();
            LogError.saveToLog(e);
        }
        return null;
    }


    public boolean deleteProduct(int id) {
        try {
            Product product = findById(id);
            if (product.getStatus() == 0) {
                boolean allDeleted = true;

                // Xóa tất cả các imageProduct
                for (ImageProduct imageProduct : product.getImage_product()) {
                    boolean imageProductDeleted = imageProductService.deleteImageProduct(imageProduct.getId());
                    if (!imageProductDeleted) {
                        allDeleted = false;
                        break;
                    }
                }

                // Xóa tất cả các Storage
                if (allDeleted) {
                    for (Storage storage : product.getListStorage()) {
                        boolean storageDeleted = storageService.deleteStorage(storage.getId());
                        if (!storageDeleted) {
                            allDeleted = false;
                            break;
                        }
                    }
                }

                // Nếu tất cả imageProduct và Storage đã xóa thành công, xóa product
                if (allDeleted) {
                    productRepository.deleteById(id);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            LogError.saveToLog(e);
            return false;
        }
    }

    public Boolean existsById(Integer id) {
        return productRepository.existsById(id) ? true : false;
    }

    public List<Object[]> getTop10Products() {
        return productRepository.getTop10Products();
    }

    public List<Product> findSimilarProducts(int id) {
        return productRepository.findSimilarProducts(id);
    }

    //Admin
    public Page<Product> findAll(Optional<Integer> offset, Optional<Integer> sp, Optional<String> field, Optional<String> sortType, Optional<String> key, Optional<String> keyword, Optional<String> stt) {
        String sortby = field.orElse("product_name");
        int itemStart = offset.orElse(0);
        int sizePage = sp.orElse(10);
        String keyfind = key.orElse("");
        String keywords = keyword.orElse("");
        String status = stt.orElse("");
        Sort.Direction direction;

        // Sort
        String typeSort = sortType.orElse("asc");

        if (sortby == null || sortby.isEmpty()) {
            sortby = "product_name";
        }

        if (typeSort == null || typeSort.isEmpty()) {
            typeSort = "asc";
        }

        if (typeSort.equals("asc")) {
            direction = Sort.Direction.ASC;
        } else {
            direction = Sort.Direction.DESC;
        }

        Sort sort = Sort.by(direction, sortby);

        // Kiểm tra nếu tất cả các tham số đều rỗng thì trả về tất cả dữ liệu
        if (keyfind.isEmpty() && keywords.isEmpty() && status.isEmpty()) {
            return productRepository.getAll(PageRequest.of(itemStart, sizePage, sort), status);
        }

        // Xử lý logic để lấy dữ liệu dựa trên các tham số được chỉ định
        if (keyfind.equals("name")) {
            return productRepository.getAllbyName(PageRequest.of(itemStart, sizePage, sort), keywords, status);
        } else if (keyfind.equals("shop")) {
            return productRepository.getAllbyShop(PageRequest.of(itemStart, sizePage, sort), keywords, status);
        } else if (keyfind.equals("id")) {
            return productRepository.getAllbyId(PageRequest.of(itemStart, sizePage, sort), keywords, status);
        } else if (keyfind.isEmpty() && !keywords.isEmpty()) {
            return productRepository.getAllbyId(PageRequest.of(itemStart, sizePage, sort), keywords, status);
        } else {
            return productRepository.getAll(PageRequest.of(itemStart, sizePage, sort), status);
        }
    }


    public Product adminUpdateStatus(int id, int status) {
        Product product = findById(id);
        if (status == 0) {
            product.setStatus(1);
        } else if (status == 1) {
            product.setStatus(3);
        } else if (status == 3) {
            product.setStatus(0);
        }
        return productRepository.save(product);
    }

    public Page<Product> searchBusiness(
            Optional<Integer> offset,
            Optional<Integer> sp,
            Optional<String> field,
            Optional<String> sortType,
            Optional<String> key,
            Optional<String> valueKeyword,
            Optional<Integer> cate,
            Optional<Integer> ishop,
            Optional<String> stt,
            Optional<String> isActive
    ) {
        String sortby = field.orElse("product_name");
        int itemStart = offset.orElse(0);
        int sizePage = sp.orElse(10);
        String keyfind = key.orElse("");
        String keywords = valueKeyword.orElse("");
        int idCategoryItem = cate.orElse(0);
        int idShop = ishop.orElse(0);
        Shop shop = shopService.findById(idShop);
        Sort.Direction direction;
        String status = stt.orElse("");
        // Sort
        String typeSort = sortType.orElse("asc");
        if (sortby == null || sortby.isEmpty()) {
            sortby = "product_name";
        }
        if (typeSort == null || typeSort.isEmpty()) {
            typeSort = "asc";
        }
        if (typeSort.equals("asc")) {
            direction = Sort.Direction.ASC;
        } else {
            direction = Sort.Direction.DESC;
        }
        Sort sort = Sort.by(direction, sortby);
        try {
            if (isActive.isPresent() && isActive.get().equals("unactive")) {
                if (keyfind.equals("id")) {
                    return productRepository.searchProductByIdAndCategoryZeroQuantity(PageRequest.of(itemStart, sizePage, sort), keywords, categoryService.findByIdCategoryItem(idCategoryItem).orElse(null), shop, status);
                } else {
                    return productRepository.searchProductByNameAndCategoryZeroQuantity(keywords, categoryService.findByIdCategoryItem(idCategoryItem).orElse(null), shop, status, PageRequest.of(itemStart, sizePage, sort));
                }
            } else {
                if (keyfind.equals("id")) {
                    return productRepository.searchProductByIdAndCategory(PageRequest.of(itemStart, sizePage, sort), keywords, categoryService.findByIdCategoryItem(idCategoryItem).orElse(null), shop, status);
                } else {
                    return productRepository.searchProductByNameAndCategory(keywords, categoryService.findByIdCategoryItem(idCategoryItem).orElse(null), shop, status, PageRequest.of(itemStart, sizePage, sort));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void BanProduct(int id, int status) {
        productRepository.BanProduct(id, status);
    }

    public SearchResult search(Optional<String> keyword) {
        if (keyword.isPresent()) {
            Page<Product> listProduct = productRepository.searchBarProduct(keyword.get(), PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "product_name")));
            Page<Shop> listShop = productRepository.searchBarShop(keyword.get(), PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "shop_name")));
            SearchResult result = new SearchResult();
            if (listShop != null) {
                result.setShopList(listShop);
            }
            if (listProduct != null) {
                result.setProductList(listProduct);
            }
            return result;
        }
        return null;
    }

    public Page<Product> searchUser(
            Optional<Integer> offset,
            Optional<Integer> sp,
            Optional<String> field,
            Optional<ArrayList<Double>> price,
            Optional<Integer> idcate,
            Optional<Integer> rate,
            Optional<Integer> cate
    ) {
        String sortby = "product_name";
        int itemStart = offset.orElse(0);
        int sizePage = sp.orElse(10);

        int idCategoryItem = idcate.orElse(0);

        Sort.Direction direction;

        // Sort
        String typeSort = field.orElse("asc");
        if (sortby == null || sortby.isEmpty()) {
            sortby = "product_name";
        }
        if (typeSort == null || typeSort.isEmpty()) {
            typeSort = "asc";
        }
        if (typeSort.equals("asc")) {
            direction = Sort.Direction.ASC;
        } else {
            direction = Sort.Direction.DESC;
        }
        Sort sort = Sort.by(direction, sortby);
        try {
                Double minPrice = price.get().get(0);
                Double maxPrice = price.get().get(1);
                return productRepository.searchProductUser(PageRequest.of(itemStart, sizePage, sort),cate.orElse(0), categoryService.findByIdCategoryItem(idCategoryItem).orElse(null), minPrice, maxPrice,rate.orElse(0));
            } catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
    public int getAmountProduct(int status){
        return productRepository.getAmountProduct(status);
    }
    public int getQuantityProduct(int status){
        return productRepository.getTotalQuantityDifference(status);
    }
    }
