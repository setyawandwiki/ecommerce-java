package com.stwn.ecommerce_java.controller;

import com.stwn.ecommerce_java.model.ProductRequest;
import com.stwn.ecommerce_java.model.ProductResponse;
import com.stwn.ecommerce_java.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(path = "/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findProductById(@PathVariable("id") Long productId){
        ProductResponse productResponse = productService.findById(productId);
        return ResponseEntity.ok(productResponse);
    }
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(){
        return ResponseEntity.ok(productService.findAll());
    }
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid ProductRequest request){
        ProductResponse productResponse = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }
    @PutMapping(path = "/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable("id") Long productId,
                                                         @RequestBody @Valid ProductRequest request){
        return ResponseEntity.ok(productService.update(productId, request));
    }
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long productId){
        productService.delete(productId);
        return ResponseEntity.noContent().build();
    }
}
