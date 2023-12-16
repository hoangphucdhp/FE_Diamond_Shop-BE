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
@Table(name = "Shop")

public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String shop_name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_account")
    @JsonIgnore
    private Account accountShop;

    private String image;
    private Date create_date;
    @OneToOne(mappedBy = "shopAddress", cascade = CascadeType.ALL)
    private AddressShop addressShop;

    private int status;
    @OneToMany(mappedBy = "shop")

    private List<Product> products;

    @OneToMany(mappedBy = "shopOrder")
    private List<OrderDetail> listOrder;

}
