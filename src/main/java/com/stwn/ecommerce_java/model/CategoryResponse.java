package com.stwn.ecommerce_java.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.stwn.ecommerce_java.entity.Category;
import com.stwn.ecommerce_java.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
/*ubah responnya menjadi snake case*/
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CategoryResponse {
    private Long categoryId;
    private String name;

    public static CategoryResponse fromCategory(Category category){
        return CategoryResponse.builder()
                .categoryId(category.getCategoryId())
                .name(category.getName())
                .build();
    }
}
