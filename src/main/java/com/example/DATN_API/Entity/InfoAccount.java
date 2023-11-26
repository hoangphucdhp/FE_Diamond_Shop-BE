package com.example.DATN_API.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "info_account")
public class InfoAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "id_account")
    @JsonIgnore
    private Account Infaccount;

    private String fullname;

    private String id_card;

    private String phone;

    private boolean gender;

    private String email;

    private String image;

}
