package com.stwn.ecommerce_java.service;

import com.stwn.ecommerce_java.common.errors.ResourceNotFoundException;
import com.stwn.ecommerce_java.entity.Category;
import com.stwn.ecommerce_java.entity.Product;
import com.stwn.ecommerce_java.entity.ProductCategory;
import com.stwn.ecommerce_java.model.CategoryResponse;
import com.stwn.ecommerce_java.model.PaginatedProductResponse;
import com.stwn.ecommerce_java.model.ProductRequest;
import com.stwn.ecommerce_java.model.ProductResponse;
import com.stwn.ecommerce_java.repository.CategoryRepository;
import com.stwn.ecommerce_java.repository.ProductCategoryRepository;
import com.stwn.ecommerce_java.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;
    @Override
    public List<ProductResponse> findAll() {
        return productRepository.findAll()
                .stream().map(product -> {
                    List<CategoryResponse> productCategories = getProductCategories(product.getId());
                    return ProductResponse.fromProductResponse(product, productCategories);
                }).toList();
    }

    @Override
    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id : " + id));
        List<CategoryResponse> categoryResponses = getProductCategories(id);
        return ProductResponse.fromProductResponse(product, categoryResponses);
    }

    @Override
    public Page<ProductResponse> findByPage(Pageable pageable) {
        return productRepository.findByPageable(pageable)
                /*rubah data dari product ke product response*/
                .map(product -> {
                    List<CategoryResponse> productCategories = getProductCategories(product.getId());
                    return ProductResponse.fromProductResponse(product, productCategories);
                });
    }

    @Override
    @Transactional
    public ProductResponse create(ProductRequest request) {
        List<Category> categories = getCategoriesByIds(request.getCategoryIds());
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .userId(request.getUser().getUserId())
                .stockQuantity(request.getStockQuantity())
                .weight(request.getWeight())
                .build();

        Product createdProduct = productRepository.save(product);
        List<ProductCategory> productCategories = categories.stream()
                .map(category -> {
                    ProductCategory productCategory = ProductCategory.builder().build();
                    ProductCategory.ProductCategoryId productCategory1 = new ProductCategory.ProductCategoryId();
                    productCategory1.setCategoryId(category.getCategoryId());
                    productCategory1.setProductId(createdProduct.getId());
                    productCategory.setId(productCategory1);
                    return productCategory;
                }).toList();
        productCategoryRepository.saveAll(productCategories);
        List<CategoryResponse> categoryResponseList = categories.stream().map(CategoryResponse::fromCategory).toList();
        return ProductResponse.fromProductResponse(createdProduct, categoryResponseList);
    }

    @Override
    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("product not found with id : " + id));
        List<Category> categories = getCategoriesByIds(request.getCategoryIds());
        existingProduct.setName(request.getName());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setStockQuantity(request.getStockQuantity());
        existingProduct.setWeight(request.getWeight());

        Product save = productRepository.save(existingProduct);

        List<ProductCategory> existingProductCategories = productCategoryRepository.findCategoriesByProductId(id);
        productCategoryRepository.deleteAll(existingProductCategories);

        List<ProductCategory> productCategories = categories.stream()
                .map(category -> {
                    ProductCategory productCategory = ProductCategory.builder().build();
                    ProductCategory.ProductCategoryId productCategory1 = new ProductCategory.ProductCategoryId();
                    productCategory1.setCategoryId(category.getCategoryId());
                    productCategory1.setProductId(id);
                    productCategory.setId(productCategory1);
                    return productCategory;
                }).toList();
        productCategoryRepository.saveAll(productCategories);
        List<CategoryResponse> categoryResponseList = categories.stream().map(CategoryResponse::fromCategory).toList();
        return ProductResponse.fromProductResponse(save, categoryResponseList);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id : " + id));
        List<ProductCategory> productCategories = productCategoryRepository.findCategoriesByProductId(id);
        productCategoryRepository.deleteAll(productCategories);
        productRepository.delete(product);
    }

    @Override
    public Page<ProductResponse> findByNameAndPageable(String name, Pageable pageable) {
        name = "%" + name.toLowerCase() + "%";
        return productRepository.findByNamePageable(name, pageable)
                /*rubah data dari product ke product response*/
                .map(product -> {
                    List<CategoryResponse> productCategories = getProductCategories(product.getId());
                    return ProductResponse.fromProductResponse(product, productCategories);
                });
    }

    @Override
    public PaginatedProductResponse convertProductPage(Page<ProductResponse> productPage) {
        return PaginatedProductResponse.builder()
                .data(productPage.getContent())
                .pageNo(productPage.getNumber())
                .pageSize(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .last(productPage.isLast())
                .build();
    }

    private List<Category> getCategoriesByIds(List<Long> categoryIds) {
        return categoryIds.stream()
                .map(value ->categoryRepository.findById(value)
                        .orElseThrow(()-> new ResourceNotFoundException("Category not found for id : " + value)))
                .toList();
    }

    private List<CategoryResponse> getProductCategories(Long productId) {
        List<ProductCategory> productCategories = productCategoryRepository.findCategoriesByProductId(productId);
        List<Long> categoriIds = productCategories.stream()
                .map(productCategory -> productCategory.getId().getCategoryId()).toList();
        return categoryRepository.findAllById(categoriIds)
                .stream().map(CategoryResponse::fromCategory).toList();
    }
}
