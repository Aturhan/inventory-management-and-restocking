package com.abdullahturhan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


    @Entity
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    @Table(name = "products")
    public class Product {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        @Column(updatable = false)
        private String id;
        @Column(nullable = false)
        private String name;
        @Column(nullable = false)
        private Integer quantity;
        @Column(name = "threshold_value",nullable = false,updatable = false)
        private Integer thresholdValue;
        @Column(nullable = false)
        private Double price;
        @Column(nullable = false)
        private String category;
        @CreationTimestamp
        @Column(name = "created_at")
        private LocalDateTime createdAt;
        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

    }

