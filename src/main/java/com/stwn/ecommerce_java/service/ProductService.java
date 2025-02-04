package com.stwn.ecommerce_java.service;

import com.stwn.ecommerce_java.entity.Category;
import com.stwn.ecommerce_java.entity.Product;
import com.stwn.ecommerce_java.model.CategoryResponse;
import com.stwn.ecommerce_java.model.ProductRequest;
import com.stwn.ecommerce_java.model.ProductResponse;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductResponse> findAll();
   ProductResponse findById(Long id);
    ProductResponse create(ProductRequest request);
    ProductResponse update(Long id, ProductRequest request);
    void delete(Long id);
}
