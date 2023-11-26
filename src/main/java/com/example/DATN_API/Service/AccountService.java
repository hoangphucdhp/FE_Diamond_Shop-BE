package com.example.DATN_API.Service;

import com.example.DATN_API.Entity.Account;
import com.example.DATN_API.Reponsitories.AccountReponsitory;
import com.example.DATN_API.Reponsitories.RoleAccountReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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


    public Page<Account> findAll(Optional<Integer> offset, Optional<Integer> sp, Optional<String> field, Optional<String> sortType, Optional<String> key, Optional<String> keyword, Optional<String> ischeck) {

            String sortby = field.orElse("id");
            int itemStart = offset.orElse(0);
            int sizePage = sp.orElse(10);
            String keyfind = key.orElse("");
            String keywords = keyword.orElse("");

            Sort.Direction direction;

            // Sort
            String typeSort = sortType.orElse("asc");

            if (sortby == null || sortby.isEmpty()) {
                sortby = "id";
            }

            if (typeSort == null || typeSort.isEmpty()) {
                typeSort = "asc";
            }

            if (typeSort.equals("asc")) {
                direction = Sort.Direction.ASC;
            } else {
                direction = Sort.Direction.DESC;
            }

            Sort sort = Sort.by(direction, sortby);
            if(ischeck.isPresent()&&ischeck.get().equals("account")){
                if (keyfind.equals("username")) {
                    return accountReponsitory.getAllfindbyUsername(PageRequest.of(itemStart, sizePage, sort), keywords);
                } else if (keyfind.equals("fullname")) {
                    return accountReponsitory.getAllfindbyFullname(PageRequest.of(itemStart, sizePage, sort), keywords);
                } else if (keyfind.equals("") && !keywords.equals("")) {
                    return accountReponsitory.getAllfindbyFullname(PageRequest.of(itemStart, sizePage, sort), keywords);
                } else {
                    return accountReponsitory.getAll(PageRequest.of(itemStart, sizePage, sort));
                }
            }else{
                if(keyfind.equals(("shop_name"))){
                    return accountReponsitory.getByShopName(PageRequest.of(itemStart, sizePage, sort), keywords);
                }else if (keyfind.equals("fullname")) {
                    return accountReponsitory.getAllfindbyFullname(PageRequest.of(itemStart, sizePage, sort), keywords);
                } else if (keyfind.equals("") && !keywords.equals("")) {
                    return accountReponsitory.getByShopName(PageRequest.of(itemStart, sizePage, sort), keywords);
                }else {
                    return accountReponsitory.getAll(PageRequest.of(itemStart, sizePage, sort));
                }
            }

        }


        public Page<Account> findAll (Optional < Integer > offset, Optional < Integer > sp, Optional < String > field){
            String sort = field.orElse("create_date");
            int itemStart = offset.orElse(0);
            ;
            int sizePage = sp.orElse(10);
            return accountReponsitory.getAll(PageRequest.of(itemStart, sizePage, Sort.Direction.DESC, sort));
        }


        public Account findById (Integer id){
            return accountReponsitory.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tài khoản không được tìm thấy"));
        }

        public Account banAccount (Integer id){
            Optional<Account> acc = accountReponsitory.findById(id);
            Account account = acc.get();
            account.setStatus(false);
            accountReponsitory.save(account);
            return account;
        }

        public Account createAccount (Account account){
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

        public Account changePass (Account account){
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


        public Account findByEmail (String email){
            return accountReponsitory.findByEmail(email);
        }


        public Account AdminUpdate ( int id, boolean status){
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
