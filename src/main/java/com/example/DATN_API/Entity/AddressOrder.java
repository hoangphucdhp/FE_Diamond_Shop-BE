//package com.example.DATN_API.Entity;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name = "address_order")
//public class AddressOrder {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//    private String city;
//    private String district;
//    private String ward;
//    private String address;
//
//    @OneToOne(mappedBy = "address")
//    @JsonIgnore
//    private Order order;
//}
