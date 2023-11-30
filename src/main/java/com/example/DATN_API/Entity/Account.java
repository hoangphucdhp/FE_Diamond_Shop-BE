package com.example.DATN_API.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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


    @OneToMany(mappedBy = "accountCreateCategory",fetch = FetchType.LAZY)
    public List<Category> listCategory;

    @OneToMany(mappedBy = "account",fetch = FetchType.LAZY)
    public List<CategoryItem> listCategoryItem;

    @OneToMany(mappedBy = "account",fetch = FetchType.LAZY)
    public List<RoleAccount> listRole;

    @OneToOne(mappedBy = "accountShop",fetch = FetchType.LAZY)
    private Shop shop;

    @OneToOne(mappedBy = "Infaccount",fetch = FetchType.LAZY)
    private InfoAccount infoAccount;

    @OneToMany(mappedBy = "Addressaccount",fetch = FetchType.LAZY)
    private List<AddressAccount> address_account;

    @OneToMany(mappedBy = "account_like",fetch = FetchType.LAZY)
    private List<LikeProduct> likeProductes;

    @OneToMany(mappedBy = "accountOrder",fetch = FetchType.LAZY)
    private List<Order> orders;

    @OneToMany(mappedBy = "account_check",fetch = FetchType.LAZY)
    List<StatusOrder> statusOrders;

    @OneToMany(mappedBy = "sender")
    @JsonIgnore
    private List<ChatMessage> senderMessage;

    @OneToMany(mappedBy = "receiver")
    @JsonIgnore
    private List<ChatMessage> receiverMessage;
}