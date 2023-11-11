package com.abdullahturhan.service;

import com.abdullahturhan.dto.ProductDto;
import com.abdullahturhan.dto.ProductRestockDto;
import com.abdullahturhan.dto.TakeOutProductDto;
import com.abdullahturhan.exception.ProductNotFoundException;
import com.abdullahturhan.exception.QuantityNotEnoughException;
import com.abdullahturhan.model.Product;
import com.abdullahturhan.producer.KafkaProducer;
import com.abdullahturhan.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {
    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private KafkaProducer kafkaProducer;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void itShouldFindProducts_WhenExistsCategory() throws ProductNotFoundException {
        // given
        String category ="Electronics";
        Product product = Product.builder()
                .id(UUID.randomUUID().toString())
                .name("Product 1")
                .thresholdValue(100)
                .category("Electronics")
                .quantity(100)
                .price(1000.0)
                .createdAt(LocalDateTime.now())
                .build();
        Product product2 = Product.builder()
                .id(UUID.randomUUID().toString())
                .name("Product 2")
                .thresholdValue(100)
                .category("Electronics")
                .quantity(100)
                .price(1000.0)
                .createdAt(LocalDateTime.now())
                .build();

        List<Product> productList = List.of(product,product2);

        // when
        when(productRepository.findByCategory(category)).thenReturn(Optional.of(productList));
        List<ProductDto> result = productService.getProductsByCategory(category);

        // assert
        assertFalse(result.isEmpty());
    }

    @Test
    void itShouldAddProduct_WhenValid() {
        // given
        ProductDto product = ProductDto.builder()
                .name("Product 1")
                .thresholdValue(100)
                .category("Electronics")
                .quantity(100)
                .price(1000.0)
                .build();

        // act
        productService.addProduct(product);

        // verify
        verify(productRepository,times(1)).save(any());
    }

    @Test
    void itShouldTakeOutProduct_WhenProductExists() throws QuantityNotEnoughException, ProductNotFoundException {
        // given
        String id = UUID.randomUUID().toString();
        TakeOutProductDto takeOutProductDto = TakeOutProductDto.builder()
                .productId(id)
                .quantity(100)
                .build();

        Product product = Product.builder()
                .name("Product")
                .id(id)
                .createdAt(LocalDateTime.now())
                .thresholdValue(100)
                .quantity(200)
                .category("Electronics")
                .build();

        // when
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        doNothing().when(kafkaProducer).publish(anyString(), any());
        productService.takeOutProduct(takeOutProductDto);

        // Assert
        assertEquals(100, product.getQuantity());
        verify(productRepository, times(1)).save(product);
        verify(kafkaProducer, times(1)).publish(anyString(), any());

    }

    @Test
    void itShouldUpdateProductQuantity_WhenProductExists() throws ProductNotFoundException {
        // given
        ProductRestockDto productRestockDto = ProductRestockDto.builder()
                .name("Product")
                .quantity(100)
                .build();

        Product product = Product.builder()
                .name("Product")
                .id(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .thresholdValue(100)
                .quantity(200)
                .category("Electronics")
                .createdAt(LocalDateTime.now())
                .build();

        // when
        when(productRepository.findByName(any())).thenReturn(Optional.of(product));
        productService.updateProductQuantity(productRestockDto);

        // assert
        assertEquals(300, product.getQuantity());
        verify(productRepository, times(1)).save(product);
    }
}