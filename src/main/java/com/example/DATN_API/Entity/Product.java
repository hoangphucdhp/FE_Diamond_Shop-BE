package com.example.DATN_API.Entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_shop")
    @JsonIgnore
    private Shop shop;

    private String product_name;

    @OneToMany(mappedBy = "product_image")
    private List<ImageProduct> image_product;

    private double price;

    private double promotion_price;

    private Date create_date;
    private Date start_promotion;
    private Date end_promotion;
    private String description;

    private int status;


    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_category_item")
    private CategoryItem categoryItem_product;

    @OneToOne(mappedBy = "product_like")
    private LikeProduct likeProduct;

    @OneToOne(mappedBy = "id_Product")
    @JsonIgnore
    private Storage storage;

    @JsonIgnore
    @OneToMany(mappedBy = "productOrder")
    private List<OrderDetail> orderDetails;

}
