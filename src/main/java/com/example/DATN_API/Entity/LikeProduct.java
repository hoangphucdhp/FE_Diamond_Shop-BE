package com.example.DATN_API.Entity;
import java.time.LocalDateTime;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "like_product")
public class LikeProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "id_product")
    private Product product_like;

    @ManyToOne
    @JoinColumn(name = "id_account")
    @JsonIgnore
    private Account account_like;
    
    @Column(name = "create_date")
    private LocalDateTime createDate; // Thêm trường createDate và setter


    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
    
    public void setProductLike(Product product) {
        this.product_like = product;
    }

    public void setAccountLike(Account account) {
        this.account_like = account;
    }
}
