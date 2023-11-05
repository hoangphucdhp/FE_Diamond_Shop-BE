package com.example.DATN_API.Entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category_item")

public class CategoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_category")
    private Category categoryNew;

    private String type_category_item;

    private Date create_date;

    private Boolean status;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_account")
    private Account account;

    @OneToMany(mappedBy = "categoryItem_product")
    @JsonIgnore
    List<Product> products;
}
