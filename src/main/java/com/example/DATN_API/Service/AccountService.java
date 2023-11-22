package com.example.DATN_API.Service;

import com.example.DATN_API.Entity.Account;
import com.example.DATN_API.Entity.Order;
import com.example.DATN_API.Entity.Role;
import com.example.DATN_API.Entity.RoleAccount;
import com.example.DATN_API.Reponsitories.AccountReponsitory;
import com.example.DATN_API.Reponsitories.RoleAccountReponsitory;
import com.example.DATN_API.Reponsitories.RoleReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    AccountReponsitory accountReponsitory;
    @Autowired
    RoleAccountReponsitory roleAccountReponsitory;

    public Account findByUsername(String username) {
        return accountReponsitory.findByUsername(username);
    }

    public List<Account> findAll() {
        return accountReponsitory.findAll();
    }

    public Page<Account> findAll(Optional<Integer> offset, Optional<Integer> sp, Optional<String> field, Optional<String> key, Optional<String> keyword) {
        String sort = field.orElse("create_date");
        int itemStart = offset.orElse(0);
        int sizePage = sp.orElse(10);
        String keyfind = key.orElse("");
        String keywords = keyword.orElse("");
        if (keyfind.equals("username")) {
            System.out.println(1);
            return accountReponsitory.getAllfindbyUsername(PageRequest.of(itemStart, sizePage, Sort.Direction.DESC, sort), keywords);
        } else if (keyfind.equals("fullname")) {
            System.out.println(2);
            return accountReponsitory.getAllfindbyFullname(PageRequest.of(itemStart, sizePage, Sort.Direction.DESC, sort), keywords);
        } else if (keyfind.equals("") && !keywords.equals("")) {
            System.out.println(3);
            return accountReponsitory.getAllfindbyFullname(PageRequest.of(itemStart, sizePage, Sort.Direction.DESC, sort), keywords);
        } else {
            System.out.println(4);
            return accountReponsitory.getAll(PageRequest.of(itemStart, sizePage, Sort.Direction.DESC, sort));
        }
    }

    public Account findById(Integer id) {
        return accountReponsitory.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tài khoản không được tìm thấy"));
    }

    public Account banAccount(Integer id) {
        Optional<Account> acc = accountReponsitory.findById(id);
        Account account = acc.get();
        account.setStatus(false);
        accountReponsitory.save(account);
        return account;
    }

    public Account createAccount(Account account) {
        try {
            PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            Account accountCreate = accountReponsitory.save(account);
            accountCreate.setPassword(passwordEncoder.encode(accountCreate.getPassword()));
            return accountCreate;
        } catch (Exception e) {
            e.printStackTrace();
            LogError.saveToLog(e);
        }
        return null;
    }

    public Account changePass(Account account) {
        try {
            Account accountCreate = accountReponsitory.save(account);
            return accountCreate;
        } catch (Exception e) {
            e.printStackTrace();
            LogError.saveToLog(e);
        }
        return null;
    }

    public Account AdminUpdate(int id, boolean status) {
        try {
            Account account = findById(id);
            account.setStatus(status);
            return accountReponsitory.save(account);
        } catch (Exception e) {
            e.printStackTrace();
            LogError.saveToLog(e);
        }
        return null;
    }
}
