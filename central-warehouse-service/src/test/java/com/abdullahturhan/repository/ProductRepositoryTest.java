package com.abdullahturhan.repository;

import com.abdullahturhan.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    void itShouldCheck_IfProductExistsName() {
        // given
        Product product = Product.builder()
                .name("Name")
                .category("Category")
                .price(200.0)
                .quantity(100)
                .thresholdValue(100)
                .build();
        productRepository.save(product);
        // when
        Product expected = productRepository.findByName("Name");

        // then
        assertThat(expected).isEqualTo(product);
    }

    @Test
    void itShouldCheck_IfProductDoesNotExistsName() {
        // given
        Product product = Product.builder()
                .name("Name1")
                .category("Category")
                .price(200.0)
                .quantity(100)
                .thresholdValue(100)
                .build();
        productRepository.save(product);
        // when
        Product expected = productRepository.findByName("Name2");

        // then
        assertThat(expected).isNotEqualTo(product);
    }
}