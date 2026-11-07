package com.example.sembakoapi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

@Table("products")
public class Product {
  @Id public Long id;
  public String name;
  public Long categoryId;
  public Long supplierId;
  public long price;
  public int stock;
  public int minStock;
  public String unit;

  @Transient public String categoryName;
  @Transient public String supplierName;
}
