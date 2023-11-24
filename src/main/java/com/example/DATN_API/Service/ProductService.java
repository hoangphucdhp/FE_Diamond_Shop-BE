package com.example.DATN_API.Service;

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

    public Page<Product> findAll(Optional<Integer> offset, Optional<Integer> sp,
                                 Optional<String> field, Optional<Integer> idshop) {
        String sort = field.orElse("create_date");
        int itemStart = offset.orElse(0);
        int sizePage = sp.orElse(20);
        int ishop = idshop.orElse(0);
        if (ishop != 0) {
            Shop shop = shopService.findById(ishop);
            return productRepository.findAllByShop(PageRequest.of(itemStart, sizePage, Sort.Direction.DESC, sort), shop);
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
        ;
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
        product.setStart_promotion(productold.getStart_promotion());
        product.setEnd_promotion(productold.getEnd_promotion());
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
    public Page<Product> findAll(Optional<Integer> offset, Optional<Integer> sp, Optional<String> field, Optional<String> sortType, Optional<String> key, Optional<String> keyword) {
        String sortby = field.orElse("product_name");
        int itemStart = offset.orElse(0);
        int sizePage = sp.orElse(10);
        String keyfind = key.orElse("");
        String keywords = keyword.orElse("");

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

        if (keyfind.equals("name")) {
            return productRepository.getAllbyName(PageRequest.of(itemStart, sizePage, sort), keywords);
        } else if (keyfind.equals("shop")) {
            return productRepository.getAllbyShop(PageRequest.of(itemStart, sizePage, sort), keywords);
        } else if (keyfind.equals("id")) {
            return productRepository.getAllbyId(PageRequest.of(itemStart, sizePage, sort), keywords);
        } else if (keyfind.equals("") && !keywords.equals("")) {
            return productRepository.getAllbyId(PageRequest.of(itemStart, sizePage, sort), keywords);
        } else {
            return productRepository.getAll(PageRequest.of(itemStart, sizePage, sort));
        }
    }

    public Product adminUpdateStatus(int id, int status) {
        Product product = findById(id);
        if (status == 0) {
            product.setStatus(1);
        } else if (status == 1) {
            product.setStatus(2);
        } else if (status == 2) {
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
            Optional<Integer> ishop
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
            if (keyfind.equals("id") && idCategoryItem == 0) {
                return productRepository.getAllbyIdBussiness(PageRequest.of(itemStart, sizePage, sort), keywords, shop);
            } else if (keyfind.equals("product_name") && idCategoryItem == 0) {
                return productRepository.getAllbyNameBussiness(PageRequest.of(itemStart, sizePage, sort), keywords, shop);
            } else if (keyfind.equals("") && idCategoryItem != 0) {
                return productRepository.searchProductByCategory(PageRequest.of(itemStart, sizePage, sort), idCategoryItem, shop);
            } else if (keyfind.equals("id") && idCategoryItem != 0) {
                CategoryItem categoryItem = categoryService.findByIdCategoryItem(idCategoryItem);
                return productRepository.searchProductByIdAndCategory(PageRequest.of(itemStart, sizePage, sort), keywords, categoryItem, shop);
            } else if (keyfind.equals("product_name") && idCategoryItem != 0) {
                CategoryItem categoryItem = categoryService.findByIdCategoryItem(idCategoryItem);
                return productRepository.searchProductByNameAndCategory(keywords, categoryItem, shop, PageRequest.of(itemStart, sizePage, sort));
            } else if (keyfind.equals("") && idCategoryItem == 0 && !keywords.isEmpty()) {
                return productRepository.getAllbyIdBussiness(PageRequest.of(itemStart, sizePage, sort), keywords, shop);
            } else {
                return productRepository.findAllByShop(PageRequest.of(itemStart, sizePage, sort), shop);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}