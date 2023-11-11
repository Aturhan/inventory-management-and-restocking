package com.abdullahturhan.controller;

import com.abdullahturhan.dto.ProductDto;
import com.abdullahturhan.dto.ProductRestockDto;
import com.abdullahturhan.dto.TakeOutProductDto;
import com.abdullahturhan.exception.ProductNotFoundException;
import com.abdullahturhan.exception.QuantityNotEnoughException;
import com.abdullahturhan.model.Product;
import com.abdullahturhan.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/warehouse1")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping(path = "/id")
    public ResponseEntity<ProductDto> getProductById(@RequestParam("id") String productId) throws ProductNotFoundException {
            return ResponseEntity.status(HttpStatus.FOUND).body(productService.getProductsById(productId));
    }
    @GetMapping(path = "/category")
    public ResponseEntity<List<ProductDto>> getProductByCategory(@RequestParam("category") String category) throws ProductNotFoundException {
        return ResponseEntity.status(HttpStatus.FOUND).body(productService.getProductsByCategory(category));
    }
    @PostMapping(path = "/add")
    public ResponseEntity<?> addProduct(@RequestBody @Valid ProductDto productDto){
        productService.addProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PostMapping(path = "/takeout")
    public ResponseEntity<Void> takeOut(@RequestBody TakeOutProductDto takeOutProductDto) throws QuantityNotEnoughException, ProductNotFoundException {
        productService.takeOutProduct(takeOutProductDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping(path = "/update")
    public ResponseEntity<Void> updateProductQuantity(@RequestBody ProductRestockDto productReStockDto) throws ProductNotFoundException {
        productService.updateProductQuantity(productReStockDto);
        return ResponseEntity.status(203).build();

    }
    @GetMapping(path = "/find")
    public ResponseEntity<Product> getProduct(@RequestBody ProductRestockDto productReStockDto) throws ProductNotFoundException {
        return ResponseEntity.status(HttpStatus.FOUND).body(productService.getProductByName(productReStockDto));
    }

}
