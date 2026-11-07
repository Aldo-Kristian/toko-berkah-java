package com.example.sembakoapi.repository;

import com.example.sembakoapi.model.Supplier;
import org.springframework.data.repository.ListCrudRepository;

public interface SupplierRepository extends ListCrudRepository<Supplier, Long> {
}
