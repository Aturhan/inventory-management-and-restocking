package com.abdullahturhan.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    @NotBlank
    private String name;
    @NotNull
    @Min(0)
    private Integer quantity;
    @NotNull
    @Min(0)
    private Integer thresholdValue;
    @NotNull
    @Min(0)
    private Double price;
    @NotBlank
    private String category;
}
