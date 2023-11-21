package com.example.DATN_API.Service;

import com.example.DATN_API.Entity.Account;
import com.example.DATN_API.Entity.Order;
import com.example.DATN_API.Reponsitories.AccountReponsitory;
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

    public Account findByUsername(String username) {
		return accountReponsitory.findByUsername(username);
	}

    public List<Account> findAll() {
        return accountReponsitory.findAll();
    }

    public Page<Account> findAll(Optional<Integer> offset, Optional<Integer> sp, Optional<String> field) {
        String sort = field.orElse("create_date");
        int itemStart = offset.orElse(0);
        ;
        int sizePage = sp.orElse(10);
        return accountReponsitory.getAll(PageRequest.of(itemStart, sizePage, Sort.Direction.DESC, sort));
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
			account.setPassword(passwordEncoder.encode(account.getPassword()));
			return accountReponsitory.save(account);
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
}
