package com.example.DATN_API.Controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.example.DATN_API.Entity.Account;
import com.example.DATN_API.Entity.Category;
import com.example.DATN_API.Entity.CategoryItem;
import com.example.DATN_API.Entity.ResponObject;
import com.example.DATN_API.Service.IStorageSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.DATN_API.Service.CategoryService;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/category")
@CrossOrigin()
public class CategoryController {
    @Autowired
    CategoryService CategoryService;
    @Autowired
    IStorageSerivce iStorageSerivce;

    @GetMapping()
    public ResponseEntity<List<Category>> getAll() {
        List<Category> categories = CategoryService.findAll();
        for (Category category : categories) {
            category.removeDuplicateCategoryItems();
        }
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Category> findById(@PathVariable Integer id) {
        Category categories = CategoryService.findByIdCategory(id);
        categories.removeDuplicateCategoryItems();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<ResponObject> create(@RequestParam("id_account") Integer idAccount, @RequestParam("image") MultipartFile image, @RequestParam("type_category") String type_category, @RequestParam("create_date") Date create_date) {
        String name = iStorageSerivce.storeFile(image);
        Account newAccount = CategoryService.findAccountById(idAccount);
        Category category = new Category();
        category.setImage(name);
        category.setAccountCreateCategory(newAccount);
        category.setStatus(true);
        category.setType_category(type_category);
        category.setCreate_date(create_date);
        Category newcate = CategoryService.createCategory(category);
        return new ResponseEntity<>(new ResponObject("success", "Thêm thành công.", newcate),
                HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponObject> update(@PathVariable("id") Integer id, @RequestParam("type_category") Optional<String> type_category, @RequestParam("image") Optional<MultipartFile> image) {
        MultipartFile imagesave = image.orElse(null);
        String type_categorysave = type_category.orElse("");
        Category categoryold = CategoryService.findByIdCategory(id);
        Category categorysave = new Category();
        categorysave.setId(categoryold.getId());
        categorysave.setType_category(categoryold.getType_category());
        categorysave.setAccountCreateCategory(categoryold.getAccountCreateCategory());
        categorysave.setStatus(categoryold.getStatus());
        categorysave.setCreate_date(categoryold.getCreate_date());
        if (imagesave == null && type_categorysave.equals("")) {
            categorysave.setImage(categoryold.getImage());
            categorysave.setType_category(categoryold.getType_category());
            CategoryService.updateCategory(categorysave);
        } else if (imagesave == null && !type_categorysave.equals("")) {
            categorysave.setImage(categoryold.getImage());
            categorysave.setType_category(type_categorysave);
            CategoryService.updateCategory(categorysave);
        } else if (imagesave != null && type_categorysave.equals("")) {
            String name = iStorageSerivce.storeFile(imagesave);
            categorysave.setImage(name);
            categorysave.setType_category(categoryold.getType_category());
            CategoryService.updateCategory(categorysave);
        } else {
            String name = iStorageSerivce.storeFile(imagesave);
            categorysave.setImage(name);
            categorysave.setType_category(type_categorysave);
            CategoryService.updateCategory(categorysave);
        }
        return new ResponseEntity<>(new ResponObject("success", "Cập nhật thành công.", categorysave), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ResponObject> delete(@PathVariable Integer id) {
        if (CategoryService.deleteCategory(id)) {
            return new ResponseEntity<>(new ResponObject("success", "Xóa thành công.", id), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponObject("error", "Có lỗi xảy ra.", id), HttpStatus.OK);
    }


    //CategoryItem
    @GetMapping("/categoryItem")
    public ResponseEntity<List<CategoryItem>> getAllCategoryItem() {
        return new ResponseEntity<>(CategoryService.findAllCategoryItem(), HttpStatus.OK);
    }

    @GetMapping("/categoryItem/{id}")
    public ResponseEntity<CategoryItem> findByIdCategoryItem(@PathVariable Integer id) {
        if (CategoryService.existsByIdCategoryItem(id)) {
            return new ResponseEntity<>(CategoryService.findByIdCategoryItem(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/categoryItem")
    public ResponseEntity<ResponObject> createCategoryItem(@RequestParam("type_categoryItem") String typeCategoryItem, @RequestParam("category") Integer idCategory, @RequestParam("create_date") Date create_date, @RequestParam("idAccount") Integer idAccount) {
        Category categorysave = CategoryService.findByIdCategory(idCategory);
        Account accountsave = CategoryService.findAccountById(idAccount);
        CategoryItem newcategoryItem = new CategoryItem();
        newcategoryItem.setType_category_item(typeCategoryItem);
        newcategoryItem.setCategory(categorysave);
        newcategoryItem.setAccount(accountsave);
        newcategoryItem.setCreate_date(create_date);
        newcategoryItem.setStatus(true);
        CategoryItem newItem = CategoryService.createCategoryItem(newcategoryItem);
        return new ResponseEntity<>(new ResponObject("success", "Thêm thành công.", newItem),
                HttpStatus.CREATED);
    }

    @PutMapping("/categoryItem/{id}")
    public ResponseEntity<ResponObject> updateCategoryItem(@PathVariable("id") Integer id, @RequestParam("type_categoryItem") Optional<String> typeCategoryItem, @RequestParam("category") Optional<Integer> idCategory, @RequestParam("idAccount") Integer idAccount) {
        String typeCategoryItemsave = typeCategoryItem.orElse("");
        int idCategorysave = idCategory.orElse(0);
        Category categorysave = CategoryService.findByIdCategory(idCategorysave);
        Account accountsave = CategoryService.findAccountById(idAccount);
        CategoryItem categoryItemold = CategoryService.findByIdCategoryItem(id);
        categoryItemold.setAccount(accountsave);
        if (typeCategoryItemsave.equals("") && idCategorysave != 0) {
            categoryItemold.setCategory(categorysave);
        } else if (!typeCategoryItemsave.equals("") && idCategorysave == 0) {
            categoryItemold.setType_category_item(typeCategoryItemsave);
        } else if (!typeCategoryItemsave.equals("") && idCategorysave != 0) {
            categoryItemold.setType_category_item(typeCategoryItemsave);
            categoryItemold.setCategory(categorysave);
        }
        CategoryItem newcategoryItem = CategoryService.updateCategoryItem(categoryItemold);
        return new ResponseEntity<>(new ResponObject("success", "Cập nhật thành công.", newcategoryItem),
                HttpStatus.OK);
    }

    @DeleteMapping("/categoryItem/{id}")
    public ResponseEntity<ResponObject> deleteCategoryItem(@PathVariable("id") Integer id) {
        if (CategoryService.deleteCategoryItem(id)) {
            return new ResponseEntity<>(new ResponObject("success", "Xóa thành công.", id), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponObject("error", "Có lỗi xảy ra.", id), HttpStatus.OK);

    }

}