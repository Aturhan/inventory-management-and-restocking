package com.abdullahturhan.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    @NotBlank
    private String name;
    @NotNull
    private Integer quantity;
    @NotNull
    private Integer thresholdValue;
    @NotBlank
    private String category;
    @NotNull
    private Double price;
}
