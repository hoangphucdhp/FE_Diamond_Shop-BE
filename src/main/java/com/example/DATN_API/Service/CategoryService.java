package com.example.DATN_API.Service;

import java.util.List;
import java.util.Optional;

import com.example.DATN_API.Entity.Account;
import com.example.DATN_API.Entity.Category;
import com.example.DATN_API.Entity.CategoryItem;
import com.example.DATN_API.Reponsitories.AccountReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;

import com.example.DATN_API.Reponsitories.CategoryItemReponsitory;
import com.example.DATN_API.Reponsitories.CategoryReponsitory;

@Service
public class CategoryService {
    @Autowired
    CategoryReponsitory CategoryReponsitory;
    @Autowired
    CategoryItemReponsitory CategoryItemReponsitory;
    @Autowired
    AccountReponsitory accountReponsitory;


    public Page<Category> findAll(Optional<Integer> offset, Optional<Integer> sp, Optional<String> field, Optional<String> sortType, Optional<String> key, Optional<String> keyword) {
        String sortby = field.orElse("type_category");
        int itemStart = offset.orElse(0);
        int sizePage = sp.orElse(5);
        String keyfind = key.orElse("");
        String keywords = keyword.orElse("");

        Sort.Direction direction;
        // Sort
        String typeSort = sortType.orElse("asc");
        if (sortby == null || sortby.isEmpty()) {
            sortby = "type_category";
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
        if (keyfind.equals("id")) {
            return CategoryReponsitory.getAllById(PageRequest.of(itemStart, sizePage, sort), keywords);
        } else if (keyfind.equals("type_category")) {
            return CategoryReponsitory.getAllByType_category(PageRequest.of(itemStart, sizePage, sort), keywords);
        } else if (keyfind.equals("") && !keywords.equals("")) {
            return CategoryReponsitory.getAllById(PageRequest.of(itemStart, sizePage, sort), keywords);
        } else {
            return CategoryReponsitory.getAll(PageRequest.of(itemStart, sizePage, sort));
        }

    }

    public Category findByIdCategory(int id) {
        Optional<Category> category = CategoryReponsitory.findById(id);
        return category.get();
    }


    public Category findByTypeCategory(String id) {
        return CategoryReponsitory.findByType_category(id) != null ? CategoryReponsitory.findByType_category(id) : null;
    }

    public Category createCategory(Category Category) {
        return CategoryReponsitory.save(Category);
    }

    public Category updateCategory(Category Category) {
        return CategoryReponsitory.save(Category);
    }

    public Boolean deleteCategory(int id) {

        try {
            Category category = findByIdCategory(id);
            if (category.getListCategory().size() < 1) {

                CategoryReponsitory.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            LogError.saveToLog(e);
        }
        return false;

    }

    public Boolean existsByIdCategory(Integer id) {

        return CategoryReponsitory.existsById(id) ? true : false;
    }

    // CategoryItem
    public List<CategoryItem> findAllCategoryItem() {
        return CategoryItemReponsitory.findAll();
    }

    public Optional<CategoryItem> findByIdCategoryItem(int id) {
        Optional<CategoryItem> Category = CategoryItemReponsitory.findById(id);
        return Category;
    }

    public CategoryItem createCategoryItem(CategoryItem Category) {
        try {
            return CategoryItemReponsitory.save(Category);
        } catch (Exception e) {

            LogError.saveToLog(e);
        }
        return null;
    }

    public CategoryItem updateCategoryItem(CategoryItem CategoryItem) {
        try {
            return CategoryItemReponsitory.save(CategoryItem);
        } catch (Exception e) {

            LogError.saveToLog(e);
        }
        return null;
    }

    public Boolean deleteCategoryItem(int id) {
        try {
            CategoryItem categoryItem = findByIdCategoryItem(id).get();
            if (categoryItem.getProducts().size() < 1) {

                CategoryItemReponsitory.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {

            LogError.saveToLog(e);
        }
        return false;
    }

    public Boolean existsByIdCategoryItem(Integer id) {
        return CategoryItemReponsitory.existsById(id) ? true : false;
    }

    public Account findAccountById(int id) {
        Optional<Account> newaccount = accountReponsitory.findById(id);
        return newaccount.get();
    }

    public CategoryItem findByTypeCategoryItem(String id,int cate) {
        return CategoryItemReponsitory.findByType_categoryItem(id,cate) != null ? CategoryItemReponsitory.findByType_categoryItem(id,cate) : null;
    }
}


