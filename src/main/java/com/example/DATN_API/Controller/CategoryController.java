package com.example.DATN_API.Controller;


import java.util.Date;
import java.util.List;
import java.util.Optional;


import com.example.DATN_API.Entity.Account;
import com.example.DATN_API.Entity.Category;
import com.example.DATN_API.Entity.CategoryItem;
import com.example.DATN_API.Entity.ResponObject;
import com.example.DATN_API.Service.CategoryService;
import com.example.DATN_API.Service.IStorageSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping("/api/")
@CrossOrigin()
public class CategoryController {
    @Autowired
    CategoryService CategoryService;
    @Autowired
    IStorageSerivce iStorageSerivce;

    @GetMapping("category")
    public ResponseEntity<Page<Category>> getAll(@RequestParam("offset") Optional<Integer> offSet,
                                                 @RequestParam("sizePage") Optional<Integer> sizePage,
                                                 @RequestParam("sort") Optional<String> sort,
                                                 @RequestParam("sortType") Optional<String> sortType,
                                                 @RequestParam("key") Optional<String> keyfind,
                                                 @RequestParam("keyword") Optional<String> keyword) {
        Page<Category> categories = CategoryService.findAll(offSet, sizePage, sort, sortType, keyfind, keyword);
        for (Category category : categories) {
            category.removeDuplicateCategoryItems();
        }
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("category/{id}")
    public ResponseEntity<Category> findById(@PathVariable Integer id) {
        Category categories = CategoryService.findByIdCategory(id);
        categories.removeDuplicateCategoryItems();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping("auth/category")
    @PreAuthorize("hasRole('ROLE_Admin')")
    public ResponseEntity<ResponObject> create(@RequestParam("id_account") Integer idAccount, @RequestParam("image") String image, @RequestParam("type_category") String type_category, @RequestParam("create_date") Date create_date) {
        Account newAccount = CategoryService.findAccountById(idAccount);
        Category category = new Category();
        category.setImage(image);
        category.setAccountCreateCategory(newAccount);
        category.setStatus(true);
        category.setType_category(type_category);
        category.setCreate_date(create_date);

        if (CategoryService.findByTypeCategory(type_category) == null) {
            Category newcate = CategoryService.createCategory(category);
            return new ResponseEntity<>(new ResponObject("success", "Thêm thành công!", newcate),
                    HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ResponObject("error", "Tên loại sản phẩm đã tồn tại!", null),
                    HttpStatus.CREATED);
        }
    }

    @PutMapping("auth/category/{id}")
    @PreAuthorize("hasRole('ROLE_Admin')")
    public ResponseEntity<ResponObject> update(@PathVariable("id") Integer id, @RequestParam("type_category") Optional<String> type_category, @RequestParam("image") Optional<String> image) {
        String imagesave = image.orElse("");
        String type_categorysave = type_category.orElse("");
        Category categoryold = CategoryService.findByIdCategory(id);

            if (CategoryService.findByTypeCategory(type_categorysave) != null&& !type_categorysave.equals(categoryold.getType_category())) {
                return new ResponseEntity<>(new ResponObject("error", "Tên loại sản phẩm đã tồn tại!", null),
                        HttpStatus.OK);
            } else {
                categoryold.setType_category(type_categorysave);
                categoryold.setImage(imagesave);
                CategoryService.updateCategory(categoryold);
        }
        return new ResponseEntity<>(new ResponObject("success", "Cập nhật thành công.", categoryold), HttpStatus.OK);

    }

    @DeleteMapping("auth/category/{id}")
    @PreAuthorize("hasRole('ROLE_Admin')")
    public ResponseEntity<ResponObject> delete(@PathVariable Integer id) {
        if (CategoryService.deleteCategory(id)) {
            return new ResponseEntity<>(new ResponObject("success", "Xóa thành công.", id), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponObject("error", "Có lỗi xảy ra.", id), HttpStatus.OK);
    }


    //CategoryItem
    @GetMapping("category/categoryItem")
    public ResponseEntity<List<CategoryItem>> getAllCategoryItem() {
        return new ResponseEntity<>(CategoryService.findAllCategoryItem(), HttpStatus.OK);
    }

    @GetMapping("category/categoryItem/{id}")
    public ResponseEntity<CategoryItem> findByIdCategoryItem(@PathVariable Integer id) {
        if (CategoryService.existsByIdCategoryItem(id)) {
            return new ResponseEntity<>(CategoryService.findByIdCategoryItem(id).get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("auth/categoryItem")
    @PreAuthorize("hasRole('ROLE_Admin')")
    public ResponseEntity<ResponObject> createCategoryItem(@RequestParam("type_categoryItem") String typeCategoryItem, @RequestParam("category") Integer idCategory, @RequestParam("create_date") Date create_date, @RequestParam("idAccount") Integer idAccount) {
        Category categorysave = CategoryService.findByIdCategory(idCategory);
        Account accountsave = CategoryService.findAccountById(idAccount);
        CategoryItem newcategoryItem = new CategoryItem();
        newcategoryItem.setType_category_item(typeCategoryItem);
        newcategoryItem.setCategory(categorysave);
        newcategoryItem.setAccount(accountsave);
        newcategoryItem.setCreate_date(create_date);
        newcategoryItem.setStatus(true);

        if (CategoryService.findByTypeCategoryItem(typeCategoryItem,categorysave.getId()) == null) {
            CategoryItem newItem = CategoryService.createCategoryItem(newcategoryItem);
            return new ResponseEntity<>(new ResponObject("success", "Thêm thành công.", newItem),
                    HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ResponObject("error", "Tên phân loại sản phẩm đã tồn tại!", null),
                    HttpStatus.CREATED);
        }

    }

    @PutMapping("auth/categoryItem/{id}")
    @PreAuthorize("hasRole('ROLE_Admin')")
    public ResponseEntity<ResponObject> updateCategoryItem(@PathVariable("id") Integer id, @RequestParam("type_categoryItem") Optional<String> typeCategoryItem, @RequestParam("category") Optional<Integer> idCategory, @RequestParam("idAccount") Integer idAccount) {
        String typeCategoryItemsave = typeCategoryItem.orElse("");
        int idCategorysave = idCategory.orElse(0);
        Category categorysave = CategoryService.findByIdCategory(idCategorysave);
        Account accountsave = CategoryService.findAccountById(idAccount);
        CategoryItem categoryItemold = CategoryService.findByIdCategoryItem(id).get();
        categoryItemold.setAccount(accountsave);
        if (typeCategoryItemsave.equals("") && idCategorysave != 0) {
            categoryItemold.setCategory(categorysave);
        } else if (!typeCategoryItemsave.equals("") && idCategorysave == 0) {

            if (CategoryService.findByTypeCategoryItem(typeCategoryItemsave,categorysave.getId()) != null&& !typeCategoryItemsave.equals(categoryItemold.getType_category_item())) {
                return new ResponseEntity<>(new ResponObject("error", "Tên phân loại sản phẩm đã tồn tại!", null),
                        HttpStatus.OK);
            } else {
                categoryItemold.setType_category_item(typeCategoryItemsave);
            }
        } else if (!typeCategoryItemsave.equals("") && idCategorysave != 0) {
            if (CategoryService.findByTypeCategoryItem(typeCategoryItemsave,categorysave.getId()) != null&& !typeCategoryItemsave.equals(categoryItemold.getType_category_item())) {
                return new ResponseEntity<>(new ResponObject("error", "Tên phân loại sản phẩm đã tồn tại!", null),
                        HttpStatus.OK);
            } else {
                categoryItemold.setType_category_item(typeCategoryItemsave);
                categoryItemold.setCategory(categorysave);
            }

        }
        CategoryItem newcategoryItem = CategoryService.updateCategoryItem(categoryItemold);
        return new ResponseEntity<>(new ResponObject("success", "Cập nhật thành công.", newcategoryItem),
                HttpStatus.OK);
    }

    @DeleteMapping("auth/categoryItem/{id}")
    @PreAuthorize("hasRole('ROLE_Admin')")
    public ResponseEntity<ResponObject> deleteCategoryItem(@PathVariable("id") Integer id) {
        if (CategoryService.deleteCategoryItem(id)) {
            return new ResponseEntity<>(new ResponObject("success", "Xóa thành công.", id), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponObject("error", "Có lỗi xảy ra.", id), HttpStatus.OK);
    }

}