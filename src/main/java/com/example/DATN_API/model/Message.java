package com.example.DATN_API.model;


import lombok.*;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Message {
    private String sender;
    private String receiver;
    private String image;
    private String message;
    private Date time;
    private Status status;
}
