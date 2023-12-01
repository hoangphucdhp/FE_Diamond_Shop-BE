package com.example.DATN_API.Controller;

import com.example.DATN_API.Entity.*;

import com.example.DATN_API.Service.AccountService;
import com.example.DATN_API.Service.AddressAccountService;
//import com.example.DATN_API.Service.AuthService;
import com.example.DATN_API.Service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin
@RequiredArgsConstructor
public class AccountController {

    private final AuthenticationService authenticationService;
    @Autowired
    AccountService accountService;
    @Autowired
    AddressAccountService addressAccountService;


    @GetMapping("/account/getAll")
    public ResponseEntity<ResponObject> getAll(@RequestParam("offset") Optional<Integer> offSet,
                                               @RequestParam("sizePage") Optional<Integer> sizePage,
                                               @RequestParam("sort") Optional<String> sort) {
        Page<Account> accounts = accountService.findAll(offSet, sizePage, sort);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponObject(
                        "SUCCESS", "GET ALL ACCOUNT", accounts
                )
        );
    }


    @GetMapping("/account/{id}")
    public ResponseEntity<ResponObject> getAccountById(@PathVariable("id") Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                "SUCCESS", "get by id successfully", accountService.findAccountById(id)
        ));
    }
    @GetMapping("/account/{id}/address")
    public ResponseEntity<ResponObject> getAddressDefault(@PathVariable("id") int id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                "SUCCESS", "get address default by id successfully", addressAccountService.getAddressDefault(id)
        ));
    }
//    @PostMapping("/auth/register")
//    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
//        return ResponseEntity.ok(authenticationService.register(request));
//    }
//    @PostMapping("/auth/login")
//    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRqeuest request) {
//        return ResponseEntity.ok(authenticationService.authenticate(request));
//    }

//    @PostMapping("/auth/login")
//    public LoginRespon login(@RequestBody @Validated LoginRequest request) {
//        return authService.attemLogin(request.getEmail(), request.getPassword());
//    }
//
//    @GetMapping("/secured")
//    public String secured(@AuthenticationPrincipal LoginRequest userPrincipal) {
//        return "Login success! " + userPrincipal.getEmail() + " Password : " + userPrincipal.getPassword();
//    }
//
//    @GetMapping("/admin")
//    public String admin(@AuthenticationPrincipal UserPrincipal userPrincipal) {
//        System.out.println(userPrincipal.getUsername());
//        return "you are a admin! ";
//    }
//    @GetMapping("/get_account_detail/{username}")
//    public ResponseEntity<ResponObject> getDetailAccount(@PathVariable("username") String username){
//        Optional<Account> account = accountReponsitory.findById(username);
//        if(account.isEmpty()){
//            return ResponseEntity.status(HttpStatus.OK).body(
//                    new ResponObject(
//                            "0","Không tìm thấy thông tài khoản: "+username,""
//                    )
//            );
//        }else {
//            return ResponseEntity.status(HttpStatus.OK).body(
//                    new ResponObject(
//                            "1"," thông tài khoản: "+username,account.get()
//                    )
//            );
//        }
//
//    }

//    @PostMapping("create")
//    public ResponseEntity<ResponObject> create(@RequestBody Account account){
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:MM:ss");
//        account.setCreate_date(new Date());
//        Account accountSave = accountReponsitory.save(account);
//
//    return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
//
//    ));
//    }
//    @PutMapping ("update/{username}")
//    public ResponseEntity<ResponObject> update(@PathVariable("username") String username,@RequestBody Account account){
//        account.setUsername(username);
//        Account accountSave = accountReponsitory.save(account);
//        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
//            "1","update account successfully",accountSave
//        ));
//    }
//    @PutMapping("delete/{username}")
//    public ResponseEntity<ResponObject> delete(@PathVariable("username") String username){
//        Optional<Account> acc = accountReponsitory.findById(username);
//        Account account = acc.get();
//        account.setStatus(false);
//        Account accountSave = accountReponsitory.save(account);
//        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
//                "1","delete account successfully",accountSave
//        ));
//    }

}
