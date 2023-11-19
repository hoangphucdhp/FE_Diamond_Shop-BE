package com.example.DATN_API.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private Date create_date;
    private boolean status;


    @OneToMany(mappedBy = "accountCreateCategory")
    public List<Category> listCategory;

    @OneToMany(mappedBy = "account")
    public List<CategoryItem> listCategoryItem;

    @OneToOne(mappedBy = "accountShop")
    private Shop shop;

    @OneToOne(mappedBy = "Infaccount")
    private InfoAccount infoAccount;


    @OneToMany(mappedBy = "Addressaccount")
    private List<AddressAccount> address_account;


    @OneToMany(mappedBy = "account_like")
    private List<LikeProduct> likeProductes;

    @OneToMany(mappedBy = "accountOrder")
    private List<Order> orders;

    @OneToMany(mappedBy = "account_check")
    List<StatusOrder> statusOrders;
}
