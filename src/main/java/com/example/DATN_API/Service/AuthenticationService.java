package com.example.DATN_API.Service;

import com.example.DATN_API.Entity.*;
import com.example.DATN_API.Reponsitories.AccountReponsitory;
import com.example.DATN_API.Reponsitories.RoleAccountResponsitory;
import com.example.DATN_API.Security.JwtService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AccountReponsitory accountReponsitory;
    private final RoleAccountResponsitory roleUserReponsitory;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        Role role = new Role();
        role.setId(1);
        RoleAccount roleAccount = new RoleAccount();
        roleAccount.setRole(role);

        List<RoleAccount> roleUsers = new ArrayList<>();
        roleUsers.add(roleAccount);

        var user = Account.builder()
                .us(request.getUsername())
                .pw(passwordEncoder.encode(request.getPassword()))
                .roles(roleUsers)
                .create_date(new Date())
                .build();
        roleAccount.setAccount_role(user);
        accountReponsitory.save(user);
        roleUserReponsitory.save(roleAccount);
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRqeuest request) {
        System.out.println("voo");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var user = accountReponsitory.findByUsername(request.getUsername()).orElseThrow();
        System.out.println(user.getUsername());
        if (user == null) {
            System.out.println("null");
            return AuthenticationResponse.builder()
                    .status(false)
                    .token("")
                    .build();
        }
        var jwtToken = jwtService.generateToken(user);
        System.out.println(jwtToken);
        return AuthenticationResponse.builder()
                .status(true)
                .token(jwtToken)
                .data(user)
                .build();
    }

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public List<Account> getAll() {
        return accountReponsitory.findAll();
    }
}
