package com.abdullahturhan.controller;

import com.abdullahturhan.dto.ProductDto;
import com.abdullahturhan.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;



class ProductControllerTest {
    @InjectMocks
    private ProductController productController;
    @Mock
    private ProductService productService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void itShouldSaveProduct_WhenValidProductDto() throws Exception {
        // given
        ProductDto request = ProductDto.builder()
                .name("Iphone 11")
                .category("Electronics")
                .quantity(2000)
                .thresholdValue(200)
                .price(25000.0)
                .build();

        // when
        doNothing().when(productService).addProduct(any(ProductDto.class));

        ObjectMapper objectMapper = new ObjectMapper();
        String productDtoJson = objectMapper.writeValueAsString(request);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/central-warehouse/insert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productDtoJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());

    }

}