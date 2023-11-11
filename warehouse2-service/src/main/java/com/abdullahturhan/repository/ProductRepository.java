package com.abdullahturhan.repository;

import com.abdullahturhan.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,String> {
    Optional<Product> findByName(String name);
    Optional<List<Product>> findByCategory(String category);
}
