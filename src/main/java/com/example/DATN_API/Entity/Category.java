package com.example.DATN_API.Entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String type_category;
    private Date create_date;
    private String image;
    private Boolean status;

    @OneToMany(mappedBy = "category")
    private List<CategoryItem> listCategory;


    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "id_account")
    private Account accountCreateCategory;

    public void removeDuplicateCategoryItems() {
        if (listCategory != null) {
            Set<Integer> seenIds = new HashSet<>();
            List<CategoryItem> uniqueCategoryItems = new ArrayList<>();

            for (CategoryItem categoryItem : listCategory) {
                if (seenIds.add(categoryItem.getId())) {
                    uniqueCategoryItems.add(categoryItem);
                }
            }
            listCategory.clear();
            listCategory.addAll(uniqueCategoryItems);
        }
    }
}

