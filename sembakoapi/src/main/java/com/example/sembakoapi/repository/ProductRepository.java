package com.example.sembakoapi.repository;

import com.example.sembakoapi.model.Product;
import org.springframework.data.repository.ListCrudRepository;

public interface ProductRepository extends ListCrudRepository<Product, Long> {
  long countByCategoryId(Long categoryId);
  long countBySupplierId(Long supplierId);
}
