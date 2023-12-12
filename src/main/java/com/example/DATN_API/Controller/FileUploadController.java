package com.example.DATN_API.Controller;

import com.example.DATN_API.Entity.ImageProduct;
import com.example.DATN_API.Entity.Product;
import com.example.DATN_API.Entity.ResponObject;
import com.example.DATN_API.Service.IStorageSerivce;
import com.example.DATN_API.Service.ImageProductService;
import com.example.DATN_API.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@CrossOrigin("*")
@RequestMapping(path = "/api/uploadImageProduct")
public class FileUploadController {
    @Autowired
    IStorageSerivce iStorageSerivce;
    @Autowired
    ImageProductService imageProductService;
    @Autowired
    ProductService productService;

    @GetMapping()
    public ResponseEntity<List<ImageProduct>> getAll() {
        return new ResponseEntity<>(imageProductService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{fileName:.+}")
    public ResponseEntity<byte[]> readDetailFile(@PathVariable("fileName") String fileName) {
        try {
            byte[] bytes = iStorageSerivce.readFileContent(fileName);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
        } catch (Exception exception) {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping()
    public ResponseEntity<ResponObject> uploadFile(@RequestParam("images") List<String> file,
                                                   @RequestParam("idProduct") int idProduct) {

        Product product = productService.findById((idProduct));
        for (String item : file
        ) {
            ImageProduct ImageProduct = new ImageProduct();
            ImageProduct.setProduct_image(product);
            ImageProduct.setUrl(item);
            imageProductService.createImageProduct(ImageProduct);
        }
        return new ResponseEntity<>(new ResponObject("SUCCESS", "Image has been added.", "name"),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public void deleteImage(@PathVariable("id") int idImage) {
        imageProductService.deleteImageProduct(idImage);
    }
}
