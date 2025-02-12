package com.stwn.ecommerce_java.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserAddressRequest {
    @NotBlank(message = "address name is required")
    @Size(max = 100, message = "address must be at most 100 character")
    private String addressName;
    @NotBlank(message = "street address is required")
    @Size(max = 255, message = "street address must be at most 255 character")
    private String streetAddress;
    @NotBlank(message = "city is required")
    @Size(max = 100, message = "city must bet most 100 character")
    private String city;
    @NotBlank(message = "state is required")
    @Size(max = 100, message = "state must bet most 100 character")
    private String state;
    @NotBlank(message = "postal code is required")
    @Size(max = 20, message = "postal code must bet most 20 character")
    private String postalCode;
    @NotBlank(message = "country is required")
    @Size(max = 100, message = "country must bet most 100 character")
    private String country;
    private Boolean isDefault;
}
