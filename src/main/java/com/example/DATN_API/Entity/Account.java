package com.example.DATN_API.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account")
public class Account implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "username")
    private String us;
    @Column(name = "password")
    private String pw;
    @CreatedDate
    private Date create_date;
    private boolean status;
    private String provider;
    @OneToMany(mappedBy = "account_role", fetch = FetchType.EAGER)
    private List<RoleAccount> roles;

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

    @OneToMany(mappedBy = "sender")
    @JsonIgnore
    private List<ChatMessage> senderMessage;

    @OneToMany(mappedBy = "receiver")
    @JsonIgnore
    private List<ChatMessage> receiverMessage;

    @OneToMany(mappedBy = "account_rate")
    @JsonIgnore
    public List<Rate> rates;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roles.stream().forEach(
                item -> {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + item.getRole().getRole_name()));
                }
        );
        return authorities;
    }

    @Override
    public String getPassword() {
        return pw;
    }

    @Override
    public String getUsername() {
        return us;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
