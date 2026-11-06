package com.example.sembakoapp.data.model;

import java.io.Serializable;

public class Product implements Serializable {
  private static final long serialVersionUID = 1L;

  public long id;
  public String name;
  public long categoryId;
  public long supplierId;
  public long price;
  public int stock;
  public int minStock;
  public String unit;

  /** Joined by the repository - not sent when creating/updating. */
  public String categoryName;

  public String supplierName;

  public Product() {}

  public Product(
      long id,
      String name,
      long categoryId,
      long supplierId,
      long price,
      int stock,
      int minStock,
      String unit) {
    this.id = id;
    this.name = name;
    this.categoryId = categoryId;
    this.supplierId = supplierId;
    this.price = price;
    this.stock = stock;
    this.minStock = minStock;
    this.unit = unit;
  }

  public boolean isLowStock() {
    return stock <= minStock;
  }
}
