package com.example.DATN_API.Controller;

import com.example.DATN_API.Entity.Account;
import com.example.DATN_API.Entity.ChatMessage;
import com.example.DATN_API.Reponsitories.ChatMessageReponsitory;
import com.example.DATN_API.Service.AccountService;
import com.example.DATN_API.Service.IStorageSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import com.example.DATN_API.model.Message;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private ChatMessageReponsitory chatMessageReponsitory;
    @Autowired
    private AccountService accountService;
    @Autowired
    IStorageSerivce iStorageSerivce;

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Message receiveMessage(@Payload Message message) {
        return message;
    }

    @MessageMapping("/private-message")
    public Message recMessage(@Payload Message message) {
        try {
            Account accountSender = accountService.findByUsername(message.getSender()).get();
            Account accountReceiver = accountService.findByUsername(message.getReceiver()).get();
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setSender(accountSender);
            chatMessage.setReceiver(accountReceiver);
            chatMessage.setTime(new Date());
            chatMessage.setImage(message.getImage());
            chatMessage.setMessage(message.getMessage());
            //chatMessageReponsitory.saveWithImage(chatMessage.getSender().getId(), chatMessage.getReceiver().getId(), chatMessage.getImage(), chatMessage.getMessage(), chatMessage.getTime());
            chatMessageReponsitory.save(chatMessage);
            simpMessagingTemplate.convertAndSendToUser(message.getReceiver(), "/private", message);
        } catch (Exception e){
            e.printStackTrace();
        }
        return message;
    }

}