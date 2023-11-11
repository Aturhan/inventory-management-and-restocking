package com.abdullahturhan.service;

import com.abdullahturhan.dto.ProductDto;
import com.abdullahturhan.dto.ProductRestockDto;
import com.abdullahturhan.dto.TakeOutProductDto;
import com.abdullahturhan.exception.ProductNotFoundException;
import com.abdullahturhan.exception.QuantityNotEnoughException;
import com.abdullahturhan.model.Product;
import com.abdullahturhan.producer.KafkaProducer;
import com.abdullahturhan.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final KafkaProducer kafkaProducer;

    public ProductService(ProductRepository productRepository, KafkaProducer kafkaProducer) {
        this.productRepository = productRepository;
        this.kafkaProducer = kafkaProducer;
    }

    public ProductDto getProductsById(String productId) throws ProductNotFoundException {
        final Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        return converter(product);
    }

    public List<ProductDto> getProductsByCategory(String category) throws ProductNotFoundException {
        List<Product> products = productRepository.findByCategory(category)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        return products.stream().map(model ->
                ProductDto.builder()
                        .category(model.getCategory())
                        .name(model.getName())
                        .thresholdValue(model.getThresholdValue())
                        .quantity(model.getQuantity())
                        .price(model.getPrice())
                        .build()
        ).toList();
    }

    @Transactional
    public void addProduct(ProductDto productDto){
        final Product product = Product.builder()
                .name(productDto.getName())
                .category(productDto.getCategory())
                .price(productDto.getPrice())
                .quantity(productDto.getQuantity())
                .thresholdValue(productDto.getThresholdValue())
                .build();
        productRepository.save(product);
        log.info(String.format("Product inserted successfully into database = %s ",product));
    }

    public void takeOutProduct(TakeOutProductDto productDto) throws ProductNotFoundException, QuantityNotEnoughException {
        Product product = productRepository.findById(productDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        if (product.getQuantity() < productDto.getQuantity()){
            final ProductDto payload = converter(product);
            kafkaProducer.publish("thresholdValue_error_warehouse2",payload);
            log.info(String.format("Event sent to central with this payload: %s",payload));
            throw  new QuantityNotEnoughException(String.format("Product has not enough quantity but restock request " +
                    "sent to central warehouse: %d",product.getQuantity()));
        }
        final Integer newQuantity = product.getQuantity() - productDto.getQuantity();
        product.setQuantity(newQuantity);
        productRepository.save(product);
        log.info(String.format("The product was released from the warehouse quantity=  %s , product = %s",product.getQuantity(),product));

        if (product.getQuantity() <= product.getThresholdValue()){
            final ProductDto payload = converter(product);
            kafkaProducer.publish("thresholdValue_error_warehouse2",payload);
            log.info(String.format("Event sent to central with this payload: %s",payload));
        }
    }

    public void updateProductQuantity(ProductRestockDto productRestockDto) throws ProductNotFoundException {
        Product product = productRepository.findByName(productRestockDto.getName())
                .orElseThrow(()-> new ProductNotFoundException("Product not found"));
        final Integer newQuantity = product.getQuantity() + productRestockDto.getQuantity();
        product.setQuantity(newQuantity);
        productRepository.save(product);
        log.info(String.format("Product updated = %s with quantity = %d",product.getName(),product.getQuantity()));
    }

    @Transactional
    public void deleteProductById(String productId) throws ProductNotFoundException {
        Optional<Product> product = Optional.ofNullable(productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found")));
        product.ifPresent(productRepository::delete);

    }

    protected ProductDto converter(Product product){
        return ProductDto.builder()
                .name(product.getName())
                .category(product.getCategory())
                .thresholdValue(product.getThresholdValue())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }

}
