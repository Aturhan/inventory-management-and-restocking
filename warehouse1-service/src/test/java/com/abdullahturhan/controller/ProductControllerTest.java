package com.abdullahturhan.controller;

import com.abdullahturhan.dto.ProductDto;
import com.abdullahturhan.dto.TakeOutProductDto;
import com.abdullahturhan.exception.ProductNotFoundException;
import com.abdullahturhan.exception.QuantityNotEnoughException;
import com.abdullahturhan.model.Product;
import com.abdullahturhan.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductControllerTest {
    @Mock
    private ProductService productService;
    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }


    @Test
    void itShouldFindProducts_WhenExistsCategory() throws Exception {
        // given
        String category = "Electronics";
        ProductDto product1 = ProductDto.builder()
                .name("Product 1")
                .category("Electronics")
                .price(100.0)
                .quantity(100)
                .thresholdValue(100)
                .build();

        ProductDto product2 = ProductDto.builder()
                .name("Product 2")
                .category("Electronics")
                .price(100.0)
                .quantity(100)
                .thresholdValue(100)
                .build();
        List<ProductDto> productsList = List.of(product1, product2);
        when(productService.getProductsByCategory(category)).thenReturn(productsList);

        // when
        when(productService.getProductsByCategory(category)).thenReturn(productsList);



        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/warehouse1/category")
                        .param("category",category)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(category))
                .andExpect(MockMvcResultMatchers.status().isFound());

    }

    @Test
    void itShould_addProduct_WhenProductDtoValid() throws Exception {
        // given
        ProductDto product = ProductDto.builder()
                .name("Product")
                .category("Electronics")
                .price(100.0)
                .quantity(100)
                .thresholdValue(100)
                .build();

        // when
        doNothing().when(productService).addProduct(any(ProductDto.class));

        ObjectMapper objectMapper = new ObjectMapper();
        String productDtoJson = objectMapper.writeValueAsString(product);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/warehouse1/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productDtoJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        
    }

    @Test
    void takeOut() throws Exception {
        // given
        TakeOutProductDto productDto = TakeOutProductDto.builder()
                .productId(UUID.randomUUID().toString())
                .quantity(100)
                .build();

        // when
        doNothing().when(productService).takeOutProduct(productDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String productDtoJson = objectMapper.writeValueAsString(productDto);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/warehouse1/takeout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productDtoJson)
                ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Disabled
    void updateProductQuantity() {
    }
}