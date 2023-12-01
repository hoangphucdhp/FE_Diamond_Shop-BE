package com.example.DATN_API.Controller;

import com.example.DATN_API.Entity.AuthenticationResponse;
import com.example.DATN_API.Entity.AuthenticationRqeuest;
import com.example.DATN_API.Service.AuthenticationService;
import com.example.DATN_API.Entity.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }
    @PostMapping("test")
    public ResponseEntity<String> register1(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok("dang ky");
    }
    @PostMapping("login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRqeuest request) {
        System.out.println("123");
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
