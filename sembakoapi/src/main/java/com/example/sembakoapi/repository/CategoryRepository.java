package com.example.sembakoapi.repository;

import com.example.sembakoapi.model.Category;
import org.springframework.data.repository.ListCrudRepository;

public interface CategoryRepository extends ListCrudRepository<Category, Long> {
}
