package com.example.DATN_API.Controller;

import com.example.DATN_API.Entity.*;
import com.example.DATN_API.Service.AccountService;
import com.example.DATN_API.Service.AuthenticationService;
import com.example.DATN_API.Service.InfoAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
@CrossOrigin("*")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @Autowired
    AccountService accountService;
    @Autowired
    InfoAccountService infoAccountService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("register")
    public ResponseEntity<ResponObject> register(@RequestBody RegisterRequest request) {
        if (accountService.findByUsername(request.getUsername()).isPresent()) {
            return new ResponseEntity<>(new ResponObject("error", "Tài khoản đã tồn tại!", null), HttpStatus.CREATED);
        } else if (request.getUsername().length() < 8) {
            return new ResponseEntity<>(new ResponObject("error", "Độ dài tối thiểu của tài khoản là 8 ký tự!", null), HttpStatus.CREATED);
        } else if (request.getPassword().length() < 8) {
            return new ResponseEntity<>(new ResponObject("error", "Độ dài tối thiểu của mật khẩu là 8 ký tự!", null), HttpStatus.CREATED);
        } else if (infoAccountService.findByEmail(request.getEmail()).isPresent()) {
            return new ResponseEntity<>(new ResponObject("error", "Email này đã được sử dụng cho một tài khoản khác!", null), HttpStatus.CREATED);
        } else {
            Optional<AuthenticationResponse> registrationResult = Optional.ofNullable(authenticationService.register(request));
            return registrationResult.map(authenticationResponse -> new ResponseEntity<>(new ResponObject("success", "Đăng kí thành công!", authenticationResponse), HttpStatus.CREATED)).orElseGet(() -> new ResponseEntity<>(new ResponObject("error", "Đăng kí không thành công!", null), HttpStatus.CREATED));
        }
    }

    @PostMapping("registerWithGoogle")
    public ResponseEntity<ResponObject> registerGoogle(@RequestParam("email") String email, @RequestParam("displayName") String displayName) {
        AuthenticationResponse au = authenticationService.registerGoogle(email, displayName);
        if (au == null) {
            return new ResponseEntity<>(new ResponObject("error", "Email đã được sử dụng với tài khoản của FE", ""),
                    HttpStatus.CREATED);
        } else {
            AuthenticationRqeuest authenticationRqeuest = new AuthenticationRqeuest(email, "googlePassword");
            return new ResponseEntity<>(new ResponObject("success", "Thành công", authenticationService.authenticate(authenticationRqeuest)),
                    HttpStatus.CREATED);
        }
    }


//    @PostMapping("test")
//    public ResponseEntity<String> register1(@RequestBody RegisterRequest request) {
//        return ResponseEntity.ok("dang ky");
//    }

    @PostMapping("login")
    public ResponseEntity<ResponObject> login(@RequestBody AuthenticationRqeuest request) {
        Optional<Account> optionalAccount = accountService.findByUsername(request.getUsername());
        if (optionalAccount.isEmpty()) {
            return new ResponseEntity<>(new ResponObject("error", "Tài khoản không tồn tại!", null), HttpStatus.CREATED);
        }
        if (optionalAccount.get().getProvider().equals("google")) {
            return new ResponseEntity<>(new ResponObject("error", "Tài khoản không tồn tại!", null), HttpStatus.CREATED);
        }
        Account account = optionalAccount.get();
        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            return new ResponseEntity<>(new ResponObject("error", "Mật khẩu không chính xác!", null), HttpStatus.CREATED);
        } else if (!account.isStatus()) {
            return new ResponseEntity<>(new ResponObject("error", "TÀI KHOẢN NÀY HIỆN TẠI ĐANG BỊ KHÓA, VUI LÒNG LIÊN HỆ CHO QUẢN TRỊ VIÊN NẾU GẶP VẪN ĐỀ!!", null), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new ResponObject("success", "Đăng nhập thành công", authenticationService.authenticate(request)), HttpStatus.CREATED);
    }
}
