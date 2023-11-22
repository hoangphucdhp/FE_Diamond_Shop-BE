package com.example.DATN_API.Entity;

import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rate")
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "id_product")
    @JsonIgnore
    private Product product_rate;

    private int star;

    private String description;

    @ManyToOne
    @JoinColumn(name = "create_by")
    private Account account_rate;
    
  @Column(name = "create_date")
    private LocalDateTime createDate;
}
