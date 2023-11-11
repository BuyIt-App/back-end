package com.buyit.productservice.controller;

import com.buyit.productservice.Service.ProductService;
import com.buyit.productservice.dto.ProductRes;
import com.buyit.productservice.model.Product;
import com.buyit.productservice.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepo productRepo;

    @GetMapping("/all")
    public ResponseEntity<List<ProductRes>> getAllProducts() {
        List<ProductRes> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable long productId) {
        try{
        ProductRes product = productService.getById(productId);
            return ResponseEntity.ok(product);
        }catch (Exception e){
            return new ResponseEntity<>( e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{productId}/updateQuantity/{quantity}")
    public void updateProductQuantity(@PathVariable long productId, @PathVariable long quantity){
        productService.updateProductQuantity(productId, quantity);
    }

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @PutMapping("/update")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(product);
        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
       try{
           Product product = productService.deleteProduct(productId);
           return ResponseEntity.ok(product);
       }catch (Exception e){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product Not found");
       }
    }


}
