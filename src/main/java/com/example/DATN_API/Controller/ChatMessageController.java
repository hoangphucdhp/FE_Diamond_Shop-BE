package com.example.DATN_API.Controller;

import com.example.DATN_API.Entity.Account;
import com.example.DATN_API.Entity.ChatMessage;
import com.example.DATN_API.Entity.ResponObject;
import com.example.DATN_API.Reponsitories.ChatMessageReponsitory;
import com.example.DATN_API.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin("*")
public class ChatMessageController {
    @Autowired
    ChatMessageReponsitory chatMessageReponsitory;
    @Autowired
    AccountService accountService;

    @GetMapping("/getchatbysender/{id}")
    public ResponseEntity<ResponObject> getChat(@PathVariable("id") Integer id) {
        Account accountsender = accountService.findById(id);
        List<ChatMessage> chatMessageSender = chatMessageReponsitory.getAllBySender(accountsender);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponObject(
                        "SUCCESS", "GET SUCCESFULLY", chatMessageSender));
    }

    @GetMapping("/getchatbyreceiver/{id}")
    public ResponseEntity<ResponObject> getChatby(@PathVariable("id") Integer id) {
        Account accountsender = accountService.findById(id);
        List<ChatMessage> chatMessageSender = chatMessageReponsitory.getAllByReceiver(accountsender);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponObject(
                        "SUCCESS", "GET SUCCESFULLY", chatMessageSender));
    }

    @GetMapping("/getchatby")
    public ResponseEntity<ResponObject> getChatReceiver() {
        List<ChatMessage> chatMessageSender = chatMessageReponsitory.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponObject(
                        "SUCCESS", "GET SUCCESFULLY1234", chatMessageSender));
    }
}
