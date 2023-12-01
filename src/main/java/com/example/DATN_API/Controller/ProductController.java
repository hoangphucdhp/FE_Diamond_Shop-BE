package com.example.DATN_API.Controller;

import com.example.DATN_API.Entity.*;
import com.example.DATN_API.Service.CategoryService;
import com.example.DATN_API.Service.ProductService;
import com.example.DATN_API.Service.ShopService;
import com.example.DATN_API.Service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

@RestController
@RequestMapping("/api/product")
@CrossOrigin("*")
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    ShopService shopService;
    @Autowired
    StorageService storageService;
    @Autowired
    CategoryService categoryService;

    @GetMapping("/findAll")
    public ResponseEntity<ResponObject> findAll(@RequestParam("offset") Optional<Integer> offSet,
                                                @RequestParam("sizePage") Optional<Integer> sizePage,
                                                @RequestParam("sort") Optional<String> sort,
                                                @RequestParam("status") Optional<Integer> status) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponObject(
                "SUCCESS", "FIND ALL PRODUCT", productService.getPageProduct(status, offSet, sizePage, sort)
        ));
    }

    @GetMapping()
    public ResponseEntity<List<Product>> getAll() {
        return new ResponseEntity<>(productService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/getByShop")
    public ResponseEntity<Page<Product>> getAllbyShop(@RequestParam("offset") Optional<Integer> offSet,
                                                      @RequestParam("sizePage") Optional<Integer> sizePage,
                                                      @RequestParam("sort") Optional<String> sort, @RequestParam("key") Optional<String> key,
                                                      @RequestParam("keyword") Optional<String> keyword, @RequestParam("shop") Optional<Integer> idshop) {
        return new ResponseEntity<>(productService.findAllbyShop(offSet, sizePage, sort, key, keyword, idshop), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Product> findById(@PathVariable Integer id) {
        if (productService.existsById(id)) {
            return new ResponseEntity<>(productService.findById(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/shop/{shop}")
    public ResponseEntity<ResponObject> create(@PathVariable("shop") int shop, @RequestBody Product product) {
        Shop shop2 = shopService.findById(shop);
        product.setShop(shop2);
        Product productnew = productService.createProduct(product);
        return new ResponseEntity<>(new ResponObject("success", "Thêm thành công.", productnew),
                HttpStatus.CREATED);
    }


    @PutMapping("{id}")
    public ResponseEntity<ResponObject> update(@PathVariable("id") Integer id, @RequestBody Product product) {
        if (!productService.existsById(id))
            return new ResponseEntity<>(
                    new ResponObject("error", "Sản phẩm : " + id + "không tồn tại.", product),
                    HttpStatus.NOT_FOUND);

        Product productnew = productService.updateProduct(id, product);
        return new ResponseEntity<>(new ResponObject("success", "Cập nhật thành công.", productnew),
                HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ResponObject> delete(@PathVariable("id") Integer id) {
        if (!productService.existsById(id))
            return new ResponseEntity<>(new ResponObject("NOT_FOUND", "Product_id: " + id + " does not exists.", id),
                    HttpStatus.NOT_FOUND);
        Product product = productService.findById(id);
        product.setStatus(2);
        productService.updateProduct(id, product);
        return new ResponseEntity<>(new ResponObject("success", "Xóa thành công.", id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponObject> delete2(@PathVariable("id") Integer id) {
        if (productService.deleteProduct(id)) {
            return new ResponseEntity<>(new ResponObject("success", "Xóa thành công.", id), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponObject("error", "Xóa không thành công.", id), HttpStatus.OK);
    }

    // Storage
    @PostMapping("/createStorage/{product}")
    public ResponseEntity<ResponObject> createStorage(@PathVariable("product") Integer product,
                                                      @RequestBody Storage storage) {
        Product newProduct = productService.findById(product);
        storage.setProduct(newProduct);
        Storage storagesave = storageService.createStorage(storage);
        return new ResponseEntity<>(new ResponObject("success", "Thêm thành công.", storagesave),
                HttpStatus.CREATED);
    }

    @PutMapping("/updateStorage/{id}/{idProduct}")
    public ResponseEntity<ResponObject> updateStorage(@PathVariable("id") Integer id,
                                                      @PathVariable("idProduct") Integer idProduct, @RequestBody Storage storage) {
        Product newProduct = productService.findById(idProduct);
        storage.setProduct(newProduct);
        Storage storagesave = storageService.updateStorage(id, storage);
        return new ResponseEntity<>(new ResponObject("SUCCESS", "Storage has been added.", storagesave),
                HttpStatus.CREATED);
    }

    @PutMapping("/verify/{id}")
    public ResponseEntity<ResponObject> verifyProduct(@PathVariable("id") Integer id) {
        Product product = productService.findById(id);
        product.setStatus(1);
        productService.createProduct(product);
        return new ResponseEntity<>(new ResponObject("SUCCESS", "verify product succsess", product),
                HttpStatus.CREATED);
    }

    @PutMapping("/ban/{id}")
    public ResponseEntity<ResponObject> banProduct(@PathVariable("id") Integer id) {
        Product product = productService.findById(id);
        product.setStatus(2);
        productService.createProduct(product);
        return new ResponseEntity<>(new ResponObject("SUCCESS", "ban product succsess", product),
                HttpStatus.CREATED);
    }

    @GetMapping("/top10")
    public ResponseEntity<List<Object[]>> getTop10Products() {
        List<Object[]> top10Products = productService.getTop10Products();
        if (top10Products.isEmpty()) {
            // không có dữ liệu
            return ResponseEntity.noContent().build();
        } else {
            // có dữ liệu và trả về kết quả
            return ResponseEntity.ok(top10Products);
        }
    }

    // Hiển thị những sản phẩm tương tự theo categoryItem_product
    @GetMapping("/{id}/similar-products")
    public ResponseEntity<ResponObject> findSimilarProducts(@PathVariable("id") Integer id) {
        if (productService.existsById(id)) {
            List<Product> similarProducts = productService.findSimilarProducts(id);
            Object responseData = new Object[]{"similarProducts", similarProducts};
            return new ResponseEntity<>(new ResponObject("SUCCESS", "Similar products retrieved successfully.", responseData), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ResponObject("NOT_FOUND", "Product with id: " + id + " not found.", null), HttpStatus.NOT_FOUND);
    }

    //Admin
    @GetMapping("/getAll")
    public ResponseEntity<ResponObject> getAll(@RequestParam("offset") Optional<Integer> offSet,
                                               @RequestParam("sizePage") Optional<Integer> sizePage,
                                               @RequestParam("sort") Optional<String> sort,
                                               @RequestParam("sortType") Optional<String> sortType,
                                               @RequestParam("key") Optional<String> keyfind,
                                               @RequestParam("keyword") Optional<String> keyword) {
        Page<Product> accounts = productService.findAll(offSet, sizePage, sort, sortType, keyfind, keyword);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponObject(
                        "SUCCESS", "GET ALL ACCOUNT", accounts));

    }

    @PutMapping("/adminupdate/{id}")
    public ResponseEntity<ResponObject> AdminProduct(@PathVariable("id") Integer id) {
        Product product = productService.findById(id);
        product.setStatus(2);
        productService.createProduct(product);
        return new ResponseEntity<>(new ResponObject("SUCCESS", "ban product succsess", product),
                HttpStatus.CREATED);
    }

    @PutMapping("/adminupdatestatus/{id}")
    public ResponseEntity<ResponObject> AdminUpdateProduct(@PathVariable("id") Integer id, @RequestParam("status") Integer status) {
        return new ResponseEntity<>(new ResponObject("SUCCESS", "Cập nhật thành công", productService.adminUpdateStatus(id, status)),
                HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponObject> search(@RequestParam("key") Optional<String> key, @RequestParam("keyword") Optional<String> valueKeyword,
                                               @RequestParam("category") Optional<Integer> idCategoryItem, @RequestParam("shop") Optional<Integer> idshop, @RequestParam("offset") Optional<Integer> offSet,
                                               @RequestParam("sizePage") Optional<Integer> sizePage,
                                               @RequestParam("sort") Optional<String> sort, @RequestParam("sortType") Optional<String> sortType, @RequestParam("isActive") Optional<String> isCheck) {
        return new ResponseEntity<>(new ResponObject("SUCCESS", "Thành công", productService.searchBusiness(offSet, sizePage, sort, sortType, key, valueKeyword, idCategoryItem, idshop,isCheck)),
                HttpStatus.OK);
    }

    @GetMapping("/{id}/shop")
    public ResponseEntity<ResponObject> getShopByProduct(@PathVariable("id") Integer id) {
        Map<Integer, Object[]> listProducts = new HashMap<>();
        Shop shop = null;
        Object[] dataReturn = null;
        // CHECK ID PRODUCT .....
        for (Shop s : shopService.findAll()) {
            for (Product p : s.getProducts()) {
                if (p.getId() == id) {
                    shop = s;
                    break;
                }
            }
        }

        // GET LIST PRODUCT AT SHOP NO LOOP
        if (shop != null) {
            for (Product p : shop.getProducts()) {
                listProducts.put(p.getId(), new Object[]{p.getId(), p.getProduct_name(), p.getPrice(),
                        p.getCategoryItem_product(), p.getStatus(), p.getImage_product()});
            }
            dataReturn = new Object[]{shop.getId(), shop.getShop_name(), shop.getAddressShop(), listProducts, shop.getImage()};
        }

        if (listProducts.size() == 0) {
            return new ResponseEntity<>(new ResponObject("error", "Không có thông tin", null), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(new ResponObject("success", "GET LIST SUCCESS", dataReturn), HttpStatus.OK);
        }

    }

    @GetMapping("/exportProductsToExcel")
    public ResponseEntity<byte[]> exportProductsToExcel() {
        List<Product> productList = productService.findAll(); // Thay thế bằng phương thức lấy danh sách sản phẩm từ dịch vụ của bạn

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Products");

            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Product Name", "Price", "Create Date", "Description", "Status", "Category ID","Shop Id"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }


            int rowNum = 1;
            for (Product product : productList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(product.getId());
                row.createCell(1).setCellValue(product.getProduct_name());
                row.createCell(2).setCellValue(product.getPrice());
                row.createCell(3).setCellValue(product.getCreate_date().toString()); // Đây là ngày, cần xử lý định dạng sao cho phù hợp
                row.createCell(4).setCellValue(product.getDescription());
                row.createCell(5).setCellValue(product.getStatus());
                row.createCell(6).setCellValue(product.getCategoryItem_product().getId());
                row.createCell(7).setCellValue(product.getShop().getId());
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=products.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/importProductsFromExcel")
    public String importProductsFromExcel(@RequestParam("file") MultipartFile file) {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // Lấy sheet đầu tiên (index 0)

            Iterator<Row> iterator = sheet.iterator();
            List<Product> productList = new ArrayList<>();

            // Bỏ qua header row (nếu có)
            if (iterator.hasNext()) {
                iterator.next(); // Bỏ qua header row
            }

            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                Product product = new Product();
                product.setProduct_name(currentRow.getCell(1).getStringCellValue());
                product.setPrice(currentRow.getCell(2).getNumericCellValue());
                product.setCreate_date(new Date());
                product.setDescription(currentRow.getCell(4).getStringCellValue());
                product.setStatus(0);
                CategoryItem categoryItem = categoryService.findByIdCategoryItem((int) currentRow.getCell(6).getNumericCellValue());
                product.setCategoryItem_product(categoryItem);
                Shop shop=shopService.findById((int) currentRow.getCell(7).getNumericCellValue());
                product.setShop(shop);
                // Tiếp tục đặt các giá trị cho các trường khác tương ứng với cột trong file Excel
                productService.createProduct(product);
            }


            return "Import successful!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Import failed!";
        }
    }

}