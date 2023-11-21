package com.example.DATN_API.Controller;

import com.example.DATN_API.Entity.Account;
import com.example.DATN_API.Entity.AddressAccount;
import com.example.DATN_API.Entity.AddressShop;
import com.example.DATN_API.Entity.InfoAccount;
import com.example.DATN_API.Entity.MailInformation;
import com.example.DATN_API.Entity.ResponObject;
import com.example.DATN_API.Entity.Role;
import com.example.DATN_API.Entity.RoleAccount;
import com.example.DATN_API.Entity.Shop;
import com.example.DATN_API.Reponsitories.AccountReponsitory;
import com.example.DATN_API.Service.AccountService;
import com.example.DATN_API.Service.AddressAccountService;
import com.example.DATN_API.Service.AddressShopService;
import com.example.DATN_API.Service.InfoAccountService;
import com.example.DATN_API.Service.MailServiceImplement;
import com.example.DATN_API.Service.RoleAccountService;
import com.example.DATN_API.Service.ShopService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.swing.text.html.Option;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/account/")
@CrossOrigin
public class AccountController {

        @Autowired
        AccountService accountService;

        @Autowired
        InfoAccountService infoAccountService;

        @Autowired
        ShopService shopService;

        @Autowired
        AddressShopService addressService;

        @Autowired
        MailServiceImplement mailServiceImplement;

        @Autowired
        RoleAccountService roleAccService;

        @Autowired
        AddressAccountService addressAccountService;

        @Autowired
        HttpSession session;

        @GetMapping("/getAll")
        public ResponseEntity<ResponObject> getAll(@RequestParam("offset") Optional<Integer> offSet,
                        @RequestParam("sizePage") Optional<Integer> sizePage,
                        @RequestParam("sort") Optional<String> sort) {
                Page<Account> accounts = accountService.findAll(offSet, sizePage, sort);
                return ResponseEntity.status(HttpStatus.OK).body(
                                new ResponObject(
                                                "SUCCESS", "GET ALL ACCOUNT", accounts));

        }

