package com.example.sembakoapi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("suppliers")
public class Supplier {
  @Id public Long id;
  public String name;
  public String phone;
  public String address;
}
