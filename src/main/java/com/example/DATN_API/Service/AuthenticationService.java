package com.example.DATN_API.Service;

import com.example.DATN_API.Entity.*;
import com.example.DATN_API.Reponsitories.AccountReponsitory;
import com.example.DATN_API.Reponsitories.InfoAccountReponsitory;
import com.example.DATN_API.Reponsitories.RoleAccountResponsitory;
import com.example.DATN_API.Security.JwtService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AccountReponsitory accountReponsitory;
    private final RoleAccountResponsitory roleUserReponsitory;
    private final InfoAccountReponsitory infoAccountReponsitory;


    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Autowired
    AccountService accountService;

    public AuthenticationResponse register(RegisterRequest request) {
        Role role = new Role();
        role.setId(3);
        RoleAccount roleAccount = new RoleAccount();
        roleAccount.setRole(role);

        List<RoleAccount> roleUsers = new ArrayList<>();
        roleUsers.add(roleAccount);
        InfoAccount infoAccount = new InfoAccount();
        var user = Account.builder()
                .us(request.getUsername())
                .pw(passwordEncoder().encode(request.getPassword()))
                .roles(roleUsers)
                .create_date(new Date())
                .status(true)
                .provider("myweb")
                .build();
        roleAccount.setAccount_role(user);
        infoAccount.setEmail(request.getEmail());
        infoAccount.setInfaccount(user);

        accountReponsitory.save(user);
        roleUserReponsitory.save(roleAccount);
        infoAccountReponsitory.save(infoAccount);
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse registerGoogle(String email, String displayName) {
        if (accountService.findByUsername(email).isPresent() && accountService.findByUsername(email).get().getProvider().equals("myweb")) {
            return null;
        } else if (accountService.findByEmail(email) != null && accountService.findByEmail(email).getProvider().equals("myweb")) {
            return null;
        } else if (!accountService.findByUsername(email).isPresent() && accountService.findByEmail(email) == null) {
            Role role = new Role();
            role.setId(3);
            RoleAccount roleAccount = new RoleAccount();
            roleAccount.setRole(role);
            List<RoleAccount> roleUsers = new ArrayList<>();
            roleUsers.add(roleAccount);
            InfoAccount infoAccount = new InfoAccount();
            var user = Account.builder()
                    .us(email)
                    .pw(passwordEncoder().encode("googlePassword"))
                    .roles(roleUsers)
                    .create_date(new Date())
                    .status(true)
                    .provider("google")
                    .build();
            roleAccount.setAccount_role(user);
            infoAccount.setEmail(email);
            infoAccount.setFullname(displayName);
            infoAccount.setInfaccount(user);

            Account account= accountReponsitory.save(user);
            List<RoleAccount> roleAccount1= Collections.singletonList(roleUserReponsitory.save(roleAccount));
            InfoAccount inf= infoAccountReponsitory.save(infoAccount);
            account.setRoles(roleAccount1);
            account.setInfoAccount(inf);
            var jwtToken = jwtService.generateToken(account);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } else {
            var jwtToken = jwtService.generateToken(accountService.findByUsername(email).get());
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRqeuest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var user = accountReponsitory.findByUsername(request.getUsername()).orElseThrow();

        if (user == null) {
            return AuthenticationResponse.builder()
                    .status(false)
                    .token("")
                    .build();
        }
        Account account = accountService.findByUsername(request.getUsername()).get();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .status(true)
                .token(jwtToken)
                .data(account)
                .build();
    }

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public List<Account> getAll() {
        return accountReponsitory.findAll();
    }
}
