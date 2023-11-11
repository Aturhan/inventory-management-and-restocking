package com.abdullahturhan.service;

import com.abdullahturhan.dto.ProductDto;
import com.abdullahturhan.dto.ProductRestockDto;
import com.abdullahturhan.exception.ProductNotFoundException;
import com.abdullahturhan.exception.QuantityOutOfRangeException;
import com.abdullahturhan.model.Product;
import com.abdullahturhan.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final WebClient.Builder webClient;

    public ProductService(ProductRepository productRepository, WebClient.Builder webClient) {
        this.productRepository = productRepository;
        this.webClient = webClient;
    }

    public ProductDto findById(String id) throws ProductNotFoundException{
        final Product product = productRepository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException("Product not found"));
        return ProductDto.builder()
                .name(product.getName())
                .thresholdValue(product.getThresholdValue())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .category(product.getCategory())
                .build();
    }
    @Transactional
    public void addProduct(ProductDto productDto){
        Product product = Product.builder()
                .name(productDto.getName())
                .thresholdValue(productDto.getThresholdValue())
                .quantity(productDto.getQuantity())
                .price(productDto.getPrice())
                .category(productDto.getCategory())
                .build();
        productRepository.save(product);
        log.info(String.format("Product inserted successfully =  %s",product));
    }

    public void lookForAndTrySendNewQuantityForWarehouse1(ProductDto productDto) throws ProductNotFoundException, QuantityOutOfRangeException {
        Product product = productRepository.findByName(productDto.getName());
        if (product == null) {
            log.info("Product not found");
            throw new ProductNotFoundException("Product not found");
        }

        if (product.getQuantity() <= 10) {
            throw new QuantityOutOfRangeException("Quantity out of range");
        }
         final ProductRestockDto productRestockDto = ProductRestockDto.builder()
                .name(product.getName())
                .quantity(product.getQuantity())
                .build();
        try {
            sendRestockRequestToWarehouse1(productRestockDto);
            log.info(String.format("The stock replenishment request has been successfully sent to relevant service and product sent= %s",productRestockDto));
        } catch (Exception e) {
            log.error("Exception while sending to warehouse1 " + e.getMessage());
        }

    }


    public void lookForAndTrySendNewQuantityForWarehouse2(ProductDto productDto) throws ProductNotFoundException, QuantityOutOfRangeException {
        Product product = productRepository.findByName(productDto.getName());
        if (product == null){
            log.info("Product not found");
            throw new ProductNotFoundException("Product not found");
        }

        if (product.getQuantity() <= 10){
            throw new QuantityOutOfRangeException("Product quantity is out of range to send");
        }
        final ProductRestockDto productRestockDto = ProductRestockDto.builder()
                .name(product.getName())
                .quantity(product.getQuantity())
                .build();
        try {
            sendRestockRequestToWarehouse2(productRestockDto);
            log.info(String.format("The stock replenishment request has been successfully sent to relevant service and product sent= %s",productRestockDto));
        }catch (Exception e) {
            log.error("Exception while sending to warehouse2 "+e.getMessage());
        }

    }

    public void sendRestockRequestToWarehouse1(ProductRestockDto productRestockDto){
        webClient.build().patch()
                .uri("http://warehouse1-service/api/warehouse1/update")
                .bodyValue(productRestockDto)
                .retrieve()
                .bodyToMono(Void.class).block();
    }

    public void sendRestockRequestToWarehouse2(ProductRestockDto productRestockDto){
        webClient.build().patch()
                .uri("http://warehouse2-service/api/warehouse2/update")
                .bodyValue(productRestockDto)
                .retrieve()
                .bodyToMono(Void.class).block();
    }

    }



