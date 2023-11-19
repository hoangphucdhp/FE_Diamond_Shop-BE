package com.example.DATN_API.Controller;

import com.example.DATN_API.Entity.Account;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        
        // @PostMapping("/login")
	// public ResponseEntity<ResponObject> login(@RequestBody Account a) {
	// 	PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
	// 	Account account = accountService.findByUsername(a.getUsername());
	// 	if (account != null) {
	// 		if (passwordEncoder.matches(a.getPassword(), account.getPassword())) {
	// 			return new ResponseEntity<>(new ResponObject("success", "OK VÀO", account),
	// 					HttpStatus.CREATED);
	// 		} else {
	// 			return new ResponseEntity<>(new ResponObject("error", "SAI PASS", null),
	// 					HttpStatus.OK);
	// 		}
	// 	} else {
	// 		return new ResponseEntity<>(new ResponObject("error", "TÀI KHOẢN KHÔNG TỒN TẠI!", null),
	// 				HttpStatus.OK);
	// 	}
	// }

	// @PostMapping("/create")
	// public ResponseEntity<ResponObject> register(@RequestBody Account a) {
	// 	try {
	// 		// CREATE ACCOUNT
	// 		a.setCreatedate(new Date());
	// 		a.setStatus(false);
	// 		accountService.createAccount(a);
	// 		// CREATE ACCOUNT ROLE
	// 		Account account = accountService.findByUsername(a.getUsername());
	// 		Role role = new Role();
	// 		role.setId(1);
	// 		RoleAccount roleAccount = new RoleAccount();
	// 		roleAccount.setAccount(account);
	// 		roleAccount.setRole(role);
	// 		roleAccountService.createRoleAccount(roleAccount);
	// 		return new ResponseEntity<>(new ResponObject("success", "OK CREATE", a), HttpStatus.CREATED);
	// 	} catch (Exception e) {
	// 		e.printStackTrace();
	// 		return new ResponseEntity<>(new ResponObject("error", "FAIL", null), HttpStatus.OK);
	// 	}
	// }

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
        public ResponseEntity<Map<String, Object>> login(@RequestBody Account account) {
                Map<String, Object> response = new HashMap<>();
                try {
                        Account accounts = accountService.findByUsername(account.getUsername());
                        if (account.getUsername().equals(accounts.getUsername())
                                        && account.getPassword().equals(accounts.getPassword())
                                        && accounts.isStatus() == false) {
                                response.put("success", true);
                                response.put("message", "ĐĂNG NHẬP THÀNH CÔNG!");
                                response.put("data", account);
                        } else {
                                if (account.getUsername().equals(accounts.getUsername())
                                                && !account.getPassword().equals(accounts.getPassword())) {
                                        response.put("message", "MẬT KHẨU KHÔNG HỢP LỆ!");
                                } else if (accounts.isStatus() == true) {
                                        response.put("message",
                                                        "TÀI KHOẢN BẠN ĐĂNG NHẬP HIỆN TẠI ĐANG BỊ KHÓA, VUI LÒNG LIÊN HỆ CHO QUẢN TRỊ VIÊN NẾU GẶP VẪN ĐỀ!");
                                } else {
                                        response.put("message", "ĐÚNG MỖI CÁI NỊT!");
                                }
                                response.put("success", false);
                                response.put("data", account);
                        }
                } catch (Exception e) {
                        response.put("message", "TÊN ĐĂNG NHẬP KHÔNG HỢP LỆ!");
                        e.printStackTrace();
                }
                return ResponseEntity.ok(response);
        }

        @PostMapping("/{email}")
        public ResponseEntity<Map<String, Object>> codeValidate(@PathVariable("email") String email) {
                Map<String, Object> response = new HashMap<>();
                try {
                        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                                        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
                        String charSet = "1234567890";
                        // Begin validate Email
                        if (Pattern.compile(regexPattern).matcher(email).matches() != true) {
                                response.put("message", "EMAIL KHÔNG HỢP LỆ!");

                        } else if (infoAccountService.findByEmail(email) != null) {
                                response.put("message", "EMAIL NÀY ĐÃ ĐƯỢC SỬ DỤNG CHO MỘT TÀI KHOẢN KHÁC!");
                        } else {
                                String code = "";
                                Random rand = new Random();
                                int len = 8;
                                for (int i = 0; i < len; i++) {
                                        code += charSet.charAt(rand.nextInt(charSet.length()));
                                }
                                MailInformation mail = new MailInformation();
                                mail.setTo(email);
                                mail.setSubject("MÃ XÁC NHẬN");
                                mail.setBody("<html><body>" + "<p>Xin chào " + email + ",</p>"
                                                + "<p>Chúng tôi nhận được yêu cầu đăng ký tài khoản FE Shop của bạn.</p>"
                                                + "<p>Vui lòng không chia sẽ mã này cho bất cứ ai:" + "<h3>" + code
                                                + "</h3>" + "</p>"
                                                + "<p>Trân trọng,</p>"
                                                + "<p>Bạn có thắc mắc? Liên hệ chúng tôi tại đây khuong8177@gmail.com.</p>"
                                                + "</body></html>");
                                mailServiceImplement.send(mail);
                                response.put("success", true);
                                response.put("code", code);
                                response.put("message", "MÃ OTP ĐÃ ĐƯỢC GỬI QUA MAIL CỦA BẠN");
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
                        Account createAcc = new Account();
                        RoleAccount roleAcc = new RoleAccount();
                        Role role = new Role();
                        InfoAccount inAcc = new InfoAccount();
                        LocalDate localDate = LocalDate.now();
                        Date date = java.sql.Date.valueOf(localDate);
                        // Begin validate
                        if (accounts != null) {
                                response.put("message", " TÊN TÀI KHOẢN ĐÃ TỒN TẠI!");
                        } else if (account.getUsername().length() < 6) {
                                response.put("message", "TÊN TÀI KHOẢN QUÁ NGẮN!");
                        } else if (account.getPassword().length() < 6) {
                                response.put("message", "MẬT KHẨU QUÁ NGẮN!");
                        } else if (infoAccountService.findByEmail(email) != null) {
                                response.put("message", "EMAIL NÀY ĐÃ ĐƯỢC SỬ DỤNG CHO MỘT TÀI KHOẢN KHÁC!");
                        } else {
                                // Account
                                createAcc.setUsername(account.getUsername());
                                createAcc.setPassword(account.getPassword());
                                createAcc.setCreate_date(date);
                                createAcc.setStatus(false);
                                // Begin create new Account
                                accountService.createAccount(createAcc);
                                // Crate role
                                role.setId(1);
                                roleAcc.setAccount_role(createAcc);
                                roleAcc.setRole(role);
                                roleAccService.createRoleAcc(roleAcc);
                                // Default info
                                Account findAcc = accountService.findByUsername(account.getUsername());
                                inAcc.setFullname(findAcc.getUsername());
                                inAcc.setEmail(email);
                                inAcc.setInfaccount(findAcc);
                                infoAccountService.createProfile(inAcc);
                                response.put("success", true);
                                response.put("message", "ĐĂNG KÝ THÀNH CÔNG!");
                                response.put("data", createAcc);
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
        public ResponseEntity<Map<String, Object>> updateProfile(@PathVariable("username") String username,
                        @RequestBody InfoAccount inAccount) {
                Map<String, Object> response = new HashMap<>();
                String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
                try {

                        Account account = accountService.findByUsername(username);
                        InfoAccount inAccounts = infoAccountService.findById_account(account.getId());
                        InfoAccount inCheck = infoAccountService.findByEmail(inAccount.getEmail());
                        InfoAccount inCheck1 = infoAccountService.findByPhone(inAccount.getPhone());
                        InfoAccount inCheck2 = infoAccountService.findByIdCard(inAccount.getId_card());
                        if (inAccount.getPhone().length() != 10) {
                                response.put("message", "SỐ ĐIỆN THOẠI KHÔNG HỢP LỆ!");
                        } else if (!inAccount.getPhone().substring(0, 1).equals("0")) {
                                response.put("message", "SỐ ĐIỆN THOẠI KHÔNG HỢP LỆ!");
                        } else if (inCheck1 != null && inCheck1.getInfaccount().getId() != account.getId()) {
                                response.put("message", "SỐ ĐIỆN THOẠI NÀY ĐÃ ĐƯỢC SỬ DỤNG CHO MỘT TÀI KHOẢN KHÁC!");
                        } else if (inAccount.getId_card().length() != 12) {
                                response.put("message", "SỐ CĂN CƯỚC CÔNG DÂN KHÔNG HỢP LỆ!");
                        } else if (inCheck2 != null && inCheck2.getInfaccount().getId() != account.getId()) {
                                response.put("message",
                                                "SỐ CĂN CƯỚC CÔNG DÂN NÀY ĐÃ ĐƯỢC SỬ DỤNG CHO MỘT TÀI KHOẢN KHÁC!");
                        } else if (Pattern.compile(regexPattern).matcher(inAccount.getEmail()).matches() != true) {
                                response.put("message", "EMAIL KHÔNG HỢP LỆ!");
                        } else if (inCheck != null && inCheck.getInfaccount().getId() != account.getId()) {
                                response.put("message", "EMAIL NÀY ĐÃ ĐƯỢC SỬ DỤNG CHO MỘT TÀI KHOẢN KHÁC!");
                        } else {
                                int phone = Integer.parseInt(inAccount.getPhone());
                                inAccount.setInfaccount(account);
                                inAccount.setId(inAccounts.getId());
                                infoAccountService.createProfile(inAccount);
                                response.put("success", true);
                                response.put("message", "CẬP NHẬT THÔNG TIN THÀNH CÔNG!");
                        }
                } catch (NumberFormatException e) {
                        response.put("message",
                                        "SAI ĐỊNH DẠNG SỐ ĐIỆN THOẠI, VUI LÒNG CHỈ NHẬP CÁC SỐ 0 - 9 VÀ KHÔNG NHẬP QUÁ 10 SỐ!");
                        e.printStackTrace();
                } catch (Exception e) {
                        response.put("message", "Lỗi CẬP NHẬT PROFILE!");
                        e.printStackTrace();
                }
                return ResponseEntity.ok(response);
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
}
