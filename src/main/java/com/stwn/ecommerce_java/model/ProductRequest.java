package com.stwn.ecommerce_java.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {
    @NotBlank(message = "nama tidak boleh kosong")
    @Size(min = 2, max = 100, message = "nama produk harus antara 2 - 100")
    private String name;
    @Positive(message = "harga harus lebih besar dari 0")
    @Digits(integer = 10, fraction = 2, message = "harga memiliki maksimal 10 digit dan 2 angka dibelakang koma")
    private BigDecimal price;
    @NotNull(message = "deskripsi produk tidak boleh null")
    @Size(max = 100, message = "deskripsi produk tidak boleh lebih dari 100 karakter")
    private String description;
}
