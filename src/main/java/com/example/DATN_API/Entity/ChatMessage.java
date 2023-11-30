package com.example.DATN_API.Entity;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "message")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne()
    @JoinColumn(name = "sender")
    private Account sender;

    @ManyToOne()
    @JoinColumn(name = "receiver")
    private Account receiver;
    @Column(name = "image", columnDefinition = "TEXT")
    private String image;
    private String message;

    @Timestamp
    private Date time;
}

