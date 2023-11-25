package com.example.DATN_API.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnore;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account_role")
public class RoleAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "id_account")
    private Account account_role;

    @ManyToOne
    @JoinColumn(name = "id_account")
    @JsonIgnore
    private Account account;

    @OneToOne
    @JoinColumn(name = "id_role")
    private Role role;
}
