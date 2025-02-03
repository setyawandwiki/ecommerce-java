package com.stwn.ecommerce_java.controller;

import com.stwn.ecommerce_java.model.ProductRequest;
import com.stwn.ecommerce_java.model.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(path = "/products")
public class ProductController {
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findProductById(@PathVariable("id") Long productId){
        return ResponseEntity.ok(
                ProductResponse.builder()
                        .name("product" + productId)
                        .price(BigDecimal.ONE)
                        .description("deskripsi product")
                        .build()
        );
    }
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(){
        return ResponseEntity.ok(
                List.of(ProductResponse.builder()
                                .name("product 1")
                                .description("deskripsi produk")
                                .price(BigDecimal.ONE)
                        .build(),
                        ProductResponse.builder()
                                .name("product 2")
                                .description("deskripsi produk 2")
                                .price(BigDecimal.TWO)
                                .build()
                )
        );
    }
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request){
        return ResponseEntity.ok(
                ProductResponse.builder()
                        .name(request.getName())
                        .price(request.getPrice())
                        .description(request.getDescription())
                        .build()
        );
    }
    @PutMapping(path = "/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable("id") Long productId,
                                                         @RequestBody ProductRequest request){
        return ResponseEntity.ok(
                ProductResponse.builder()
                        .name(request.getName())
                        .price(request.getPrice())
                        .description(request.getDescription())
                        .build()
        );
    }
}
