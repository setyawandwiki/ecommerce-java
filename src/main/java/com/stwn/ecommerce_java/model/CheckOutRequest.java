package com.stwn.ecommerce_java.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CheckOutRequest {
    private Long userId;
    @NotEmpty(message = "at least 1 cart item must be selected for checkout")
    @Size(min = 1, message = "at least one cart item must be selected")
    private List<Long> selectedCartItemIds;
    @NotNull(message = "user addres id required")
    private Long userAddressId;
}