        @GetMapping("/{id}")
        public ResponseEntity<ResponObject> getAccountById(@PathVariable("id") Integer id) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                                "SUCCESS", "get by id successfully", accountService.findById(id)));
        }

        @GetMapping("/{id}/address")
        public ResponseEntity<ResponObject> getAddressDefault(@PathVariable("id") int id) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                                "SUCCESS", "get address default by id successfully",
                                addressAccountService.getAddressDefault(id)));
        }

        @PostMapping("/login")
        public ResponseEntity<ResponObject> login(@RequestBody Account account) {
                try {
                        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
                        Account accounts = accountService.findByUsername(account.getUsername());
                        if (accounts != null) {
                                if (passwordEncoder.matches(account.getPassword(), accounts.getPassword())
                                                && accounts.isStatus() == false) {
                                        return new ResponseEntity<>(
                                                        new ResponObject("success", "Đăng nhập thành công!", accounts),
                                                        HttpStatus.CREATED);
                                } else {
                                        if (account.getUsername().equals(accounts.getUsername())
                                                        && !account.getPassword().equals(accounts.getPassword())) {
                                                return new ResponseEntity<>(new ResponObject("error",
                                                                "Mật khẩu không chính xác!", null), HttpStatus.OK);
                                        } else if (accounts.isStatus() == true) {
                                                return new ResponseEntity<>(new ResponObject("error",
                                                                "TÀI KHOẢN BẠN ĐĂNG NHẬP HIỆN TẠI ĐANG BỊ KHÓA, VUI LÒNG LIÊN HỆ CHO QUẢN TRỊ VIÊN NẾU GẶP VẪN ĐỀ!",
                                                                account), HttpStatus.OK);
                                        }
                                }

                        } else {
                                return new ResponseEntity<>(new ResponObject("error", "Tài khoản không tồn tại!", null),
                                                HttpStatus.OK);
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return null;
        }

        @PostMapping("/{email}")
        public ResponseEntity<Map<String, Object>> codeValidate(@PathVariable("email") String email) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("code", "");
                try {
                        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                                        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
                        String charSet = "1234567890";
                        // Begin validate Email
                        if (Pattern.compile(regexPattern).matcher(email).matches() != true) {
                                response.put("message", "Email không hợp lệ!");

                        } else if (infoAccountService.findByEmail(email) != null) {
                                response.put("message", "Email này đã được sử dụng cho một tài khoản khác!");
                        } else {
                                String code = "";
                                Random rand = new Random();
                                int len = 6;
                                for (int i = 0; i < len; i++) {
                                        code += charSet.charAt(rand.nextInt(charSet.length()));
                                }
                                session.setAttribute("emailRegister", email);
                                MailInformation mail = new MailInformation();
                                mail.setTo(email);
                                mail.setSubject("MÃ XÁC NHẬN");
                                mail.setBody("<html><body>" + "<p>Xin chào " + email + ",</p>"
                                                + "<p>Chúng tôi nhận được yêu cầu đăng ký tài khoản FE Shop của bạn.</p>"
                                                + "<p>Vui lòng không chia sẽ mã này cho bất cứ ai:" + "<h3>" + code
                                                + "</h3>" + "</p>"
                                                + "<p>Trân trọng,</p>"
                                                + "<p>Bạn có thắc mắc? Liên hệ chúng tôi tại đây khuong8177@gmail.com.</p>"
                                                + "<p>Thời gian tồn tại của mã OTP là 5 phút.</p>"
                                                + "</body></html>");
                                mailServiceImplement.send(mail);
                                response.put("success", true);
                                response.put("code", code);
                                response.put("message", "Một mã xác nhận đã được gửi đến email của bạn!");
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return ResponseEntity.ok(response);
        }

        @PostMapping("/{email}/{newpassword}")
        public ResponseEntity<Map<String, Object>> rePassword(@PathVariable("email") String email,
                        @PathVariable("newpassword") String newpassword) {
                Map<String, Object> response = new HashMap<>();
                try {
                        InfoAccount inAcc = infoAccountService.findByEmail(email);
                        Account account = accountService.findById(inAcc.getInfaccount().getId());
                        account.setPassword(newpassword);
                        accountService.createAccount(account);
                        response.put("message", "ĐẶT LẠI MẬT KHẨU THÀNH CÔNG!");

                } catch (Exception e) {
                        e.printStackTrace();
                }
                return ResponseEntity.ok(response);
        }

        @PostMapping("/register/{email}")
        public ResponseEntity<Map<String, Object>> register(@PathVariable("email") String email,
                        @RequestBody Account account) {
                Map<String, Object> response = new HashMap<>();
                try {
                        Account accounts = accountService.findByUsername(account.getUsername());
                        RoleAccount roleAcc = new RoleAccount();
                        Role role = new Role();
                        InfoAccount inAcc = new InfoAccount();
                        LocalDate localDate = LocalDate.now();
                        Date date = java.sql.Date.valueOf(localDate);
                        // Begin validate
                        if (accounts != null) {
                                response.put("message", " Tài khoản đã tồn tại!");
                        } else if (account.getUsername().length() < 8) {
                                response.put("message", "Độ dài tối thiểu của tài khoản là 8 ký tự!");
                        } else if (account.getPassword().length() < 8) {
                                response.put("message", "Độ dài tối thiểu của mật khẩu là 8 ký tự!");
                        } else if (infoAccountService.findByEmail(email) != null) {
                                response.put("message", "Email này đã được sử dụng cho một tài khoản khác!");
                        } else {
                                // Account
                                account.setCreate_date(date);
                                account.setStatus(false);
                                accountService.createAccount(account);
                                // Create role
                                Account accountCheck = accountService.findByUsername(account.getUsername());
                                role.setId(1);
                                roleAcc.setAccount(accountCheck);
                                roleAcc.setRole(role);
                                roleAccService.createRoleAcc(roleAcc);
                                // Default info
                                inAcc.setFullname(account.getUsername());
                                inAcc.setEmail(email);
                                inAcc.setGender(false);
                                inAcc.setInfaccount(accountCheck);
                                infoAccountService.createProfile(inAcc);
                                response.put("success", true);
                                response.put("message", "ĐĂNG KÝ THÀNH CÔNG!");
                                response.put("data", account);
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return ResponseEntity.ok(response);
        }

        @PostMapping("/forgot")
        public ResponseEntity<Map<String, Object>> forgotPassword(@RequestBody InfoAccount inAccount) {
                Map<String, Object> response = new HashMap<>();
                try {
                        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                                        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
                        String charSet = "1234567890";
                        // Begin validate Email
                        if (Pattern.compile(regexPattern).matcher(inAccount.getEmail()).matches() != true) {
                                response.put("message", "EMAIL KHÔNG HỢP LỆ!");
                        } else if (infoAccountService.findByEmail(inAccount.getEmail()) == null) {
                                response.put("message", "KHÔNG TÌM THẤY TÀI KHOẢN CÓ EMAIL " + inAccount.getEmail());
                        } else {
                                InfoAccount inAccounts = infoAccountService.findByEmail(inAccount.getEmail());
                                Account account = accountService
                                                .findByUsername(inAccounts.getInfaccount().getUsername());
                                if (inAccounts.getInfaccount().isStatus() == true) {
                                        response.put("message",
                                                        "HIỆN TẠI, TÀI KHOẢN CỦA BẠN ĐANG BỊ KHÓA, VUI LÒNG LIÊN HỆ CSKH ĐỂ ĐƯỢC HỔ TRỢ SỚM NHẤT!");
                                } else {
                                        // Email = true, then begin random new password and update
                                        String newPassword = "";
                                        Random rand = new Random();
                                        int len = 8;
                                        for (int i = 0; i < len; i++) {
                                                newPassword += charSet.charAt(rand.nextInt(charSet.length()));
                                        }
                                        MailInformation mail = new MailInformation();
                                        mail.setTo(inAccount.getEmail());
                                        mail.setSubject("Quên mật khẩu");
                                        mail.setBody("<html><body>" + "<p>Xin chào " + account.getUsername() + ",</p>"
                                                        + "<p>Chúng tôi nhận được yêu cầu thiết lập lại mật khẩu cho tài khoản FE Shop của bạn.</p>"
                                                        + "<p>Vui lòng không chia sẽ mã này cho bất cứ ai:" + "<h3>"
                                                        + newPassword + "</h3>"
                                                        + "</p>"
                                                        + "<p>Nếu bạn không yêu cầu thiết lập lại mật khẩu, vui lòng liên hệ Bộ phận Chăm sóc Khách hàng tại đây</p>"
                                                        + "<p>Trân trọng,</p>"
                                                        + "<p>Bạn có thắc mắc? Liên hệ chúng tôi tại đây khuong8177@gmail.com.</p>"
                                                        + "</body></html>");
                                        mailServiceImplement.send(mail);
                                        response.put("success", true);
                                        response.put("code", newPassword);
                                        response.put("message", "MÃ OTP CỦA BẠN ĐÃ ĐƯỢC GỬI QUA EMAIL!");
                                }
                        }
                } catch (Exception e) {
                        response.put("message", "KHÔNG TÌM THẤY TÀI KHOẢN CÓ EMAIL " + inAccount.getEmail());
                        e.printStackTrace();
                }
                return ResponseEntity.ok(response);
        }

        @PostMapping("/profile")
        public ResponseEntity<InfoAccount> profileAccount() {
                if (infoAccountService.findById_account(5) != null) {
                        return new ResponseEntity<>(infoAccountService.findById_account(5), HttpStatus.OK);
                }
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        @PostMapping("/updateprofile/{username}")
        public ResponseEntity<ResponObject> updateProfile(@PathVariable("username") String username,
                        @RequestBody InfoAccount inAccount) {
                try {

                        Account account = accountService.findByUsername(username);
                        InfoAccount inAccounts = infoAccountService.findById_account(account.getId());
                        InfoAccount inCheck1 = infoAccountService.findByPhone(inAccount.getPhone());
                        InfoAccount inCheck2 = infoAccountService.findByIdCard(inAccount.getId_card());
                        if (inAccount.getPhone().length() != 10) {
                                return new ResponseEntity<>(
                                                new ResponObject("error", "Số điện thoại không hợp lệ!", null),
                                                HttpStatus.OK);
                        } else if (!inAccount.getPhone().substring(0, 1).equals("0")) {
                                return new ResponseEntity<>(
                                                new ResponObject("error", "Số điện thoại không hợp lệ!", null),
                                                HttpStatus.OK);
                        } else if (inCheck1 != null && inCheck1.getInfaccount().getId() != account.getId()) {
                                return new ResponseEntity<>(
                                                new ResponObject("error",
                                                                "Số điện thoại đã được sử dụng cho một tài khoản khác!",
                                                                null),
                                                HttpStatus.OK);
                        } else if (inAccount.getId_card().length() != 12) {
                                return new ResponseEntity<>(
                                                new ResponObject("error", "Số CCCD không hợp lệ!", null),
                                                HttpStatus.OK);
                        } else if (inCheck2 != null && inCheck2.getInfaccount().getId() != account.getId()) {
                                return new ResponseEntity<>(
                                                new ResponObject("error",
                                                                "Số CCCD đã được sử dụng cho một tài khoản khác!",
                                                                null),
                                                HttpStatus.OK);
                        } else {
                                inAccounts.setEmail(inAccount.getEmail());
                                inAccounts.setPhone(inAccount.getPhone());
                                inAccounts.setGender(inAccount.isGender());
                                inAccounts.setFullname(inAccount.getFullname());
                                inAccounts.setId_card(inAccount.getId_card());
                                infoAccountService.createProfile(inAccounts);
                                return new ResponseEntity<>(
                                                new ResponObject("success", "Cập nhật thông tin thành công!",
                                                                inAccounts),
                                                HttpStatus.OK);

                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return null;
        }

        @PostMapping("/changepass")
        public ResponseEntity<Map<String, Object>> changePass(@RequestBody Account account) {
                Map<String, Object> response = new HashMap<>();
                try {
                        Account accounts = accountService.findByUsername(account.getUsername());
                        if (account.getPassword().equals("")) {
                                response.put("message", "VUI LÒNG NHẬP MẬT KHẨU CŨ!");
                        } else if (account.getPassword().length() < 6) {
                                response.put("message", "MẬT KHẨU QUÁ NGẮN!");
                        } else {
                                accounts.setPassword(account.getPassword());
                                accountService.changePass(accounts);
                                response.put("success", true);
                                response.put("message", "ĐỔI MẬT KHẨU THÀNH CÔNG!");
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                        response.put("message", "LỖI THAY ĐỔI MẬT KHẨU");
                }
                return ResponseEntity.ok(response);
        }

        @PostMapping("/saleregis/{username}/{shop}")
        public ResponseEntity<Map<String, Object>> saleRegis(@PathVariable("username") String username,
                        @PathVariable("shop") String shop_name, @RequestBody AddressShop address) {
                Map<String, Object> response = new HashMap<>();
                LocalDate localDate = LocalDate.now();
                Date date = java.sql.Date.valueOf(localDate);
                try {
                        Account accounts = accountService.findByUsername(username);
                        Shop shops = shopService.existByAccount(accounts.getId());
                        if (shops != null) {
                                response.put("message",
                                                "BẠN ĐÃ GỬI 1 YÊU CẦU ĐĂNG KÝ LÊN HỆ THỐNG, VUI LÒNG CHỜ PHẢN HỒI TỪ CHÚNG TÔI ĐỂ TIẾP TỤC!");
                        } else {
                                Shop shop = new Shop();
                                // Create shop
                                Account account = accountService.findByUsername(username);
                                shop.setAccountShop(account);
                                shop.setShop_name(shop_name);
                                shop.setCreate_date(date);
                                shop.setStatus(0);
                                shopService.createShop(shop);
                                // Create shop address
                                address.setShopAddress(shop);
                                addressService.createAddressShop(address);
                                response.put("success", true);
                                response.put("message", "GỬI YÊU CẦU THÀNH CÔNG, VUI LÒNG CHỜ XÉT DUYỆT!");
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                        response.put("message", "LỖI ĐĂNG KÝ BÁN HÀNG");
                }
                return ResponseEntity.ok(response);
        }

        @PostMapping("/createAddress/{username}")
        public ResponseEntity<ResponObject> createAddressAccount(@PathVariable("username") String username,
                        @RequestBody AddressAccount addressAccount) {
                try {
                        Account account = accountService.findByUsername(username);
                        List<AddressAccount> setStatusAddress = addressAccountService
                                        .findAllAddressAccount(account.getId());
                        for (AddressAccount address : setStatusAddress) {
                                address.setStatus(false);
                                addressAccountService.save(address);
                        }
                        addressAccount.setAddressaccount(account);
                        addressAccount.setStatus(true);
                        addressAccountService.save(addressAccount);
                        List<AddressAccount> listAddress = addressAccountService.findAllAddressAccount(account.getId());
                        return new ResponseEntity<>(
                                        new ResponObject("success", "Thêm mới địa chỉ thành công!", listAddress),
                                        HttpStatus.CREATED);
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return null;
        }

        @PostMapping("/useAddress/{username}/{idAddress}")
        public ResponseEntity<ResponObject> useAddress(@PathVariable("username") String username,
                        @PathVariable("idAddress") int idAddress) {
                try {
                        Account account = accountService.findByUsername(username);
                        List<AddressAccount> setStatusAddress = addressAccountService
                                        .findAllAddressAccount(account.getId());
                        for (AddressAccount address : setStatusAddress) {
                                address.setStatus(false);
                                addressAccountService.save(address);
                        }
                        AddressAccount addressAccount = addressAccountService.findById(idAddress);
                        addressAccount.setAddressaccount(account);
                        addressAccount.setStatus(true);
                        addressAccountService.save(addressAccount);
                        List<AddressAccount> listAddress = addressAccountService.findAllAddressAccount(account.getId());
                        return new ResponseEntity<>(
                                        new ResponObject("success", "Sử dụng địa chỉ mới thành công!", listAddress),
                                        HttpStatus.CREATED);
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return null;
        }

        @PostMapping("/deleteAddress/{username}/{idAddress}")
        public ResponseEntity<ResponObject> deleteAddressAccount(@PathVariable("username") String username,
                        @PathVariable("idAddress") int idAddress) {
                try {
                        Account account = accountService.findByUsername(username);
                        addressAccountService.delete(idAddress);
                        List<AddressAccount> listAddress = addressAccountService.findAllAddressAccount(account.getId());
                        return new ResponseEntity<>(
                                        new ResponObject("success", "Xóa địa chỉ thành công!", listAddress),
                                        HttpStatus.CREATED);
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return null;
        }

        @PostMapping("/updateAddress/{username}/{idAddress}")
        public ResponseEntity<ResponObject> updateAddressAccount(@PathVariable("username") String username,
                        @PathVariable("idAddress") int idAddress,@RequestBody AddressAccount addressUpdate) {
                try {
                        Account account = accountService.findByUsername(username);
                        AddressAccount addressAccount = addressAccountService.findById(idAddress);
                        addressAccount.setAddressaccount(account);
                        addressAccount.setCity(addressUpdate.getCity());
                        addressAccount.setDistrict(addressUpdate.getDistrict());
                        addressAccount.setWard(addressUpdate.getWard());
                        addressAccount.setAddress(addressUpdate.getAddress());
                        addressAccountService.save(addressAccount);
                        List<AddressAccount> listAddress = addressAccountService.findAllAddressAccount(account.getId());
                        return new ResponseEntity<>(
                                        new ResponObject("success", "Cập nhật thành công!", listAddress),
                                        HttpStatus.CREATED);
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return null;
        }
}
